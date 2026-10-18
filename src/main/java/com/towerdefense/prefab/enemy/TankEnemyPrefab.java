package com.towerdefense.prefab.enemy;

import com.towerdefense.behaviour.ContactDamage;
import com.towerdefense.behaviour.Health;
import com.towerdefense.behaviour.PathFollower;
import com.towerdefense.behaviour.RewardOnDeath;
import com.towerdefense.behaviour.SpeedModifiers;
import com.towerdefense.collision.CircleCollider;
import com.towerdefense.collision.CollisionLayer;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Prefab;
import com.towerdefense.entity.enemy.EnemyType;
import com.towerdefense.render.EnemyRenderer;
import com.towerdefense.render.HealthBarRenderer;

import javafx.scene.paint.Color;

public class TankEnemyPrefab implements Prefab {
    private static final double MAX_HEALTH = 180;
    private static final double SPEED = 38;
    private static final double GOAL_DAMAGE = 4;
    private static final int GOLD_REWARD = 30;
    private static final double SIZE = 30;

    @Override
    public GameObject instantiate() {
        GameObject enemy = new GameObject();

        enemy.addBehaviour(new EnemyRenderer(EnemyType.TANK));
        enemy.addBehaviour(new Health(MAX_HEALTH));
        enemy.addBehaviour(new HealthBarRenderer(40, 5, 24));
        enemy.addBehaviour(new CircleCollider(SIZE / 2, CollisionLayer.ENEMY).detects(CollisionLayer.GOAL));
        enemy.addBehaviour(new ContactDamage(GOAL_DAMAGE, true));
        enemy.addBehaviour(new PathFollower(SPEED));
        enemy.addBehaviour(new RewardOnDeath(GOLD_REWARD));
        enemy.addBehaviour(new SpeedModifiers());

        return enemy;
    }
}
