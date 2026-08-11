package com.project.oag.app.service;

import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.ArtworkView;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.ArtworkViewRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArtworkViewService {

    private final ArtworkViewRepository artworkViewRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;

    public ArtworkViewService(ArtworkViewRepository artworkViewRepository,
                              ArtworkRepository artworkRepository,
                              UserRepository userRepository) {
        this.artworkViewRepository = artworkViewRepository;
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void recordView(Long artworkId, Long userId) {
        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));

        ArtworkView view = new ArtworkView();
        view.setArtwork(artwork);
        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            view.setUser(user);
        }
        artworkViewRepository.save(view);
    }
}
