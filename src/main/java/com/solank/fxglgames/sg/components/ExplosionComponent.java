package com.solank.fxglgames.sg.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class ExplosionComponent extends Component {

   // Animation channels
   private final AnimatedTexture texture;
   private final AnimationChannel upDown;

   // Physics and state variables
   private PhysicsComponent physics = new PhysicsComponent();
   private boolean physicsReady = false;

   public ExplosionComponent() {
      this.upDown = createAnimationChannel("yukine/weapons/Explosionrow.png", Duration.seconds(0.6), 6);
      this.texture = new AnimatedTexture(upDown);
      configureTexture();
      physics.setOnPhysicsInitialized(() -> {});
   }

   @Override
   public void onAdded() {
      configureEntityView();
      initializePhysics();
   }

   public void stopMovingX() {
      if (isPhysicsReady()) {
         physics.setVelocityX(0);
      }
   }

   private AnimationChannel createAnimationChannel(String imagePath, Duration duration, int frames) {
      return new AnimationChannel(FXGL.image(imagePath), duration, frames);
   }

   private void configureTexture() {
      texture.setScaleX(1);
      texture.setScaleY(1);
   }

   private void configureEntityView() {
      entity.getViewComponent().addChild(texture);
      texture.loopAnimationChannel(upDown);
      texture.setTranslateY(-15);
      texture.setTranslateZ(50);
   }

   private void initializePhysics() {
      physics.setOnPhysicsInitialized(() -> physicsReady = true);
   }

   private boolean isPhysicsReady() {
      return physicsReady;
   }
}
