/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Sprites.Goomba;
import com.mygdx.game.platformer;
import com.mygdx.game.screens.PlayScreen;

/**
 *
 * @author Влад
 */
public class B2WorldCreator {

    private Array<Goomba> goombas;

    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        Body body;

        //Отрисовка слоя объектов в зависимости от их формы
        //Отрисовка прямоугольных элементов слоя Objects
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / platformer.PPM, (rect.getY() + rect.getHeight() / 2) / platformer.PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth() / 2) / platformer.PPM, (rect.getHeight() / 2) / platformer.PPM);

            fdef.filter.categoryBits = platformer.OBJECT_BIT;

            fdef.shape = shape;
            body.createFixture(fdef).setUserData(this);
        }

        for (MapObject object : map.getLayers().get("Lines").getObjects().getByType(PolylineMapObject.class)) {
            PolylineMapObject line = (PolylineMapObject) object;

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(line.getPolyline().getX() / platformer.PPM, line.getPolyline().getY() / platformer.PPM);
            body = world.createBody(bdef);

            ChainShape cshape = new ChainShape();

            float[] tVertices = new float[line.getPolyline().getVertices().length];

            for (int i = 0; i < tVertices.length; i++) {
                tVertices[i] = line.getPolyline().getVertices()[i] / platformer.PPM;
            }

            cshape.createChain(tVertices);

            fdef.filter.categoryBits = platformer.GROUND_BIT;

            fdef.shape = cshape;
            body.createFixture(fdef);
        }
        for (MapObject object : map.getLayers().get("Border").getObjects().getByType(PolylineMapObject.class)) {
            PolylineMapObject line = (PolylineMapObject) object;

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(line.getPolyline().getX() / platformer.PPM, line.getPolyline().getY() / platformer.PPM);
            body = world.createBody(bdef);

            ChainShape cshape = new ChainShape();

            float[] tVertices = new float[line.getPolyline().getVertices().length];

            for (int i = 0; i < tVertices.length; i++) {
                tVertices[i] = line.getPolyline().getVertices()[i] / platformer.PPM;
            }

            cshape.createChain(tVertices);

            fdef.shape = cshape;
            body.createFixture(fdef);
            // cshape.dispose();

        }
    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }

}
