package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Random;

public class WGML extends ApplicationAdapter {
    SpriteBatch batch;
    OrthographicCamera camera;
    ShapeRenderer shape, shape2;
    World world;
    Box2DDebugRenderer debugRenderer;
    Character knight;
    Map[][] maps;
    int currentMapIndex1;
    int currentMapIndex2;
    float difficulty;
    Random rd;

    @Override
    public void create() {
        // create the camera and the SpriteBatch    
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        shape.setAutoShapeType(true);
        shape2 = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        world = new World(new Vector2(0, 0), true); // No gravity in x or y direction
        world.setContactListener(new CollisionHandler());
        mapSetUp();

        debugRenderer = new Box2DDebugRenderer();
        knight = new Character(500, 500, 100, 100, 200, 200);
        difficulty = 1;
        rd = new Random();
    }

    @Override
    public void render() {
        world.step(1/60f, 6, 2); // Update the world
        debugRenderer.render(world, camera.combined); // Render Box2D world for debugging

        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        shape.setProjectionMatrix(camera.combined);
        //shape2.setProjectionMatrix(camera.combined);

        shape.begin();
		shape.setColor(Color.WHITE);
        handleSwitchMap();
        Map curMap = getCurrentMap();
        curMap.draw(shape);

        //render the character
		knight.update(curMap);
		for (Projectile p:curMap.getProjectileList()) {
			p.update(curMap);
		}
		for (Enemy e: curMap.getEnemyList()) {
			e.update(curMap);
		}
        knight.draw(shape);
        shape.end();
        //update the position of the character
    }

    public void mapSetUp() {
        maps = new Map[2][3];
        Boolean[] gateValidity = new Boolean[4];

        gateValidity[0] = false;
        gateValidity[1] = true;
        gateValidity[2] = false;
        gateValidity[3] = true;
        Map map1 = new Map(world, gateValidity); // Pass the world to the Map constructor
        map1.addBlock(0, 300, 100, 700, false);
        map1.addBlock(0, 780, 100, 700, false);
        map1.addBlock(1220, 300, 100, 700, false);
        map1.addBlock(1220, 780, 100, 700, false);
        map1.addEnemy(1000, 500, 100, 100, 10, false);
        map1.addTriangle(600, 400, 650, 450, 550, 450);
        maps[0][0] = map1;

        gateValidity[0] = false;
        gateValidity[1] = true;
        gateValidity[2] = true;
        gateValidity[3] = true;
        Map map2 = new Map(world, gateValidity); // Pass the world to the Map constructor
        map2.addBlock(200, 200, 200, 200, false);
        map2.addBlock(200, 680, 200, 200, false);
        map2.addBlock(1520, 200, 200, 200, false);
        map2.addBlock(1520, 680, 200, 200, false);
        map2.addBlock(830, 410, 300, 300, false);
        maps[0][1] = map2;

        gateValidity[0] = false;
        gateValidity[1] = true;
        gateValidity[2] = true;
        gateValidity[3] = false;
        Map map3 = new Map(world, gateValidity); // Pass the world to the Map constructor
        map3.addBlock(0, 400, 50, 350, false);
        map3.addBlock(0, 680, 50, 350, false);
        map3.addBlock(1570, 400, 50, 350, false);
        map3.addBlock(1570, 680, 50, 350, false);
        map3.addBlock(500, 0, 350, 50, false);
        map3.addBlock(1370, 0, 350, 50, false);
        map3.addBlock(500, 730, 350, 50, false);
        map3.addBlock(1370, 730, 350, 50, false);
        maps[0][2] = map3;

        gateValidity[0] = true;
        gateValidity[1] = false;
        gateValidity[2] = false;
        gateValidity[3] = true;
        Map map4 = new Map(world, gateValidity); // Pass the world to the Map constructor
        map4.addBlock(870, 450, 180, 180, false);
        map4.addBlock(260, 200, 50, 1400, false);
        map4.addBlock(260, 830, 50, 1400, false);
        map4.addBlock(350, 250, 50, 50, false);
        map4.addBlock(550, 250, 100, 50, false);
        map4.addBlock(850, 250, 50, 50, false);
        map4.addBlock(1025, 250, 75, 50, false);
        map4.addBlock(1400, 250, 250, 50, false);
        map4.addBlock(490, 730, 100, 50, false);
        map4.addBlock(790, 690, 140, 50, false);
        map4.addBlock(970, 780, 50, 50, false);
        map4.addBlock(1470, 710, 120, 50, false);
        map4.addBlock(910, 0, 200, 50, false);
        maps[1][0] = map4;

        gateValidity[0] = true;
        gateValidity[1] = false;
        gateValidity[2] = true;
        gateValidity[3] = true;
        Map map5 = new Map(world, gateValidity); // Pass the world to the Map constructor
        map5.addBlock(200, 300, 100, 100, false);
        map5.addBlock(400, 200, 150, 150, false);
        map5.addBlock(700, 100, 200, 200, false);
        map5.addBlock(1000, 300, 250, 100, false);
        map5.addBlock(1300, 400, 300, 150, false);
        map5.addBlock(500, 600, 200, 100, false);
        map5.addBlock(800, 700, 150, 250, false);
        map5.addBlock(1100, 800, 100, 300, false);
        map5.addBlock(1400, 900, 200, 200, false);
        map5.addBlock(100, 100, 50, 50, false);
        map5.addBlock(1600, 1000, 180, 180, false);
        maps[1][1] = map5;

        gateValidity[0] = true;
        gateValidity[1] = false;
        gateValidity[2] = true;
        gateValidity[3] = false;
        Map map6 = new Map(world, gateValidity); // Pass the world to the Map constructor
        map6.addBlock(150, 200, 100, 100, false);
        map6.addBlock(300, 400, 150, 150, false);
        map6.addBlock(600, 200, 200, 200, false);
        map6.addBlock(900, 400, 100, 300, false);
        map6.addBlock(1200, 500, 250, 150, false);
        map6.addBlock(500, 700, 200, 100, false);
        map6.addBlock(800, 800, 100, 250, false);
        map6.addBlock(1100, 900, 150, 100, false);
        map6.addBlock(1400, 600, 200, 200, false);
        map6.addBlock(100, 500, 100, 150, false);
        map6.addBlock(1600, 700, 150, 100, false);
        map6.addBlock(1700, 200, 100, 200, false);
        map6.addBlock(400, 900, 200, 150, false);
        map6.addBlock(1300, 300, 100, 200, false);
        maps[1][2] = map6;

        currentMapIndex1 = 1;
        currentMapIndex2 = 1;
    }


    public Map getCurrentMap() {
        return maps[currentMapIndex1][currentMapIndex2];
    }

    public void handleSwitchMap() {
        knight.checkGateCollision(getCurrentMap(), this);
    }

    public String switchMap(int index1, int index2) {
        if ((index1 >= 0 && index1 < maps.length) && (index2 >= 0 && index2 < maps[1].length)) {
            currentMapIndex1 = index1;
            currentMapIndex2 = index2;
            return "done";
        }
        return "invalid indices";
    }

    @Override
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
        shape.dispose();
        shape2.dispose();
    }
}

