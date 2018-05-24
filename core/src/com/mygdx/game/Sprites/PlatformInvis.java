/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.platformer;
import com.mygdx.game.screens.PlayScreen;
import static java.lang.Math.abs;

/**
 *
 * @author Влад
 */
public class PlatformInvis extends Platform {

    private float stateTime;
    private float spawnX;
    private float spawnY;
    private float currentX;
    private float currentY;
    public boolean destroyed;
    public boolean setToDestroy;
    

    //Конструктор
    public PlatformInvis(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / platformer.PPM, 16 / platformer.PPM);
        //platformTexture = new Texture(Gdx.files.internal("platform.png"));

        this.setTexture(platformTexture);
        //this.setColor(0.42f, 0.9f, 0.9f, 1);
        b2body.setGravityScale(0);
        spawnY = b2body.getPosition().y;
        spawnX = b2body.getPosition().x;
        setToDestroy = false;
        destroyed = false;
        
    }
    //Обновление за единицу времени
    public void update(float dt) {
        stateTime += dt;
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        b2body.setLinearVelocity(velocity);
        currentY = b2body.getPosition().y;
        currentX = b2body.getPosition().x;
        System.out.println(stateTime);
        if(stateTime > 3f && destroyed == false){
            b2body.setActive(false);
            destroyed = true;
            stateTime = 0;
        }
        if(stateTime > 3f && destroyed == true){
            b2body.setActive(true);
            destroyed = false;
            stateTime = 0;
        }
        /*if(stateTime > 5f && destroyed == 2){
            destroyed = 0;
            stateTime = 0;
        }*/
       
    }

    //Задание физического тела и фикстуры платформы
    @Override
    protected void definePlatform(float x, float y) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x / platformer.PPM, y / platformer.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.friction = 3f;
        PolygonShape shape = new PolygonShape();
        Rectangle rect = new Rectangle();
        rect.height = 6 / platformer.PPM;
        rect.width = 15 / platformer.PPM;
        shape.setAsBox((rect.getWidth()), (rect.getHeight() / 2));

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }
    

    //Установка стандартной скорости
    @Override
    protected void setVelocity() {
        velocity = new Vector2(0, 0);
    }

     public void draw(Batch batch) {
        if (destroyed == false) {
            batch.draw(this.getTexture(), this.getX() - .15f, this.getY() + 0.09f, 16f / 26, 5f / 42);
        }
    }
}
