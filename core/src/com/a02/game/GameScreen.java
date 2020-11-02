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
    ArrayList<GameObject> objects = new ArrayList<GameObject>(); //Objetos en el juego
    ArrayList<Texture> textures = new ArrayList<Texture>();  //Texturas de los objetos del juego
    ArrayList<GameObject> deadObject = new ArrayList<GameObject>(); //No usado de momento

    Texture imgL;
    Texture imgWall;
    Texture imgFire;
    Texture imgElec;
    Texture imgB;
    Texture inventoryTexture;
    Texture mapTexture;

    Enemy larry;
    GameObject beacon;
    GameObject wall;
    Attacker elec;
    Trap fire;
    Inventory inventory;

    Map map;
    OrthographicCamera camera;

    float secTimer;

    public GameScreen(MainGame game) {
        this.game = game;
        secTimer = 0;

        larry = new Enemy(12, 90, 16, 16, "Test2.png", 1, "Larry", 200, 1, 30);

        beacon= new GameObject(145,90,16,16,"beacon.png",0,"Beacon", 1000, true,1000,false,true);
        wall= new GameObject(260,135,16,18,"Muro.png",0,"Wall", 1000, true,500,true,true);
        elec= new Attacker(280,135,16,18,"Electricidad.png",2,"Electricity",100,true,1000,true,true,"Spark",20);
        fire=new Trap(260,115,16,18,"Fuego.png",3,"Fire",1000,true,1000,true,true,"Burn",15);

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
        game.entityBatch = new SpriteBatch();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        secTimer += Gdx.graphics.getDeltaTime();

        //System.out.println(secTimer);

        ArrayList<GameObject> copy=new ArrayList<GameObject>();
        ArrayList<Texture> copyt=new ArrayList<Texture>();
        //Actualiza cámara
        camera.update();
        game.entityBatch.setProjectionMatrix(camera.combined);

        wall.buy(this,objects,textures,inventory,map);
        elec.buy(this,objects,textures,inventory,map);
        fire.buy(this,objects,textures,inventory,map);

        larry.update(beacon.getX(), beacon.getY(),objects, secTimer);
        for (GameObject object:objects) {
            if (object.getHp()!=0){
                copy.add(object);
                copyt.add(textures.get(objects.indexOf(object)));
            }
        }
        objects=copy;
        textures=copyt;

        game.entityBatch.begin();

        game.entityBatch.draw(mapTexture, 0, 0);
        game.entityBatch.draw(mapTexture, 0, 0);

        game.entityBatch.draw(imgL, larry.getX(), larry.getY());
        game.entityBatch.draw(inventoryTexture, inventory.getX(), inventory.getY());
        game.entityBatch.draw(imgWall, wall.getX(), wall.getY());
        game.entityBatch.draw(imgElec, elec.getX(), elec.getY());
        game.entityBatch.draw(imgFire, fire.getX(), fire.getY());

        for (GameObject object:objects) {
            game.entityBatch.draw(textures.get(objects.indexOf(object)), object.getX(), object.getY());
        }
//        for(Iterator i = objects.iterator(); i.hasNext();){
//            GameObject deadObject = (GameObject) i.next();
//            if (deadObject.getHp() <= 0){
//                objects.remove(deadObject);
//            }
//        }

        game.entityBatch.end();

        //Borrado de Objetos cuando su vida baja a 0
        Iterator iterator = objects.iterator();
        while (iterator.hasNext())
        {
            GameObject object = (GameObject) iterator.next();
            if (object.getHp()<=0){
                iterator.remove();
            }
        }

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
        game.entityBatch.dispose();
        imgL.dispose();
        imgB.dispose();
    }


}
