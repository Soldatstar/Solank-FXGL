package com.solank.fxglgames.sg.components.weapons;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.TransformComponent;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public abstract class AbstractWeapon implements WeaponStrategy {
    protected TransformComponent transform;
    protected ImageView weaponView;
    private final Entity entity;


    public AbstractWeapon(Entity entity) {
        this.entity = entity;
        transform = entity.getComponent(TransformComponent.class);
    }


    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(weaponView);
        transform = entity.getComponent(TransformComponent.class);
    }

    @Override
    public void onUpdate(double tpf) {
        Point2D mousePosition = FXGL.getInput().getMousePositionUI();
        Point2D entityPosition = transform.getPosition().subtract(FXGL.getGameScene().getViewport().getOrigin());
        double angle = Math.atan2(mousePosition.getY() - entityPosition.getY(),
                mousePosition.getX() - entityPosition.getX());
        weaponView.setRotate(Math.toDegrees(angle));
    }

    @Override
    public ImageView getView() {
        return weaponView;
    }


}
