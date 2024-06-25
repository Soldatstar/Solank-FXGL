package com.solank.fxglgames.sg.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;

public class AbstractNoiseComponent extends Component {
    protected AnimatedTexture texture;


    protected AnimationChannel upDown;
    protected Entity yukine;
    protected double distanceToTarget;


    protected PhysicsComponent noisePhysics;

    public AbstractNoiseComponent() {
    }

    protected Point2D getDirection() {
        double noiseX = getEntity().getX();
        double noiseY = getEntity().getY();

        double yukineX = yukine.getX();
        double yukineY = yukine.getY();

        double directionX = yukineX - noiseX;
        double directionY = yukineY - noiseY;

        distanceToTarget = Math.sqrt(directionX * directionX + directionY * directionY);

        if (distanceToTarget > 0) {
            directionX /= distanceToTarget;
            directionY /= distanceToTarget;
        }

        if (directionX < 0) {
            texture.setScaleX(-2);
        } else {
            texture.setScaleX(2);
        }

        return new Point2D(directionX, directionY);
    }

    @Override
    public void onAdded() {
        super.onAdded();
        noisePhysics= getEntity().getComponent(PhysicsComponent.class);
    }
}
