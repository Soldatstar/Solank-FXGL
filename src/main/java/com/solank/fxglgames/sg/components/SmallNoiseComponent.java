package com.solank.fxglgames.sg.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.solank.fxglgames.sg.SGApp;
import javafx.util.Duration;

public class SmallNoiseComponent extends Component {
    private final AnimatedTexture texture;
    private final AnimationChannel upDown;
    private final Entity yukine;
    private PhysicsComponent physics;

    public SmallNoiseComponent(Entity yukine) {
        int randomInt = SGApp.random.nextInt(4) + 1;
        this.upDown =
                new AnimationChannel(FXGL.image("noise/noise-up-down" + randomInt + ".png"), Duration.seconds(0.5), 3);

        this.texture = new AnimatedTexture(upDown);
        texture.setScaleX(2);
        texture.setScaleY(2);
        texture.setTranslateY(+10);

        this.yukine = yukine;
        if (texture.getAnimationChannel() != upDown) {
            texture.loopAnimationChannel(upDown);
        }
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


        if (directionX < 0) {
            texture.setScaleX(-2);
        } else {
            texture.setScaleX(2);
        }

        double speed = 10000;


        getEntity().getComponent(PhysicsComponent.class).setVelocityX((directionX * speed * tpf));
        getEntity().getComponent(PhysicsComponent.class).setVelocityY((directionY * speed * tpf));
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(upDown);
    }
}
