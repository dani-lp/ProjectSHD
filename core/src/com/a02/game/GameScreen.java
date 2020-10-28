package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen {
    final MainGame game;
    SpriteBatch batch;
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

    OrthographicCamera camera;

    public GameScreen(MainGame game) {
        this.game = game;

        larry = new Enemy(20, 100, 16, 16, "Test2.png", 1, "Larry", 200, 1, 1);
        larry = new Enemy(150, 120, 16, 16, "Test2.png", 1, "Larry", 200, 1, 0.4f);
        larry2 = new Enemy(80, 15, 16, 16, "Test2.png", 1, "Larry2", 200, 1, 0.4f);
        larry3 = new Enemy(30, 160, 16, 16, "Test2.png", 1, "Larry3", 200, 1, 0.4f);
        larry4 = new Enemy(200, 75, 16, 16, "Test2.png", 1, "Larry4", 200, 1, 0.4f);
        imgL = new Texture(Gdx.files.internal(larry.getSprite()));
        beacon= new GameObject(20,50,16,16,"beacon.png",0,"Beacon", 1000, true, 1000);
        imgL2 = new Texture(Gdx.files.internal(larry2.getSprite()));
        imgL3 = new Texture(Gdx.files.internal(larry3.getSprite()));
        imgL4 = new Texture(Gdx.files.internal(larry4.getSprite()));
        beacon= new GameObject(145,90,16,16,"beacon.png",0,"Beacon", 1000, true, 1000);
        box= new GameObject(260,140,12,12,"Test1.png",0,"Box", 1000, true, 1000);

        imgBox=new Texture(Gdx.files.internal(box.getSprite()));
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

        box.buy(this);
        larry.move(beacon.getX(), beacon.getY());
        larry2.move(beacon.getX(), beacon.getY());
        larry3.move(beacon.getX(), beacon.getY());
        larry4.move(beacon.getX(), beacon.getY());
        larry.attack(beacon);
        larry2.attack(beacon);
        larry3.attack(beacon);
        larry4.attack(beacon);

        batch.begin();
        batch.draw(imgL, larry.getX(), larry.getY());
        batch.draw(imgL2, larry2.getX(), larry2.getY());
        batch.draw(imgL3, larry3.getX(), larry3.getY());
        batch.draw(imgL4, larry4.getX(), larry4.getY());
        batch.draw(imgB, beacon.getX(), beacon.getY());
        batch.draw(inventoryTexture, inventory.getX(), inventory.getY());
        batch.draw(imgBox, box.getX(), box.getY());
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
