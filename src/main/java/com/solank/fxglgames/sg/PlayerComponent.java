package com.solank.fxglgames.sg;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

public class PlayerComponent extends Component {
    private final AnimatedTexture texture;

    private final AnimationChannel left;
    private final AnimationChannel right;
    private final AnimationChannel upDown;

    public PlayerComponent(AnimatedTexture texture, AnimationChannel left, AnimationChannel right,
                           AnimationChannel upDown) {
        this.left = new AnimationChannel(FXGL.image("yukine-left.png"), Duration.seconds(0.5), 3);
        this.right = new AnimationChannel(FXGL.image("yukine-right.png"), Duration.seconds(0.5), 3);
        this.upDown = new AnimationChannel(FXGL.image("yukine-up-down.png"), Duration.seconds(0.5), 3);
        this.texture = new AnimatedTexture(upDown);
    }
}
