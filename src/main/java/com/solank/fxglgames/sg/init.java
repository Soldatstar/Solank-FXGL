package com.solank.fxglgames.sg;

import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.ui.Position;
import com.almasb.fxgl.ui.ProgressBar;
import com.solank.fxglgames.sg.collision.BulletNoiseCollisionHandler;
import com.solank.fxglgames.sg.collision.BulletStaticCollisionHandler;
import com.solank.fxglgames.sg.collision.PlayerNoiseCollisionHandler;
import com.solank.fxglgames.sg.components.PlayerComponent;
import com.solank.fxglgames.sg.model.SGFactory;
import com.solank.fxglgames.sg.ui.SGMainMenu;
import static com.solank.fxglgames.sg.manager.StaticStrings.*;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class init {
    private final com.solank.fxglgames.sg.SGApp SGApp;

    public init(com.solank.fxglgames.sg.SGApp SGApp) {
        this.SGApp = SGApp;
    }

    protected void initInput() {
        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                com.solank.fxglgames.sg.SGApp.yukine.getComponent(PlayerComponent.class).moveRight();
            }

            @Override
            protected void onActionEnd() {
                com.solank.fxglgames.sg.SGApp.yukine.getComponent(PlayerComponent.class).stopMovingX();
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                com.solank.fxglgames.sg.SGApp.yukine.getComponent(PlayerComponent.class).moveLeft();
            }

            @Override
            protected void onActionEnd() {
                com.solank.fxglgames.sg.SGApp.yukine.getComponent(PlayerComponent.class).stopMovingX();
            }
        }, KeyCode.A);

        FXGL.getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onAction() {
                com.solank.fxglgames.sg.SGApp.yukine.getComponent(PlayerComponent.class).glide();
            }

            @Override
            protected void onActionBegin() {
                com.solank.fxglgames.sg.SGApp.yukine.getComponent(PlayerComponent.class).jump();
            }
        }, KeyCode.SPACE);

        FXGL.getInput().addAction(new UserAction("Shoot") {
            @Override
            protected void onAction() {
                SGApp.getShootingManager().shoot();

            }
        }, MouseButton.PRIMARY);

        FXGL.getInput().addAction(new UserAction("Left") {
            @Override
            protected void onActionBegin() {
                FXGL.getInput().mockKeyPress(KeyCode.A);
            }

            @Override
            protected void onActionEnd() {
                FXGL.getInput().mockKeyRelease(KeyCode.A);
            }
        }, KeyCode.LEFT);

        FXGL.getInput().addAction(new UserAction("Right") {
            @Override
            protected void onActionBegin() {
                FXGL.getInput().mockKeyPress(KeyCode.D);
            }

            @Override
            protected void onActionEnd() {
                FXGL.getInput().mockKeyRelease(KeyCode.D);
            }
        }, KeyCode.RIGHT);


        FXGL.getInput().addAction(new UserAction("Change Weapon") {
            @Override
            protected void onActionBegin() {
                SGApp.getShootingManager().switchWeapon();
            }
        }, KeyCode.Q);

        //getInput().addAction(new UserAction("Jump") {
        //    @Override
        //    protected void onActionBegin() {
        //        getInput().mockKeyPress(KeyCode.SPACE);
        //    }

        //    @Override
        //    protected void onActionEnd() {
        //        getInput().mockKeyRelease(KeyCode.SPACE);
        //    }
        //}, KeyCode.UP);
    }

    void initHPBar() {
        SGApp.setHpBar(new ProgressBar(false));
        SGApp.getHpBar().setMinValue(0);
        SGApp.getHpBar().setMaxValue(com.solank.fxglgames.sg.SGApp.YUKINE_MAX_HEALTH);
        SGApp.getHpBar().setCurrentValue(com.solank.fxglgames.sg.SGApp.YUKINE_MAX_HEALTH);
        SGApp.getHpBar().currentValueProperty().bind(FXGL.getdp(HEALTH_ENTITY));
        SGApp.getHpBar().setWidth(300);
        SGApp.getHpBar().setLabelVisible(true);
        SGApp.getHpBar().setLabelPosition(Position.RIGHT);
        SGApp.getHpBar().setFill(Color.GREEN);
        SGApp.getHpBar().setLayoutX(FXGL.getAppWidth() - 390);
        SGApp.getHpBar().setLayoutY(15);

        FXGL.getGameScene().addUINodes(SGApp.getHpBar());
    }


    void initCooldownBar() {
        SGApp.setCooldownBar(new ProgressBar(false));
        SGApp.getCooldownBar().setMinValue(0);
        SGApp.getCooldownBar().setMaxValue(100);
        SGApp.getCooldownBar().prefWidth(200);
        SGApp.getCooldownBar().setCurrentValue(100);
        FXGL.getGameScene().addUINodes(SGApp.getCooldownBar());
    }

    void initScoreLabel() {
        Label scoreLabel = new Label();
        scoreLabel.setTextFill(Color.ORANGERED);
        scoreLabel.setEffect(new DropShadow(15, Color.WHITE));
        scoreLabel.setFont(Font.font(30.0));
        scoreLabel.setAlignment(Pos.CENTER);
        scoreLabel.textProperty().bind(FXGL.getip(SCORE_ENTITY).asString("%d"));
        FXGL.addUINode(scoreLabel, (double) FXGL.getAppWidth() / 2, 2);
    }

    protected void initSettings(GameSettings settings) {
        settings.setTitle(TITLE);
        settings.setVersion(VERSION);
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(false);

        settings.setSceneFactory(new SceneFactory() {

            @Override
            public FXGLMenu newMainMenu() {
                return new SGMainMenu();
            }
        });
    }

    protected void initPhysics() {
        FXGL.getPhysicsWorld().setGravity(0, 600);
        FXGL.getPhysicsWorld().addCollisionHandler(new PlayerNoiseCollisionHandler());
        FXGL.getPhysicsWorld().addCollisionHandler(new BulletNoiseCollisionHandler());
        FXGL.getPhysicsWorld().addCollisionHandler(new BulletStaticCollisionHandler());
    }

    public void initFactory() {
        Image explosion = FXGL.getAssetLoader().loadImage("yukine/weapons/Explosion.png");
        SGFactory.setExplosionWidth(explosion.getWidth() / 2);
        SGFactory.setExplosionHeight(explosion.getHeight() / 2);
    }
}