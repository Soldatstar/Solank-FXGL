package com.solank.fxglgames.sg;

import com.almasb.fxgl.dsl.components.KeepOnScreenComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.solank.fxglgames.sg.components.BulletComponent;
import com.solank.fxglgames.sg.components.PlayerComponent;
import com.solank.fxglgames.sg.components.SmallNoiseComponent;
import com.solank.fxglgames.sg.components.TallNoiseComponent;
import com.solank.fxglgames.sg.components.WeaponComponent;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.play;

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
            .with(new KeepOnScreenComponent())
            .zIndex(100)
            .buildAndAttach();
    }

    @Spawns("Ground")
    public Entity spawnGround(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);

        return entityBuilder(data)
            .with(physics)
            .viewWithBBox(new Rectangle(getAppWidth() + 20000, 150, Color.BLACK))
            .at(-100, getAppHeight() - 15)
            //.bbox(new HitBox(new Point2D(-100, getAppHeight() - 15), BoundingShape.box(getAppWidth()+20000, 12)))
            .buildAndAttach();
    }


    @Spawns("BGBuilding")
    public Entity spawnBGBuilding(SpawnData data) {
        double v1 = ((int) (Math.random() * 100) + 100);
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);

        //return entityBuilder(data) of a background building with a random height and width between 100 and 200
        return entityBuilder(data)
            .with(physics)
            .viewWithBBox(new Rectangle((int) (Math.random() * 100) + 100, v1, Color.BLUE))
            .at(data.getX(), data.getY() - v1)
            .buildAndAttach();
    }

    @Spawns("SmallNoise")
    public Entity spawnSmallNoise(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return entityBuilder(data)
            .type(Type.NOISE)
            //.view("noise.png")
            .with(physics)
            .bbox(new HitBox(BoundingShape.box(41, 41)))
            .with(new SmallNoiseComponent(data.get("Yukine")))
            .with(new CollidableComponent(true))
            .with("damage", 15.0)
            .with("health", 10.0)
            .with("score", 10)
            .buildAndAttach();
    }


    @Spawns("TallNoise")
    public Entity spawnTallNoise(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return entityBuilder(data)
            .type(Type.NOISE)
            //.view("noise.png")
            .with(physics)
            .bbox(new HitBox(BoundingShape.box(41, 61)))
            .with(new TallNoiseComponent(data.get("Yukine")))
            .with(new CollidableComponent(true))
            .with("damage", 25.0)
            .with("health", 20.0)
            .with("score", 20)
            .buildAndAttach();
    }

    @Spawns("Bullet")
    public Entity spawnBullet(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);
        Entity yukine = data.get("Yukine");
        double mouseX = data.get("mouseX");
        double mouseY = data.get("mouseY");
        WeaponComponent weaponComponent = yukine.getComponent(WeaponComponent.class);
        Point2D weaponOuterPoint = weaponComponent.getWeaponOuterPoint();

        play("weapons/shooting.wav");
        return entityBuilder()
            .type(Type.BULLET)
            .at(weaponOuterPoint)
            .viewWithBBox("yukine/weapons/bullet.png")
            .with(new CollidableComponent(true))
            .with(new BulletComponent())
            .with(new ProjectileComponent(new Point2D(mouseX - yukine.getX(), mouseY - yukine.getY()), 1000))
            .with("damage", 10.0)
            .buildAndAttach();
    }

    @Spawns("Cloud")
    public Entity spawnCloud(SpawnData data) {
        return entityBuilder(data)
            .viewWithBBox(new Rectangle(50, 50, Color.BLUE))
            .at(data.getX(), data.getY(), -200)
            .buildAndAttach();
    }
}
