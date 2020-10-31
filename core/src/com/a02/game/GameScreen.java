package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class GameScreen implements Screen {
    final MainGame game;
    SpriteBatch batch;
    ArrayList<GameObject> objects= new ArrayList<GameObject>();
    ArrayList<Texture> textures= new ArrayList<Texture>();
    Texture imgL;
    Texture imgL2;
    Texture imgL3;
    Texture imgL4;
    Texture imgBox;
    Enemy larry;
    Enemy larry2;
    Enemy larry3;
    Enemy larry4;
    GameObject beacon;
    GameObject box;
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
        box= new GameObject(260,140,12,12,"Test1.png",0,"Box", 1000, true,1000,true);

        objects.add(box);
        objects.add(beacon);

        imgL = new Texture(Gdx.files.internal(larry.getSprite()));
        imgBox=new Texture(Gdx.files.internal(box.getSprite()));
        imgB= new Texture(Gdx.files.internal(beacon.getSprite()));

        textures.add(imgBox);
        textures.add(imgB);

        inventory = new Inventory();
        inventoryTexture = new Texture(Gdx.files.internal(inventory.getSprite()));
        inventory.insert(box);

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

        box.buy(this,objects,textures,inventory,map);

        larry.move(beacon.getX(), beacon.getY(),objects);

        batch.begin();

        batch.draw(mapTexture, 0, 0);

        batch.draw(imgL, larry.getX(), larry.getY());
        batch.draw(inventoryTexture, inventory.getX(), inventory.getY());
        batch.draw(imgBox, box.getX(), box.getY());
        for (GameObject object:objects) {
            batch.draw(textures.get(objects.indexOf(object)), object.getX(), object.getY());
        }
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
