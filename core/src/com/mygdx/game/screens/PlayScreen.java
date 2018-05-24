package com.mygdx.game.screens;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.platformer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Extensions.ModuleEngine;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.Goomba;
import com.mygdx.game.Sprites.MarioClone;
import com.mygdx.game.Sprites.Mushroom;
import com.mygdx.game.Sprites.PlatformCircular;
import com.mygdx.game.Sprites.PlatformDifficult;
import com.mygdx.game.Sprites.PlatformLeftRight;
import com.mygdx.game.Sprites.PlatformInvis;
import com.mygdx.game.Sprites.PlatformUpDown;
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.Tools.WorldContactListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;

/**
 *
 * @author Влад
 */
public class PlayScreen implements Screen {

    //Переменные Tiled map 
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private platformer game;
    private OrthographicCamera gamecam;
    private FitViewport gamePort;
    private Hud hud;

    //Переменные Box2D
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    //Объекты игры
    public MarioClone player;
   // private Goomba goomba;
    private Goomba goomba1;
    private Goomba goomba2;
    private Goomba goomba3;
    private Goomba goomba4;
    private Mushroom mushroom;
    private Mushroom mushroom1;
    private PlatformLeftRight platform;
    private PlatformLeftRight platform2;
    private PlatformUpDown platformUpDown;
    private PlatformUpDown platformUpDown2;
    private PlatformCircular platformCircular;
    private PlatformDifficult platformDifficult;
    private PlatformInvis platformInvis;
    private TextureAtlas atlas;
    
    //Слушатель коллизий
    private WorldContactListener listener;

    //Звуковые переменные
    private Music music;
    private Sound jumpSound;
    private Sound dieSound;
    private Sound winSound;

