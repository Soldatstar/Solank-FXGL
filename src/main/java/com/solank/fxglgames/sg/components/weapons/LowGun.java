package com.solank.fxglgames.sg.components.weapons;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.texture.Texture;
import com.solank.fxglgames.sg.components.weapons.bullet.BulletType;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getInput;
import static com.solank.fxglgames.sg.SGApp.YUKINE_ENTITY;

public class LowGun extends AbstractWeapon {

   public LowGun(Entity entity) {
      super(entity);
      Texture weaponTexture = FXGL.getAssetLoader().loadTexture("yukine/weapons/LowGun.png");
      weaponTexture.setScaleX(5);
      weaponTexture.setScaleY(5);
      this.weaponView = new ImageView(weaponTexture.getImage());
      weaponView.setTranslateZ(-200);
      weaponView.setTranslateY(-15);
      weaponView.setTranslateX(-45);
   }

      @Override
      public double getCooldownDuration() {
         return 1.25;
      }

      @Override
      public double getShotPauseDuration() {
         return 0.35;
      }

      @Override
      public Point2D getWeaponOuterPoint() {
         double angle = Math.toRadians(weaponView.getRotate());
         double offsetX = 60; // Adjust the X offset based on the weapon's dimensions
         double offsetY = 60; // Adjust the Y offset based on the weapon's dimensions

         double outerX = transform.getX() + offsetX * Math.cos(angle);
         double outerY = transform.getY() + offsetY * Math.sin(angle);

         return new Point2D(outerX + 10, outerY - 10);
      }

      @Override
      public double getDamage() {
         return 5;
      }

      @Override
      public BulletType getBulletType() {
         return BulletType.EXPLOSION_BULLET;
      }

      @Override
      public int getHits() {
         return 0;

      }

   @Override
   public double getBulletSpeed() {
      return 500;
   }

   @Override
      public void shoot() {
          Point2D mousePosition = new Point2D(getInput().getMouseXWorld(), getInput().getMouseYWorld());
          Point2D weaponOuterPoint = getWeaponOuterPoint();
          Point2D directionToMouse = mousePosition.subtract(weaponOuterPoint).normalize();

          Point2D leftDirection = new Point2D(
                  directionToMouse.getX() * Math.cos(Math.toRadians(-10)) - directionToMouse.getY() * Math.sin(Math.toRadians(-10)),
                  directionToMouse.getX() * Math.sin(Math.toRadians(-10)) + directionToMouse.getY() * Math.cos(Math.toRadians(-10))
          );
          Point2D rightDirection = new Point2D(
                  directionToMouse.getX() * Math.cos(Math.toRadians(10)) - directionToMouse.getY() * Math.sin(Math.toRadians(10)),
                  directionToMouse.getX() * Math.sin(Math.toRadians(10)) + directionToMouse.getY() * Math.cos(Math.toRadians(10))
          );

          spawnBullet(leftDirection);
          spawnBullet(rightDirection);
      }



}
