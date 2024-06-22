package com.solank.fxglgames.sg.components;

import com.almasb.fxgl.entity.Entity;
import com.solank.fxglgames.sg.components.weapons.Weapon;

public class Yukine extends Entity {
    Entity yukine;
    Weapon weapon;
    public Yukine() {
        super();
    }

    public void setWeapon(Weapon weapon) {

        //TODO: Handle switiching of weapons correctly, remove old weapon component

        yukine.addComponent(weapon.getWeaponComponent());
        this.weapon = weapon;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setYukine(Entity yukine) {
        this.yukine = yukine;
    }
}
