package com.solank.fxglgames.sg.manager;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.ui.ProgressBar;
import com.solank.fxglgames.sg.components.weapons.*;
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
    private int weaponIndex = 0;

    public WeaponManager(GameWorld gameWorld, Entity yukine, ProgressBar cooldownBar) {
        this.gameWorld = gameWorld;
        this.yukine = yukine;
        this.cooldownBar = cooldownBar;
        this.cooldown = false;
        this.elapsedTime = 0.0;
        this.activeWeapon = yukine.getComponent(WeaponComponent.class);
        weapons.add(new SmallGun(yukine));
        weapons.add(new BigGun(yukine));
        weapons.add(new LowGun(yukine));
    }

    public void update(double tpf) {
        elapsedTime += tpf;
    }

    public void shoot() {
        if (cooldown || elapsedTime < activeWeapon.getShotPauseDuration()) {
            return;
        }

        elapsedTime = 0.0;
        activeWeapon.shoot();



        cooldownBar.setCurrentValue(cooldownBar.getCurrentValue() - activeWeapon.getCoolDownDecrement());

        if (cooldownBar.getCurrentValue() < 2) {
            cooldown = true;
            runOnce(() -> cooldown = false, Duration.seconds(activeWeapon.getCooldownDuration()));
        }
    }

    public void switchWeapon() {
        yukine.getViewComponent().removeChild(activeWeapon.getWeapon().getView());

        activeWeapon.setWeapon(weapons.get(++weaponIndex % weapons.size()));

        yukine.getViewComponent().addChild(activeWeapon.getWeapon().getView());
    }
}
