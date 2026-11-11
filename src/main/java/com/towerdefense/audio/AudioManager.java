package com.towerdefense.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class AudioManager {

    private static final AudioManager INSTANCE = new AudioManager();
    private static final double MUSIC_VOLUME = 0.35;
    private static final double EFFECT_VOLUME = 0.55;

    private final Map<String, AudioClip> effects = new HashMap<>();
    private MediaPlayer musicPlayer;
    private String[] playlist;
    private int nextTrackIndex;

    private AudioManager() {
    }

    public static AudioManager getInstance() {
        return INSTANCE;
    }

    public void playMusic(String resourcePath) {
        stopMusic();
        musicPlayer = new MediaPlayer(new Media(resourceUrl(resourcePath)));
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicPlayer.setVolume(MUSIC_VOLUME);
        musicPlayer.play();
    }

    public void playEffect(String resourcePath) {
        effects.computeIfAbsent(
                resourcePath,
                path -> new AudioClip(resourceUrl(path)))
                .play(EFFECT_VOLUME);
    }

    public void playPlaylist(String... resourcePaths) {
        if (resourcePaths.length == 0) {
            throw new IllegalArgumentException("Playlist must contain at least one track");
        }
        for (String resourcePath : resourcePaths) {
            resourceUrl(resourcePath);
        }

        stopMusic();
        playlist = resourcePaths.clone();
        nextTrackIndex = 0;
        playNextTrack();
    }

    public void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.setOnEndOfMedia(null);
            musicPlayer.stop();
            musicPlayer.dispose();
            musicPlayer = null;
        }
        playlist = null;
    }

    public void stopAll() {
        stopMusic();
        effects.values().forEach(AudioClip::stop);
    }

    private static String resourceUrl(String resourcePath) {
        URL resource = AudioManager.class.getResource(resourcePath);
        if (resource == null) {
            throw new IllegalArgumentException("Audio resource not found: " + resourcePath);
        }
        return resource.toExternalForm();
    }

    private void playNextTrack() {
        String resourcePath = playlist[nextTrackIndex];
        nextTrackIndex = (nextTrackIndex + 1) % playlist.length;

        musicPlayer = new MediaPlayer(new Media(resourceUrl(resourcePath)));
        musicPlayer.setVolume(MUSIC_VOLUME);
        musicPlayer.setOnEndOfMedia(() -> {
            musicPlayer.dispose();
            musicPlayer = null;
            playNextTrack();
        });
        musicPlayer.play();
    }
}
