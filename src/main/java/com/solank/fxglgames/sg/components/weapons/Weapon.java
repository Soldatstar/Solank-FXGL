package com.solank.fxglgames.sg.components.weapons;

import com.solank.fxglgames.sg.components.WeaponComponent;

public interface Weapon {
     public double getCooldownDuration();
     public double getShotPauseDuration();
     public WeaponComponent getWeaponComponent();

}
