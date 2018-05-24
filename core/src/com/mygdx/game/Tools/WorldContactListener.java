/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.Sprites.Enemy;
import com.mygdx.game.Sprites.MarioClone;
import com.mygdx.game.Sprites.Mushroom;
import com.mygdx.game.platformer;

/**
 *
 * @author Влад
 */
public class WorldContactListener implements ContactListener {

    private boolean isGround;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        //Проверка на столкновение различных коллизий и последующие действия при столкновении
        switch (cDef) {
            case platformer.ENEMY_HEAD_BIT | platformer.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == platformer.ENEMY_HEAD_BIT) {
                    ((Enemy) fixA.getUserData()).hitOnHead();
                } else if (fixB.getFilterData().categoryBits == platformer.ENEMY_HEAD_BIT) {
                    ((Enemy) fixB.getUserData()).hitOnHead();
                }
                break;
            case platformer.ENEMY_BIT | platformer.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == platformer.ENEMY_BIT) {
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                } else if (fixB.getFilterData().categoryBits == platformer.ENEMY_BIT) {
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                }
                break;
            case platformer.MARIO_BIT | platformer.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == platformer.ENEMY_BIT) {
                    ((Enemy) fixA.getUserData()).kill((MarioClone) fixB.getUserData());
                } else if (fixB.getFilterData().categoryBits == platformer.ENEMY_BIT) {
                    ((Enemy) fixB.getUserData()).kill((MarioClone) fixA.getUserData());
                }
                break;
            case platformer.ENEMY_BIT | platformer.ENEMY_BIT:
                ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case platformer.MARIO_BIT | platformer.ITEM_BIT:
                if (fixA.getFilterData().categoryBits == platformer.ITEM_BIT) {
                    ((Mushroom) fixA.getUserData()).destroy((MarioClone) fixB.getUserData());

                } else if (fixB.getFilterData().categoryBits == platformer.ITEM_BIT) {
                    ((Mushroom) fixB.getUserData()).destroy((MarioClone) fixA.getUserData());
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public boolean getStatus() {
        return isGround;
    }

}
