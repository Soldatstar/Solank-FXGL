package com.solank.fxglgames.sg.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

public class SelfDestructComponent extends Component {
    private final Duration duration;
    private LocalTimer timer;

    public SelfDestructComponent(Duration seconds) {
        this.duration = seconds;
    }

    @Override
    public void onAdded() {
        timer = FXGL.newLocalTimer();
        timer.capture();
    }

    @Override
    public void onUpdate(double tpf) {
        if (timer.elapsed(duration)) {
            entity.removeFromWorld();
        }
    }
}
