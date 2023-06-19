package com.solank.fxglgames.sg.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class PlayerComponent extends Component {
    private static final int PLAYER_SPEED = 300;
    private static final double JUMP_FORCE = 21000;
    private static final double GLIDE_FORCE = 800;
    private static final double JUMP_COOLDOWN = 0.5;
    private final AnimatedTexture texture;
    private final AnimationChannel left;
    private final AnimationChannel right;
    private final AnimationChannel upDown;
    private PhysicsComponent physics;
    private boolean physicsReady;
    private double jumpCooldown = 0;

    private boolean canJump = true;

    public PlayerComponent() {

        this.left = new AnimationChannel(FXGL.image("yukine/yukine-left.png"), Duration.seconds(0.5), 3);
        this.right = new AnimationChannel(FXGL.image("yukine/yukine-right.png"), Duration.seconds(0.5), 3);
        this.upDown = new AnimationChannel(FXGL.image("yukine/yukine-up-down.png"), Duration.seconds(1), 3);
        this.texture = new AnimatedTexture(upDown);
        texture.setScaleX(2);
        texture.setScaleY(2);
    }

    @Override
    public void onUpdate(double tpf) {
        if (!physics.isMoving() && texture.getAnimationChannel() != upDown) {
            texture.loopAnimationChannel(upDown);
        }
        // reduce jump cooldown
        jumpCooldown -= tpf;
        //when jump cooldown is 0, you can jump
        if (jumpCooldown <= 0) {
            canJump = true;
        } 
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(upDown);
        texture.setTranslateY(-15);
        texture.setTranslateZ(100);

        physics.setOnPhysicsInitialized(() -> physicsReady = true);

    }

    public void right() {
        if (texture.getAnimationChannel() != right) {
            texture.loopAnimationChannel(right);
        }

        if (!isPhysicsReady()) {
            return;
        }
        if (physics.isMovingY()) {
            physics.setVelocityX(Math.sqrt(PLAYER_SPEED * PLAYER_SPEED / 2f));
        } else {
            physics.setVelocityX(PLAYER_SPEED);
        }
    }

    public void left() {
        if (texture.getAnimationChannel() != left) {
            texture.loopAnimationChannel(left);
        }

        if (!isPhysicsReady()) {
            return;
        }
        if (physics.isMovingY()) {
            physics.setVelocityX(-Math.sqrt(PLAYER_SPEED * PLAYER_SPEED / 2f));
        } else {
            physics.setVelocityX(-PLAYER_SPEED);
        }
    }

    public void up() {
        if (texture.getAnimationChannel() != upDown) {
            texture.loopAnimationChannel(upDown);
        }
    }

    public void glide() {
        physics.applyForceToCenter(new Point2D(0, -GLIDE_FORCE));
    }


    public void down() {
        if (texture.getAnimationChannel() != upDown) {
            texture.loopAnimationChannel(upDown);
        }
    }

    public void stopMovingX() {
        if (!isPhysicsReady()) {
            return;
        }
        physics.setVelocityX(0);
    }

    public boolean isPhysicsReady() {
        return physicsReady;
    }

    public void jump() {
        if (canJump) {
            // Apply an upward force to the entity at its center point
            Point2D force = new Point2D(0, -JUMP_FORCE);
            Point2D pointOfApplication = entity.getCenter();
            physics.applyForce(force, pointOfApplication);
            //set jump cooldown
            jumpCooldown = JUMP_COOLDOWN;
            canJump = false;
        }

    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }
}
