package com.solank.fxglgames.sg.components.weapons;

import com.almasb.fxgl.dsl.FXGL;
import com.solank.fxglgames.sg.components.WeaponComponent;
import javafx.scene.image.ImageView;
import com.almasb.fxgl.texture.Texture;

public class SmallGun implements Weapon{
    private WeaponComponent weaponComponent;
    private final double cooldownDuration;
    private final double shotPauseDuration;

    public SmallGun( double cooldownDuration, double shotPauseDuration) {
        createWeapon();
        this.cooldownDuration = cooldownDuration;
        this.shotPauseDuration = shotPauseDuration;
    }
    @Override
    public double getCooldownDuration() {
        return cooldownDuration;
    }

    @Override
    public double getShotPauseDuration() {
        return shotPauseDuration;
    }

    @Override
    public WeaponComponent getWeaponComponent() {
        return weaponComponent;
    }

    public void createWeapon() {
        Texture weaponTexture = FXGL.getAssetLoader().loadTexture("yukine/weapons/weapon.png");
        weaponTexture.setScaleX(5);
        weaponTexture.setScaleY(5);
        ImageView weaponView = new ImageView(weaponTexture.getImage());
        weaponView.setTranslateZ(-200);
        weaponView.setTranslateY(-15);
        weaponView.setTranslateX(-45);
        this.weaponComponent = new WeaponComponent(weaponView);
    }
}
