/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.platformer;

/**
 *
 * @author Влад
 */
public class WinScreen implements Screen{
    private Viewport viewport;
    private Stage stage;
    private Game game;
    private  Label gameOverLabel;

    
    public WinScreen(Game game, int time){
        this.game = game;
        viewport = new FitViewport(platformer.V_WIDTH, platformer.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((platformer)game).batch);
        
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        
        gameOverLabel = new Label("YOU WIN", font);
        gameOverLabel.setColor(Color.GREEN);
        Label playAgainLabel = new Label("Click to play again", font);
        Label winTime = new Label(String.format("%03d", time), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        winTime.setText(String.format("%03d", time));
        table.add(gameOverLabel).expandX();
        table.row();
        table.add(winTime).expandX().padTop(10);
        table.row();
        table.add(playAgainLabel).expandX().padTop(10);
        
        stage.addActor(table);
    }
    
    
    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()){
            game.setScreen(new PlayScreen((platformer)game));
            dispose();
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
