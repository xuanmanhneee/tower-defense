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
import com.towerdefense.render.HealthBarRenderer;
import com.towerdefense.render.DiscRenderer;
import com.towerdefense.render.RenderLayer;

import javafx.scene.paint.Color;

public class ScoutEnemyPrefab implements Prefab {
    private static final double MAX_HEALTH = 40;
    private static final double SPEED = 160;
    private static final double GOAL_DAMAGE = 1;
    private static final int GOLD_REWARD = 8;
    private static final double SIZE = 18;

    @Override
    public GameObject instantiate() {
        GameObject enemy = new GameObject();

        enemy.addBehaviour(new DiscRenderer(RenderLayer.ENEMY, Color.web("#ef5350"), 12));
        enemy.addBehaviour(new Health(MAX_HEALTH));
        enemy.addBehaviour(new HealthBarRenderer(24, 4, 14));
        enemy.addBehaviour(new CircleCollider(SIZE / 2, CollisionLayer.ENEMY).detects(CollisionLayer.GOAL));
        enemy.addBehaviour(new ContactDamage(GOAL_DAMAGE, true));
        enemy.addBehaviour(new PathFollower(SPEED));
        enemy.addBehaviour(new RewardOnDeath(GOLD_REWARD));
        enemy.addBehaviour(new SpeedModifiers());

        return enemy;
    }
}
