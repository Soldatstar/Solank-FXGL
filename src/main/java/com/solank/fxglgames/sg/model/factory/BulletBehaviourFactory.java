package com.solank.fxglgames.sg.model.factory;

import com.solank.fxglgames.sg.model.bullet.ArmourPiercingBulletBehaviour;
import com.solank.fxglgames.sg.model.bullet.BulletBehaviour;
import com.solank.fxglgames.sg.model.bullet.RegularBulletBehaviour;
import com.solank.fxglgames.sg.model.bullet.BulletType;

public class BulletBehaviourFactory {
    public static BulletBehaviour getBehaviour(BulletType type) {
        switch (type) {
            case NORMAL_BULLET:
                return new RegularBulletBehaviour();
            case ARMOR_PIERCING_BULLET:
                return new ArmourPiercingBulletBehaviour();
            default:
                throw new IllegalArgumentException("Unknown bullet type: " + type);
        }
    }
}
