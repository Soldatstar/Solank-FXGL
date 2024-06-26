package com.solank.fxglgames.sg.components.weapons.bullet;

import com.almasb.fxgl.entity.Entity;
import com.solank.fxglgames.sg.model.Type;

public class RegularBullet extends AbstractBulletBehaviour {
    @Override
    public void onHit(Entity bullet, Entity target) {
        long startTime = System.nanoTime();

        if (!target.isType(Type.NOISE)) {
            return;
        }

        super.onHit(bullet, target);
        bullet.removeFromWorld();
        long endTime = System.nanoTime();
        //print time in ns
        System.out.println("RegularBullet.onHit() took " + (endTime - startTime) + " ns");
    }
}
