package com.solank.fxglgames.sg.model.factory;

import com.solank.fxglgames.sg.model.bullet.*;

public class BulletBehaviourFactory {
    public static BulletBehaviour getBehaviour(BulletType type) {
        switch (type) {
            case NORMAL_BULLET:
                return new RegularBulletBehaviour();
            case ARMOR_PIERCING_BULLET:
                return new ArmourPiercingBulletBehaviour();
            case EXPLOSION_BULLET:
                return new ExplosionBullet();
            default:
                throw new IllegalArgumentException("Unknown bullet type: " + type);
        }
    }
}
