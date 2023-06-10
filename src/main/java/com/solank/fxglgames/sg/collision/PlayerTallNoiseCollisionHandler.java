package com.solank.fxglgames.sg.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.solank.fxglgames.sg.Type;

import static com.almasb.fxgl.dsl.FXGL.getd;
import static com.almasb.fxgl.dsl.FXGLForKtKt.inc;
import static com.almasb.fxgl.dsl.FXGLForKtKt.play;

public class PlayerTallNoiseCollisionHandler extends CollisionHandler {
    public PlayerTallNoiseCollisionHandler() {

        super(Type.YUKINE, Type.TALLNOISE);
    }

    @Override
    protected void onCollisionBegin(Entity a, Entity b) {
        if (getd("Health") < 25.0) {
            inc("Health", -getd("Health"));
        } else {
            inc("Health", -25.0);
        }
        b.removeFromWorld();
        play("hit.wav");
    }
}