    private int lastTime;
    private boolean isBot = false;

    
    public PlayScreen(platformer game) {
        
        atlas = new TextureAtlas("Mario_and_Enemies.pack");
        this.game = game;
        //Создаем камеру 
        gamecam = new OrthographicCamera();
        gamecam.setToOrtho(false, 5, 5);
        gamePort = new FitViewport(platformer.V_WIDTH / platformer.PPM, platformer.V_HEIGHT / platformer.PPM, gamecam);

        //Создаем интерфейс игры с отображением информации
        hud = new Hud(game.batch);

        //Загружаем нашу карту и устанавливаем отрисовку карты
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("lvlmap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / platformer.PPM);
        renderer.setMap(map);
        renderer.setView(gamecam);

        //Создаем мир в Box2D и объекты
        //Стандарт гравитации -400
        world = new World(new Vector2(0, -400 / platformer.PPM), true);
        b2dr = new Box2DDebugRenderer();
        //platform = new PlatformLeftRight(this, 739, 55, 1.8f);
        platformUpDown = new PlatformUpDown(this, 23f * platformer.PPM, 0.8f * platformer.PPM, 2.8f);
        platform2 = new PlatformLeftRight(this, 86f * platformer.PPM, 0.6f * platformer.PPM, 2.5f);
        platformUpDown2 = new PlatformUpDown(this, 85.4f * platformer.PPM, 1f * platformer.PPM, 2.8f);
        //platformCircular = new PlatformCircular(this, 2f * platformer.PPM, 2f * platformer.PPM);
        platformDifficult = new PlatformDifficult(this, 32.1f * platformer.PPM, 1.43f * platformer.PPM);
        platformInvis = new PlatformInvis(this, 15.7f * platformer.PPM, 1.2f * platformer.PPM);

        player = new MarioClone(this);
        String[] arr = new String[1];
        JFileChooser fileopen = new JFileChooser();    
        fileopen.setCurrentDirectory(new File("..\\build\\classes\\main\\com\\mygdx\\game\\Extensions\\modules"));
       int ret = fileopen.showDialog(null, "Открыть файл");
       String filePath="";
       if(ret == JFileChooser.APPROVE_OPTION)
        {
            filePath =  fileopen.getSelectedFile().getPath();
              System.out.println(filePath);
              isBot = true;
        }
        arr[0] = filePath; 
        ModuleEngine.main(arr, player);
        listener = new WorldContactListener();
        world.setContactListener(listener);
        music = platformer.manager.get("Hiked Up Tube Socks.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.2f);
        music.play();
        mushroom = new Mushroom(this, 0.16f * platformer.PPM, 1.41f * platformer.PPM);
        mushroom1 = new Mushroom(this, 62.1f * platformer.PPM, 0.8f * platformer.PPM);
        //goomba = new Goomba(this, 2f * platformer.PPM, 1f * platformer.PPM);
        goomba1 = new Goomba(this, 25f * platformer.PPM, 0.8f * platformer.PPM);
        goomba2 = new Goomba(this, 69f * platformer.PPM, 0.8f * platformer.PPM);
        goomba3 = new Goomba(this, 80f * platformer.PPM, 0.8f * platformer.PPM);
        goomba4 = new Goomba(this, 83.5f * platformer.PPM, 0.8f * platformer.PPM);
        creator = new B2WorldCreator(this);

    }

    //Отрисовка игры
    @Override
    public void render(float delta) {
        update(delta);
        //Очищаем экран игры черным цветом
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Отрендерить нашу карту
        renderer.render();

        //Отрендерить дебагер линий Box2D
        b2dr.render(world, gamecam.combined);

        //Отрисовка модели марио
        game.batch.setProjectionMatrix(gamecam.combined);

        game.batch.begin();
        player.draw(game.batch);
       // goomba.draw(game.batch);
        goomba1.draw(game.batch);
        goomba2.draw(game.batch);
        goomba3.draw(game.batch);
        goomba4.draw(game.batch);
        mushroom.draw(game.batch);
        mushroom1.draw(game.batch);
        //Задание текстур платформам, криво но они не задавались нормально стандартными методами
       // game.batch.draw(platform.getTexture(), platform.getX() - .15f, platform.getY() + 0.09f, 16f / 26, 5f / 42);
        game.batch.draw(platformUpDown.getTexture(), platformUpDown.getX() - .15f, platformUpDown.getY() + 0.09f, 16f / 26, 5f / 42);
        platformInvis.draw(game.batch);
       
        //game.batch.draw(platformInvis.getTexture(), platformInvis.getX() - .15f, platformInvis.getY() + 0.09f, 16f / 26, 5f / 42);
      
        game.batch.draw(platform2.getTexture(), platform2.getX() - .15f, platform2.getY() + 0.09f, 16f / 26, 5f / 42);
        game.batch.draw(platformUpDown2.getTexture(), platformUpDown2.getX() - .15f, platformUpDown2.getY() + 0.09f, 16f / 26, 5f / 42);
        game.batch.draw(platformDifficult.getTexture(), platformDifficult.getX() - .15f, platformDifficult.getY() + 0.09f, 16f / 26, 5f / 42);
        game.batch.end();

        //Установка камеры интерфейса
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        if (gameOver()) {
            music.stop();
            game.setScreen(new GameOverScreen(game));
            dieSound = platformer.manager.get("mariodie.wav", Sound.class);
            dieSound.play(0.7f);
        }
        if (gameWin()) {
            lastTime = 600 - hud.getWorldTime();
            music.stop();
            game.setScreen(new WinScreen(game, lastTime));
            winSound = platformer.manager.get("win.wav", Sound.class);
            winSound.play(0.7f);
        }
        if(isBot == true)
        ModuleEngine._execute.run();

    }

    //Обновление в единицу времени
    public void update(float dt) {
        handleInput(dt);

        world.step(1 / 60f, 6, 2);
        player.update(dt);
       // platform.update(dt);
        platformUpDown.update(dt);
        platform2.update(dt);
        platformUpDown2.update(dt);
        platformInvis.update(dt);
        // platformCircular.update(dt);
        platformDifficult.update(dt);
       // goomba.update(dt);
        goomba1.update(dt);
        goomba2.update(dt);
        goomba3.update(dt);
        goomba4.update(dt);
        hud.update(dt);
        mushroom.update(dt);
        mushroom1.update(dt);

        //Установка следования камеры за игроком
        if (player.b2body.getPosition().x < 4f) {
            gamecam.position.x = 4f;
        } else if (player.b2body.getPosition().x > 92f) {
            gamecam.position.x = 92f;
        } else {
            gamecam.position.x = player.b2body.getPosition().x;
        }
        gamecam.update();
        renderer.setView(gamecam);
    }

    //Адаптация игры под размер экрана
    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    //Проверка проигрыша
    public boolean gameOver() {/*
         if (player.b2body.getPosition().y < 0) {
         return true;
         }*/

        if (player.marioIsDead == true) {
            return true;
        }
        return false;
    }

    //Проверка выигрыша
    public boolean gameWin() {
        if (player.b2body.getPosition().x >= 94.2 && player.b2body.getPosition().x <= 94.9 && player.b2body.getPosition().y >= 1.4 && player.b2body.getPosition().y <= 1.725) {
            return true;
        }
        return false;
    }

    @Override
    public void hide() {

    }

    //Освобождение памяти
    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();

    }

    @Override
    public void show() {

    }

    //Получить карту
    public TiledMap getMap() {
        return map;
    }

    //Получить игровой мир
    public World getWorld() {
        return world;
    }

    //Получить пак текстур
    public TextureAtlas getAtlas() {
        return atlas;
    }

    //Отслеживание нажатия клавиш и управление марио
    public void handleInput(float dt) {
        if(isBot == false){//Стандарт прыжка 4f
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (player.isGround() == true) {
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
                jumpSound = platformer.manager.get("jump.mp3", Sound.class);
                jumpSound.play(0.2f);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
            player.b2body.applyLinearImpulse(new Vector2(0.15f, 0), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
            player.b2body.applyLinearImpulse(new Vector2(-0.15f, 0), player.b2body.getWorldCenter(), true);
        }

    }
    }
}
