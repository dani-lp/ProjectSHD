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

    public Inventory drawing;
    public Inventory inventory;
    public Inventory weapons;
    public Inventory defensive;
    public Inventory tramps;

    Map map;
    OrthographicCamera camera;

    BitmapFont font;

    public int secTimer;   //Contador de segundos. Suma 1 cada fotograma.
    float animationTimer;   //Contador para animaciones
    private static boolean LOGGING = true;

    private void log(Level level, String msg, Throwable excepcion) {
        if (!LOGGING) return;
        if (logger==null) {  // Logger por defecto local:
            logger = Logger.getLogger("GameScreen");  // Nombre del logger - el de la clase
            logger.setLevel( Level.ALL );  // Loguea todos los niveles
        }
        if (excepcion==null)
            logger.log( level, msg );
        else
            logger.log( level, msg, excepcion );
    }

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
        weapons = new Inventory();
        defensive = new Inventory();
        tramps = new Inventory();

        crearObjetos();

        drawing= inventory;
        ronda2();

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
        //Cambios de inventario

        if (1576<Gdx.input.getX() && 1606>Gdx.input.getX() && 45<Gdx.input.getY() && 75>Gdx.input.getY() && Gdx.input.isTouched()){
            drawing=weapons;
        } else if (1668<Gdx.input.getX() && 1698>Gdx.input.getX() && 45<Gdx.input.getY() && 75>Gdx.input.getY() && Gdx.input.isTouched()){
            drawing=defensive;
        } else if (1751<Gdx.input.getX() && 1781>Gdx.input.getX() && 45<Gdx.input.getY() && 75>Gdx.input.getY() && Gdx.input.isTouched()){
            drawing=tramps;
        } else if (1840<Gdx.input.getX() && 1870>Gdx.input.getX() && 45<Gdx.input.getY() && 75>Gdx.input.getY() && Gdx.input.isTouched()){
            drawing=inventory;
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
            log( Level.INFO, "Error en la conexion a la base de datos", null );
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
                    larry.animations(3,1,2,2,2,2);
                    enemies.add(larry);
                }
                sc.close();


            } catch (FileNotFoundException e) {
                log( Level.INFO, "No se ha podido abrir el fichero", null );
            }


        } catch (DBException e) {
            log( Level.INFO, "No se ha podido obtener el enemigo", null );
        }
    }

    public void ronda2(){
        try {
            DBManager.dbManager.connect("Databases/base.db");
        } catch (DBException e) {
            log( Level.INFO, "Error en la conexion a la base de datos", null );
        }
        Enemy larry= new Enemy();
        try {
            try {
                Scanner sc= new Scanner(new FileInputStream("core/assets/ronda1.csv"));
                while (sc.hasNext()) {
                    String line= sc.next();
                    String[] campos = line.split(";");
                    if (enemies.size()<6){
                        larry = DBManager.dbManager.getEnemy(0);
                        larry.animations(3,1,2,2,2,2);
                    }else{
                        larry = DBManager.dbManager.getEnemy(1);
                        larry.setDeathpath("e1-death.png");
                        larry.animations(2,2,5,1,2,2);
                    }

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
                log( Level.INFO, "No se ha podido abrir el fichero", null );
            }


        } catch (DBException e) {
            log( Level.INFO, "No se ha podido obtener el enemigo", null );
        }
    }

    public void crearObjetos(){
        try {
            DBManager.dbManager.connect("Databases/base.db");
        } catch (DBException e) {
            log( Level.INFO, "Error en la conexion a la base de datos", null );
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
                    defensive.insert(def);

                }

            } catch (DBException e) {
                log( Level.INFO, "No se ha podido obtener el defender", null );
            }
        }

        for (int i = 0; i < 5; i++){
            try {
                Trap trap=DBManager.dbManager.getTrap(i);
                trap.hpBar.setMaxHP(trap.getHp());
                trap.textures();
                objects.add(trap);
                inventory.insert(trap);
                tramps.insert(trap);
            } catch (DBException e) {
                log( Level.INFO, "No se ha podido obtener la trampa", null );
            }
        }

        for (int i = 0; i < 1; i++){
            try {
                Attacker att=DBManager.dbManager.getAttacker(i);
                att.hpBar.setMaxHP(att.getHp());
                att.textures();
                objects.add(att);
                inventory.insert(att);
                weapons.insert(att);
            } catch (DBException e) {
                log( Level.INFO, "No se ha podido obtener el objeto de ataque", null );
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
