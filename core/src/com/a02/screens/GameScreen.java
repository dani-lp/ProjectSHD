package com.a02.screens;

import com.a02.entity.Attacker;
import com.a02.entity.Defender;
import com.a02.entity.Enemy;
import com.a02.entity.Trap;
import com.a02.entity.GameObject;
import com.a02.component.Inventory;
import com.a02.game.MainGame;
import com.a02.component.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static com.a02.game.MainGame.mainGameScreen;

public class GameScreen implements Screen {
    final MainGame game;

    private static Logger logger = Logger.getLogger(GameScreen.class.getName());

    private static boolean buying;
    private static int gold;

    List<GameObject> objects = new ArrayList<GameObject>(); //Objetos en el juego
    List<Enemy> enemies = new ArrayList<Enemy>(); // Enemigos del juego

    Enemy larry;
    Enemy larry2;
    Enemy larry3;
    Enemy larry4;
    Enemy larry5;
    Enemy larry6;
    Enemy larry7;
    Enemy larry8;
    Enemy larry9;
    Enemy larry10;
    Enemy larry11;

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



    public void ronda1(){
        larry = new Enemy(12, 63, 16, 16, 1,200, 100, 10,0);
        larry2 = new Enemy(275, 25, 16, 16,1, 200, 100, 10,300);
        larry3 = new Enemy(100, 359, 16, 16,1,200, 100, 10,470);
        larry4 = new Enemy(350, 220, 16, 16,1,200, 100, 10,500);
        larry5 = new Enemy(-12, 363, 16, 16,1,200, 100, 10,680);
        larry6 = new Enemy(445, 25, 16, 16,1,200, 100, 10,742);
        larry7 = new Enemy(100, 459, 16, 16, 1,200, 100, 10,790);
        larry8 = new Enemy(350, 320, 16, 16,1,200, 100, 10,825);
        larry9 = new Enemy(512, 63, 16, 16,1,200, 100, 10,875);
        larry10 = new Enemy(-45, 25, 16, 16,1,200, 100, 10,905);
        larry11 = new Enemy(100, -59, 16, 16,1,200, 100, 10,925);

        enemies.add(larry);
        enemies.add(larry2);
        enemies.add(larry3);
        enemies.add(larry4);
        enemies.add(larry5);
        enemies.add(larry6);
        enemies.add(larry7);
        enemies.add(larry8);
        enemies.add(larry9);
        enemies.add(larry10);
        enemies.add(larry11);
    }

    public GameScreen(MainGame game) {
        this.game = game;
        buying = false;
        gold = 6000;

        font = new BitmapFont(Gdx.files.internal("Fonts/test.fnt"));

        secTimer = 0;
        animationTimer = 0;

        Logger.getLogger("").setLevel(Level.INFO);
        Logger.getLogger("").getHandlers()[0].setLevel(Level.INFO);
        logger.info("Inicio del GameScreen");


        beacon = new Defender(145,90,16,16,0,"Beacon", 1000, true,1000);
        wall = new Defender(260,135,16,18,1,"Defense", 1000, true,900);
        elec = new Attacker(280,135,16,18,2,"Electricity",100,true,500,"Spark",50);
        fire = new Trap(260,115,16,18,3,"Fire",10,true,1000,"FREEZE",1000);

        objects.add(wall);
        objects.add(elec);
        objects.add(fire);
        objects.add(beacon);

        inventory = new Inventory();
        inventory.insert(wall);
        inventory.insert(elec);
        inventory.insert(fire);

        ronda1();

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

        //Dibujado
        game.entityBatch.begin();
        game.entityBatch.draw(map.getTexture(), 0, 0);

        for (GameObject object:objects) {
            game.entityBatch.draw(object.getTexture(), object.getX(), object.getY());
        }

        for (Enemy enemy: enemies) {
            if (enemy.state != Enemy.State.DEAD) {
                game.entityBatch.draw(enemy.getCurrentAnimation(animationTimer), enemy.getX(), enemy.getY());
            }
        }

        for (GameObject object:objects) {
            game.entityBatch.draw(object.hpBar.getBackground(), object.hpBar.getX(), object.hpBar.getY(), 14,2);
            game.entityBatch.draw(object.hpBar.getForeground(), object.hpBar.getX(), object.hpBar.getY(), object.hpBar.getCurrentWidth(),2);
        }
        for (Enemy enemy: enemies) {
            game.entityBatch.draw(enemy.hpBar.getBackground(), enemy.hpBar.getX(), enemy.hpBar.getY(), 14,2);
            game.entityBatch.draw(enemy.hpBar.getForeground(), enemy.hpBar.getX(), enemy.hpBar.getY(), enemy.hpBar.getCurrentWidth(),2);
        }

        game.entityBatch.draw(inventory.getTexture(), inventory.getX(), inventory.getY());

        for (GameObject object:inventory.getObjects()) {    //Objetos del inventario
            game.entityBatch.draw(object.getTexture(), object.getX(), object.getY());
        }

        font.draw(game.entityBatch, "ORO  " + Integer.toString(gold), 5, 175);

        game.entityBatch.end();


        if (beacon.getHp()<=0) {
            game.setScreen(new MenuScreen(game));
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
