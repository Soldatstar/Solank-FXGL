package com.solank.fxglgames.sg.components;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

public interface WeaponStrategy{
    void onAdded();
    void onUpdate(double tpf);
    Point2D getWeaponOuterPoint();
    public double getCooldownDuration();
    public double getShotPauseDuration();

    ImageView getView();
}
