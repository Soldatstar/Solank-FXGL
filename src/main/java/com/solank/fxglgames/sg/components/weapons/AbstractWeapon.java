package com.solank.fxglgames.sg.components.weapons;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.TransformComponent;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

import static com.almasb.fxgl.dsl.FXGL.getInput;
import static com.solank.fxglgames.sg.manager.StaticStrings.*;

public abstract class AbstractWeapon implements WeaponStrategy {
    protected final Entity entity;
    protected TransformComponent transform;
    protected ImageView weaponView;
    private final boolean cooldown = false;
    private final double elapsedTime = 0.0;


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
    public double getBulletSpeed() {
        return 1000;
    }

    @Override
    public ImageView getView() {
        return weaponView;
    }

    @Override
    public void shoot() {
        Point2D mousePosition = new Point2D(getInput().getMouseXWorld(), getInput().getMouseYWorld());
        Point2D weaponOuterPoint = getWeaponOuterPoint();
        Point2D directionToMouse = mousePosition.subtract(weaponOuterPoint).normalize();
        spawnBullet(directionToMouse);
    }

    @Override
    public double getCoolDownDecrement() {
        return getShotPauseDuration() * 125;
    }


    protected void spawnBullet(Point2D direction) {
        entity.getWorld().spawn(BULLET_ENTITY, new SpawnData().put(YUKINE_ENTITY, entity)
                .put("weaponOuterPoint", getWeaponOuterPoint())
                .put("direction", direction)
                .put("bulletSpeed", getBulletSpeed())
                .put("damage", getDamage())
                .put("bulletType", getBulletType())
                .put("hits", getHits()));

    }

}
