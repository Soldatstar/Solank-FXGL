package com.solank.fxglgames.sg.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.solank.fxglgames.sg.SGApp;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class SmallNoiseComponent extends AbstractNoiseComponent {

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

        Point2D direction = getDirection();

        double speed = 10000;

        noisePhysics.setVelocityX((direction.getX() * speed * tpf));
        noisePhysics.setVelocityY((direction.getY() * speed * tpf));
    }

    @Override
    public void onAdded() {
        super.onAdded();
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(upDown);

    }

    @Override
    public void onRemoved() {
    }


}
