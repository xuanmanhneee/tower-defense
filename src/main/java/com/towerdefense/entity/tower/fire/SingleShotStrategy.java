package com.towerdefense.entity.tower.fire;

import com.towerdefense.behaviour.Launchable;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Vector2;
import com.towerdefense.core.World;
import com.towerdefense.prefab.projectile.ProjectilePrefab;

public class SingleShotStrategy implements FireStrategy {
    private final ProjectilePrefab projectilePrefab;

    public SingleShotStrategy(ProjectilePrefab projectilePrefab) {
        this.projectilePrefab = projectilePrefab;
    }

    @Override
    public void fire(Vector2 origin, Vector2 direction, GameObject target, World world) {
        GameObject projectile = world.instantiate(projectilePrefab, origin);
        projectile.getBehaviour(Launchable.class).launch(direction, target);
    }
}