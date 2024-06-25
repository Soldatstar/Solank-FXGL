package com.solank.fxglgames.sg.components.weapons;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.solank.fxglgames.sg.components.weapons.bullet.BulletType;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public class BigGun extends AbstractWeapon {

    public BigGun(Entity entity) {
        super(entity);
        Texture weaponTexture = FXGL.getAssetLoader().loadTexture("yukine/weapons/BigGun.png");
        weaponTexture.setScaleX(5);
        weaponTexture.setScaleY(5);
        this.weaponView = new ImageView(weaponTexture.getImage());
        weaponView.setTranslateZ(-200);
        weaponView.setTranslateY(-15);
        weaponView.setTranslateX(-45);
    }

    @Override
    public Point2D getWeaponOuterPoint() {
        double angle = Math.toRadians(weaponView.getRotate());
        double offsetX = 60; // Adjust the X offset based on the weapon's dimensions
        double offsetY = 60; // Adjust the Y offset based on the weapon's dimensions

        double outerX = transform.getX() + offsetX * Math.cos(angle);
        double outerY = transform.getY() + offsetY * Math.sin(angle);

        return new Point2D(outerX + 10, outerY - 10);
    }

    @Override
    public double getCooldownDuration() {
        return 1.5;
    }

    @Override
    public double getShotPauseDuration() {
        return 0.45;
    }

    @Override
    public int getDamage() {
        return 20;
    }

    @Override
    public BulletType getBulletType() {
        return BulletType.ARMOR_PIERCING_BULLET;
    }

    @Override
    public int getHits() {
        return 2;
    }


}
