/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.screens.PlayScreen;

/**
 *
 * @author Влад
 */
public abstract class Enemy extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;

    //Конструктор врага
    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy(x, y);
        velocity = new Vector2(1, 0);
    }

    //Создание физического тела врага
    protected abstract void defineEnemy(float x, float y);

    //Удар по голове врага
    public abstract void hitOnHead();

    //Обновление за единицу времени
    public abstract void update(float dt);

    //Смена скорости на противоположную
    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x = -velocity.x;
            if (y) {
                velocity.y = -velocity.y;
            }
        }
    }
    
    //Убийство Марио
    public abstract void kill(MarioClone mario);
}
