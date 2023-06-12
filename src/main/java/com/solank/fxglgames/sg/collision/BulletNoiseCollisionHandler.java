package com.solank.fxglgames.sg.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.solank.fxglgames.sg.Type;
import com.solank.fxglgames.sg.components.TallNoiseComponent;

import static com.almasb.fxgl.dsl.FXGLForKtKt.inc;
import static com.almasb.fxgl.dsl.FXGLForKtKt.play;
import static com.solank.fxglgames.sg.SGApp.yukine;

public class BulletNoiseCollisionHandler extends CollisionHandler {
    int pushBackForce = 200;

    public BulletNoiseCollisionHandler() {
        super(Type.BULLET, Type.NOISE);
    }

    @Override
    protected void onCollisionBegin(Entity a, Entity b) {
        play("hit/enemyhit.wav");
        double noiseRemainingHealth = b.getDouble("health");
        double damage = a.getDouble("damage");
        int score = b.getInt("score");


        if (damage >= noiseRemainingHealth) {
            inc("Score", +score);
            b.removeFromWorld();
        } else {
            b.setProperty("health", noiseRemainingHealth - damage);
            b.getComponentOptional(TallNoiseComponent.class).ifPresent(TallNoiseComponent::freeze);
            //push back noise away from bullet
            if (yukine.getX() < b.getX()) {
                b.getComponent(PhysicsComponent.class).setLinearVelocity(pushBackForce, 0);
            } else {
                b.getComponent(PhysicsComponent.class).setLinearVelocity((-1) * pushBackForce, 0);
            }
        }
        a.removeFromWorld();
    }
}
