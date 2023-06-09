package com.solank.fxglgames.sg.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public class WeaponComponent extends Component {
    private final ImageView weaponView;
    private TransformComponent transform;

    public WeaponComponent(ImageView weaponView) {
        this.weaponView = weaponView;
    }

    public static WeaponComponent createWeapon() {
        Texture weaponTexture = FXGL.getAssetLoader().loadTexture("yukine/weapons/weapon.png");
        weaponTexture.setScaleX(5);
        weaponTexture.setScaleY(5);
        ImageView weaponView = new ImageView(weaponTexture.getImage());
        weaponView.setTranslateZ(-200);
        weaponView.setTranslateY(-15);
        weaponView.setTranslateX(-45);
        return new WeaponComponent(weaponView);
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(weaponView);
        transform = entity.getComponent(TransformComponent.class);
    }

    @Override
    public void onUpdate(double tpf) {
        // Get the position of the mouse cursor
        Point2D mousePosition = FXGL.getInput().getMousePositionUI();

        // Get the position of the entity
        Point2D entityPosition = transform.getPosition().subtract(FXGL.getGameScene().getViewport().getOrigin());

        // Calculate the angle between the entity and the mouse cursor
        double angle = Math.atan2(mousePosition.getY() - entityPosition.getY(),
            mousePosition.getX() - entityPosition.getX());

        // Set the rotation angle of the weapon to point towards the mouse cursor
        weaponView.setRotate(Math.toDegrees(angle));
    }

    public Point2D getWeaponOuterPoint() {
        double angle = Math.toRadians(weaponView.getRotate());
        double offsetX = 60; // Adjust the X offset based on the weapon's dimensions
        double offsetY = 60; // Adjust the Y offset based on the weapon's dimensions

        double outerX = transform.getX() + offsetX * Math.cos(angle);
        double outerY = transform.getY() + offsetY * Math.sin(angle);

        return new Point2D(outerX + 10, outerY - 10);
    }
}
