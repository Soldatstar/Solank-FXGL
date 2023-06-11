package com.solank.fxglgames.sg.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

public class BulletComponent extends Component {
    Entity bullet;
    double bulletflytime = 0;
    Double maxflytime = 2.0;

    @Override
    public void onAdded() {
        super.onAdded();
        bullet = getEntity();
    }

    @Override
    public void onUpdate(double tpf) {

        super.onUpdate(tpf);
            bulletflytime += tpf;
            if (bulletflytime > maxflytime) {
                bullet.removeFromWorld();
            }
}

}
