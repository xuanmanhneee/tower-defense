package com.towerdefense.wave;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public final class WaveLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private WaveLoader() {
    }

    public static WaveConfig load(String resourcePath) {
        try (InputStream input = WaveLoader.class.getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalArgumentException("Wave file not found: " + resourcePath);
            }

            WaveConfig config = MAPPER.readValue(input, WaveConfig.class);
            validate(config, resourcePath);
            return config;
        } catch (IOException exception) {
            throw new IllegalStateException("Cannot load wave file: " + resourcePath, exception);
        }
    }

    private static void validate(WaveConfig config, String resourcePath) {
        if (config == null || config.waves() == null || config.waves().isEmpty()) {
            throw new IllegalArgumentException("Wave file has no waves: " + resourcePath);
        }

        for (int waveIndex = 0; waveIndex < config.waves().size(); waveIndex++) {
            WaveData wave = config.waves().get(waveIndex);
            if (wave == null || wave.groups() == null || wave.groups().isEmpty()) {
                throw new IllegalArgumentException("Wave " + (waveIndex + 1) + " has no spawn groups");
            }
            if (wave.delayAfter() < 0) {
                throw new IllegalArgumentException("Wave " + (waveIndex + 1) + " has a negative delayAfter");
            }

            for (int groupIndex = 0; groupIndex < wave.groups().size(); groupIndex++) {
                SpawnGroupData group = wave.groups().get(groupIndex);
                String location = "Wave " + (waveIndex + 1) + ", group " + (groupIndex + 1);
                if (group == null || group.enemy() == null || group.enemy().isBlank()) {
                    throw new IllegalArgumentException(location + " has no enemy id");
                }
                if (group.count() <= 0) {
                    throw new IllegalArgumentException(location + " must have count greater than zero");
                }
                if (group.interval() <= 0) {
                    throw new IllegalArgumentException(location + " must have interval greater than zero");
                }
            }
        }
    }
}
