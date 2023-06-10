package com.solank.fxglgames.sg;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.Position;
import com.almasb.fxgl.ui.ProgressBar;
import com.solank.fxglgames.sg.collision.BulletNoiseCollisionHandler;
import com.solank.fxglgames.sg.collision.PlayerNoiseCollisionHandler;
import com.solank.fxglgames.sg.components.PlayerComponent;
import com.solank.fxglgames.sg.components.WeaponComponent;
import com.solank.fxglgames.sg.ui.SGMainMenu;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.addUINode;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getInput;
import static com.almasb.fxgl.dsl.FXGL.getPhysicsWorld;
import static com.almasb.fxgl.dsl.FXGL.getd;
import static com.almasb.fxgl.dsl.FXGL.getdp;
import static com.almasb.fxgl.dsl.FXGL.getip;
import static com.almasb.fxgl.dsl.FXGL.inc;
import static com.almasb.fxgl.dsl.FXGL.random;
import static com.almasb.fxgl.dsl.FXGL.run;
import static com.almasb.fxgl.dsl.FXGL.runOnce;

public class SGApp extends GameApplication {
    private Entity yukine;
    private double elapsedTime = 0.0;
    private ProgressBar cooldownBar;
    private ProgressBar hpBar;
    private Rectangle cooldownBackground;

    private static final double COOLDOWN_DURATION = 1.3;
    private static final double SHOT_PAUSE_DURATION = 0.2;
    private boolean cooldown;
    private GameWorld gameWorld;
    private static final String YUKINE_ENTITY = "Yukine";
    private static final String HEALTH_ENTITY = "Health";
    private static final String SCORE_ENTITY = "Score";
    private Music bgm;


