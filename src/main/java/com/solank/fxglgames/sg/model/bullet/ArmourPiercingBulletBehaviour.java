package com.solank.fxglgames.sg.model.bullet;

import com.almasb.fxgl.entity.Entity;

public class ArmourPiercingBulletBehaviour implements BulletBehaviour{
    @Override
    public void onHit(Entity bullet, Entity target) {
        int hits = bullet.getInt("hits");
        if (hits == 0) {
            bullet.removeFromWorld();
        } else {
            bullet.setProperty("hits", hits - 1);
        }
    }
}
