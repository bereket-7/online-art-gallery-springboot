package com.project.oag.app.service;

import com.project.oag.app.dto.MessageBodyRequestDto;
import com.project.oag.app.dto.MessageResponseDto;
import com.project.oag.app.dto.MessageThreadRequestDto;
import com.project.oag.app.dto.MessageThreadResponseDto;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.Message;
import com.project.oag.app.entity.MessageThread;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.MessageRepository;
import com.project.oag.app.repository.MessageThreadRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UserAuthorizationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    private final MessageThreadRepository threadRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;
    private final NotificationWebSocketService notificationService;

    public MessageService(MessageThreadRepository threadRepository,
                          MessageRepository messageRepository,
                          UserRepository userRepository,
                          ArtworkRepository artworkRepository,
                          NotificationWebSocketService notificationService) {
        this.threadRepository = threadRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.artworkRepository = artworkRepository;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<MessageThreadResponseDto> listThreads(Long userId) {
        return threadRepository.findByBuyerIdOrArtistIdOrderByCreationDateDesc(userId, userId).stream()
                .map(thread -> toThreadDto(thread, userId, false))
                .toList();
    }

    @Transactional(readOnly = true)
    public MessageThreadResponseDto getThread(Long threadId, Long userId) {
        MessageThread thread = getParticipatingThread(threadId, userId);
        return toThreadDto(thread, userId, true);
    }

    @Transactional
    public MessageThreadResponseDto createThread(Long buyerId, MessageThreadRequestDto request) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User artist = userRepository.findById(request.getArtistId())
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
        final Artwork artwork = request.getArtworkId() == null ? null
                : artworkRepository.findById(request.getArtworkId())
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));
        MessageThread thread = threadRepository
                .findByBuyerIdAndArtistIdAndArtworkId(buyerId, request.getArtistId(), request.getArtworkId())
                .orElseGet(() -> {
                    MessageThread created = new MessageThread();
                    created.setBuyer(buyer);
                    created.setArtist(artist);
                    created.setArtwork(artwork);
                    return threadRepository.save(created);
                });
        addMessage(thread, buyer, request.getBody());
        return toThreadDto(thread, buyerId, true);
    }

    @Transactional
    public MessageResponseDto addMessage(Long threadId, Long senderId, MessageBodyRequestDto request) {
        MessageThread thread = getParticipatingThread(threadId, senderId);
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toMessageDto(addMessage(thread, sender, request.getBody()));
    }

    private Message addMessage(MessageThread thread, User sender, String body) {
        Message message = new Message();
        message.setThread(thread);
        message.setSender(sender);
        message.setBody(body);
        Message saved = messageRepository.save(message);
        String recipientEmail = sender.getId().equals(thread.getBuyer().getId())
                ? thread.getArtist().getEmail()
                : thread.getBuyer().getEmail();
        notificationService.sendUserNotification(recipientEmail, "New message: " + body, "MESSAGE");
        return saved;
    }

    private MessageThread getParticipatingThread(Long threadId, Long userId) {
        MessageThread thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new ResourceNotFoundException("Thread not found"));
        if (!thread.getBuyer().getId().equals(userId) && !thread.getArtist().getId().equals(userId)) {
            throw new UserAuthorizationException("Not a participant in this thread");
        }
        return thread;
    }

    private MessageThreadResponseDto toThreadDto(MessageThread thread, Long viewerId, boolean includeMessages) {
        MessageThreadResponseDto dto = new MessageThreadResponseDto();
        dto.setThreadId(thread.getId());
        dto.setArtistId(thread.getArtist() != null ? thread.getArtist().getId() : null);
        dto.setBuyerId(thread.getBuyer() != null ? thread.getBuyer().getId() : null);
        dto.setArtworkId(thread.getArtwork() != null ? thread.getArtwork().getId() : null);
        dto.setUnread(messageRepository.countByThreadIdAndReadFalseAndSenderIdNot(thread.getId(), viewerId));
        messageRepository.findTopByThreadIdOrderByCreatedAtDesc(thread.getId())
                .ifPresent(last -> dto.setLastMessage(toMessageDto(last)));
        if (includeMessages) {
            dto.setMessages(messageRepository.findByThreadIdOrderByCreatedAtAsc(thread.getId()).stream()
                    .map(this::toMessageDto)
                    .toList());
        }
        return dto;
    }

    private MessageResponseDto toMessageDto(Message message) {
        MessageResponseDto dto = new MessageResponseDto();
        dto.setId(message.getId());
        dto.setThreadId(message.getThread() != null ? message.getThread().getId() : null);
        User sender = message.getSender();
        if (sender != null) {
            dto.setSenderId(sender.getId());
            dto.setSenderName(String.join(" ",
                    sender.getFirstName() == null ? "" : sender.getFirstName(),
                    sender.getLastName() == null ? "" : sender.getLastName()).trim());
        }
        dto.setBody(message.getBody());
        dto.setCreatedAt(message.getCreatedAt());
        dto.setRead(message.isRead());
        return dto;
    }
}
