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
import java.sql.*;
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
    Trap tConfuse;
    Trap tDamage;
    Trap tFire;
    Trap tFreeze;
    Trap tTeleport;

    Inventory inventory;

    Map map;
    OrthographicCamera camera;

    BitmapFont font;

    int secTimer;   //Contador de segundos. Suma 1 cada fotograma.
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

        crearObjetos();

        inventory = new Inventory();

        inventory.insert(elec);
        inventory.insert(wall);

        inventory.insert(tConfuse);
        inventory.insert(tDamage);
        inventory.insert(tFire);
        inventory.insert(tFreeze);
        inventory.insert(tTeleport);

        objects.add(wall);
        objects.add(elec);
        objects.add(beacon);

        objects.add(tConfuse);
        objects.add(tDamage);
        objects.add(tFire);
        objects.add(tFreeze);
        objects.add(tTeleport);

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
        tConfuse.grabObject(map,objects);
        tDamage.grabObject(map,objects);
        tFire.grabObject(map,objects);
        tFreeze.grabObject(map,objects);
        tTeleport.grabObject(map,objects);

        elec.update(objects, enemies, secTimer);

        //Actualiza "presencia" de enemigos y objetos
        ListIterator<GameObject> objectIterator = objects.listIterator();
        while(objectIterator.hasNext()){
            GameObject tempObj = objectIterator.next();
            tempObj.update(objects, enemies, secTimer);
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
           if (object != null) game.entityBatch.draw(object.getTexture(), object.getX(), object.getY());
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
    public void ronda1(){
        int idE=0;
        int hpE=0;
        int attackE=0;
        float speed=0;
        int goldValue=0;
        String walkpath="";
        String attackpath="";
        String deathpath="";

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("No se ha podido cargar el driver de la base de datos");
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:Databases/Database.db")) {

            // A partir de una conexiÃ³n activa obtenemos el objeto para ejecutar
            // sentencias SQL en la base de datos.
            try (Statement stmt = conn.createStatement()) {

                try (ResultSet rs = stmt.executeQuery("SELECT * FROM enemy")) {

                    while (rs.next()) {
                        // Mientras tenga filas
                        // Cogemos cada columna contenida en la fila
                        idE = rs.getInt("ID_E");
                        hpE = rs.getInt("HP_E");
                        attackE = rs.getInt("ATTACKDAMAGE_E");
                        speed = rs.getFloat("SPEED_E");
                        goldValue = rs.getInt("GOLD_VALUE_E");
                        walkpath = rs.getString("WALKPATH");
                        attackpath = rs.getString("ATTACKPATH");
                        deathpath = rs.getString("DEATHPATH");

                        // y hacemos algo con esos datos: imprimir, crear un objeto, etc
                    }
                } catch (SQLException e) {
                    System.out.println("No se ha podido ejecutar la sentencia SQL." + e.getMessage());
                }

            } catch (SQLException e) {
                // No se ha podido obtener la conexiÃ³n a la base de datos
                System.out.println("Error. No se ha podido crear el statement " + e.getMessage());
            }

        } catch (SQLException e) {
            // No se ha podido obtener la conexiÃ³n a la base de datos
            System.out.println("Error. No se ha podido conectar a la base de datos. " + e.getMessage());
        }
        larry = new Enemy(12, 63, 16, 16, idE,hpE, attackE, speed,0,goldValue,walkpath,attackpath,deathpath);
        larry2 = new Enemy(275, 25, 16, 16,idE, hpE, attackE, speed,300,goldValue,walkpath,attackpath,deathpath);
        larry3 = new Enemy(100, 359, 16, 16,idE,hpE, attackE, speed,470,goldValue,walkpath,attackpath,deathpath);
        larry4 = new Enemy(350, 220, 16, 16,idE,hpE, attackE, speed,500,goldValue,walkpath,attackpath,deathpath);
        larry5 = new Enemy(-12, 363, 16, 16,idE,hpE, attackE, speed,680,goldValue,walkpath,attackpath,deathpath);
        larry6 = new Enemy(445, 25, 16, 16,idE,hpE, attackE, speed,742,goldValue,walkpath,attackpath,deathpath);
        larry7 = new Enemy(100, 459, 16, 16,idE,hpE, attackE, speed,790,goldValue,walkpath,attackpath,deathpath);
        larry8 = new Enemy(350, 320, 16, 16,idE,hpE, attackE, speed,825,goldValue,walkpath,attackpath,deathpath);
        larry9 = new Enemy(512, 63, 16, 16,idE,hpE, attackE, speed,875,goldValue,walkpath,attackpath,deathpath);
        larry10 = new Enemy(-45, 25, 16, 16,idE,hpE, attackE, speed,905,goldValue,walkpath,attackpath,deathpath);
        larry11 = new Enemy(100, -59, 16, 16,idE,hpE, attackE, speed,925,goldValue,walkpath,attackpath,deathpath);

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

    public void crearObjetos(){
        int idD=0;
        int priceD=0;
        boolean unlockedD=false;
        String typeD="";
        int hpD=0;

        int idT=0;
        int priceT=0;
        boolean unlockedT=false;
        String typeT="";
        int hpT=0;
        String effectT="";
        int attackdamageT=0;

        int idA=0;
        int priceA=0;
        boolean unlockedA=false;
        String typeA="";
        int hpA=0;
        String attackTypeA="";
        int attackdamageA=0;

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("No se ha podido cargar el driver de la base de datos");
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:Databases/base.db")) {

            // A partir de una conexiÃ³n activa obtenemos el objeto para ejecutar
            // sentencias SQL en la base de datos.
            try (Statement stmt = conn.createStatement()) {

                try (ResultSet rs = stmt.executeQuery("SELECT * FROM defender")) {
                    int runs=0;
                    while (rs.next()) {
                        // Mientras tenga filas
                        // Cogemos cada columna contenida en la fila
                        idD=rs.getInt("ID_D");
                        priceD=rs.getInt("PRICE_D");
                        int num=rs.getInt("UNLOCKED_D");
                        if (num==1){
                            unlockedD=true;
                        }
                        typeD=rs.getString("TYPE_D");
                        hpD=rs.getInt("HP_A");
                        switch (runs){
                            case 0:
                                beacon = new Defender(145,90,16,16,idD,typeD, priceD, unlockedD,50000); //TODO: volver a poner con BD
                                break;
                            case 1:
                                wall = new Defender(260,135,16,18,idD,typeD, priceD, unlockedD,hpD);
                                break;
                        }
                        runs++;
                        // y hacemos algo con esos datos: imprimir, crear un objeto, etc
                    }
                } catch (SQLException e) {
                    System.out.println("No se ha podido ejecutar la sentencia SQL." + e.getMessage());
                }

                try (ResultSet rs = stmt.executeQuery("SELECT * FROM trap")){
                    while (rs.next()) {
                        // Mientras tenga filas
                        // Cogemos cada columna contenida en la fila
                        idT=rs.getInt("ID_T");

                        priceT=rs.getInt("PRICE_T");
                        int num=rs.getInt("UNLOCKED_T");
                        if (num==1){
                            unlockedT=true;
                        }
                        typeT=rs.getString("TYPE");
                        hpT=rs.getInt("HP_T");
                        effectT=rs.getString("EFFECT");
                        attackdamageT=rs.getInt("ATTACKDAMAGE_T");

                        switch (idT){

                            case 0:
                                tFreeze = new Trap(260,115,16,18,idT,typeT,priceT,unlockedT,hpT,effectT,attackdamageT);
                                break;
                            case 1:
                                tFire = new Trap(260,115,16,18,idT,typeT,priceT,unlockedT,hpT,effectT,attackdamageT);
                                break;
                            case 2:
                                tDamage = new Trap(260,115,16,18,idT,typeT,priceT,unlockedT,hpT,effectT,attackdamageT);
                                break;
                            case 3:
                                tConfuse = new Trap(260,115,16,18,idT,typeT,priceT,unlockedT,hpT,effectT,attackdamageT);
                                break;
                            case 4:
                                tTeleport = new Trap(260,115,16,18,idT,typeT,priceT,unlockedT,hpT,effectT,attackdamageT);
                                break;
                        }

                        // y hacemos algo con esos datos: imprimir, crear un objeto, etc
                    }


                } catch (SQLException e) {
                    System.out.println("No se ha podido ejecutar la sentencia SQL." + e.getMessage());
                }

                try (ResultSet rs = stmt.executeQuery("SELECT * FROM attacker")) {
                    int runs=0;
                    while (rs.next()) {
                        // Mientras tenga filas
                        // Cogemos cada columna contenida en la fila
                        idA=rs.getInt("ID_A");
                        priceA=rs.getInt("PRICE_A");
                        int num=rs.getInt("UNLOCKED_A");
                        if (num==1){
                            unlockedA=true;
                        }
                        typeA=rs.getString("TYPE_A");
                        hpA=rs.getInt("HP_A");
                        attackTypeA=rs.getString("ATTACK_TYPE");
                        attackdamageA=rs.getInt("ATTACKDAMAGE_A");
                        switch (runs){
                            case 0:
                                elec = new Attacker(280,135,16,18,idA,typeA,priceA,unlockedA,hpA,attackTypeA,attackdamageA);
                                break;
                            //case 1:
                            //wall = new Defender(260,135,16,18,idD,typeD, prizeD, unlockedD,hpD);
                        }
                        runs++;
                        // y hacemos algo con esos datos: imprimir, crear un objeto, etc
                    }

                } catch (SQLException e) {
                    System.out.println("No se ha podido ejecutar la sentencia SQL." + e.getMessage());
                }

            } catch (SQLException e) {
                // No se ha podido obtener la conexiÃ³n a la base de datos
                System.out.println("Error. No se ha podido crear el statement " + e.getMessage());
            }



        } catch (SQLException e) {
            // No se ha podido obtener la conexiÃ³n a la base de datos
            System.out.println("Error. No se ha podido conectar a la base de datos. " + e.getMessage());
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
