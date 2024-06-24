package com.solank.fxglgames.sg.model.bullet;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.solank.fxglgames.sg.components.TallNoiseComponent;

import static com.almasb.fxgl.dsl.FXGLForKtKt.inc;
import static com.almasb.fxgl.dsl.FXGLForKtKt.play;
import static com.solank.fxglgames.sg.SGApp.yukine;

public class AbstractBulletBehaviour implements BulletBehaviour {

    static int PUSH_BACK_FORCE = 200;

    @Override
    public void onHit(Entity bullet, Entity noise) {
        play("hit/enemyhit.wav");

        double noiseRemainingHealth = noise.getDouble("health");
        double damage = bullet.getDouble("damage");
        int score = noise.getInt("score");
        handleDamage(noise, damage, noiseRemainingHealth, score);


    }

    protected void handleDamage(Entity noise, double damage, double noiseRemainingHealth, int score) {
        if (damage >= noiseRemainingHealth) {
            inc("Score", +score);
            noise.removeFromWorld();
        } else {
            handlePartialDamage(noise, damage);
        }
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
