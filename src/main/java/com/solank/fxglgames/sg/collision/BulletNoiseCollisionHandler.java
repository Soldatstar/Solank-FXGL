package com.solank.fxglgames.sg.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.solank.fxglgames.sg.components.weapons.bullet.BulletBehaviour;
import com.solank.fxglgames.sg.components.weapons.bullet.BulletType;
import com.solank.fxglgames.sg.model.Type;
import com.solank.fxglgames.sg.model.factory.BulletBehaviourFactory;

public class BulletNoiseCollisionHandler extends CollisionHandler {

    public BulletNoiseCollisionHandler() {
        super(Type.BULLET, Type.NOISE);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity noise) {
        BulletType bulletType = bullet.getObject("bulletType");
        BulletBehaviour behavior = BulletBehaviourFactory.getBehaviour(bulletType);
        behavior.onHit(bullet, noise);
    }
}
