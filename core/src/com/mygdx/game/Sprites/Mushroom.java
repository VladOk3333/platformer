/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.platformer;
import com.mygdx.game.screens.PlayScreen;

/**
 *
 * @author Влад
 */
public class Mushroom extends Sprite {

    private World world;
    private PlayScreen screen;
    public Body b2body;
    public boolean destroyed;
    public float stateTime;
    public boolean setToDestroy;
    public Texture texture;
    public TextureRegion textureReg;

    //Конструктор гриба
    public Mushroom(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineMushroom(x, y);
        texture = new Texture(Gdx.files.internal("mushroom.png"));
        textureReg = new TextureRegion(texture);
        //texture = new TextureRegion(screen.getAtlas().findRegion("mushroom"), 16, 0, 16, 16);
        stateTime = 0;
        setToDestroy = false;
        setBounds(getX(), getY(), 16 / platformer.PPM, 16 / platformer.PPM);
        destroyed = false;
    }

    //Создание физического тела гриба
    private void defineMushroom(float x, float y) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x / platformer.PPM, y / platformer.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / platformer.PPM);

        fdef.filter.categoryBits = platformer.ITEM_BIT;
        fdef.filter.maskBits = platformer.GROUND_BIT
                | platformer.MARIO_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    //Отрисовка гриба
    public void draw(Batch batch) {
        if (!destroyed || stateTime < 1) {
            super.draw(batch);
        }
    }

    //Обновление за единицу времени гриба
    public void update(float dt) {
        stateTime += dt;
        if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
        } else if (!destroyed) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(texture);
        }

    }
    
    //Уничожение гриба
    public void destroy(MarioClone mario) {
        mario = screen.player;
        setToDestroy = true;
        if (!mario.marioIsBig) {
            mario.grow();
        }
    }

}
