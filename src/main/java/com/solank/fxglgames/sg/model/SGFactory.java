package com.solank.fxglgames.sg.model;

import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
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
import com.solank.fxglgames.sg.components.PlayerComponent;
import com.solank.fxglgames.sg.components.SelfDestructComponent;
import com.solank.fxglgames.sg.components.SmallNoiseComponent;
import com.solank.fxglgames.sg.components.TallNoiseComponent;
import com.solank.fxglgames.sg.components.BulletComponent;
import com.solank.fxglgames.sg.components.weapons.WeaponComponent;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.solank.fxglgames.sg.manager.StaticStrings.*;

public class SGFactory implements EntityFactory {

    private static double explosionWidth;
    private static double explosionHeight;


    public static void setExplosionWidth(double explosionWidth) {
        SGFactory.explosionWidth = explosionWidth;
    }

    public static void setExplosionHeight(double explosionHeight) {
        SGFactory.explosionHeight = explosionHeight;
    }

    @Spawns(YUKINE_ENTITY)
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
                .with(new HealthIntComponent(150))
                .zIndex(100)
                .buildAndAttach();
    }

    @Spawns(GROUND_ENTITY)
    public Entity spawnGround(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);

        return entityBuilder(data)
                .type(Type.WALL)
                .with(physics)
                .with(new CollidableComponent(true))
                .viewWithBBox(new Rectangle(getAppWidth() + 20000, 150, Color.BLACK))
                .at(-100, getAppHeight() - 15)
                .buildAndAttach();
    }

    @Spawns("BGBuilding")
    public Entity spawnBGBuilding(SpawnData data) {
        double v1 = ((int) (Math.random() * 100) + 100);
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);

        //TODO: Generate Buildings semi-random
        return entityBuilder(data)
                .with(physics)
                .viewWithBBox(new Rectangle((int) (Math.random() * 100) + 100, v1, Color.BLUE))
                .at(data.getX(), data.getY() - v1)
                .buildAndAttach();
    }

    @Spawns(SMALL_NOISE_ENTITY)
    public Entity spawnSmallNoise(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return entityBuilder(data)
                .type(Type.NOISE)
                .with(physics)
                .bbox(new HitBox(BoundingShape.box(41, 41)))
                .with(new SmallNoiseComponent(data.get("Yukine")))
                .with(new CollidableComponent(true))
                .with(new HealthIntComponent(10))
                .with("damage", 15)
                .with("score", 10)
                .buildAndAttach();
    }

    @Spawns(TALL_NOISE_ENTITY)
    public Entity spawnTallNoise(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return entityBuilder(data)
                .type(Type.NOISE)
                .with(physics)
                .bbox(new HitBox(BoundingShape.box(41, 61)))
                .with(new TallNoiseComponent(data.get("Yukine")))
                .with(new CollidableComponent(true))
                .with(new HealthIntComponent(20))
                .with("damage", 25)
                .with("score", 20)
                .buildAndAttach();
    }

    @Spawns(BULLET_ENTITY)
    public Entity spawnBullet(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);
        play("weapons/shooting.wav");
        return entityBuilder()
                .type(Type.BULLET)
                .at((Point2D) data.get("weaponOuterPoint"))
                .viewWithBBox("yukine/weapons/bullet.png")
                .with(new CollidableComponent(true))
                .with(new BulletComponent())
                .with(new ProjectileComponent(data.get("direction"), data.get("bulletSpeed")))
                .with("damage", data.get("damage"))
                .with("bulletType", data.get("bulletType"))
                .with("hits", data.get("hits"))
                .buildAndAttach();
    }

    @Spawns("Cloud")
    public Entity spawnCloud(SpawnData data) {
        int random = (int) (Math.random() * 2) + 1;
        return entityBuilder(data)
                .viewWithBBox("background/cloud" + random + ".png")
                .at(data.getX(), data.getY(), -200)
                .buildAndAttach();
    }

    @Spawns("Wall")
    public Entity spawnWall(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);
        //TODO: Add a wall entity with a width of 10 and a height of the app height
        return entityBuilder(data)
                .with(physics)
                .viewWithBBox(new Rectangle(10, getAppHeight(), Color.INDIANRED))
                .at(data.getX(), data.getY())
                //.bbox(new HitBox(new Point2D(-100, getAppHeight() - 15), BoundingShape.box(getAppWidth()+20000, 12)))
                .buildAndAttach();
    }

    @Spawns(EXPLOSION_ENTITY)
    public Entity spawnExplosion(SpawnData data) {
        double radius = data.get("Radius");

        play("hit/explosion.wav");

        return entityBuilder()
                .at(((double) data.get("pointX")) - explosionWidth, ((double) data.get("pointY") - explosionHeight))
                .scale(radius / explosionWidth, radius / explosionHeight)
                .view("yukine/weapons/Explosion.png")
                .with(new ExpireCleanComponent(Duration.seconds(.1)))
                .buildAndAttach();
    }

}
