package com.solank.fxglgames.sg.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class PlayerComponent extends Component {

    // Constants for player movement
    private static final int PLAYER_SPEED = 300;
    private static final double JUMP_FORCE = 21000;
    private static final double GLIDE_FORCE = 800;
    private static final double JUMP_COOLDOWN = 0.8;

    // Animation channels
    private final AnimatedTexture texture;
    private final AnimationChannel left;
    private final AnimationChannel right;
    private final AnimationChannel upDown;

    // Physics and state variables
    private PhysicsComponent physics;
    private boolean physicsReady = false;
    private double jumpCooldown = 0;
    private boolean canJump = true;

    public PlayerComponent() {
        this.left = createAnimationChannel("yukine/yukine-left.png", Duration.seconds(0.5), 3);
        this.right = createAnimationChannel("yukine/yukine-right.png", Duration.seconds(0.5), 3);
        this.upDown = createAnimationChannel("yukine/yukine-up-down.png", Duration.seconds(1), 3);
        this.texture = new AnimatedTexture(upDown);
        configureTexture();
    }

    @Override
    public void onUpdate(double tpf) {
        handleIdleAnimation();
        updateJumpCooldown(tpf);
    }

    @Override
    public void onAdded() {
        configureEntityView();
        initializePhysics();
    }

    public void moveRight() {
        handleMovement(right, PLAYER_SPEED, 1);
    }

    public void moveLeft() {
        handleMovement(left, -PLAYER_SPEED, -1);
    }

    public void jump() {
        if (canJump) {
            applyJumpForce();
            startJumpCooldown();
        }
    }

    public void glide() {
        physics.applyForceToCenter(new Point2D(0, -GLIDE_FORCE));
    }

    public void stopMovingX() {
        if (isPhysicsReady()) {
            physics.setVelocityX(0);
        }
    }

    private AnimationChannel createAnimationChannel(String imagePath, Duration duration, int frames) {
        return new AnimationChannel(FXGL.image(imagePath), duration, frames);
    }

    private void configureTexture() {
        texture.setScaleX(2);
        texture.setScaleY(2);
    }

    private void handleIdleAnimation() {
        if (!physics.isMoving() && texture.getAnimationChannel() != upDown) {
            texture.loopAnimationChannel(upDown);
        }
    }

    private void updateJumpCooldown(double tpf) {
        jumpCooldown -= tpf;
        if (jumpCooldown <= 0) {
            setCanJump(true);
        }
    }

    private void configureEntityView() {
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(upDown);
        texture.setTranslateY(-15);
        texture.setTranslateZ(100);
    }

    private void initializePhysics() {
        physics.setOnPhysicsInitialized(() -> physicsReady = true);
    }

    private void handleMovement(AnimationChannel channel, int speed, int direction) {
        if (texture.getAnimationChannel() != channel) {
            texture.loopAnimationChannel(channel);
        }

        if (!isPhysicsReady()) {
            return;
        }

        if (physics.isMovingY()) {
            physics.setVelocityX(direction * Math.sqrt(PLAYER_SPEED * PLAYER_SPEED / 2f));
        } else {
            physics.setVelocityX(speed);
        }
    }

    private void applyJumpForce() {
        Point2D force = new Point2D(0, -JUMP_FORCE);
        Point2D pointOfApplication = entity.getCenter();
        physics.applyForce(force, pointOfApplication);
    }

    private void startJumpCooldown() {
        jumpCooldown = JUMP_COOLDOWN;
        canJump = false;
    }

    private boolean isPhysicsReady() {
        return physicsReady;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }
}
