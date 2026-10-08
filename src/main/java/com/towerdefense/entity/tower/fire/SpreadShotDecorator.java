package com.towerdefense.entity.tower.fire;

import com.towerdefense.core.GameObject;
import com.towerdefense.core.Vector2;
import com.towerdefense.core.World;

public class SpreadShotDecorator extends FireStrategyDecorator {

    private final FireStrategy sideShot;
    private final int extraShotsEachSide;
    private final double spreadAngleDegrees;

    public SpreadShotDecorator(FireStrategy main,
            FireStrategy sideShot,
            int extraShotsEachSide,
            double spreadAngleDegrees) {
        super(main);
        this.sideShot = sideShot;
        this.extraShotsEachSide = extraShotsEachSide;
        this.spreadAngleDegrees = spreadAngleDegrees;
    }

    @Override
    public void fire(Vector2 origin, Vector2 direction,
            GameObject target, World world) {
        wrapped.fire(origin, direction, target, world);   // viên chính

        for (int i = 1; i <= extraShotsEachSide; i++) {
            double angle = spreadAngleDegrees * i;
            sideShot.fire(origin, rotate(direction, -angle), target, world);
            sideShot.fire(origin, rotate(direction, angle), target, world);
        }
    }

    private Vector2 rotate(Vector2 v, double degrees) {
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        return new Vector2(
                v.x() * cos - v.y() * sin,
                v.x() * sin + v.y() * cos);
    }
}