package com.solank.fxglgames.sg;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.ProgressBar;
import com.solank.fxglgames.sg.components.WeaponComponent;
import com.solank.fxglgames.sg.components.WeaponStrategy;
import com.solank.fxglgames.sg.components.weapons.BigGun;
import com.solank.fxglgames.sg.components.weapons.SmallGun;
import com.solank.fxglgames.sg.manager.WeaponManager;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAudioPlayer;

public class SGApp extends GameApplication {
    public static final String VERSION = "0.0.3";
    public static final String TITLE = "SG";
    public static final double HEALTH_REGENRATE = 0.05;
    public static final String YUKINE_ENTITY = "Yukine";
    static final String HEALTH_ENTITY = "Health";
    static final String SCORE_ENTITY = "Score";
    static final Double YUKINE_MAX_HEALTH = 150.0;
    public static Random random = new Random();
    public static Entity yukine;
    private final com.solank.fxglgames.sg.init init = new init(this);
    private ProgressBar cooldownBar;
    private ProgressBar hpBar;
    private GameWorld gameWorld;
    private Music bgm;
    private WeaponManager shootingManager;
    private WeaponComponent activeWeapon;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {

        init.initSettings(settings);
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

        Texture backgroundTexture = FXGL.getAssetLoader().loadTexture("background/city.jpg");
        FXGL.getGameScene().setBackgroundRepeat(backgroundTexture.getImage());
        gameWorld.addEntityFactory(new SGFactory());
        gameWorld.create("Ground", new SpawnData());
        yukine = gameWorld.create(YUKINE_ENTITY, new SpawnData((double) getAppWidth() / 2, getAppHeight() - (double) 64));
        SmallGun smallGun = new SmallGun(yukine);
        activeWeapon = new WeaponComponent(smallGun);
        yukine.addComponent(activeWeapon);
        BigGun bigGun = new BigGun(yukine);
        getGameScene().getViewport().setBounds(0, 0, Integer.MAX_VALUE, getAppHeight() + 100);
        getGameScene().getViewport().bindToEntity(yukine, getAppWidth() / 2, (getAppHeight() / 2) + 300);



        int x;
        for (int i = 0; i < 50; i++) {
            x = 500 * i;
            int rnd = random.nextInt(100);
            gameWorld.spawn("Cloud", new SpawnData(x, 100 + rnd));
        }
        gameWorld.spawn("Wall", new SpawnData(0, 0));
        gameWorld.spawn("Wall", new SpawnData(20000, 0));
        run(this::SpawnNoiseSide, Duration.seconds(2.4));
        run(this::SpawnNoiseTop, Duration.seconds(2));
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

    }

    @Override
    protected void onUpdate(double tpf) {
        updateHealth();
        updateCooldownBar();
        shootingManager.update(tpf);
        getPhysicsWorld().onUpdate(tpf);

        if (getd(HEALTH_ENTITY) <= 0) {
            gameOver(false);
        }

        if (yukine.getX() > 19800) {
            //TODO: Add real win condition
            gameOver(true);
        }
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
        if (getd(HEALTH_ENTITY) < YUKINE_MAX_HEALTH) {
            inc(HEALTH_ENTITY, +HEALTH_REGENRATE);
        }
    }

    private void SpawnNoiseTop() {
        int x = random(0, getAppWidth());
        int y = 10;
        gameWorld.create("SmallNoise", new SpawnData(x + yukine.getX(), y).put(YUKINE_ENTITY, yukine));
    }

    private void SpawnNoiseSide() {
        int side = random(0, 1);
        int appWidth = (getAppWidth() + 20) / 2;
        int x = (int) yukine.getX() + appWidth;
        int y = getAppHeight() - 100;
        if (side == 0) {
            x = (int) yukine.getX() - appWidth;
        }
        gameWorld.create("TallNoise", new SpawnData(x, y).put(YUKINE_ENTITY, yukine));
    }

    private void gameOver(boolean reachedEndOfGame) {
        getInput().setRegisterInput(false);
        getInput().setProcessInput(false);
        getAudioPlayer().stopMusic(bgm);
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
}
