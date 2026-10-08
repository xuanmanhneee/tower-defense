package com.towerdefense.test;

import com.towerdefense.collision.CircleCollider;
import com.towerdefense.collision.CollisionLayer;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Vector2;

public class TestCollisionMask {

    public static void main(String[] args) {
        testMask();
        testGeometry();
        testParameters();

        System.out.println("All tests passed.");
    }


    private static void testMask() {
        CircleCollider goal =
                new CircleCollider(14, CollisionLayer.GOAL)
                        .detects(CollisionLayer.ENEMY);

        CircleCollider enemy =
                new CircleCollider(12, CollisionLayer.ENEMY);

        check(
                goal.interactsWith(enemy),
                "Goal should detect enemy"
        );

        CircleCollider projectile =
                new CircleCollider(4, CollisionLayer.PROJECTILE);

        check(
                !goal.interactsWith(projectile),
                "Goal should not detect projectile"
        );

        check(
                !enemy.interactsWith(goal),
                "Enemy should not detect goal by default"
        );

        CircleCollider sensor =
                new CircleCollider(10, CollisionLayer.GOAL)
                        .detects(
                                CollisionLayer.ENEMY,
                                CollisionLayer.PROJECTILE
                        );

        check(
                sensor.interactsWith(enemy),
                "Sensor should detect enemy"
        );

        check(
                sensor.interactsWith(projectile),
                "Sensor should detect projectile"
        );

        check(
                goal.interactsWith(enemy),
                "Interaction should work from goal to enemy"
        );

        check(
                !enemy.interactsWith(goal),
                "Interaction should not be symmetric"
        );
    }


    private static void testGeometry() {
        CircleCollider a =
                colliderAt(
                        new CircleCollider(10, CollisionLayer.ENEMY),
                        0, 0
                );

        CircleCollider b =
                colliderAt(
                        new CircleCollider(10, CollisionLayer.GOAL),
                        15, 0
                );

        check(
                a.overlaps(b),
                "Circles should overlap"
        );

        check(
                b.overlaps(a),
                "Overlap should be symmetric"
        );

        CircleCollider far =
                colliderAt(
                        new CircleCollider(10, CollisionLayer.GOAL),
                        25, 0
                );

        check(
                !a.overlaps(far),
                "Circles should not overlap when far apart"
        );

        CircleCollider touching =
                colliderAt(
                        new CircleCollider(10, CollisionLayer.GOAL),
                        20, 0
                );

        check(
                a.overlaps(touching),
                "Touching circles should count as overlap"
        );

        CircleCollider near =
                colliderAt(
                        new CircleCollider(10, CollisionLayer.GOAL),
                        12, 16
                );

        CircleCollider small =
                colliderAt(
                        new CircleCollider(9, CollisionLayer.GOAL),
                        12, 16
                );

        check(
                a.overlaps(near),
                "Circle at (12, 16) should overlap"
        );

        check(
                !a.overlaps(small),
                "Smaller circle at (12, 16) should not overlap"
        );
    }

    // ---------- tham số ----------

    private static void testParameters() {
        expectException(
                IllegalArgumentException.class,
                () -> new CircleCollider(0, CollisionLayer.ENEMY)
        );

        expectException(
                IllegalArgumentException.class,
                () -> new CircleCollider(-5, CollisionLayer.ENEMY)
        );

        expectException(
                NullPointerException.class,
                () -> new CircleCollider(10, null)
        );
    }

    // ---------- helpers ----------

    private static CircleCollider colliderAt(
            CircleCollider collider,
            double x,
            double y
    ) {
        GameObject obj = new GameObject();
        obj.getTransform().setPosition(new Vector2(x, y));
        obj.addBehaviour(collider);

        return collider;
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void expectException(
            Class<? extends Throwable> expected,
            Runnable action
    ) {
        try {
            action.run();
        } catch (Throwable e) {
            if (expected.isInstance(e)) {
                return;
            }

            throw new AssertionError(
                    "Expected "
                            + expected.getSimpleName()
                            + " but got "
                            + e.getClass().getSimpleName(),
                    e
            );
        }

        throw new AssertionError(
                "Expected " + expected.getSimpleName()
        );
    }
}