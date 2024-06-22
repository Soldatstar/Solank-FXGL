package com.solank.fxglgames.sg.components.weapons;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

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
