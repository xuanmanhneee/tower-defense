package com.towerdefense.collision;

import java.util.ArrayList;
import java.util.List;

import com.towerdefense.core.Behaviour;
import com.towerdefense.core.GameObject;

public class CollisionSystem {

    private final List<CircleCollider> colliders = new ArrayList<>(); // tái sử dụng, không cấp phát mỗi frame

    public void update(List<GameObject> objects) {
        colliders.clear();
        for (GameObject o : objects) {
            if (!o.isAlive()) continue;
            CircleCollider c = o.getBehaviour(CircleCollider.class);
            if (c != null) colliders.add(c);
        }

        for (int i = 0; i < colliders.size(); i++) {
            CircleCollider a = colliders.get(i);
            for (int j = i + 1; j < colliders.size(); j++) {
                CircleCollider b = colliders.get(j);

                boolean aWantsB = a.interactsWith(b);
                boolean bWantsA = b.interactsWith(a);
                if (!aWantsB && !bWantsA) continue;      // lọc bằng mask trước, rẻ nhất

                GameObject ao = a.getGameObject(), bo = b.getGameObject();
                if (!ao.isAlive() || !bo.isAlive()) continue; // đạn đã trúng địch 1 thì bỏ qua địch 2

                if (!a.overlaps(b)) continue;

                if (aWantsB) notify(ao, bo);
                if (bWantsA) notify(bo, ao);
            }
        }
    }

    private void notify(GameObject self, GameObject other) {
        for (Behaviour b : self.getBehaviours()) { 
            if (b instanceof CollisionListener l) l.onCollision(other);
        }
    }
}