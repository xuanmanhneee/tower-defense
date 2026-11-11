package com.towerdefense.wave;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WaveLoaderTest {

    @Test
    void loadsLevelWaveDataFromResources() {
        WaveConfig config = WaveLoader.load("/waves/level-1.json");

        assertEquals(3, config.waves().size());
        assertEquals("grunt", config.waves().get(0).groups().get(0).enemy());
        assertEquals(8, config.waves().get(0).groups().get(0).count());
        assertEquals(12, config.waves().get(1).groups().get(0).count());
        assertEquals(5, config.waves().get(1).groups().get(1).count());
        assertEquals(3, config.waves().get(2).groups().get(0).count());
    }
}
