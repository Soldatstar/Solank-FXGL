package com.solank.fxglgames.sg.components.weapons.bullet;

import com.almasb.fxgl.entity.Entity;

public interface BulletBehaviour {
    void onHit(Entity bullet, Entity target);
}