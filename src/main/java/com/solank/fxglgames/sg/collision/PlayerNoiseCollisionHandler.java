package com.solank.fxglgames.sg.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.solank.fxglgames.sg.Type;

import static com.almasb.fxgl.dsl.FXGLForKtKt.inc;
import static com.almasb.fxgl.dsl.FXGLForKtKt.play;

public class PlayerNoiseCollisionHandler extends CollisionHandler {
    public PlayerNoiseCollisionHandler() {

        super( Type.YUKINE, Type.NOISE);
    }

    @Override
    protected void onCollisionBegin(Entity a, Entity b) {
            inc("Health", -20.0);
            b.removeFromWorld();
            play("hit.wav");

        if (a.isType(Type.BULLET) && b.isType(Type.NOISE)) {
            inc("Score", +10);
            b.removeFromWorld();
            a.removeFromWorld();
            play("hit.wav");
        }
    }
}
