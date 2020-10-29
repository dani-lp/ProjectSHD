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
    ArrayList<GameObject> objects= new ArrayList<>();
    ArrayList<InventoryObject> objectsInv = new ArrayList<>();
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
    InventoryObject boxInv;

    Texture imgB;
    Texture inventoryTexture;
    Texture imgBoxInv;

    OrthographicCamera camera;

    public GameScreen(MainGame game) {
        this.game = game;

        larry = new Enemy(20, 100, 16, 16, "Test2.png", 1, "Larry", 200, 1, 0.5f);
        larry = new Enemy(150, 120, 16, 16, "Test2.png", 1, "Larry", 200, 1, 0.5f);
        larry2 = new Enemy(80, 15, 16, 16, "Test2.png", 1, "Larry2", 200, 1, 0.5f);
        larry3 = new Enemy(30, 160, 16, 16, "Test2.png", 1, "Larry3", 200, 1, 0.5f);
        larry4 = new Enemy(200, 75, 16, 16, "Test2.png", 1, "Larry4", 200, 1, 0.5f);

        beacon= new GameObject(145,90,16,16,"beacon.png",0,"Beacon", 1000);
        //box= new GameObject(260,140,12,12,"Test1.png",0,"Box", 1000);
        objects.add(beacon);
        //objects.add(box);

        boxInv = new InventoryObject(260,140,12,12,"Test1.png","Box",20, true );

        imgL = new Texture(Gdx.files.internal(larry.getSprite()));
        imgL2 = new Texture(Gdx.files.internal(larry2.getSprite()));
        imgL3 = new Texture(Gdx.files.internal(larry3.getSprite()));
        imgL4 = new Texture(Gdx.files.internal(larry4.getSprite()));

        //imgBox=new Texture(Gdx.files.internal(box.getSprite()));
        imgBoxInv = new Texture(Gdx.files.internal(boxInv.getSprite()));
        imgB= new Texture(Gdx.files.internal(beacon.getSprite()));

        inventory = new Inventory();
        inventoryTexture = new Texture(Gdx.files.internal(inventory.getSprite()));
        inventory.insert(box);

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

        box.place(this);
        larry.move(beacon.getX(), beacon.getY());
        larry2.move(beacon.getX(), beacon.getY());
        larry3.move(beacon.getX(), beacon.getY());
        larry4.move(beacon.getX(), beacon.getY());
        larry.attack(objects);
        larry2.attack(objects);
        larry3.attack(objects);
        larry4.attack(objects);

        batch.begin();
        batch.draw(imgL, larry.getX(), larry.getY());
        batch.draw(imgL2, larry2.getX(), larry2.getY());
        batch.draw(imgL3, larry3.getX(), larry3.getY());
        batch.draw(imgL4, larry4.getX(), larry4.getY());
        batch.draw(imgB, beacon.getX(), beacon.getY());
        batch.draw(inventoryTexture, inventory.getX(), inventory.getY());
        batch.draw(imgBoxInv, boxInv.getX(), boxInv.getY());
        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
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
