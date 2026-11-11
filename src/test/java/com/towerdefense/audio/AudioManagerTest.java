package com.towerdefense.audio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AudioManagerTest {

    @Test
    void returnsTheSameInstance() {
        assertSame(AudioManager.getInstance(), AudioManager.getInstance());
    }

    @Test
    void rejectsMissingResources() {
        assertThrows(
                IllegalArgumentException.class,
                () -> AudioManager.getInstance().playMusic("/audios/missing.mp3"));
        assertThrows(
                IllegalArgumentException.class,
                () -> AudioManager.getInstance().playEffect("/audios/missing.mp3"));
        assertThrows(
                IllegalArgumentException.class,
                () -> AudioManager.getInstance().playPlaylist());
        assertThrows(
                IllegalArgumentException.class,
                () -> AudioManager.getInstance().playPlaylist("/audios/missing.mp3"));
    }

    @Test
    void packagesEveryUsedAudioResource() {
        for (String path : new String[]{
                "/audios/menu.mp3",
                "/audios/bgm_1.mp3",
                "/audios/bgm_2.mp3",
                "/audios/shoot.mp3",
                "/audios/spread.mp3",
                "/audios/slow.mp3",
                "/audios/explode.mp3",
                "/audios/upgrate_tower.mp3",
                "/audios/ace.mp3",
                "/audios/win.mp3",
                "/audios/placeholder.mp3"}) {
            assertNotNull(AudioManager.class.getResource(path), path);
        }
    }

    @Test
    void resolvesNamedAudioWithPlaceholderFallback() {
        assertEquals("/audios/win.mp3", AudioResources.fromName("win"));
        assertEquals("/audios/placeholder.mp3", AudioResources.fromName("lose"));
    }
}
