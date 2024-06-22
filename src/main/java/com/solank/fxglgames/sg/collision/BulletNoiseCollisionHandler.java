package com.solank.fxglgames.sg.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.solank.fxglgames.sg.components.TallNoiseComponent;
import com.solank.fxglgames.sg.model.bullet.BulletBehaviour;
import com.solank.fxglgames.sg.model.factory.BulletBehaviourFactory;
import com.solank.fxglgames.sg.model.bullet.BulletType;
import com.solank.fxglgames.sg.model.Type;

import static com.almasb.fxgl.dsl.FXGLForKtKt.inc;
import static com.almasb.fxgl.dsl.FXGLForKtKt.play;
import static com.solank.fxglgames.sg.SGApp.yukine;

public class BulletNoiseCollisionHandler extends CollisionHandler {
    static int PUSH_BACK_FORCE = 200;

    public BulletNoiseCollisionHandler() {
        super(Type.BULLET, Type.NOISE);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity noise) {
        play("hit/enemyhit.wav");

        double noiseRemainingHealth = noise.getDouble("health");
        double damage = bullet.getDouble("damage");
        BulletType bulletType = bullet.getObject("bulletType");
        int score = noise.getInt("score");

        if (damage >= noiseRemainingHealth) {
            inc("Score", +score);
            noise.removeFromWorld();
        } else {
            handlePartialDamage(noise, damage);
        }

        BulletBehaviour behavior = BulletBehaviourFactory.getBehaviour(bulletType);
        behavior.onHit(bullet, noise);
    }

    private void handlePartialDamage(Entity noise, double damage) {
        double newHealth = noise.getDouble("health") - damage;
        noise.setProperty("health", newHealth);

        noise.getComponentOptional(TallNoiseComponent.class).ifPresent(TallNoiseComponent::freeze);

        PhysicsComponent physics = noise.getComponent(PhysicsComponent.class);
        double pushDirection = (yukine.getX() < noise.getX()) ? PUSH_BACK_FORCE : -PUSH_BACK_FORCE;
        physics.setLinearVelocity(pushDirection, 0);
    }

}
