package com.solank.fxglgames.sg.model.bullet;

import com.almasb.fxgl.entity.Entity;

public class ArmourPiercing extends AbstractBulletBehaviour {
    @Override
    public void onHit(Entity bullet, Entity target) {
        super.onHit(bullet, target);

        int hits = bullet.getInt("hits");
        if (hits == 0) {
            bullet.removeFromWorld();
        } else {
            bullet.setProperty("hits", hits - 1);
        }
    }
}
