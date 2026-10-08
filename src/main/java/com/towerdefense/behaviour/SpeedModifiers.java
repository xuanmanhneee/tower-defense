package com.towerdefense.behaviour;

import java.util.ArrayList;
import java.util.List;

import com.towerdefense.core.Behaviour;

public class SpeedModifiers extends Behaviour {
    private final List<Entry> entries = new ArrayList<>();

    private final double MIN_MULTIPLIER = 0.4;

    private static class Entry {
        double multiplier, remaining;

        Entry(double m, double d) {
            multiplier = m;
            remaining = d;
        }
    }

    public void add(double multiplier, double duration) {
        entries.add(new Entry(multiplier, duration));
    }

    public double getMultiplier() {
        double result = 1.0;
        for (int i = 0; i < entries.size(); i++)
            result *= entries.get(i).multiplier;
        return Math.max(MIN_MULTIPLIER, result);
    }

    @Override
    public void update(double dt) {
        for (int i = entries.size() - 1; i >= 0; i--) {
            if ((entries.get(i).remaining -= dt) <= 0)
                entries.remove(i);
        }
    }

    @Override
    public void onReset() {
        entries.clear();
    }
}
