package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.mygdx.game.screens.PlayScreen;

public class platformer extends Game {

    //Размеры экрана в пикселях
    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 210;

    //Масштаб игры
    public static final float PPM = 50;

    //Упаковщик спрайтов
    public SpriteBatch batch;

    //Менеджер для работы с музыкой и звуковыми файлами
    public static AssetManager manager;

    //Биты для коллизий Box2D
    public static final short NOTHING_BIT = 0;
    public static final short GROUND_BIT = 1;
    public static final short MARIO_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short OBJECT_BIT = 8;
    public static final short ENEMY_BIT = 16;
    public static final short ENEMY_HEAD_BIT = 32;
    public static final short ITEM_BIT = 64;
    public static final short MARIO_HEAD_BIT = 128;

    //Создание начальной обстановки и подгрузка файлов
    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();
        manager.load("Hiked Up Tube Socks.mp3", Music.class);
        manager.load("jump.mp3", Sound.class);
        manager.load("mariodie.wav", Sound.class);
        manager.load("win.wav", Sound.class);
        manager.load("bump.wav", Sound.class);
        manager.load("stomp.wav", Sound.class);
        manager.load("powerup.wav", Sound.class);
        manager.load("powerdown.wav", Sound.class);
        manager.load("powerup_spawn.wav", Sound.class);
        manager.finishLoading();
        setScreen(new PlayScreen(this));
    }

    //Отрисовка игры
    @Override
    public void render() {
        super.render();
    }

    //Очистка памяти
    @Override
    public void dispose() {
        super.dispose();
        manager.dispose();
        batch.dispose();
    }

}
