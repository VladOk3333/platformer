/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.Extensions;

import com.mygdx.game.Sprites.MarioClone;
import com.mygdx.game.platformer;
import com.mygdx.game.screens.PlayScreen;

public interface Module {

  public static final int EXIT_SUCCESS = 0;
  public static final int EXIT_FAILURE = 1;

  public void load(MarioClone player);
  public int run(/*platformer game*/);
  public void unload(/*platformer game*/);
}
