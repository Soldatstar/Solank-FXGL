package com.solank.fxglgames.sg.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import com.solank.fxglgames.sg.SGApp;
import javafx.util.Duration;

public class TallNoiseComponent extends Component {
    private final AnimatedTexture texture;
    private final AnimationChannel upDown;
    private final Entity yukine;
    private final LocalTimer freezeTimer;
    private final Duration freezeDuration = Duration.seconds(0.7);
    private PhysicsComponent physics;
    private double jumpCoolDown = 2;
    private boolean canJump = true;
    private boolean frozen = false;
    private int health = 2;


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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
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
        double noiseX = getEntity().getX();
        double noiseY = getEntity().getY();

        double yukineX = yukine.getX();
        double yukineY = yukine.getY();

        double directionX = yukineX - noiseX;
        double directionY = yukineY - noiseY;

        double length = Math.sqrt(directionX * directionX + directionY * directionY);

        if (length > 0) {
            directionX /= length;
        }

        if (directionX < 0) {
            texture.setScaleX(-2);
        } else {
            texture.setScaleX(2);
        }

        double speed = 8000;
        getEntity().getComponent(PhysicsComponent.class).setVelocityX((directionX * speed * tpf));

        if (length < 200 && yukineY < noiseY && canJump) {
            getEntity().getComponent(PhysicsComponent.class).setVelocityY(-speed * tpf * 3);
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
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(upDown);
        texture.setTranslateY(+14);
    }

    public PhysicsComponent getPhysics() {
        return physics;
    }
}
