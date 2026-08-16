package com.project.oag.app.controller;

import com.project.oag.app.dto.MessageBodyRequestDto;
import com.project.oag.app.dto.MessageThreadRequestDto;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.MessageService;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/messages/threads")
@Tag(name = "Messages")
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;

    public MessageController(MessageService messageService, UserRepository userRepository) {
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_MESSAGE')")
    public ResponseEntity<GenericResponse> list(HttpServletRequest request) {
        return prepareResponse(HttpStatus.OK, "Threads retrieved",
                messageService.listThreads(resolveUserId(request)));
    }

    @GetMapping("/{threadId}")
    @PreAuthorize("hasAuthority('USER_MESSAGE')")
    public ResponseEntity<GenericResponse> get(HttpServletRequest request, @PathVariable Long threadId) {
        return prepareResponse(HttpStatus.OK, "Thread retrieved",
                messageService.getThread(threadId, resolveUserId(request)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_MESSAGE')")
    public ResponseEntity<GenericResponse> create(HttpServletRequest request,
                                                  @Valid @RequestBody MessageThreadRequestDto dto) {
        return prepareResponse(HttpStatus.CREATED, "Thread created",
                messageService.createThread(resolveUserId(request), dto));
    }

    @PostMapping("/{threadId}")
    @PreAuthorize("hasAuthority('USER_MESSAGE')")
    public ResponseEntity<GenericResponse> reply(HttpServletRequest request,
                                                 @PathVariable Long threadId,
                                                 @Valid @RequestBody MessageBodyRequestDto dto) {
        return prepareResponse(HttpStatus.CREATED, "Message sent",
                messageService.addMessage(threadId, resolveUserId(request), dto));
    }

    private Long resolveUserId(HttpServletRequest request) {
        String email = getLoggedInUserName(request);
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
        return user.getId();
    }
}
