package com.solank.fxglgames.sg.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.solank.fxglgames.sg.Type;
import com.solank.fxglgames.sg.components.TallNoiseComponent;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGLForKtKt.inc;
import static com.almasb.fxgl.dsl.FXGLForKtKt.play;

public class BulletTallNoiseCollisionHandler extends CollisionHandler {
    public BulletTallNoiseCollisionHandler() {
        super(Type.BULLET, Type.TALLNOISE);
    }

    private PhysicsComponent tallNoisePhysics;
    private double pushbackSpeed = 1000;

    @Override
    protected void onCollisionBegin(Entity bullet, Entity tallNoise) {
        tallNoisePhysics = tallNoise.getComponent(PhysicsComponent.class);
        double bulletX = bullet.getX();
        double tallNoiseX = tallNoise.getX();

        if (tallNoise.getComponent(TallNoiseComponent.class).getHealth() == 1) {
            inc("Score", +20);
            tallNoise.removeFromWorld();
        } else {
            tallNoise.getComponent(TallNoiseComponent.class).setHealth(tallNoise.getComponent(TallNoiseComponent.class).getHealth() - 1);
            if (bulletX > tallNoiseX) {
                tallNoisePhysics.setLinearVelocity(-pushbackSpeed, 0);
            } else {
                tallNoisePhysics.setLinearVelocity(pushbackSpeed, 0);
            }
        }

        bullet.removeFromWorld();
        play("hit.wav");
    }
}
