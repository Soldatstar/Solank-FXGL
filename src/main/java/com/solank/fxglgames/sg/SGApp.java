package com.solank.fxglgames.sg;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.KeepOnScreenComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.Position;
import com.almasb.fxgl.ui.ProgressBar;
import com.solank.fxglgames.sg.collision.BulletNoiseCollisionHandler;
import com.solank.fxglgames.sg.collision.PlayerNoiseCollisionHandler;
import com.solank.fxglgames.sg.components.PlayerComponent;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.addUINode;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
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
import static com.almasb.fxgl.dsl.FXGL.loopBGM;
import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.random;
import static com.almasb.fxgl.dsl.FXGL.run;
import static com.almasb.fxgl.dsl.FXGL.runOnce;

public class SGApp extends GameApplication {
    private Entity yukine;
    private double jumpVelocity = -600;
    private double elapsedTime = 0.0;
    private ProgressBar cooldownBar;
    private ProgressBar hpBar ;
    private Rectangle cooldownBackground;
    private Text cooldownText;
    private static final double COOLDOWN_DURATION = 1.3;
    private static final double SHOT_PAUSE_DURATION = 0.2; // Adjust the value as needed
    private double elapsedTime2 = 0.0;
    private boolean cooldown;
    private GameWorld gameWorld;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("SG");
        settings.setVersion("0.0.1");
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setMainMenuEnabled(true);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("Health", 100.0);
        vars.put("Score", 0);
    }

    @Override
    protected void initGame() {
        gameWorld = getGameWorld();
        Texture backgroundTexture = FXGL.getAssetLoader().loadTexture("city.jpg");
        FXGL.getGameScene().setBackgroundRepeat(backgroundTexture.getImage());
       gameWorld.addEntityFactory(new SGFactory());
       gameWorld.create("Ground", new SpawnData());
        yukine = gameWorld.create("Yukine", new SpawnData((double) getAppWidth() / 2, getAppHeight() - (double) 64));

        run(this::spawnNoise, Duration.seconds(0.6));
        run(this::spawnNoise, Duration.seconds(1.4));
        loopBGM("bgm.mp3");
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
                yukine.getComponent(PlayerComponent.class).jump();
            }
        }, KeyCode.SPACE);

        getInput().addAction(new UserAction("Shoot") {
            @Override
            protected void onActionBegin() {

                if (elapsedTime >= SHOT_PAUSE_DURATION) {
                    shoot();
                } else {
                    System.out.println("Shot on cooldown");
                }
            }
        }, MouseButton.SECONDARY);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 8000);
        getPhysicsWorld().addCollisionHandler(new PlayerNoiseCollisionHandler());
        getPhysicsWorld().addCollisionHandler(new BulletNoiseCollisionHandler());

    }

    @Override
    protected void onUpdate(double tpf) {
        updateHealth();
        elapsedTime += tpf;
        elapsedTime2 += tpf;
        updateCooldownBar();

        getPhysicsWorld().onUpdate(tpf);



        getGameWorld().getEntitiesByType(Type.BULLET).forEach(bullet -> {
            if (bullet.getX() < 0 || bullet.getX() > getAppWidth()
                || bullet.getY() < 0 || bullet.getY() > getAppHeight()) {
                bullet.removeFromWorld();
                return;
            }
        });

        if (getd("Health") <= 0) {
            gameOver(false);
        }
    }

    private void updateHealth() {
        if (getd("Health") <100.0) {
            inc("Health", +0.05);
        }
    }

    @Override
    protected void initUI() {

        Label scoreLabel = new Label();
        scoreLabel.setTextFill(Color.LIGHTGRAY);
        scoreLabel.setFont(Font.font(20.0));
        scoreLabel.textProperty().bind(getip("Score").asString("Score: %d"));
        addUINode(scoreLabel, 20, 5);

        cooldownBar = new ProgressBar();
        cooldownBar.setMinValue(0);
        cooldownBar.setMaxValue(100);
        cooldownBar.prefWidth(200);
        cooldownBar.setCurrentValue(100);

        cooldownBackground = new Rectangle(200, 20);
        cooldownBackground.setFill(Color.WHITE);

        cooldownText = new Text();
        cooldownText.setText("COOLDOWN");
        cooldownText.setFill(Color.WHITE);
        cooldownText.setFont(Font.font(14));

        StackPane cooldownPane = new StackPane(cooldownBackground, cooldownBar, cooldownText);
        cooldownPane.setLayoutX(20);
        cooldownPane.setLayoutY(50);
        hpBar = new ProgressBar();
        hpBar.setMinValue(0);
        hpBar.setMaxValue(100);
        hpBar.setCurrentValue(100);
        hpBar.currentValueProperty().bind(getdp("Health"));
        hpBar.setWidth(300);
        hpBar.setLabelVisible(true);
        hpBar.setLabelPosition(Position.RIGHT);
        hpBar.setFill(Color.GREEN);
        hpBar.setLayoutX(20);
        hpBar.setLayoutY(80);

        getGameScene().addUINodes(hpBar);

        getGameScene().addUINodes(cooldownPane);
    }

    private void updateCooldownBar() {
        if (cooldownBar.getCurrentValue() < 100) {
            cooldownBar.setCurrentValue(cooldownBar.getCurrentValue() + 1);
        }
    }




    private void spawnNoise() {
        int side = random(0, 2);
        int x = 10;
        int y = 10;
        gameWorld.create("SmallNoise", new SpawnData(x, y).put("Yukine", yukine));
    }



    private void shoot() {
        if (cooldown) {
            return;
        }

        elapsedTime = 0.0;

        gameWorld.create("Bullet", new SpawnData().put("Yukine", yukine).put("mouseX", getInput().getMouseXWorld()).put("mouseY", getInput().getMouseYWorld()));

        cooldownBar.setCurrentValue(cooldownBar.getCurrentValue() - 20);

        // Start the cooldown timer
        if (cooldownBar.getCurrentValue() < 2) {
            cooldown = true;
            runOnce(() -> {
                cooldown = false;
            }, Duration.seconds(COOLDOWN_DURATION));
        }
    }






    private void gameOver(boolean reachedEndOfGame) {
        StringBuilder builder = new StringBuilder();
        builder.append("Game Over!\n\n");
        if (reachedEndOfGame) {
            builder.append("You have reached the end of the game!\n\n");
        }
        builder.append("Final score: ")
            .append(FXGL.geti("Score"));
        FXGL.getDialogService().showMessageBox(builder.toString(), () -> FXGL.getGameController().gotoMainMenu());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
