package com.solank.fxglgames.sg.components.weapons.bullet;

import com.almasb.fxgl.entity.Entity;
import com.solank.fxglgames.sg.model.Type;

public class ArmourPiercing extends AbstractBulletBehaviour {
    @Override
    public void onHit(Entity bullet, Entity target) {
        long startTime = System.nanoTime();

        if (!target.isType(Type.NOISE)) {
            return;
        }

        super.onHit(bullet, target);

        int hits = bullet.getInt("hits");
        if (hits == 0) {
            bullet.removeFromWorld();
        } else {
            bullet.setProperty("hits", hits - 1);
        }

        long endTime = System.nanoTime();
        //print time in ns
        System.out.println("ArmourPiercing.onHit() took " + (endTime - startTime) + " ns");
    }
}
