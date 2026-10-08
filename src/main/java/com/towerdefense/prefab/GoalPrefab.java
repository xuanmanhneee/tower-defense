package com.towerdefense.prefab;

import com.towerdefense.behaviour.GoalReceiver;
import com.towerdefense.behaviour.Health;
import com.towerdefense.collision.CircleCollider;
import com.towerdefense.collision.CollisionLayer;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Prefab;
import com.towerdefense.render.DiscRenderer;
import com.towerdefense.render.HealthBarRenderer;
import com.towerdefense.render.RenderLayer;

import javafx.scene.paint.Color;

public class GoalPrefab implements Prefab {

    private static final double MAX_HEALTH = 20;
    private static final double RADIUS = 14;

    @Override
    public GameObject instantiate() {
        GameObject goal = new GameObject();

        goal.addBehaviour(new DiscRenderer(RenderLayer.TOWER, Color.web("#ab47bc"), RADIUS));
        goal.addBehaviour(new CircleCollider(RADIUS, CollisionLayer.GOAL).detects(CollisionLayer.ENEMY));
        goal.addBehaviour(new GoalReceiver());
        goal.addBehaviour(new Health(MAX_HEALTH));
        goal.addBehaviour(new HealthBarRenderer(40, 5, 24));

        return goal;
    }
}