package com.solank.fxglgames.sg.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

public class SmallNoiseComponent extends Component {
    private PhysicsComponent physics;
    private Entity yukine;

    public SmallNoiseComponent(Entity yukine) {
        this.yukine = yukine;
    }


    @Override
    public void onUpdate(double tpf) {
        double noiseX = getEntity().getX();
        double noiseY = getEntity().getY();

        double yukineX = yukine.getX();
        double yukineY = yukine.getY();

        double directionX = yukineX - noiseX;
        double directionY = yukineY - noiseY;

        double length = Math.sqrt(directionX * directionX + directionY * directionY);

        if (length > 0) {
            directionX /= length;
            directionY /= length;
        }

        double speed = 255;

        getEntity().translateX(directionX * speed * tpf);
        getEntity().translateY(directionY * speed * tpf);
    }
}
