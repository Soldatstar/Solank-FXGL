package com.almasb.fxglgames.drop;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class SGApp extends GameApplication {
    private Entity yukine;
    private double jumpVelocity = -600;
    private boolean isJumping = false;

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
        //getAudioPlayer().pauseAllMusic();
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                if (yukine.getX() < getAppWidth() - 64)
                    yukine.translateX(15);
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                if (yukine.getX() > 0)
                    yukine.translateX(-15);
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onActionBegin() {
                jump();
            }
        }, KeyCode.SPACE);

        getInput().addAction(new UserAction("Shoot") {
            @Override
            protected void onActionBegin() {
                shoot();
            }
        }, MouseButton.SECONDARY);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 1000);

        onCollisionBegin(Type.YUKINE, Type.NOISE, (yukine, noise) -> {
            inc("Health", -5);
            noise.removeFromWorld();
            play("hit.wav");
        });

        onCollisionBegin(Type.BULLET, Type.NOISE, (bullet, noise) -> {
            noise.removeFromWorld();
            bullet.removeFromWorld();
            play("hit.wav");
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("Health", 100);
    }

    @Override
    protected void onUpdate(double tpf) {
        //move noise down when noise not on ground
        getGameWorld().getEntitiesByType(Type.NOISE).forEach(noise -> {
            if (noise.getY() < getAppHeight() - 64) {
                noise.translateY(5);
            }
        });
        getPhysicsWorld().onUpdate(tpf);

        if (isJumping) {
            double jumpDistance = jumpVelocity * tpf;
            double newY = yukine.getY() + jumpDistance;
            yukine.setY(newY);

            jumpVelocity += 1000 * tpf;

            if (newY >= getAppHeight() - 64) {
                yukine.setY(getAppHeight() - 64);
                isJumping = false;
                jumpVelocity = -600;
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

            double speed = 15;

            noise.translateX(directionX * speed * tpf);
            noise.translateY(directionY * speed * tpf);
        });

        getGameWorld().getEntitiesByType(Type.BULLET).forEach(bullet -> {
            if (bullet.getX() < 0|| bullet.getX() > getAppWidth()
                || bullet.getY() < 0 || bullet.getY() > getAppHeight()) {
                bullet.removeFromWorld();
                return;
            }

            double velocityX = bullet.getDouble("velocityX");
            double velocityY = bullet.getDouble("velocityY");

            bullet.translateX(velocityX * tpf);
            bullet.translateY(velocityY * tpf);
        });

        if (geti("Health") <= 0) {
            getGameController().startNewGame();
        }
    }

    @Override
    protected void initUI() {
        Label healthLabel = new Label();
        healthLabel.setTextFill(Color.LIGHTGRAY);
        healthLabel.setFont(Font.font(20.0));
        healthLabel.textProperty().bind(FXGL.getip("Health").asString("Health: %d"));
        addUINode(healthLabel, 20, 10);
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
        //randomly spawn noise at top of screen or from left/right side
        int side = random(0, 2);
        int x = 0;
        int y = 0;

        if (side == 0) {
            x = random(0, getAppWidth() - 64);
            y = -64;
        } else if (side == 1) {
            x = -64;
            y = random(0, getAppHeight() - 64);
        } else if (side == 2) {
            x = getAppWidth() + 64;
            y = random(0, getAppHeight() - 64);
        }

        Entity noise = entityBuilder()
            .type(Type.NOISE)
            .at(x, y)
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
        double mouseX = getInput().getMouseXWorld();
        double mouseY = getInput().getMouseYWorld();

        double directionX = mouseX - yukine.getX();
        double directionY = mouseY - yukine.getY();

        double length = Math.sqrt(directionX * directionX + directionY * directionY);
        directionX /= length;
        directionY /= length;

        double bulletSpeed = 1000;
        double bulletVelocityX = directionX * bulletSpeed;
        double bulletVelocityY = directionY * bulletSpeed;

        Entity bullet = entityBuilder()
            .type(Type.BULLET)
            .at(yukine.getX(), yukine.getY())
            .viewWithBBox("bullet.png")
            .collidable()
            .buildAndAttach();


        bullet.setProperty("damage", 10);
        bullet.setProperty("velocityX", bulletVelocityX);
        bullet.setProperty("velocityY", bulletVelocityY);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
