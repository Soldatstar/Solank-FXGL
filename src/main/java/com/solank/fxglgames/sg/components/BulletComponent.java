package com.solank.fxglgames.sg.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

public class BulletComponent extends Component {
    Entity bullet;

    @Override
    public void onAdded() {
        super.onAdded();
        bullet = getEntity();
    }

    @Override
    public void onUpdate(double tpf) {

        super.onUpdate(tpf);

        if (bullet.getX() < 0 || bullet.getX() > getAppWidth()
            || bullet.getY() < 0 || bullet.getY() > getAppHeight()) {
            bullet.removeFromWorld();
        }
}

}
