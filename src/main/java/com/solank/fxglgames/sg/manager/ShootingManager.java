package com.solank.fxglgames.sg.manager;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.getInput;
import static com.almasb.fxgl.dsl.FXGL.runOnce;
import static com.solank.fxglgames.sg.SGApp.YUKINE_ENTITY;

public class ShootingManager {

    private static final double COOLDOWN_DURATION = 1;
    private static final double SHOT_PAUSE_DURATION = 0.15;
    private final GameWorld gameWorld;
    private final Entity yukine;
    private final ProgressBar cooldownBar;
    private boolean cooldown;
    private double elapsedTime;

    public ShootingManager(GameWorld gameWorld, Entity yukine, ProgressBar cooldownBar) {
        this.gameWorld = gameWorld;
        this.yukine = yukine;
        this.cooldownBar = cooldownBar;
        this.cooldown = false;
        this.elapsedTime = 0.0;
    }

    public void update(double tpf) {
        elapsedTime += tpf;
    }

    public void shoot() {
        if (cooldown || elapsedTime < SHOT_PAUSE_DURATION) {
            return;
        }

        elapsedTime = 0.0;

        gameWorld.create("Bullet", new SpawnData().put(YUKINE_ENTITY, yukine).put("mouseX", getInput().getMouseXWorld())
                .put("mouseY", getInput().getMouseYWorld()));

        cooldownBar.setCurrentValue(cooldownBar.getCurrentValue() - 20);

        if (cooldownBar.getCurrentValue() < 2) {
            cooldown = true;
            runOnce(() -> cooldown = false, Duration.seconds(COOLDOWN_DURATION));
        }
    }
}
