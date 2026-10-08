package com.towerdefense.prefab.tower;

import com.towerdefense.behaviour.FireControl;
import com.towerdefense.behaviour.Targeting;
import com.towerdefense.core.GameObject;
import com.towerdefense.core.Prefab;
import com.towerdefense.entity.tower.fire.FireStrategy;
import com.towerdefense.render.DiscRenderer;
import com.towerdefense.render.RenderLayer;

import javafx.scene.paint.Color;

public abstract class TowerPrefab implements Prefab {

    // Hook có giá trị mặc định: lớp con chỉ override khi cần khác
    protected double radius() {
        return 16;
    }

    protected double range() {
        return 150;
    }

    protected double fireInterval() {
        return 1.0;
    }

    protected Color color() {
        return Color.CRIMSON;
    }

    // Factory method: bắt buộc lớp con quyết định
    protected abstract FireStrategy createFireStrategy();

    // Template method: bộ khung không ai được sửa
    @Override
    public final GameObject instantiate() {
        GameObject tower = new GameObject();
        tower.addBehaviour(new DiscRenderer(RenderLayer.TOWER, color(), radius()));
        Targeting targeting = tower.addBehaviour(new Targeting(range()));
        tower.addBehaviour(new FireControl(targeting, createFireStrategy(), fireInterval()));
        return tower;
    }
}
