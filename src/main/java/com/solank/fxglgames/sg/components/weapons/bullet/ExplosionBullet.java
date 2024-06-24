package com.solank.fxglgames.sg.components.weapons.bullet;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.solank.fxglgames.sg.model.Type;
import javafx.geometry.Rectangle2D;

import java.util.List;

import static com.solank.fxglgames.sg.model.SGFactory.EXPLOSION_ENTITY;

public class ExplosionBullet extends AbstractBulletBehaviour {
    private static final int EXPLOSION_RADIUS = 100;
    private static final double HALF_EXPLOSION_RADIUS = EXPLOSION_RADIUS / 2.0;

    @Override
    public void onHit(Entity bullet, Entity target) {
        long startTime = System.nanoTime();

        double impactPosX = bullet.getX();
        double impactPosY = bullet.getY();

        GameWorld world = bullet.getWorld();
        world.create(EXPLOSION_ENTITY, new SpawnData().put("pointX", impactPosX).put("pointY", impactPosY).put("Radius", HALF_EXPLOSION_RADIUS));

        List<Entity> allNoiseInRadius = world.getEntitiesInRange(new Rectangle2D(
                        impactPosX - HALF_EXPLOSION_RADIUS,
                        impactPosY - HALF_EXPLOSION_RADIUS,
                        EXPLOSION_RADIUS,
                        EXPLOSION_RADIUS))
                .stream()
                .filter(e -> e.isType(Type.NOISE))
                .toList();

        for (Entity noise : allNoiseInRadius) {
            double health = noise.getDouble("health");
            super.handleDamage(noise, 5, health, noise.getInt("score"));
        }
        bullet.removeFromWorld();
        long endTime = System.nanoTime();
        //print time in ns
        System.out.println("ExplosionBullet.onHit() took " + (endTime - startTime) + " ns");
    }
}
