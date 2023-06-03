package com.almasb.fxglgames.drop;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class SGApp extends GameApplication {
    private double jumpVelocity = -600;
    private boolean isJumping = false;

    private Entity yukine;

    public enum Type {
        NOISE, YUKINE, BULLET
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("SG");
        settings.setVersion("0.0.1");
        settings.setWidth(1280);
        settings.setHeight(720);
    }

    @Override
    protected void initGame() {
        Texture backgroundTexture = FXGL.getAssetLoader().loadTexture("city.jpg");
        FXGL.getGameScene().setBackgroundRepeat(backgroundTexture.getImage());

        spawnYukine();
        run(this::spawnNoise, Duration.seconds(1));
        loopBGM("bgm.mp3");
        getAudioPlayer().pauseAllMusic();
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                if (yukine.getX() < getAppWidth() - 64)
                    yukine.translateX(25);
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                if (yukine.getX() > 0)
                    yukine.translateX(-25);
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onActionBegin() {
                jump();
            }
        }, KeyCode.SPACE); // Adjust the key binding as desired
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(Type.YUKINE, Type.NOISE, (bucket, droplet) -> {
            FXGL.inc("score", 10);
            droplet.removeFromWorld();
            play("drop.wav");
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
    }

    @Override
    protected void onUpdate(double tpf) {
        getGameWorld().getEntitiesByType(Type.NOISE).forEach(droplet -> droplet.translateY(150 * tpf));

        if (isJumping) {
            double jumpDistance = jumpVelocity * tpf;
            double newY = yukine.getY() + jumpDistance;
            yukine.setY(newY);

            jumpVelocity += 1000 * tpf; // Adjust the value as needed to control the gravity effect

            // Check if character has reached the ground
            if (newY >= getAppHeight() - 64) { // Adjust the value as needed based on character's size
                yukine.setY(getAppHeight() - 64);
                isJumping = false;
                jumpVelocity = -600; // Reset the jump velocity
            }
        }

        getGameWorld().getEntitiesByType(Type.NOISE).forEach(noise -> {
            double noiseX = noise.getX();
            double noiseY = noise.getY();

            double yukineX = yukine.getX();
            double yukineY = yukine.getY();

            double directionX = yukineX - noiseX;
            double directionY = yukineY - noiseY;

            double length = Math.sqrt(directionX * directionX + directionY * directionY);

            if (length > 0) {
                directionX /= length;
                directionY /= length;
            }

            double speed = 150;

            noise.translateX(directionX * speed * tpf);
            noise.translateY(directionY * speed * tpf);
        });
    }

    @Override
    protected void initUI() {
        Label scoreLabel = new Label();
        scoreLabel.setTextFill(Color.LIGHTGRAY);
        scoreLabel.setFont(Font.font(20.0));
        scoreLabel.textProperty().bind(FXGL.getip("score").asString("Score: %d"));
        FXGL.addUINode(scoreLabel, 20, 10);
    }

    private void spawnYukine() {
        yukine = entityBuilder()
            .type(Type.YUKINE)
            .at((double) getAppWidth() / 2, getAppHeight() - (double)64)
            .viewWithBBox("Yukine.png")
            .collidable()
            .buildAndAttach();
    }

    private void spawnNoise() {
        entityBuilder()
            .type(Type.NOISE)
            .at(FXGLMath.random(0, getAppWidth() - 64), 0)
            .viewWithBBox("noise.png")
            .collidable()
            .buildAndAttach();
    }
    private void jump() {
        if (!isJumping) {
            isJumping = true;
        }
    }

    private void shoot() {
        entityBuilder()
            .type(Type.BULLET)
            .at(yukine.getX(), yukine.getY())
            .viewWithBBox("bullet.png")
            .collidable()
            .buildAndAttach();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
