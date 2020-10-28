/**
 *  Clase principal del juego, se encarga de inicializar y renderizar todos los objetos.
 */

package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class GameScreen implements Screen {
    final MainGame game;

    Texture imgL;
    Texture imgBox;
    Enemy larry;
    GameObject beacon;
    GameObject box;
    Inventory inventory;
    Texture imgB;
    Texture inventoryTexture;

    static OrthographicCamera camera;

    public GameScreen(final MainGame game) {
        this.game = game;

        larry = new Enemy(200, 100, 16, 16, "Test2.png", 1, "Larry", 200, 1, 1);
        imgL = new Texture(Gdx.files.internal(larry.getSprite()));
        beacon= new GameObject(20,50,16,16,"beacon.png",0,"Beacon", 1000, true, 1000);
        box= new GameObject(260,140,12,12,"Test1.png",0,"Box", 1000, true, 1000);

        imgBox=new Texture(Gdx.files.internal(box.getSprite()));
        imgB= new Texture(Gdx.files.internal(beacon.getSprite()));

        inventory = new Inventory();
        inventoryTexture = new Texture(Gdx.files.internal(inventory.getSprite()));
        inventory.insert(box);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 320, 180);
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
        game.entityBatch.setProjectionMatrix(camera.combined);

        //Físicas
        box.buy();
        larry.move(beacon.getX(), beacon.getY());
        larry.attack(beacon);

        //Dibujado
        game.entityBatch.begin();
        game.entityBatch.draw(imgL, larry.getX(), larry.getY());
        game.entityBatch.draw(imgB, beacon.getX(), beacon.getY());
        game.entityBatch.draw(inventoryTexture, inventory.getX(), inventory.getY());
        game.entityBatch.draw(imgBox, box.getX(), box.getY());
        game.entityBatch.end();

        //Salida manual, temporal
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
        imgL.dispose();
        imgB.dispose();
    }
}
