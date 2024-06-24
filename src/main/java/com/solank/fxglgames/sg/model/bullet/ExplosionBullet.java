package com.solank.fxglgames.sg.model.bullet;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.solank.fxglgames.sg.model.Type;
import javafx.geometry.Rectangle2D;

import java.util.List;

public class ExplosionBullet extends AbstractBulletBehaviour {
   private static final int EXPLOSION_RADIUS = 70;
   private static final double HALF_EXPLOSION_RADIUS = EXPLOSION_RADIUS / 2.0;

   @Override
   public void onHit(Entity bullet, Entity target) {
      double impactPosX = bullet.getX();
      double impactPosY = bullet.getY();

      GameWorld world = bullet.getWorld();
      world.create("Explosion", new SpawnData().put("pointX", impactPosX).put("pointY", impactPosY).put("Radius", HALF_EXPLOSION_RADIUS));

      List<Entity> allNoiseInRadius = world.getEntitiesInRange(new Rectangle2D(
                      impactPosX - HALF_EXPLOSION_RADIUS,
                      impactPosY - HALF_EXPLOSION_RADIUS,
                      EXPLOSION_RADIUS,
                      EXPLOSION_RADIUS))
              .stream()
              .filter(e -> e.isType(Type.NOISE))
              .toList();

      System.out.println("All Noise: " + allNoiseInRadius.size());
      for (Entity noise : allNoiseInRadius) {
         double health = noise.getDouble("health");
         super.handleDamage(noise, 5, health, noise.getInt("score"));
      }
      bullet.removeFromWorld();
   }
}
