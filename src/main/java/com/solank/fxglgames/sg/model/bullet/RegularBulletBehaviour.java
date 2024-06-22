package com.solank.fxglgames.sg.model.bullet;

import com.almasb.fxgl.entity.Entity;

public class RegularBulletBehaviour implements BulletBehaviour {
    @Override
    public void onHit(Entity bullet, Entity target) {
        bullet.removeFromWorld();
    }
}
