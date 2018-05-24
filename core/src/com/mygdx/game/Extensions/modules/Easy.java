/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.Extensions.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.platformer;
import com.mygdx.game.Sprites.MarioClone;
import com.mygdx.game.Extensions.Module;
import com.mygdx.game.screens.PlayScreen;
import java.util.Random;

public class Easy implements Module {

    private long _lastActionTime = 0;
    private PlayScreen _ls = null;
    public MarioClone player;


    @Override
    public int run(/*platformer game*/) {
        System.out.println("run");
          if(player.b2body.getLinearVelocity().x <= 2)
            player.b2body.applyLinearImpulse(new Vector2(0.15f, 0), player.b2body.getWorldCenter(), true);
          Random rand = new Random();
          int num = rand.nextInt(100);
          if ((_lastActionTime == 0 || (TimeUtils.millis() - _lastActionTime > 1 * 1200)) && num == 5) {
            _lastActionTime = TimeUtils.millis();
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);           
          }
        
        return 0;
    }
    

    @Override
    public void unload() {
        System.out.println("unload");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load(MarioClone player) {
        this.player = player;
    }

}
