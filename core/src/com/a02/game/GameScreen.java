package com.a02.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Iterator;

public class GameScreen implements Screen {
    final MainGame game;
    SpriteBatch batch;
    ArrayList<GameObject> objects= new ArrayList<GameObject>();
    ArrayList<Texture> textures= new ArrayList<Texture>();
    ArrayList<GameObject> deadObject = new ArrayList<GameObject>();
    ArrayList<Boolean> disponible = new ArrayList<Boolean>();
    Texture imgL;
    Texture imgL2;
    Texture imgL3;
    Texture imgL4;
    Texture imgWall;
    Texture imgFire;
    Texture imgElec;
    Enemy larry;
    Enemy larry2;
    Enemy larry3;
    Enemy larry4;
    GameObject beacon;
    GameObject wall;
    Attacker elec;
    Trap fire;
    Inventory inventory;
    Texture imgB;
    Texture inventoryTexture;
    Map map;
    Texture mapTexture;
    OrthographicCamera camera;

    public GameScreen(MainGame game) {
        this.game = game;

        larry = new Enemy(12, 90, 16, 16, "Test2.png", 1, "Larry", 200, 1, 10);

        beacon= new GameObject(145,90,16,16,"beacon.png",0,"Beacon", 1000, true,1000,false);
        wall= new GameObject(260,135,16,18,"Muro.png",0,"Wall", 1000, true,1000,true);
        elec= new Attacker(280,135,16,18,"Electricidad.png",2,"Electricity",100,true,1000,true,"Spark",20);
        fire=new Trap(260,115,16,18,"Fuego.png",3,"Fire",1000,true,1000,true,"Burn",15);

        objects.add(wall);
        objects.add(elec);
        objects.add(fire);
        objects.add(beacon);

        imgL = new Texture(Gdx.files.internal(larry.getSprite()));
        imgWall=new Texture(Gdx.files.internal(wall.getSprite()));
        imgElec=new Texture(Gdx.files.internal(elec.getSprite()));
        imgFire=new Texture(Gdx.files.internal(fire.getSprite()));
        imgB= new Texture(Gdx.files.internal(beacon.getSprite()));

        textures.add(imgWall);
        textures.add(imgElec);
        textures.add(imgFire);
        textures.add(imgB);

        inventory = new Inventory();
        inventoryTexture = new Texture(Gdx.files.internal(inventory.getSprite()));
        inventory.insert(wall);
        inventory.insert(elec);
        inventory.insert(fire);

        map = new Map("map1.png");
        mapTexture = new Texture(Gdx.files.internal(map.getSprite()));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 320, 180);
        batch = new SpriteBatch();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Actualiza cámara
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        wall.buy(this,objects,textures,inventory,map);
        elec.buy(this,objects,textures,inventory,map);
        fire.buy(this,objects,textures,inventory,map);

        larry.move(beacon.getX(), beacon.getY(),objects);

        batch.begin();

        batch.draw(mapTexture, 0, 0);

        batch.draw(imgL, larry.getX(), larry.getY());
        batch.draw(inventoryTexture, inventory.getX(), inventory.getY());
        batch.draw(imgWall, wall.getX(), wall.getY());
        batch.draw(imgElec, elec.getX(), elec.getY());
        batch.draw(imgFire, fire.getX(), fire.getY());

        for (GameObject object:objects) {
            batch.draw(textures.get(objects.indexOf(object)), object.getX(), object.getY());
        }
//        for(Iterator i = objects.iterator(); i.hasNext();){
//            GameObject deadObject = (GameObject) i.next();
//            if (deadObject.getHp() <= 0){
//                objects.remove(deadObject);
//            }
//        }

        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            System.exit(0);
        }
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
        batch.dispose();
        imgL.dispose();
        imgB.dispose();
    }


}
