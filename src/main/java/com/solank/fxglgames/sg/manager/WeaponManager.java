package com.solank.fxglgames.sg.manager;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.ui.ProgressBar;
import com.solank.fxglgames.sg.components.weapons.BigGun;
import com.solank.fxglgames.sg.components.weapons.SmallGun;
import com.solank.fxglgames.sg.components.weapons.WeaponComponent;
import com.solank.fxglgames.sg.components.weapons.WeaponStrategy;
import javafx.util.Duration;

import java.util.ArrayList;

import static com.almasb.fxgl.dsl.FXGL.getInput;
import static com.almasb.fxgl.dsl.FXGL.runOnce;
import static com.solank.fxglgames.sg.SGApp.YUKINE_ENTITY;

public class WeaponManager {
    private final GameWorld gameWorld;
    private final Entity yukine;
    private final ProgressBar cooldownBar;
    private boolean cooldown;
    private double elapsedTime;
    private final WeaponComponent activeWeapon;
    private final ArrayList<WeaponStrategy> weapons = new ArrayList<>();

    public WeaponManager(GameWorld gameWorld, Entity yukine, ProgressBar cooldownBar) {
        this.gameWorld = gameWorld;
        this.yukine = yukine;
        this.cooldownBar = cooldownBar;
        this.cooldown = false;
        this.elapsedTime = 0.0;
        this.activeWeapon = yukine.getComponent(WeaponComponent.class);
        weapons.add(new SmallGun(yukine));
        weapons.add(new BigGun(yukine));
    }

    public void update(double tpf) {
        elapsedTime += tpf;
    }

    public void shoot() {
        if (cooldown || elapsedTime < activeWeapon.getShotPauseDuration()) {
            return;
        }

        elapsedTime = 0.0;
        System.out.println(YUKINE_ENTITY);
        System.out.println(yukine);

        gameWorld.create("Bullet", new SpawnData().put(YUKINE_ENTITY, yukine).put("mouseX", getInput().getMouseXWorld())
                .put("mouseY", getInput().getMouseYWorld()));

        cooldownBar.setCurrentValue(cooldownBar.getCurrentValue() - 20);

        if (cooldownBar.getCurrentValue() < 2) {
            cooldown = true;
            runOnce(() -> cooldown = false, Duration.seconds(activeWeapon.getCooldownDuration()));
        }
    }

    public void switchWeapon() {
        yukine.getViewComponent().removeChild(activeWeapon.getWeapon().getView());

        if (activeWeapon.getWeapon() instanceof SmallGun) {
            activeWeapon.setWeapon(weapons.get(1));
        } else {
            activeWeapon.setWeapon(weapons.get(0));
        }
        yukine.getViewComponent().addChild(activeWeapon.getWeapon().getView());
    }
}
