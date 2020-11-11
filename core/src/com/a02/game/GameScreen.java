package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class GameScreen implements Screen {
    final MainGame game;

    private static boolean buying;
    private static int gold;

    List<GameObject> objects = new ArrayList<GameObject>(); //Objetos en el juego
    List<Enemy> enemies = new ArrayList<Enemy>(); // Enemigos del juego

    Enemy larry;
    Enemy larry2;
    Enemy larry3;
    Enemy larry4;
    Defender beacon;
    Defender wall;        //Enemigos y objetos
    Attacker elec;
    Trap fire;
    Inventory inventory;

    Map map;
    OrthographicCamera camera;

    BitmapFont font;

    int secTimer;   //Contador de segundos. Suma 1 cada fotograma.
    float animationTimer;   //Contador para animaciones

    public GameScreen(MainGame game) {
        this.game = game;
        buying = false;
        gold = 10000;

        font = new BitmapFont(Gdx.files.internal("Fonts/test.fnt"));

        secTimer = 0;
        animationTimer = 0;

        larry = new Enemy(12, 63, 16, 16, "Test2.png", 1, "Larry", 200, 100, 30);
        larry2 = new Enemy(45, 25, 16, 16, "Test2.png", 2, "Jeremy", 200, 100, 30);
        larry3 = new Enemy(100, 59, 16, 16, "Test2.png", 1, "Larry", 200, 100, 30);
        larry4 = new Enemy(350, 20, 16, 16, "Test2.png", 2, "Jeremy", 200, 100, 30);

        beacon = new Defender(145,90,16,16,"beacon.png",0,"Beacon","Beacon", 1000, true,10000000);
        wall = new Defender(260,135,16,18,"Muro.png",0,"Wall","Defense", 1000, true,1000);
        elec = new Attacker(280,135,16,18,"electricity.png",2,"Electricity","Attack",100,true,1000,"Spark",50);
        fire = new Trap(260,115,16,18,"Traps/fire.png",3,"Fire","Trap",10,true,1000,"CONFUSE",1000);

        enemies.add(larry);
        enemies.add(larry2);
        enemies.add(larry3);
        enemies.add(larry4);

        objects.add(wall);
        objects.add(elec);
        objects.add(fire);
        objects.add(beacon);

        inventory = new Inventory();
        inventory.insert(wall);
        inventory.insert(elec);
        inventory.insert(fire);

        map = new Map("map1.png");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 320, 180);
        game.entityBatch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        //Reset de OpenGL
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Actualiza cámara
        camera.update();
        game.entityBatch.setProjectionMatrix(camera.combined);

        //Actualiza valores estáticos
        secTimer += 1;
        animationTimer += Gdx.graphics.getDeltaTime();

        if (secTimer % 20 == 0) gold++;

        //Actualiza estados de enemigos y objetos
        wall.grabObject(map,objects);
        elec.grabObject(map,objects);
        fire.grabObject(map,objects);

        elec.update(objects, enemies, secTimer);

        //Actualiza "presencia" de enemigos y objetos
        ListIterator<GameObject> objectIterator = objects.listIterator();
        while(objectIterator.hasNext()){
            GameObject tempObj = objectIterator.next();
            tempObj.update(objects, enemies, secTimer);
            if(tempObj.getHp() <= 0) {
                map.getOccGrid()[(int) tempObj.getX()/16][(int) tempObj.getY()/18] = false;
                objectIterator.remove();
            }
        }

        ListIterator<Enemy> enemyIterator = enemies.listIterator();
        while(enemyIterator.hasNext()){
            Enemy tempEnemy = enemyIterator.next();
            tempEnemy.update(beacon.getX(), beacon.getY(), objects, enemies, secTimer);
            if(tempEnemy.getHp() <= 0) {
                enemyIterator.remove();
            }
        }

        TextureRegion currentLarryWalk = larry.walkAnimation.getKeyFrame(animationTimer, true); //Animación

        //Dibujado
        game.entityBatch.begin();
        game.entityBatch.draw(map.getTexture(), 0, 0);

        for (GameObject object:objects) {
            game.entityBatch.draw(object.getTexture(), object.getX(), object.getY());
        }

        for (Enemy enemy: enemies) {
            if (enemy.state != Enemy.State.DEAD) {
                game.entityBatch.draw(currentLarryWalk, enemy.getX(), enemy.getY());
            }
        }

        game.entityBatch.draw(inventory.getTexture(), inventory.getX(), inventory.getY());

        for (GameObject object:inventory.getObjects()) {    //Objetos del inventario
            game.entityBatch.draw(object.getTexture(), object.getX(), object.getY());
        }

        font.draw(game.entityBatch, "ORO  " + Integer.toString(gold), 5, 175);

        game.entityBatch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            System.exit(0);
        }
    }

    @Override
    public void dispose() {
        game.entityBatch.dispose();

        for (GameObject object: objects) {
            object.getTexture().dispose();
        }
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public static int getGold() {
        return gold;
    }

    public static void setGold(int gold) {
        GameScreen.gold = gold;
    }

    public static boolean isBuying() {
        return buying;
    }

    public static void setBuying(boolean buying) {
        GameScreen.buying = buying;
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
    public void show() {

    }
}
