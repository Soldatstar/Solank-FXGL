package com.solank.fxglgames.sg.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

public class ExplosionComponent extends Component {

   private static AnimationChannel explosion;

   private static AnimatedTexture texture;

   public ExplosionComponent() {
      explosion =
              new AnimationChannel(FXGL.image("yukine/weapons/Explosionrow.png"), Duration.seconds(0.5), 6);
      texture = new AnimatedTexture(explosion);
      System.out.println("explosion was build");

      texture.setScaleX(2);
      texture.setScaleY(2);
      texture.setTranslateY(+10);
   }

   @Override
   public void onAdded() {
      configureEntityView();
      configureTexture();
   }

   private void configureEntityView() {
      entity.getViewComponent().addChild(texture);
      texture.loopNoOverride(explosion);
      texture.loopAnimationChannel(explosion);
      texture.setTranslateY(+14);
   }

   private void configureTexture() {
      texture.setScaleX(1);
      texture.setScaleY(1);
   }
}
