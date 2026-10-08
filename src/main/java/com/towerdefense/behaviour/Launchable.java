package com.towerdefense.behaviour;

import com.towerdefense.core.Behaviour;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Vector2;

public abstract class Launchable extends Behaviour {
    public abstract void launch(Vector2 direction, GameObject target);
}