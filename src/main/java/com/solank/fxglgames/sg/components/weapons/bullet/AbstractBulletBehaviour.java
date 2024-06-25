package com.solank.fxglgames.sg.components.weapons.bullet;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
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
        long startTime = System.nanoTime();
        play("hit/enemyhit.wav");

        int noiseRemainingHealth = noise.getComponent(HealthIntComponent.class).getValue();
        int damage = bullet.getInt("damage");
        int score = noise.getInt("score");
        handleDamage(noise, damage, noiseRemainingHealth, score);
        long endTime = System.nanoTime();
        //print time in ns
        System.out.println("AbstractBulletBehaviour.onHit() took " + (endTime - startTime) + " ns");

    }

    protected void handleDamage(Entity noise, int damage, double noiseRemainingHealth, int score) {
        if (damage >= noiseRemainingHealth) {
            inc("Score", +score);
            noise.removeFromWorld();
        } else {
            handlePartialDamage(noise, damage);
        }
    }

    private void handlePartialDamage(Entity noise, int damage) {
        noise.getComponent(HealthIntComponent.class).damage(damage);

        //noise.getComponentOptional(TallNoiseComponent.class).ifPresent(TallNoiseComponent::freeze);

        PhysicsComponent physics = noise.getComponent(PhysicsComponent.class);
        double pushDirection = (yukine.getX() < noise.getX()) ? PUSH_BACK_FORCE : -PUSH_BACK_FORCE;
        physics.setLinearVelocity(pushDirection, 0);
    }
}
