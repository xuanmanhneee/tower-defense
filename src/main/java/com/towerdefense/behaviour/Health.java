package com.towerdefense.behaviour;

import com.towerdefense.core.Behaviour;

public class Health extends Behaviour {

    private final double maxHealth;
    private double currentHealth;

    private Runnable onDeath;

    public Health(double maxHealth) {
        if (maxHealth <= 0) {
            throw new IllegalArgumentException("Max health must be greater than zero");
        }
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public double getHealth() {
        return currentHealth;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getRatio() {
        return currentHealth / maxHealth;
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

    /** Khôi phục đầy máu. Callback {@code onDeath} là cấu hình nên được giữ nguyên. */
    @Override
    public void onReset() {
        currentHealth = maxHealth;
    }

    public void setOnDeath(Runnable onDeath) {
        this.onDeath = onDeath;
    }

    public void damage(double amount) {
        if (amount <= 0 || isDead()) return;

        currentHealth = Math.max(0, currentHealth - amount);

        if (currentHealth == 0) {
            gameObject.destroy();

            if (onDeath != null) onDeath.run();
        }
    }
}