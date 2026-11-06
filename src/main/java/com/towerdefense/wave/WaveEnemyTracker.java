package com.towerdefense.wave;

import com.towerdefense.core.Behaviour;

final class WaveEnemyTracker extends Behaviour {

    @Override
    public void onDestroy() {
        gameObject.getScene().getEventBus().publish(new EnemyRemovedEvent(gameObject));
    }
}
