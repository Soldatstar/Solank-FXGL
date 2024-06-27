package com.solank.fxglgames.sg;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthDoubleComponent;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.ProgressBar;
import com.solank.fxglgames.sg.components.weapons.SmallGun;
import com.solank.fxglgames.sg.components.weapons.WeaponComponent;
import com.solank.fxglgames.sg.manager.WeaponManager;
import com.solank.fxglgames.sg.model.SGFactory;
import javafx.util.Duration;

import java.util.Map;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAudioPlayer;
import static com.solank.fxglgames.sg.manager.StaticStrings.*;

public class SGApp extends GameApplication {

    public static final int HEALTH_REGEN_RATE = 1;
    public static HealthIntComponent YUKINE_HEALTH;
    public static final int CLOUD_SPAWN_INTERVAL = 500;
    public static final int WALL_START_POSITION = 0;
    public static final int WALL_END_POSITION = 20000;
    public static final int NOISE_SPAWN_INTERVAL_SIDE = 2;
    public static final int NOISE_SPAWN_INTERVAL_TOP = 2;

    public static final Random random = new Random();
    public static Entity yukine;

    private final Init init = new Init(this);
    private ProgressBar cooldownBar;
    private ProgressBar hpBar;
    private GameWorld gameWorld;
    private Music bgm;
    private WeaponManager shootingManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        init.initSettings(settings);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put(SCORE_ENTITY, 0);
        bgm = FXGL.getAssetLoader().loadMusic("bgm.mp3");
    }

    @Override
    protected void initGame() {
        FXGL.getAudioPlayer().loopMusic(bgm);
        gameWorld = getGameWorld();
        getInput().setRegisterInput(true);
        getInput().setProcessInput(true);

        Texture backgroundTexture = FXGL.getAssetLoader().loadTexture("background/city.jpg");
        FXGL.getGameScene().setBackgroundRepeat(backgroundTexture.getImage());
        gameWorld.addEntityFactory(new SGFactory());

        spawnInitialEntities();

        yukine = spawnYukine();
        YUKINE_HEALTH = yukine.getComponent(HealthIntComponent.class);

        setupYukineWeapon();

        setupViewport();

        spawnClouds();
        spawnWalls();

        run(this::spawnNoiseSide, Duration.seconds(NOISE_SPAWN_INTERVAL_SIDE));
        run(this::spawnNoiseTop, Duration.seconds(NOISE_SPAWN_INTERVAL_TOP));
    }

    @Override
    protected void initPhysics() {
        init.initPhysics();
    }

    @Override
    protected void initUI() {
        init.initScoreLabel();
        init.initCooldownBar();
        init.initHPBar();
        shootingManager = new WeaponManager(gameWorld, yukine, cooldownBar);
        init.initFactory();
    }

    @Override
    protected void onUpdate(double tpf) {
        updateHealth();
        updateCooldownBar();
        shootingManager.update(tpf);
        getPhysicsWorld().onUpdate(tpf);

        checkGameOverConditions();
    }

    @Override
    protected void initInput() {
        init.initInput();
    }

    private void updateCooldownBar() {
        if (cooldownBar.getCurrentValue() < 100) {
            cooldownBar.setCurrentValue(cooldownBar.getCurrentValue() + 0.75);
        }
    }

    private void updateHealth() {
        if (YUKINE_HEALTH.getValue() < YUKINE_HEALTH.getMaxValue()) {
            YUKINE_HEALTH.restore(HEALTH_REGEN_RATE);
        }
    }

    private void spawnNoiseTop() {
        int x = random(0, getAppWidth());
        int y = 10;
        gameWorld.spawn(SMALL_NOISE_ENTITY, new SpawnData(x + yukine.getX(), y).put(YUKINE_ENTITY, yukine));
    }

    private void spawnNoiseSide() {
        int side = random(0, 1);
        int appWidth = (getAppWidth() + 20) / 2;
        int x = (int) yukine.getX() + appWidth;
        int y = getAppHeight() - 100;
        if (side == 0) {
            x = (int) yukine.getX() - appWidth;
        }
        gameWorld.spawn(TALL_NOISE_ENTITY, new SpawnData(x, y).put(YUKINE_ENTITY, yukine));
    }


    private void checkGameOverConditions() {
        if (YUKINE_HEALTH.isZero()) {
            gameOver(false);
        }

        if (yukine.getX() > 19800) {
            // TODO: Add real win condition
            gameOver(true);
        }
    }

    private Entity spawnYukine() {
        return gameWorld.spawn(YUKINE_ENTITY, new SpawnData((double) getAppWidth() / 2, getAppHeight() - 64));
    }

    private void setupYukineWeapon() {
        SmallGun smallGun = new SmallGun(yukine);
        WeaponComponent activeWeapon = new WeaponComponent(smallGun);
        yukine.addComponent(activeWeapon);
    }

    private void setupViewport() {
        getGameScene().getViewport().setBounds(0, 0, Integer.MAX_VALUE, getAppHeight() + 100);
        getGameScene().getViewport().bindToEntity(yukine, (double) getAppWidth() / 2, ((double) getAppHeight() / 2) + 300);
    }

    private void spawnInitialEntities() {
        gameWorld.spawn(GROUND_ENTITY, new SpawnData());
    }

    private void spawnClouds() {
        for (int i = 0; i < 50; i++) {
            int x = CLOUD_SPAWN_INTERVAL * i;
            int rnd = random.nextInt(100);
            gameWorld.spawn("Cloud", new SpawnData(x, 100 + rnd));
        }
    }

    private void spawnWalls() {
        gameWorld.spawn("Wall", new SpawnData(WALL_START_POSITION, 0));
        gameWorld.spawn("Wall", new SpawnData(WALL_END_POSITION, 0));
    }

    public WeaponManager getShootingManager() {
        return shootingManager;
    }

    public ProgressBar getHpBar() {
        return hpBar;
    }

    public void setHpBar(ProgressBar hpBar) {
        this.hpBar = hpBar;
    }

    public ProgressBar getCooldownBar() {
        return cooldownBar;
    }

    public void setCooldownBar(ProgressBar cooldownBar) {
        this.cooldownBar = cooldownBar;
    }

    private void gameOver(boolean reachedEndOfGame) {
        getInput().setRegisterInput(false);
        getInput().setProcessInput(false);
        FXGL.getAudioPlayer().stopMusic(bgm);
        StringBuilder builder = new StringBuilder();
        builder.append("Game Over!\n\n");
        if (reachedEndOfGame) {
            builder.append("You have reached the end of the game!\n\n");
            builder.append("Thank you for trying this Demo!\n\n");
        }
        builder.append("Final score: ")
                .append(FXGL.geti(SCORE_ENTITY));
        FXGL.getDialogService().showMessageBox(builder.toString(), () -> FXGL.getGameController().gotoMainMenu());
    }
}
