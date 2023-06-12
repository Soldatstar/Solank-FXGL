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
import com.solank.fxglgames.sg.collision.BulletSmallNoiseCollisionHandler;
import com.solank.fxglgames.sg.collision.BulletTallNoiseCollisionHandler;
import com.solank.fxglgames.sg.collision.PlayerSmallNoiseCollisionHandler;
import com.solank.fxglgames.sg.collision.PlayerTallNoiseCollisionHandler;
import com.solank.fxglgames.sg.components.PlayerComponent;
import com.solank.fxglgames.sg.components.WeaponComponent;
import com.solank.fxglgames.sg.ui.SGMainMenu;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.Map;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.addUINode;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getFXApp;
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
    public static final String VERSION = "0.0.3pre";
    public static final String TITLE = "SG";
    public static final double HEALTH_REGENRATE = 0.05;
    public static Random random = new Random();
    private Entity yukine;
    private double elapsedTime = 0.0;
    private ProgressBar cooldownBar;
    private ProgressBar hpBar;

    private static final double COOLDOWN_DURATION = 1.0;
    private static final double SHOT_PAUSE_DURATION = 0.15;
    private boolean cooldown;
    private GameWorld gameWorld;
    private static final String YUKINE_ENTITY = "Yukine";
    private static final String HEALTH_ENTITY = "Health";
    private static final String SCORE_ENTITY = "Score";
    private static final Double YUKINE_MAX_HEALTH = 150.0;

    private Music bgm;

    @Override
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

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put(HEALTH_ENTITY, YUKINE_MAX_HEALTH);
        vars.put(SCORE_ENTITY, 0);
        bgm = FXGL.getAssetLoader().loadMusic("bgm.mp3");
    }

    @Override
    protected void initGame() {
        FXGL.getAudioPlayer().loopMusic(bgm);
        gameWorld = getGameWorld();
        getInput().setRegisterInput(true);
        getInput().setProcessInput(true);

        Texture backgroundTexture = FXGL.getAssetLoader().loadTexture("city.jpg");
        FXGL.getGameScene().setBackgroundRepeat(backgroundTexture.getImage());
        gameWorld.addEntityFactory(new SGFactory());
        gameWorld.create("Ground", new SpawnData());
        yukine =
            gameWorld.create(YUKINE_ENTITY, new SpawnData((double) getAppWidth() / 2, getAppHeight() - (double) 64));

        yukine.addComponent(WeaponComponent.createWeapon());

        getGameScene().getViewport().setBounds(0, 0, Integer.MAX_VALUE, getAppHeight() + 100);
        getGameScene().getViewport().bindToEntity(yukine, getAppWidth() / 2, (getAppHeight() / 2)+300);
        //spawn 20 BGbuildings
        for (int i = 0; i < 50; i++) {
            gameWorld.spawn("BGBuilding", new SpawnData(random(0, 20000), getAppHeight()));
        }
        run(this::SpawnNoiseSide, Duration.seconds(2.4));
        run(this::SpawnNoiseTop, Duration.seconds(2));
    }



    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 600);
        getPhysicsWorld().addCollisionHandler(new PlayerSmallNoiseCollisionHandler());
        getPhysicsWorld().addCollisionHandler(new BulletSmallNoiseCollisionHandler());
        getPhysicsWorld().addCollisionHandler(new BulletTallNoiseCollisionHandler());
        getPhysicsWorld().addCollisionHandler(new PlayerTallNoiseCollisionHandler());

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

    }


    private static void initScoreLabel() {
        Label scoreLabel = new Label();
        scoreLabel.setTextFill(Color.ORANGERED);
        scoreLabel.setEffect(new DropShadow(15, Color.WHITE));
        scoreLabel.setFont(Font.font(30.0));
        scoreLabel.setAlignment(Pos.CENTER);
        scoreLabel.textProperty().bind(getip(SCORE_ENTITY).asString("%d"));
        addUINode(scoreLabel, getAppWidth()/2,2 );
    }

    private void initHPBar() {
        hpBar =new ProgressBar(false);
        hpBar.setMinValue(0);
        hpBar.setMaxValue(YUKINE_MAX_HEALTH);
        hpBar.setCurrentValue(YUKINE_MAX_HEALTH);
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
        cooldownBar = new ProgressBar(false);
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
            cooldownBar.setCurrentValue(cooldownBar.getCurrentValue() + 0.75);
        }
    }

    private void updateHealth() {
        if (getd(HEALTH_ENTITY) < YUKINE_MAX_HEALTH) {
            inc(HEALTH_ENTITY, +HEALTH_REGENRATE);
        }
    }


    private void SpawnNoiseTop() {
        int side = random(0, getAppWidth());
        int x = side;
        int y = 10;
        gameWorld.create("SmallNoise", new SpawnData(x+yukine.getX(), y).put(YUKINE_ENTITY, yukine));
    }

    private void SpawnNoiseSide() {
        int side = random(0, 1);

        //int x at the right side of the screen relative to yukine X coordinate
        int x = (int) (getAppWidth() - 20+yukine.getX());
        int y = getAppHeight() -100;
        if (side == 0) {
            x = (int) (20+yukine.getX());
        }
        gameWorld.create("TallNoise", new SpawnData(x, y).put(YUKINE_ENTITY, yukine));
    }


    private void shoot() {
        if (cooldown) {
            return;
        }

        elapsedTime = 0.0;

        gameWorld.create("Bullet", new SpawnData().put(YUKINE_ENTITY, yukine).put("mouseX", getInput().getMouseXWorld())
            .put("mouseY", getInput().getMouseYWorld()));

        cooldownBar.setCurrentValue(cooldownBar.getCurrentValue() - 20);

        if (cooldownBar.getCurrentValue() < 2) {
            cooldown = true;
            runOnce(() -> {
                cooldown = false;
            }, Duration.seconds(COOLDOWN_DURATION));
        }
    }


    private void gameOver(boolean reachedEndOfGame) {
        getInput().setRegisterInput(false);
        getInput().setProcessInput(false);
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
