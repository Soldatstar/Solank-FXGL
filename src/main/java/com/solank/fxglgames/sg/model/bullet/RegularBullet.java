package com.solank.fxglgames.sg.model.bullet;

import com.almasb.fxgl.entity.Entity;

public class RegularBullet extends AbstractBulletBehaviour {
    @Override
    public void onHit(Entity bullet, Entity target) {
        super.onHit(bullet, target);
        bullet.removeFromWorld();
    }
}
