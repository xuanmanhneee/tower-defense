package com.towerdefense.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class GameObjectResetTest {

    @Test
    void resetRestoresAliveFlagPositionAndRotation() {
        GameObject o = new GameObject();
        o.getTransform().setPosition(new Vector2(3, 4));
        o.getTransform().setRotation(90);
        o.destroy();

        o.reset(new Vector2(7, 8));

        assertTrue(o.isAlive());
        assertEquals(new Vector2(7, 8), o.getTransform().getPosition());
        assertEquals(0, o.getTransform().getRotation());
    }

    @Test
    void resetCallsOnResetOfEveryBehaviourAfterPositionIsSet() {
        GameObject o = new GameObject();
        List<Vector2> positionSeen = new ArrayList<>();
        class Probe extends Behaviour {
            @Override
            public void onReset() {
                positionSeen.add(gameObject.getTransform().getPosition());
            }
        }
        o.addBehaviour(new Probe());
        o.addBehaviour(new Probe());

        o.reset(new Vector2(1, 2));

        assertEquals(List.of(new Vector2(1, 2), new Vector2(1, 2)), positionSeen);
    }
}
