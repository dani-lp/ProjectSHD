package com.a02.screens;

import com.a02.dbmanager.DBException;
import com.a02.dbmanager.DBManager;
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
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static com.a02.game.MainGame.mainGameScreen;

public class GameScreen implements Screen {
    final MainGame game;

    private static Logger logger = Logger.getLogger(GameScreen.class.getName());

    private static boolean buying;
    private static int gold;

    public List<GameObject> objects = new ArrayList<GameObject>(); //Objetos en el juego
    public List<Enemy> enemies = new ArrayList<Enemy>(); // Enemigos del juego


    public Defender beacon; //Enemigos y objetos

    public Inventory inventory;

    Map map;
    OrthographicCamera camera;

    BitmapFont font;

    public int secTimer;   //Contador de segundos. Suma 1 cada fotograma.
    float animationTimer;   //Contador para animaciones

    public GameScreen(MainGame game) {
        Logger.getLogger("").setLevel(Level.INFO);
        Logger.getLogger("").getHandlers()[0].setLevel(Level.INFO);
        logger.info("Inicio del GameScreen");

        this.game = game;
        buying = false;
        gold = 6000;

        font = new BitmapFont(Gdx.files.internal("Fonts/test.fnt"));

        secTimer = 0;
        animationTimer = 0;

        inventory = new Inventory();

        crearObjetos();

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

        //Actualiza estado de objetos del inventario
        for (GameObject object : inventory.getObjects()) {
            object.grabObject(map, objects);
        }

        //Actualiza "presencia" y estado de enemigos y objetos
        ListIterator<GameObject> objectIterator = objects.listIterator();
        while(objectIterator.hasNext()){
            GameObject tempObj = objectIterator.next();
            tempObj.update(this);
            if(tempObj.getHp() <= 0) {
                try {
                    map.getOccGrid()[(int) tempObj.getX() / 16][(int) tempObj.getY() / 18] = false;
                } catch (IndexOutOfBoundsException e) {
                    //TODO: arreglar fallos ocasionales
                }
                objectIterator.remove();
            }
        }

        ListIterator<Enemy> enemyIterator = enemies.listIterator();
        while(enemyIterator.hasNext()){
            Enemy tempEnemy = enemyIterator.next();
            tempEnemy.update(this);
            if(tempEnemy.getHp() <= 0) {
                enemyIterator.remove();
            }
        }

        //Dibujado
        draw();

        if (objects.get(0).getHp()<=0) {
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

    private void draw() {
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
            if (object != null) game.entityBatch.draw(object.getTexture(), object.getX(), object.getY());
        }

        font.draw(game.entityBatch, "ORO  " + Integer.toString(gold), 5, 175);

        game.entityBatch.end();
    }

    public void ronda1(){

        try {
            DBManager.dbManager.connect("Databases/base.db");
        } catch (DBException e) {
            e.printStackTrace();
        }
        Enemy larry= new Enemy();
        try {

            try {
                Scanner sc= new Scanner(new FileInputStream("core/assets/ronda1.csv"));
                while (sc.hasNext()) {
                    String line= sc.next();
                    String[] campos = line.split(";");

                    larry = DBManager.dbManager.getEnemy(0);
                    larry.setX(Integer.parseInt(campos[0]));
                    larry.setY(Integer.parseInt(campos[1]));
                    larry.setStartTime(Integer.parseInt(campos[2]));
                    larry.setWidth(16);
                    larry.setHeight(16);
                    larry.hpBar.setMaxHP(larry.getHp());
                    larry.animations();
                    enemies.add(larry);
                }
                sc.close();


            } catch (FileNotFoundException e) {
                System.out.println("No se a podido abrir el fichero ");
            }


        } catch (DBException e) {
            e.printStackTrace();
        }

        /**
         * larry = new Enemy(12, 63, 16, 16, idE,hpE, attackE, speed,0,goldValue,walkpath,attackpath,deathpath);
         *         larry2 = new Enemy(275, 25, 16, 16,idE, hpE, attackE, speed,300,goldValue,walkpath,attackpath,deathpath);
         *         larry3 = new Enemy(100, 359, 16, 16,idE,hpE, attackE, speed,470,goldValue,walkpath,attackpath,deathpath);
         *         larry4 = new Enemy(350, 220, 16, 16,idE,hpE, attackE, speed,500,goldValue,walkpath,attackpath,deathpath);
         *         larry5 = new Enemy(-12, 363, 16, 16,idE,hpE, attackE, speed,680,goldValue,walkpath,attackpath,deathpath);
         *         larry6 = new Enemy(445, 25, 16, 16,idE,hpE, attackE, speed,742,goldValue,walkpath,attackpath,deathpath);
         *         larry7 = new Enemy(100, 459, 16, 16,idE,hpE, attackE, speed,790,goldValue,walkpath,attackpath,deathpath);
         *         larry8 = new Enemy(350, 320, 16, 16,idE,hpE, attackE, speed,825,goldValue,walkpath,attackpath,deathpath);
         *         larry9 = new Enemy(512, 63, 16, 16,idE,hpE, attackE, speed,875,goldValue,walkpath,attackpath,deathpath);
         *         larry10 = new Enemy(-45, 25, 16, 16,idE,hpE, attackE, speed,905,goldValue,walkpath,attackpath,deathpath);
         *         larry11 = new Enemy(100, -59, 16, 16,idE,hpE, attackE, speed,925,goldValue,walkpath,attackpath,deathpath);
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
         */



    }

    public void crearObjetos(){
        try {
            DBManager.dbManager.connect("Databases/base.db");
        } catch (DBException e) {
            e.printStackTrace();
        }

            // A partir de una conexiÃ³n activa obtenemos el objeto para ejecutar
            // sentencias SQL en la base de datos.
        for (int i = 0; i < 2; i++){
            try {
                Defender def=DBManager.dbManager.getDefender(i);
                if (def.getId()==0) {
                    def.setX(145);
                    def.setY(90);
                }
                def.hpBar.setMaxHP(def.getHp());
                def.textures();
                objects.add(def);
                if (def.getId()!=0) {
                    inventory.insert(def);
                }

            } catch (DBException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 5; i++){
            try {
                Trap trap=DBManager.dbManager.getTrap(i);
                trap.hpBar.setMaxHP(trap.getHp());
                trap.textures();
                objects.add(trap);
                inventory.insert(trap);
            } catch (DBException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 1; i++){
            try {
                Attacker att=DBManager.dbManager.getAttacker(i);
                att.hpBar.setMaxHP(att.getHp());
                att.textures();
                objects.add(att);
                inventory.insert(att);
            } catch (DBException e) {
                e.printStackTrace();
            }
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
