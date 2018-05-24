/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.platformer;
import com.mygdx.game.screens.PlayScreen;

/**
 *
 * @author Влад
 */
public class PlatformDifficult extends Platform {

    private float stateTime;
    private float spawnX;
    private float spawnY;

    public PlatformDifficult(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / platformer.PPM, 16 / platformer.PPM);
        //Задание текстуры платформы
        platformTexture = new Texture(Gdx.files.internal("platform.png"));
        this.setTexture(platformTexture);
        //Начальные координаты платформы
        spawnY = b2body.getPosition().y;
        spawnX = b2body.getPosition().x;

    }

    //Обновление положения платформы за единицу времени (одновременно с этим и движение)
    public void update(float dt) {
        stateTime += dt;
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        if (stateTime < 3.0f) {
            velocity.set(0.7f, 0.5f);
            b2body.setLinearVelocity(velocity);
        }
        if (stateTime > 3.0f && stateTime < 5.0f) {
            velocity.set(0.5f, 0);
            b2body.setLinearVelocity(velocity);
        }
        if (stateTime > 5.0f && stateTime < 8.0f) {
            velocity.set(-0.7f, -0.5f);
            b2body.setLinearVelocity(velocity);
        }
        if (stateTime > 8.0f && stateTime < 10.0f) {
            velocity.set(-0.5f, 0);
            b2body.setLinearVelocity(velocity);
        }
        if (stateTime > 10.0f) {
            stateTime = 0;
        }

    }

    //Физическое определение платформы
    @Override
    protected void definePlatform(float x, float y) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x / platformer.PPM, y / platformer.PPM);
        bdef.type = BodyDef.BodyType.KinematicBody;
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

    //Задание стандартной скорости платформы
    @Override
    protected void setVelocity() {
        velocity = new Vector2(0.5f, 0);
    }

}
