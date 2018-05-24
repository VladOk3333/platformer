/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.platformer;
import com.mygdx.game.screens.PlayScreen;

/**
 *
 * @author Влад
 */
public class Goomba extends Enemy {

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    
    //Конструктор гумбы
    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        }
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setToDestroy = false;
        setBounds(getX(), getY(), 16 / platformer.PPM, 16 / platformer.PPM);
        destroyed = false;
    }

    //Создание физического тела гумбы и его головы
    @Override
    protected void defineEnemy(float x, float y) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x / platformer.PPM, y / platformer.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / platformer.PPM);

        fdef.filter.categoryBits = platformer.ENEMY_BIT;
        fdef.filter.maskBits = platformer.GROUND_BIT
                | platformer.ENEMY_BIT
                | platformer.OBJECT_BIT
                | platformer.MARIO_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //Голова гумбы
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / platformer.PPM);
        vertice[1] = new Vector2(5, 8).scl(1 / platformer.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / platformer.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / platformer.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = platformer.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    //Отрисовка гумбы
    public void draw(Batch batch) {
        if (!destroyed || stateTime < 1) {
            super.draw(batch);
        }
    }

    //Обновление за единицу времени
    public void update(float dt) {
        stateTime += dt;
        if (setToDestroy && !destroyed) {
            platformer.manager.get("stomp.wav", Sound.class).play();
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
            stateTime = 0;
        } else if (!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
        }

    }

    //Проверка на удар по голове гумбы
    @Override
    public void hitOnHead() {
        setToDestroy = true;
    }

    //Метод убийства игрока
    public void kill(MarioClone mario) {
        mario = screen.player;
        if (mario.marioIsBig) {
            mario.hit();
        } else {
            mario.setDead();
        }
    }

}
