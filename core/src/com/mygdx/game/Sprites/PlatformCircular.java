/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.Sprites;

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
public class PlatformCircular extends Platform {

    private float stateTime;
    private float spawnX;
    private float spawnY;
    private float currentX;
    private float currentY;

    //Синус и косинус, необходимые для вычисления вектора скорости круговой платформы
    private float c;
    private float s;
    //Координаты центра окружности, вокруг которой будет происходить движение
    private float xO;
    private float yO;

    //Радиус окружности
    private float rx;
    private float ry;

    //Ожидаемые координаты сдвига на определенное количество градусов
    private float x1;
    private float y1;

    public PlatformCircular(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / platformer.PPM, 16 / platformer.PPM);
        b2body.setGravityScale(0);
        spawnY = b2body.getPosition().y;
        spawnX = b2body.getPosition().x;
        xO = spawnX;
        yO = spawnY + 1;
        currentX = spawnX;
        currentY = spawnY;

        c = (float) Math.cos(0.035);
        s = (float) Math.sin(0.035);
    }

    public void update(float dt) {
        stateTime += dt;
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        rx = abs(currentX - xO);
        ry = abs(currentY - yO);
       // ДОДЕЛАТЬ ПОВЕДЕНИЕ СРОЧНО

        x1 = currentX + rx * c - ry * s;
        y1 = currentY + rx * s + ry * c;
        velocity.set(x1, y1);
        b2body.setLinearVelocity(velocity);
        currentX = x1;
        currentY = y1;

        /*  if(currentY != spawnY){
         setPosition(b2body.getPosition().x, spawnY);
         b2body.getPosition();
         }*/
    }

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

    @Override
    protected void setVelocity() {

        velocity = new Vector2(0, 0);
    }

}
