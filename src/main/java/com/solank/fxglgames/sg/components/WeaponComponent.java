package com.solank.fxglgames.sg.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public class WeaponComponent extends Component {
    private ImageView weaponView;
    private TransformComponent transform;

    public WeaponComponent(ImageView weaponView) {
        this.weaponView = weaponView;
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
        Point2D entityPosition = transform.getPosition();

        // Calculate the angle between the entity and the mouse cursor
        double angle = Math.atan2(mousePosition.getY() - entityPosition.getY(),
            mousePosition.getX() - entityPosition.getX());

        // Set the rotation angle of the weapon to point towards the mouse cursor
        weaponView.setRotate(Math.toDegrees(angle));
    }
}
