package com.solank.fxglgames.sg.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.solank.fxglgames.sg.Type;

import static com.almasb.fxgl.dsl.FXGL.getd;
import static com.almasb.fxgl.dsl.FXGLForKtKt.inc;
import static com.almasb.fxgl.dsl.FXGLForKtKt.play;

public class PlayerNoiseCollisionHandler extends CollisionHandler {
    public PlayerNoiseCollisionHandler() {

        super(Type.YUKINE, Type.NOISE);
    }

    @Override
    protected void onCollisionBegin(Entity a, Entity b) {
        if (getd("Health") < b.getDouble("damage")) {

            inc("Health", -getd("Health"));
        } else {
            inc("Health", -b.getDouble("damage"));
        }
        b.removeFromWorld();

        //random int 1 or 2
        int randomInt = (int) (Math.random() * 2) + 1;
        play("hit/hit"+ randomInt+".wav");
    }
}
