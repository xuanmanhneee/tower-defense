package com.towerdefense.behaviour;

import com.towerdefense.core.Behaviour;
import com.towerdefense.event.DeathEvent;

public class RewardOnDeath extends Behaviour {

    private final int gold;

    public RewardOnDeath(int gold) {
        this.gold = gold;
    }

    @Override
    public void onDestroy() {
        Health h = gameObject.getBehaviour(Health.class);
        if (h == null || !h.isDead()) return;   // tự hủy khi chạm goal: không thưởng

        gameObject.getScene().getEventBus().publish(new DeathEvent(gameObject, gold));
    }
}