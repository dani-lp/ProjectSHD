package com.a02.screens;

import com.a02.component.Shoot;
import com.a02.dbmanager.DBException;
import com.a02.dbmanager.DBManager;
import com.a02.entity.*;
import com.a02.component.Inventory;
import com.a02.game.MainGame;
import com.a02.component.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.a02.game.Utils.getRelativeMousePos;

public class GameScreen implements Screen {
    final MainGame game;

    private static Logger logger = Logger.getLogger(GameScreen.class.getName());

    private static boolean buying, pauseFlag;
    private static int gold;

    public List<GameObject> objects = new ArrayList<>(); //Objetos en el juego
    public List<Enemy> enemies = new ArrayList<>(); // Enemigos del juego
    public static List<Shoot> shoots= new ArrayList<>();

    public Inventory drawing;
    public Inventory fullInv; //Inventario full
    public Inventory attackInv; //Objetos de ataque
    public Inventory defInv; //Objetos de defensa
    public Inventory trapInv; //Objetos trampa

    Map map;
    OrthographicCamera camera;

    BitmapFont font;

    private UIButton pauseButton;

    public int secTimer;   //Contador de segundos. Suma 1 cada fotograma.
    float animationTimer;   //Contador para animaciones
    private static boolean LOGGING = true;

    private void log(Level level, String msg, Throwable exception) {
        if (!LOGGING) return;
        if (logger==null) {  // Logger por defecto local:
            logger = Logger.getLogger("GameScreen");  // Nombre del logger - el de la clase
            logger.setLevel( Level.ALL );  // Loguea todos los niveles
        }
        if (exception == null)
            logger.log( level, msg );
        else
            logger.log( level, msg, exception );
    }

