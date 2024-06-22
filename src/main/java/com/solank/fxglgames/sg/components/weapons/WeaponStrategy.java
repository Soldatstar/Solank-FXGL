package com.solank.fxglgames.sg.components.weapons;

import com.solank.fxglgames.sg.model.bullet.BulletType;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public interface WeaponStrategy {
    void onAdded();

    void onUpdate(double tpf);

    Point2D getWeaponOuterPoint();

    double getCooldownDuration();

    double getShotPauseDuration();

    ImageView getView();

    double getDamage();

    BulletType getBulletType();

    int getHits();
}
