package com.towerdefense.render;

import com.towerdefense.core.Behaviour;
import com.towerdefense.core.GameObject;

import javafx.scene.canvas.GraphicsContext;

import java.util.Collection;

public class GameRenderer {

    private final GraphicsContext graphics;

    public GameRenderer(GraphicsContext graphics) {
        this.graphics = graphics;
    }

    public void render(Collection<GameObject> gameObjects) {
        for (GameObject gameObject : gameObjects) {
            if (!gameObject.isAlive()) {
                continue;
            }

            for (Behaviour behaviour : gameObject.getBehaviours()) {
                if (behaviour instanceof RenderBehaviour renderer) {
                    renderer.render(graphics);
                }
            }
        }
    }
}