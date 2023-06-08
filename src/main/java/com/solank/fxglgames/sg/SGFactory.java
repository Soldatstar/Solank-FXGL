package com.solank.fxglgames.sg;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.solank.fxglgames.sg.components.PlayerComponent;
import com.solank.fxglgames.sg.components.SmallNoiseComponent;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

public class SGFactory implements EntityFactory {


    @Spawns("Yukine")
    public Entity spawnYukine(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return entityBuilder(data)
            .type(Type.YUKINE)
            .with(physics)
            .with(new PlayerComponent())
            .bbox(new HitBox(BoundingShape.box(21, 31)))
            .with(new CollidableComponent(true))
            //.with(new KeepOnScreenComponent())
            .buildAndAttach();
    }

    @Spawns("Ground")
    public Entity spawnGround(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);

        return entityBuilder(data)
            .with(physics)
            .bbox(new HitBox(new Point2D(0, getAppHeight()-15), BoundingShape.box(getAppWidth(), 12)))
            .buildAndAttach();
    }
    @Spawns("SmallNoise")
    public Entity spawnSmallNoise(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return entityBuilder(data)
            .type(Type.NOISE)
            .with(physics)
            .viewWithBBox("noise.png")
            .with(new SmallNoiseComponent(data.get("Yukine")))
            //.bbox(new HitBox(BoundingShape.box(21, 31)))
            .with(new CollidableComponent(true))
            .buildAndAttach();
    }
}
