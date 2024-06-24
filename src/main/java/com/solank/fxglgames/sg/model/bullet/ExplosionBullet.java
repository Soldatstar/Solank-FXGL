package com.solank.fxglgames.sg.model.bullet;

import com.almasb.fxgl.entity.Entity;

//Currently unused
public class ExplosionBullet implements BulletBehaviour{

   int ExplosionRadius = 10;
   @Override
   public void onHit(Entity bullet, Entity target) {
      double impactPosX = target.getX();
      double impactPosY = target.getY();
      bullet.removeFromWorld();
   }
}
