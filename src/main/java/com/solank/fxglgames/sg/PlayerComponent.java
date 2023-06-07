package com.solank.fxglgames.sg;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class PlayerComponent extends Component {
    private static final int PLAYER_SPEED = 300;
    private final AnimatedTexture texture;

    private final AnimationChannel left;
    private final AnimationChannel right;
    private final AnimationChannel upDown;
    private PhysicsComponent physics;
    private boolean physicsReady;
    private double jumpForce = 1000; // Adjust the jump force as needed
    private boolean canJump = true;

    public PlayerComponent() {

        this.left = new AnimationChannel(FXGL.image("yukine-left.png"), Duration.seconds(1), 3);
        this.right = new AnimationChannel(FXGL.image("yukine-right.png"), Duration.seconds(1), 3);
        this.upDown = new AnimationChannel(FXGL.image("yukine-up-down.png"), Duration.seconds(1), 3);
        this.texture = new AnimatedTexture(upDown);
        texture.setScaleX(2);
        texture.setScaleY(2);
    }

    @Override
    public void onUpdate(double tpf) {
        if (!physics.isMoving()) {
            if (texture.getAnimationChannel() != upDown) {
                texture.loopAnimationChannel(upDown);
            }
        }
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(upDown);

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
            Point2D force = new Point2D(0, -jumpForce);
            Point2D pointOfApplication = entity.getCenter();
            physics.applyForce(force, pointOfApplication);
            canJump = false;
        }
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }
}
