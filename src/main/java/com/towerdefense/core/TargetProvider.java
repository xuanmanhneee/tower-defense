package com.towerdefense.core;

import java.util.List;

public interface TargetProvider {
    List<GameObject> findObjectsWithBehaviour(Class<? extends Behaviour> type);
}