    //override menu during game

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("SG");
        settings.setVersion("0.0.2");
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

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put(HEALTH_ENTITY, 100.0);
        vars.put(SCORE_ENTITY, 0);
        bgm = FXGL.getAssetLoader().loadMusic("bgm.mp3");
    }

    @Override
    protected void initGame() {
        FXGL.getAudioPlayer().loopMusic(bgm);
        gameWorld = getGameWorld();
        Texture backgroundTexture = FXGL.getAssetLoader().loadTexture("city.jpg");
        FXGL.getGameScene().setBackgroundRepeat(backgroundTexture.getImage());
        gameWorld.addEntityFactory(new SGFactory());
        gameWorld.create("Ground", new SpawnData());
        yukine =
            gameWorld.create(YUKINE_ENTITY, new SpawnData((double) getAppWidth() / 2, getAppHeight() - (double) 64));

        yukine.addComponent(WeaponComponent.createWeapon());
        run(this::SpawnNoiseSide, Duration.seconds(1.8));
        run(this::SpawnNoiseTop, Duration.seconds(1.4));
    }



    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 600);
        getPhysicsWorld().addCollisionHandler(new PlayerNoiseCollisionHandler());
        getPhysicsWorld().addCollisionHandler(new BulletNoiseCollisionHandler());

    }


    @Override
    protected void initUI() {
        initScoreLabel();
        initCooldownBar();
        initHPBar();
    }

    @Override
    protected void onUpdate(double tpf) {
        updateHealth();
        elapsedTime += tpf;
        updateCooldownBar();
        getPhysicsWorld().onUpdate(tpf);

        if (getd(HEALTH_ENTITY) <= 0) {
            gameOver(false);
        }

    }

    @Override
    protected void initInput() {

        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                yukine.getComponent(PlayerComponent.class).right();
            }

            @Override
            protected void onActionEnd() {
                yukine.getComponent(PlayerComponent.class).stopMovingX();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                yukine.getComponent(PlayerComponent.class).left();
            }

            @Override
            protected void onActionEnd() {
                yukine.getComponent(PlayerComponent.class).stopMovingX();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onAction() {
                yukine.getComponent(PlayerComponent.class).glide();

            }

            @Override
            protected void onActionBegin() {
                yukine.getComponent(PlayerComponent.class).jump();
            }
        }, KeyCode.SPACE);

        getInput().addAction(new UserAction("Shoot") {
            @Override
            protected void onActionBegin() {

                if (elapsedTime >= SHOT_PAUSE_DURATION) {
                    shoot();
                }
            }
        }, MouseButton.SECONDARY);


        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onActionBegin() {
                getInput().mockKeyPress(KeyCode.A);
            }

            @Override
            protected void onActionEnd() {
                getInput().mockKeyRelease(KeyCode.A);
            }
        }, KeyCode.LEFT);


        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onActionBegin() {
                getInput().mockKeyPress(KeyCode.D);
            }

            @Override
            protected void onActionEnd() {
                getInput().mockKeyRelease(KeyCode.D);
            }
        }, KeyCode.RIGHT);


        getInput().addAction(new UserAction("Jump2") {
            @Override
            protected void onAction() {
                yukine.getComponent(PlayerComponent.class).glide();

            }

            @Override
            protected void onActionBegin() {
                yukine.getComponent(PlayerComponent.class).jump();
            }
        }, KeyCode.W);


        getInput().addAction(new UserAction("Jump3") {
            @Override
            protected void onAction() {
                yukine.getComponent(PlayerComponent.class).glide();

            }

            @Override
            protected void onActionBegin() {
                yukine.getComponent(PlayerComponent.class).jump();
            }
        }, KeyCode.UP);
    }


    private static void initScoreLabel() {
        Label scoreLabel = new Label();
        scoreLabel.setTextFill(Color.YELLOWGREEN);
        scoreLabel.setEffect(new DropShadow(5, Color.BLACK));
        scoreLabel.setFont(Font.font(30.0));
        scoreLabel.setAlignment(Pos.CENTER);
        scoreLabel.textProperty().bind(getip(SCORE_ENTITY).asString("%d"));
        addUINode(scoreLabel, getAppWidth()/2,2 );
    }

    private void initHPBar() {
        hpBar = new ProgressBar();
        hpBar.setMinValue(0);
        hpBar.setMaxValue(100);
        hpBar.setCurrentValue(100);
        hpBar.currentValueProperty().bind(getdp(HEALTH_ENTITY));
        hpBar.setWidth(300);
        hpBar.setLabelVisible(true);
        hpBar.setLabelPosition(Position.RIGHT);
        hpBar.setFill(Color.GREEN);
        hpBar.setLayoutX(getAppWidth() - 390);
        hpBar.setLayoutY(15);

        getGameScene().addUINodes(hpBar);
    }

    private void initCooldownBar() {
        cooldownBar = new ProgressBar();
        cooldownBar.setMinValue(0);
        cooldownBar.setMaxValue(100);
        cooldownBar.prefWidth(200);
        cooldownBar.setCurrentValue(100);
        StackPane cooldownPane = new StackPane(cooldownBar);
        cooldownPane.setLayoutX(20);
        cooldownPane.setLayoutY(5);
        getGameScene().addUINodes(cooldownPane);
    }

    private void updateCooldownBar() {
        if (cooldownBar.getCurrentValue() < 100) {
            cooldownBar.setCurrentValue(cooldownBar.getCurrentValue() + 1);
        }
    }

    private void updateHealth() {
        if (getd(HEALTH_ENTITY) < 100.0) {
            inc(HEALTH_ENTITY, +0.05);
        }
    }


    private void SpawnNoiseTop() {
        int side = random(0, getAppWidth());
        int x = side;
        int y = 10;
        gameWorld.create("SmallNoise", new SpawnData(x, y).put(YUKINE_ENTITY, yukine));
    }

    private void SpawnNoiseSide() {
        int side = random(0, 1);
        int x = getAppWidth() - 20;
        int y = getAppHeight() - 50;
        if (side == 0) {
            x = 20;
        }
        gameWorld.create("SmallNoise", new SpawnData(x, y).put(YUKINE_ENTITY, yukine));
    }


    private void shoot() {
        if (cooldown) {
            return;
        }

        elapsedTime = 0.0;

        gameWorld.create("Bullet", new SpawnData().put(YUKINE_ENTITY, yukine).put("mouseX", getInput().getMouseXWorld())
            .put("mouseY", getInput().getMouseYWorld()));

        cooldownBar.setCurrentValue(cooldownBar.getCurrentValue() - 30);

        if (cooldownBar.getCurrentValue() < 2) {
            cooldown = true;
            runOnce(() -> {
                cooldown = false;
            }, Duration.seconds(COOLDOWN_DURATION));
        }
    }


    private void gameOver(boolean reachedEndOfGame) {
        cooldown = false;
        FXGL.getAudioPlayer().stopMusic(bgm);
        StringBuilder builder = new StringBuilder();
        builder.append("Game Over!\n\n");
        if (reachedEndOfGame) {
            builder.append("You have reached the end of the game!\n\n");
        }
        builder.append("Final score: ")
            .append(FXGL.geti(SCORE_ENTITY));
        FXGL.getDialogService().showMessageBox(builder.toString(), () -> FXGL.getGameController().gotoMainMenu());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