    public GameScreen(MainGame game, int round) {
        Logger.getLogger("").setLevel(Level.INFO);
        Logger.getLogger("").getHandlers()[0].setLevel(Level.INFO);
        logger.info("Inicio del GameScreen");

        this.game = game;
        buying = false;
        pauseFlag = false;
        gold = 6000;

        font = new BitmapFont(Gdx.files.internal("Fonts/test.fnt"));

        secTimer = 0;
        animationTimer = 0;

        fullInv = new Inventory();
        attackInv = new Inventory();
        defInv = new Inventory();
        trapInv = new Inventory();

        createObjects();

        drawing = fullInv.sortInventory();

        switch (round) {
            case 1:
                ronda1();
                break;
            case 2:
                ronda2();
                break;
        }

        map = new Map("map1.png");

        pauseButton = new UIButton(299, 6, 16, 16, "pause.png",1);

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

        //Actualiza lógica sólo si el juego no está en pausa, pero sí realiza el dibujado.
        //TODO: menú de pausa
        if (!pauseFlag) {
            //Actualiza lógica
            updateLogic();

            //Cambios de inventario
            inventorySwap();
        }

        //Dibujado
        draw();

        if (objects.get(0).getHp()<=0) {
            game.setScreen(new MenuScreen(game));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P) || pauseButton.isJustClicked()) pauseFlag = !pauseFlag;
    }

    /**
     * Actualiza la lógica de las entidades.
     */
    private void updateLogic() {
        //Actualiza valores estáticos
        secTimer += 1;
        animationTimer += Gdx.graphics.getDeltaTime();

        if (secTimer % 20 == 0) gold++;

        //Actualiza estado de objetos del inventario
        for (GameObject object : fullInv.getObjects()) {
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

        ListIterator<Shoot> shootIterator = shoots.listIterator();
        while(shootIterator.hasNext()){
            Shoot tempSh = shootIterator.next();
            tempSh.update(this);
            if(tempSh.getHp() <= 0) {
                shootIterator.remove();
            }
        }
    }

    /**
     * Dibuja las entidades
     */
    private void draw() {
        game.entityBatch.begin();
        game.entityBatch.draw(map.getTexture(), 0, 0);

        //Objetos y enemigos
        for (GameObject object:objects) {
            game.entityBatch.draw(object.getTexture(), object.getX(), object.getY());
        }
        for (Enemy enemy:enemies) {
            if (enemy.state != Enemy.State.DEAD) {
                game.entityBatch.draw(enemy.getCurrentAnimation(animationTimer), enemy.getX(), enemy.getY());
            }
        }

        //Barras de vida y disparos
        for (GameObject object:objects) {
            game.entityBatch.draw(object.hpBar.getBackground(), object.hpBar.getX(), object.hpBar.getY(), 14,2);
            game.entityBatch.draw(object.hpBar.getForeground(), object.hpBar.getX(), object.hpBar.getY(), object.hpBar.getCurrentWidth(),2);
        }
        for (Enemy enemy: enemies) {
            game.entityBatch.draw(enemy.hpBar.getBackground(), enemy.hpBar.getX(), enemy.hpBar.getY(), 14,2);
            game.entityBatch.draw(enemy.hpBar.getForeground(), enemy.hpBar.getX(), enemy.hpBar.getY(), enemy.hpBar.getCurrentWidth(),2);
        }
        for(Shoot shoot: shoots){
            game.entityBatch.draw(new Texture(shoot.getSprite()),shoot.getX(),shoot.getY());
        }

        //Inventario
        game.entityBatch.draw(drawing.getTexture(), drawing.getX(), drawing.getY());

        //Objetos del inventario
        for (GameObject object:drawing.getObjects()) {
            if (object != null) game.entityBatch.draw(object.getTexture(), object.getX(), object.getY());
        }

        //Oro
        font.draw(game.entityBatch, "ORO  " + Integer.toString(gold), 5, 175);

        //Botones
        game.entityBatch.draw(pauseButton.getTexture(), pauseButton.getX(), pauseButton.getY());

        game.entityBatch.end();
    }

    /**
     * Cambia el inventario visible al que está en uso
     */
    private void inventorySwap() {
        Vector3 mousePos = getRelativeMousePos();
        if (mousePos.y <= 176 && mousePos.y >= 164 ) {
            if (260 <= mousePos.x && 272 >= mousePos.x && Gdx.input.isTouched()){
                drawing = attackInv.sortInventory();
            } else if (274 <= mousePos.x && 286 >= mousePos.x && Gdx.input.isTouched()){
                drawing = defInv.sortInventory();
            } else if (289 <= mousePos.x && 301 >= mousePos.x && Gdx.input.isTouched()){
                drawing = trapInv.sortInventory();
            } else if (303 <= mousePos.x && 315 >= mousePos.x && Gdx.input.isTouched()){
                drawing = fullInv.sortInventory();
            }
        }
    }

    public void ronda1(){
        try {
            DBManager.dbManager.connect("Databases/base.db");
        } catch (DBException e) {
            log( Level.INFO, "Error en la conexion a la base de datos", null );
        }
        Enemy larry = new Enemy();
        try {
            try {
                Scanner sc = new Scanner(new FileInputStream("core/assets/ronda1.csv"));
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
                    larry.setFocus(objects.get(0).getX(), objects.get(0).getY());
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
        try{
            DBManager.dbManager.disconnect();
        } catch (DBException ignored) {

        }
    }

    public void ronda2(){
        try {
            DBManager.dbManager.connect("Databases/base.db");
        } catch (DBException e) {
            log( Level.INFO, "Error en la conexion a la base de datos", null );
        }
        Enemy larry = new Enemy();
        try {
            try {
                Scanner sc= new Scanner(new FileInputStream("core/assets/ronda1.csv"));
                while (sc.hasNext()) {
                    String line = sc.next();
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
                    larry.setFocus(objects.get(0).getX(), objects.get(0).getY());
                    enemies.add(larry);
                }
                sc.close();


            } catch (FileNotFoundException e) {
                log( Level.INFO, "No se ha podido abrir el fichero", null );
            }
        } catch (DBException e) {
            log( Level.INFO, "No se ha podido obtener el enemigo", null );
        }
        try{
            DBManager.dbManager.disconnect();
        } catch (DBException ignored) {

        }
    }

    public void createObjects(){
        try {
            DBManager.dbManager.connect("Databases/base.db");
        } catch (DBException e) {
            log( Level.INFO, "Error en la conexion a la base de datos", null );
        }

            // A partir de una conexiÃ³n activa obtenemos el objeto para ejecutar
            // sentencias SQL en la base de datos.
        for (int i = 0; i < 2; i++){
            try {
                Defender def = DBManager.dbManager.getDefender(i);
                if (def.getId() == 0) {
                    def.setX(144);
                    def.setY(90);
                    def.setHp(90000);
                }
                def.hpBar.setMaxHP(def.getHp());
                def.textures();
                objects.add(def);
                if (def.getId()!=0) {
                    fullInv.insert(def);
                    defInv.insert(def);

                }

            } catch (DBException e) {
                log( Level.INFO, "No se ha podido obtener el defender", null );
            }
        }

        for (int i = 0; i < 5; i++){
            try {
                Trap trap = DBManager.dbManager.getTrap(i);
                trap.hpBar.setMaxHP(trap.getHp());
                trap.textures();
                objects.add(trap);
                fullInv.insert(trap);
                trapInv.insert(trap);
            } catch (DBException e) {
                log( Level.INFO, "No se ha podido obtener la trampa", null );
            }
        }

        for (int i = 0; i < 1; i++){
            try {
                Attacker att = DBManager.dbManager.getAttacker(i);
                att.hpBar.setMaxHP(att.getHp());
                att.textures();
                objects.add(att);
                fullInv.insert(att);
                attackInv.insert(att);
            } catch (DBException e) {
                log( Level.INFO, "No se ha podido obtener el objeto de ataque", null );
            }
        }
        Attacker disp= new Attacker(0,0,16,18,2,"Shoot",20,true,100,"Shoot",75);
        disp.hpBar.setMaxHP(disp.getHp());
        disp.textures();
        objects.add(disp);
        fullInv.insert(disp);
        attackInv.insert(disp);
        try{
            DBManager.dbManager.disconnect();
        } catch (DBException ignored) {

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
