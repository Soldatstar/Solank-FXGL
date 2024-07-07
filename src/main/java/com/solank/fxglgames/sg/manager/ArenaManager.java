package com.solank.fxglgames.sg.manager;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.solank.fxglgames.sg.manager.StaticStrings.*;
import static com.solank.fxglgames.sg.manager.StaticStrings.YUKINE_ENTITY;

public class ArenaManager {
    private final GameWorld gameWorld;
    private final Entity yukine;
    public static final int NOISE_SPAWN_INTERVAL_SIDE = 2;
    public static final int NOISE_SPAWN_INTERVAL_TOP = 2;
    private int Wave = 1;
    public ArenaManager(GameWorld gameWorld, Entity yukine) {
        this.gameWorld = gameWorld;
        this.yukine = yukine;
    }

    public void spawnNoise() {
        run(this::spawnNoiseSide, Duration.seconds(NOISE_SPAWN_INTERVAL_SIDE));
        run(this::spawnNoiseTop, Duration.seconds(NOISE_SPAWN_INTERVAL_TOP));
    }

    private void spawnNoiseTop() {
        int x = random(0, getAppWidth());
        int y = 10;
        gameWorld.create(SMALL_NOISE_ENTITY, new SpawnData(x + yukine.getX(), y).put(YUKINE_ENTITY, yukine));
    }

    private void spawnNoiseSide() {
        int side = random(0, 1);
        int appWidth = (getAppWidth() + 20) / 2;
        int x = (int) yukine.getX() + appWidth;
        int y = getAppHeight() - 100;
        if (side == 0) {
            x = (int) yukine.getX() - appWidth;
        }
        gameWorld.create(TALL_NOISE_ENTITY, new SpawnData(x, y).put(YUKINE_ENTITY, yukine));
    }
}
