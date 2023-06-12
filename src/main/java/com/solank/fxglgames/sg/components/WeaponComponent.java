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

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(weaponView);
        transform = entity.getComponent(TransformComponent.class);
        weaponView.setTranslateZ(-200);
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

    public static WeaponComponent createWeapon() {
        Texture weaponTexture = FXGL.getAssetLoader().loadTexture("yukine/weapons/weapon.png");
        weaponTexture.setScaleX(5);
        weaponTexture.setScaleY(5);
        ImageView weaponView = new ImageView(weaponTexture.getImage());
        weaponView.setTranslateZ(-200);
        weaponView.setTranslateX(-25); // Adjust the position of the weapon relative to the entity

        // Create the weapon component and add it to the entity
        return new  WeaponComponent(weaponView);
    }
}
