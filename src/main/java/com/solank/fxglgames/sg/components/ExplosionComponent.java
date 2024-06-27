package com.solank.fxglgames.sg.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

public class ExplosionComponent extends Component {

   private static AnimationChannel explosion;

   private static AnimatedTexture texture;

   public ExplosionComponent(){
       explosion = new AnimationChannel(FXGL.image("yukine/weapons/Explosionrow.png"), Duration.seconds(0.5), 6);
       texture = new AnimatedTexture(explosion);
   }

   @Override
   public void onAdded() {
      configureEntityView();
      configureTexture();
   }

   private void configureEntityView() {
      entity.getViewComponent().addChild(texture);
      texture.loopAnimationChannel(explosion);
      texture.setTranslateY(-15);
      texture.setTranslateZ(150);
   }

   private void configureTexture() {
      texture.setScaleX(1);
      texture.setScaleY(1);
   }
}
