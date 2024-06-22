package com.solank.fxglgames.sg.components.weapons;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import javafx.geometry.Point2D;

public class WeaponComponent extends Component {
    private TransformComponent transform;
    private WeaponStrategy weapon;

    public WeaponComponent(WeaponStrategy weapon) {
        this.weapon = weapon;
    }

    @Override
    public void onAdded() {
        weapon.onAdded();
    }

    @Override
    public void onUpdate(double tpf) {
        weapon.onUpdate(tpf);
    }

    public Point2D getWeaponOuterPoint() {
        return weapon.getWeaponOuterPoint();
    }

    public double getCooldownDuration() {
        return weapon.getCooldownDuration();
    }

    public double getShotPauseDuration() {
        return weapon.getShotPauseDuration();
    }

    public WeaponStrategy getWeapon() {
        return weapon;
    }

    public void setWeapon(WeaponStrategy weapon) {
        this.weapon = weapon;
    }

    public Double getDamage() {
        return weapon.getDamage();
    }
}
