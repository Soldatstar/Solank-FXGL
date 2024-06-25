package com.solank.fxglgames.sg.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import com.solank.fxglgames.sg.SGApp;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class TallNoiseComponent extends AbstractNoiseComponent {
    //private final AnimatedTexture texture;
    //private final AnimationChannel upDown;
    //private final Entity yukine;
    //private final PhysicsComponent noisePhysics;

    private final LocalTimer freezeTimer;
    private final Duration freezeDuration = Duration.seconds(0.6);
    private double jumpCoolDown = 2;
    private boolean canJump = true;
    private boolean frozen = false;

    public TallNoiseComponent(Entity yukine) {

        //random int 1 or 2
        int randomInt = SGApp.random.nextInt(2) + 1;
        freezeTimer = FXGL.newLocalTimer();
        this.upDown =
                new AnimationChannel(FXGL.image("noise/tallNoise-up-down" + randomInt + ".png"), Duration.seconds(1), 3);

        this.texture = new AnimatedTexture(upDown);
        texture.setScaleX(2);
        texture.setScaleY(2);
        texture.setTranslateY(+10);

        this.yukine = yukine;
        if (texture.getAnimationChannel() != upDown) {
            texture.loopAnimationChannel(upDown);
        }
    }

    @Override
    public void onUpdate(double tpf) {

        if (!canJump) {
            jumpCoolDown -= tpf;
            if (jumpCoolDown < 0) {
                canJump = true;
                jumpCoolDown = 1;
            }
        }
        if (!frozen) {
            move(tpf);
        } else {
            if (freezeTimer.elapsed(freezeDuration)) {
                frozen = false;
                freezeTimer.capture();
                texture.play();
            }
        }
    }

    private void move(double tpf) {
        Point2D direction = getDirection();

        double speed = 8000;
        noisePhysics.setVelocityX((direction.getX() * speed * tpf));

        if (distanceToTarget < 200 && yukine.getY() < getEntity().getY() && canJump) {
            noisePhysics.setVelocityY(-speed * tpf * 3);
            canJump = false;
        }
    }

    public void freeze() {
        frozen = true;
        freezeTimer.capture();
        texture.stop();
    }


    @Override
    public void onAdded() {
        super.onAdded();
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(upDown);
        texture.setTranslateY(+14);
    }


}
