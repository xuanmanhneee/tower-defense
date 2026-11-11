package com.towerdefense.audio;

public final class AudioResources {

    private static final String FALLBACK = "/audios/placeholder.mp3";

    private AudioResources() {
    }

    public static String fromName(String name) {
        String path = "/audios/" + name + ".mp3";
        return AudioResources.class.getResource(path) == null ? FALLBACK : path;
    }
}
