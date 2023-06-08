package com.solank.fxglgames.sg.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.solank.fxglgames.sg.Type;

import static com.almasb.fxgl.dsl.FXGLForKtKt.inc;
import static com.almasb.fxgl.dsl.FXGLForKtKt.play;

public class BulletNoiseCollisionHandler extends CollisionHandler {
    public BulletNoiseCollisionHandler() {
        super( Type.BULLET, Type.NOISE);
    }
    @Override
    protected void onCollisionBegin(Entity a, Entity b) {
            inc("Score", +10);
            b.removeFromWorld();
            a.removeFromWorld();
            play("hit.wav");
    }
}
