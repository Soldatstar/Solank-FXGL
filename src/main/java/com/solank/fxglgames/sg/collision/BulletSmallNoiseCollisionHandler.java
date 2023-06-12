package com.solank.fxglgames.sg.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.solank.fxglgames.sg.Type;

import static com.almasb.fxgl.dsl.FXGLForKtKt.inc;
import static com.almasb.fxgl.dsl.FXGLForKtKt.play;

public class BulletSmallNoiseCollisionHandler extends CollisionHandler {
    public BulletSmallNoiseCollisionHandler() {
        super(Type.BULLET, Type.NOISE);
    }

    @Override
    protected void onCollisionBegin(Entity a, Entity b) {
        play("hit.wav");
        double noiseRemainingHealth = b.getDouble("health");
        double damage = a.getDouble("damage");
        int score = b.getInt("score");
        // If the bullet's damage is greater than the noise's remaining health, then the noise is killed.
        if (damage >= noiseRemainingHealth) {
            inc("Score", +score);
            b.removeFromWorld();
        } else {
            b.setProperty("health", noiseRemainingHealth - damage);
        }
        a.removeFromWorld();
    }
}
