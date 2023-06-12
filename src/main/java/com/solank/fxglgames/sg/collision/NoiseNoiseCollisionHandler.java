package com.solank.fxglgames.sg.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.solank.fxglgames.sg.Type;
import javafx.scene.control.Tab;

public class NoiseNoiseCollisionHandler extends CollisionHandler {
    public NoiseNoiseCollisionHandler(Object a, Object b) {
        super(Type.NOISE, Type.NOISE);
        //do nothing
    }

    @Override
    protected void onCollision(Entity a, Entity b) {
    }

    @Override
    protected void onCollisionBegin(Entity a, Entity b) {
    }

    @Override
    protected void onCollisionEnd(Entity a, Entity b) {
    }

    @Override
    protected void onHitBoxTrigger(Entity a, Entity b, HitBox boxA, HitBox boxB) {
    }
}
