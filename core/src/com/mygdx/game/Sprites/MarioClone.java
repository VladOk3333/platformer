/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.platformer;
import com.mygdx.game.screens.PlayScreen;

/**
 *
 * @author Влад
 */
public class MarioClone extends Sprite {

    public enum State {

        FALLING, JUMPING, STANDING, RUNNING, GROWING
    };
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    public boolean status;
    private TextureRegion marioStand;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    
    
    
    private Animation marioRun;
    private TextureRegion marioJump;
    private Animation bigMarioRun;
    private Animation growMario;
    
    private float stateTimer;
    private boolean runningRight;
    public boolean marioIsDead;
    public boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineMario;
    

    public MarioClone(PlayScreen screen) {
        super(screen.getAtlas().findRegion("little_mario"));
        world = screen.getWorld();

        status = true;
        this.world = world;
        marioIsDead = false;
        currentState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        // Анимация бега
        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 11, 16, 16));

        }
        marioRun = new Animation(0.1f, frames);
        
        frames.clear();
        
        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 0, 16, 32));

        }
        bigMarioRun = new Animation(0.1f, frames);
        frames.clear();
        
        
        //Анимация растущего Марио
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        growMario = new Animation(0.2f, frames);
        frames.clear();
        
        
        
        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
       
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);
        frames.clear();
        defineMario();
        marioStand = new TextureRegion(getTexture(), 0, 11, 16, 16);
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);
        
        setBounds(0, 0, 16 / platformer.PPM, 16 / platformer.PPM);
        setRegion(marioStand);
    }

    //Обновление за единицу времени
    public void update(float dt) {
        if(marioIsBig){
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 - 6 / platformer.PPM);
        }
        else{
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        }
        setRegion(getFrame(dt));
        if(b2body.getPosition().y < 0){
            marioIsDead = true;
        }
        if(timeToDefineBigMario){
            defineBigMario();
        }
        if(timeToRedefineMario){
            redefineMario();
        }
        System.out.println("X= ");
        System.out.println(b2body.getPosition().x);
        System.out.println("Y= ");
        System.out.println(b2body.getPosition().y);
    }
    
    public void redefineMario(){
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);
        
        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / platformer.PPM);
        
        fdef.filter.categoryBits = platformer.MARIO_BIT;
        fdef.filter.maskBits = platformer.GROUND_BIT |
                               platformer.ENEMY_BIT |
                               platformer.OBJECT_BIT |
                               platformer.ENEMY_HEAD_BIT |
                               platformer.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        timeToRedefineMario = false;
    }
    
    
    public void hit(){
        if(marioIsBig){
            platformer.manager.get("powerdown.wav", Sound.class).play();
            marioIsBig = false;
            timeToRedefineMario = true;
            setBounds(getX(), getY(), getWidth(), getHeight() / 2);
        }
    }
    
    
    public void grow(){
        runGrowAnimation = true;
        marioIsBig = true;
        timeToDefineBigMario = true;
        setBounds(getX(), getY(), getWidth(), getHeight() * 2);
        platformer.manager.get("powerup.wav", Sound.class).play();
    }
    
    
    public void defineBigMario(){
        
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);
        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10 / platformer.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / platformer.PPM);
        
        fdef.filter.categoryBits = platformer.MARIO_BIT;
        fdef.filter.maskBits = platformer.GROUND_BIT |
                               platformer.ENEMY_BIT |
                               platformer.OBJECT_BIT |
                               platformer.ENEMY_HEAD_BIT |
                               platformer.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0, -14 / platformer.PPM));
        b2body.createFixture(fdef).setUserData(this);
        timeToDefineBigMario = false;
    }

    //Получение соответствующего кадра во время движения
    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case GROWING:
                region = (TextureRegion) growMario.getKeyFrame(stateTimer);
                if(growMario.isAnimationFinished(stateTimer));
                    runGrowAnimation = false;
                break;
            case JUMPING:
                region = marioIsBig ? bigMarioJump : marioJump;
                break;
            case RUNNING:
                region = (TextureRegion) (marioIsBig? bigMarioRun.getKeyFrame(stateTimer, true) : marioRun.getKeyFrame(stateTimer, true));
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioIsBig ? bigMarioStand : marioStand;
                break;
        }
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    //Получение состояния движения
    public State getState() {
        if(runGrowAnimation){
            return State.GROWING;
        }
        if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        } else if (b2body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (b2body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    //Проверка на смерть
    public boolean isDead() {
        return marioIsDead;
    }
    
    public void setDead(){
        marioIsDead = true;
    }

    //Создание физического тела и фикстуры марио
    public void defineMario() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(16 / platformer.PPM, 50 / platformer.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / platformer.PPM);
        
        fdef.filter.categoryBits = platformer.MARIO_BIT;
        fdef.filter.maskBits = platformer.GROUND_BIT |
                               platformer.ENEMY_BIT |
                               platformer.OBJECT_BIT |
                               platformer.ENEMY_HEAD_BIT |
                               platformer.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

    }

    //Проверка на нахождение на земле
    public boolean isGround() {
        if ((int) b2body.getLinearVelocity().y == 0) {
            status = true;
        } else {
            status = false;
        }
        return status;
    }

    
}
