package com.towerdefense.behaviour;

import com.towerdefense.collision.CollisionListener;
import com.towerdefense.core.Behaviour;
import com.towerdefense.core.GameObject;

public class ContactDamage extends Behaviour implements CollisionListener {

    private double amount;
    private final boolean destroyOnHit;

    public ContactDamage(double amount, boolean destroyOnHit) {
        this.amount = amount;
        this.destroyOnHit = destroyOnHit;
    }

    public void setAmount(double a) {
        amount = a;
    }

    @Override
    public void onCollision(GameObject other) {
        if (!gameObject.isAlive() || !other.isAlive())
            return;

        Health h = other.getBehaviour(Health.class);
        if (h == null)
            return;

        h.damage(amount);
        if (destroyOnHit)
            gameObject.destroy();
    }
}