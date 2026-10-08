package com.towerdefense.render;

import com.towerdefense.core.Behaviour;

import javafx.scene.canvas.GraphicsContext;

public abstract class RenderBehaviour extends Behaviour {

    private final RenderLayer layer;

    protected RenderBehaviour(RenderLayer layer) {
        this.layer = layer;
    }

    public RenderLayer getLayer() {
        return layer;
    }

    public abstract void render(GraphicsContext graphics);
}