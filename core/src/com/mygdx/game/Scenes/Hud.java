/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.platformer;

/**
 *
 * @author Влад
 */
public class Hud {

    public Stage stage;
    private Viewport viewport;

    private int worldTimer;
    private float timeCount;

    Label countdownLabel;
    Label timeLabel;

    //Конструктор класса
    public Hud(SpriteBatch sb) {
        worldTimer = 600;
        timeCount = 0;

        //Расположение по отношению к камере
        viewport = new FitViewport(platformer.V_WIDTH, platformer.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //Создание таблицы для расположения лейблов на экране
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        //Создание лейблов для отображения оставшегося времени
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(countdownLabel).expandX();
        stage.addActor(table);
    }

    //Обновление счетчика за единицу времени
    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            if (worldTimer == 0) {
                worldTimer = 0;
            } else {
                worldTimer--;
                countdownLabel.setText(String.format("%03d", worldTimer));
            }
            timeCount = 0;
        }
    }

    //Получить время 
    public int getWorldTime() {
        return worldTimer;
    }

    //Очистка памяти
    public void dispose() {
        stage.dispose();
    }

}
