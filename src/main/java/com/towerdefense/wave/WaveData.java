package com.towerdefense.wave;

import java.util.List;

public record WaveData(double delayAfter, List<SpawnGroupData> groups) {
}
