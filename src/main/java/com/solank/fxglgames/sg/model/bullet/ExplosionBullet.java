package com.solank.fxglgames.sg.model.bullet;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.texture.Texture;
import com.solank.fxglgames.sg.model.Type;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;

import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.animationBuilder;

//Currently unused
public class ExplosionBullet implements BulletBehaviour{
   int explosionRadius = 50;

   @Override
   public void onHit(Entity bullet, Entity target) {
      double impactPosX = bullet.getX();
      double impactPosY = bullet.getY();

      Texture explosionTexture = FXGL.getAssetLoader().loadTexture("yukine/weapons/Explosion.png");
      explosionTexture.setScaleX(0.5);
      explosionTexture.setScaleY(0.5);

      Entity explosion = new Entity();
      explosion.getViewComponent().addChild(explosionTexture);
      explosion.setPosition(new Point2D(impactPosX, impactPosY));
      explosion.setScaleX(1);
      explosion.setScaleX(1);

      GameWorld world = bullet.getWorld();
      world.addEntity(explosion);
      Animation<?> explosionAnim = animationBuilder()
              .duration(Duration.seconds(1))
              .scale(explosion)
              .from(new Point2D(explosionRadius, explosionRadius))
              .to(new Point2D(0, 0))
              .build();

      explosionAnim.start();
      explosionAnim.setOnFinished(explosion::removeFromWorld);

      List<Entity> allInRadius = world.getEntitiesInRange(new Rectangle2D(
                                                impactPosX - (double) explosionRadius / 2,
                                                impactPosY - (double) explosionRadius / 2,
                                                explosionRadius,
                                                explosionRadius));
      var allAliveInRadius = allInRadius.stream().filter(e -> e.isType(Type.NOISE)).toList();
      for (Entity noise : allAliveInRadius){
         double health = noise.getDouble("health");
         noise.setProperty("health",health-5);
      }
      bullet.removeFromWorld();
   }
}
