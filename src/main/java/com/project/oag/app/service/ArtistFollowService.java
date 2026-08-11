package com.project.oag.app.service;

import com.project.oag.app.entity.ArtistFollow;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtistFollowRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArtistFollowService {

    private final ArtistFollowRepository artistFollowRepository;
    private final UserRepository userRepository;

    public ArtistFollowService(ArtistFollowRepository artistFollowRepository, UserRepository userRepository) {
        this.artistFollowRepository = artistFollowRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void follow(Long followerId, Long artistId) {
        if (followerId.equals(artistId)) {
            throw new GeneralException("Cannot follow yourself");
        }
        if (artistFollowRepository.existsByFollowerIdAndArtistId(followerId, artistId)) {
            throw new GeneralException("Already following this artist");
        }
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Follower not found"));
        User artist = userRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));

        ArtistFollow follow = new ArtistFollow();
        follow.setFollower(follower);
        follow.setArtist(artist);
        artistFollowRepository.save(follow);
    }

    @Transactional
    public void unfollow(Long followerId, Long artistId) {
        ArtistFollow follow = artistFollowRepository.findByFollowerIdAndArtistId(followerId, artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Follow relationship not found"));
        artistFollowRepository.delete(follow);
    }

    public boolean isFollowing(Long followerId, Long artistId) {
        return artistFollowRepository.existsByFollowerIdAndArtistId(followerId, artistId);
    }

    public long followerCount(Long artistId) {
        return artistFollowRepository.countByArtistId(artistId);
    }
}
