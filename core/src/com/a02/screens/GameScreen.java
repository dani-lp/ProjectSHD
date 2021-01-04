package com.a02.screens;

import com.a02.entity.Shoot;
import com.a02.dbmanager.DBException;
import com.a02.dbmanager.DBManager;
import com.a02.entity.*;
import com.a02.component.Inventory;
import com.a02.game.MainGame;
import com.a02.component.Map;
import com.a02.game.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.a02.game.Utils.*;

public class GameScreen implements Screen {
    final MainGame game; //Clase MainGame base (utilizada para cambiar de Screens de forma global)

    private static Logger logger = Logger.getLogger(GameScreen.class.getName());

    private static boolean pauseFlag; //Flags de compra y pausa

    /**
     * Estado del juego.
     *  -BUYING: arrastrando un objeto.
     *  -DELETING: en modo de borrado de objetos.
     *  -SELECTING: controlando manualmente un objeto.
     *  -PLAYING: estado "normal" de juego.
     */
    public enum State {
        BUYING, DELETING, SELECTING, PLAYING
    }

    public State state;

    private static int gold; //Oro para comprar objetos
    private static int currentRound; //Ronda de juego actual
    private static int points; //Puntos que consigue el usuario

    public List<GameObject> objects = new ArrayList<>(); //Objetos en el juego
    public List<Enemy> enemies = new ArrayList<>(); // Enemigos del juego
    public List<Shoot> shots = new ArrayList<>(); //Disparos de juego
    public List<Obstacle> obstacles = new ArrayList<>(); //Obstaculos en los mapas

    public Defender beacon; //Punto central que deben destruir los enemigos

    public Inventory drawingInv; //Inventario actual
    public Inventory fullInv; //Inventario con todos los objetos
    public Inventory attackInv; //Objetos de ataque
    public Inventory defInv; //Objetos de defensa
    public Inventory trapInv; //Objetos trampa

    public Map map; //Mapa donde se colocan los objetos
    OrthographicCamera camera; //Cámara reescalada

    BitmapFont font; //Fuente para oro/tutorial/etc

    private final UIButton deleteButton; //Utilizado para quitar objetos ya colocados y recuperar parte de su coste

    //Botones del menú
    private final UIButton pauseButton;
    private final UIButton resumeButton;
    private final UIButton menuButton;
    private final UIButton quitButton;

    //Botones del inventario (para cambiar entre selecciones de objetos)
    private final UIButton allObjectsButton;
    private final UIButton attackerButton;
    private final UIButton defenderButton;
    private final UIButton trapButton;

    public int secTimer;   //Contador de segundos. Suma 1 cada fotograma.
    float animationTimer;   //Contador para animaciones. Suma el tiempo transcurrido entre fotogramas.
    private final static boolean LOGGING = false; //Cambiar para activar/desactivar el logging

    // Para eventos del tutorial
    public String msg1;
    public String msg2;
    private UIButton tutoBut = null;
    private int contEnt = 0; //Contador de mensajes de tutorial
    private boolean messagesEnded = false;

    public GameScreen(MainGame game, int round) {
        log(Level.INFO, "Inicio del GameScreen", null);

        this.game = game;
        this.state = State.PLAYING;
        pauseFlag = false;

        font = new BitmapFont(Gdx.files.internal("Fonts/test.fnt"));

        secTimer = 0;
        animationTimer = 0;

        fullInv = new Inventory();
        attackInv = new Inventory();
        defInv = new Inventory();
        trapInv = new Inventory();

        createObjects();

        drawingInv = fullInv.sortInventory();
        currentRound = round;
        msg1 = "en este tutorial veras como jugar," ;
        msg2 = "clicka el texto para seguir";

        //Setup por rondas
        switch (round) { //TODO: posiciones de beacon
            case 1:
                loadRound1();
                map = new Map("riverMap.png"); //Mapa de río
                gold = 60000; //TODO Oro por defecto
                break;
            case 2:
                loadRound2();
                map = new Map("emptyMap.png"); //Mapa de bosque
                gold = 60000;
                break;
            case 3:
                loadRound3();
                map = new Map("emptyMap.png"); //?
                gold = 60000;
                break;
            case 4:
                loadRound4();
                map = new Map("emptyMap.png"); //Mapa abierto
                gold = 60000;
                break;
            case 5:
                loadRound5();
                map = new Map("emptyMap.png"); //3 lados bloqueados, boss final
                gold = 60000;
                break;
            case -2:
                if (!enemies.isEmpty()) enemies.clear();
                map = new Map("emptyMap.png");
                gold = 0;
                for (GameObject obj:objects) {
                    obj.setPrice(0);
                }
                break;
            default:
                map = new Map("emptyMap.png");
                gold = 60000;
                break;
        }

        map.getOccGrid()[(int) beacon.getX() / 16][(int) beacon.getY() / 18] = true; //Casilla del beacon

        //Botones de pausa y inventario
        deleteButton = new UIButton(280, 3, 10, 20, "pala.png", "pala.png");
        pauseButton = new UIButton(301, 3, 16, 16, "pause.png", "pause.png");
        resumeButton = new UIButton(123, 113, 74, 36,
                "Buttons/resumeButtonIdle.png", "Buttons/resumeButtonPressed.png");
        menuButton = new UIButton(123, 73, 74, 36,
                "Buttons/menuButtonIdle.png","Buttons/menuButtonPressed.png");
        quitButton = new UIButton(123, 33, 74, 36,
                "Buttons/quitButtonIdle.png","Buttons/quitButtonPressed.png");

        allObjectsButton = new UIButton(259,162,15,15,
                "Buttons/Inventory/allButtonIdle.png","Buttons/Inventory/allButtonPressed.png");
        attackerButton = new UIButton(273,162,15,15,
                "Buttons/Inventory/attackerButtonIdle.png","Buttons/Inventory/attackerButtonPressed.png");
        defenderButton = new UIButton(288,162,15,15,
                "Buttons/Inventory/defenderButtonIdle.png","Buttons/Inventory/defenderButtonPressed.png");
        trapButton = new UIButton(302,162,15,15,
                "Buttons/Inventory/trapButtonIdle.png","Buttons/Inventory/trapButtonPressed.png");

        if (Settings.s.isTutorialCheck()) tutoBut = new UIButton(2,40,220,35,
                "textfield.png", "textfield.png");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 320, 180); //Ajusta la proporción de la cámara
    }

    /**
     * Renderiza los objetos de juego y actualiza su lógica cada 1/60 segundos.
     * @param delta Tiempo entre cada frame renderizado
     */
    @Override
    public void render(float delta) {
        //Reset de OpenGL
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Actualiza cámara
        camera.update();
        game.entityBatch.setProjectionMatrix(camera.combined);

        //Se actualiza el estado del tutorial
        if (Settings.s.isTutorialCheck()) updateTutorial();

        //Actualiza lógica de juego sólo si el juego no está en pausa, pero sí realiza el dibujado.
        if (pauseFlag) {
            updateMenuLogic();
        }
        else {
            //Actualiza lógica de juego
            updateGameLogic();

            //Cambios de inventario
            inventorySwap();
        }

        //Dibujado
        draw();

        //Salida de la GameScreen
        if (beacon.getHp() <= 0) { //Jugador pierde (beacon destruído)
            this.state = State.PLAYING;
            game.setScreen(new EndScreen(Settings.s.getUsername(), points, game));
        }
        else if (enemies.isEmpty() && currentRound > 0) { //Jugador gana ronda (todos los enemigos eliminados)
            game.setScreen(new GameScreen(game, ++currentRound));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || pauseButton.isJustClicked()) pauseFlag = !pauseFlag;

        updateCursor();
    }

    /**
     * Actualiza el texto en las variables String del tutorial (msg1 y msg2)
     * @param contEnt Contador de "partes" del tutorial
     */
    private void updateTutorialMessages(int contEnt) {
        switch (contEnt){
            case 1:
                msg1 = "El objeto colocado en el centro es";
                msg2 = "el beacon, si se destruye pierdes.";
                break;
            case 2:
                msg1 = "usa los objetos del inventario para";
                msg2 = "protegerlo, colocarlos costara oro.";
                break;
            case 3:
                msg1 = "conseguiras oro periodicamente o";
                msg2 = "acabando con enemigos.";
                break;
            case 4:
                msg1 = "puedes usar los botones en lo alto";
                msg2 = "del inventario para navegar ventanas.";
                break;
            case 5:
                msg1 = "tienes 3 tipos de objetos trampas,";
                msg2 = "defensivos y ofensivos.";
                break;
            case 6:
                msg1 = "los defensivos curan objetos o";
                msg2 = "ralentizan a los enemigos.";
                break;
            case 7:
                msg1 = "cada trampa tiene un efecto unico,";
                msg2 = "pero ten cuidado son de un solo uso.";
                break;
            case 8:
                msg1 = "los objetos de ataque atacan a";
                msg2 = "los enemigos a mele o a distancia.";
                break;
            case 9:
                msg1 = "Por ultimo, puedes controlar ciertos";
                msg2 = "objetos y usarlos si les haces click.";
                break;
            case 10:
                msg1 = "pulsando ESPACIO podras disparar con el ";
                msg2 = "leon o curar aliados con el martillo.";
                break;
            case 11:
                msg1 = "mientras controles un objeto no podras";
                msg2 = "colocar otros, pulsa E para dejarlo.";
                break;
            case 12:
                msg1 = "De la misma forma puedes utilizar";
                msg2 = "la pala para vender objetos y ganar oro.";
                break;
            case 13:
                msg1 = "ATENCION!!! se acerca un enemigo,";
                msg2 = "veamos como te las arreglas...";
                break;
        }
    }

    private void updateTutorial() {
        if (tutoBut.isJustClicked()){ //Pasar al siguiente texto
            contEnt++;
            if (messagesEnded && enemies.isEmpty()) {
                Settings.s.setTutorialCheck(false);
                game.setScreen(new MenuScreen(game));
            }
        }

        updateTutorialMessages(contEnt); //Actualizar texto

        if (contEnt == 13 && !messagesEnded){
            Enemy larry = new Enemy(-15,90,16,16,1,500,300,15,
                    this.secTimer + 30,200);
            larry.setFocus(beacon.getX(), beacon.getY());
            enemies.add(larry);
            messagesEnded = true;
        }

        if (messagesEnded && enemies.isEmpty()){ //Después de eliminar al enemigo
            msg1 = "Ahora estas listo para el desafio!";
            msg2 = "Clicka una ultima vez para ir al menu";
        }
    }

    private enum CurrentCursor { //Para optimización de cambio de cursores
        DEFAULT, SELECT, DELETE
    }
    private CurrentCursor currentCursor = CurrentCursor.DEFAULT;

    private void updateCursor() {
        switch (this.state) {
            case DELETING:
                if (this.currentCursor != CurrentCursor.DELETE) {
                    Pixmap pm1 = new Pixmap(Gdx.files.internal("deletingCursor.png"));
                    Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm1, 0, 0));
                    pm1.dispose();
                    this.currentCursor = CurrentCursor.DELETE;
                }
                break;
            case SELECTING:
                if (this.currentCursor != CurrentCursor.SELECT) {
                    Pixmap pm2 = new Pixmap(Gdx.files.internal("selectingCursor.png"));
                    Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm2, 0, 0));
                    pm2.dispose();
                    this.currentCursor = CurrentCursor.SELECT;
                }
                break;
            default:
                if (this.currentCursor != CurrentCursor.DEFAULT) {
                    Pixmap pm3 = new Pixmap(Gdx.files.internal("defaultCursor.png"));
                    Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm3, 0, 0));
                    pm3.dispose();
                    this.currentCursor = CurrentCursor.DEFAULT;
                }
                break;
        }
    }

    /**
     * Actualiza la lógica de las entidades.
     */
    private void updateGameLogic() {
        //Actualiza valores estáticos
        secTimer += 1;
        animationTimer += Gdx.graphics.getDeltaTime();

        if (secTimer % 20 == 0 && currentRound != -2) gold++;
        points += 1;

        //Actualiza estado de objetos del inventario
        for (GameObject object : drawingInv.getObjects()) {
            object.grabObject(this);
        }

        //Botón de eliminar objetos
        if (deleteButton.isJustClicked()){
            if (this.state != State.DELETING) {
                this.state = State.DELETING;
            }
            else {
                this.state = State.PLAYING;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.E)){
            this.state = State.PLAYING;
            for (GameObject obj:objects) {
                obj.setSelected(false);
                if (obj instanceof Defender){
                    ((Defender) obj).states(0);
                }
            }
        }

        if (this.state == State.DELETING && mouseJustClicked()) {
            Vector3 mousePos = getRelativeMousePos();
            for (GameObject obj:objects) {
                if (obj.overlapsPoint(mousePos.x, mousePos.y) && !obj.isInInventory(this) && obj.getId() != -1){
                    gold += obj.getPrice() * 0.8;
                    obj.setHp(0);
                    break;
                }
            }
        }

        //Actualiza "presencia" y estado de enemigos y objetos
        ListIterator<GameObject> objectIterator = objects.listIterator();
        while(objectIterator.hasNext()){
            GameObject tempObj = objectIterator.next();
            tempObj.update(this);
            if(tempObj.getHp() <= 0) {
                if ((tempObj instanceof Attacker && tempObj.getId() == 2) ||
                        (tempObj instanceof Defender && tempObj.getId() == 3) && tempObj.isSelected()) this.state = State.PLAYING;
                try {
                    map.getOccGrid()[(int) tempObj.getX() / 16][(int) tempObj.getY() / 18] = false;
                } catch (IndexOutOfBoundsException ignored) {

                }
                objectIterator.remove();
            }
        }

        ListIterator<Enemy> enemyIterator = enemies.listIterator();
        while(enemyIterator.hasNext()){
            Enemy tempEnemy = enemyIterator.next();
            tempEnemy.update(this);
            //Es necesario borrar desde aquí a los enemigos, para evitar un ConcurrentModificationException.
            if(tempEnemy.getDeathTimer() < secTimer && tempEnemy.getDeathTimer() != 0) {
                enemyIterator.remove();
            }
        }

        ListIterator<Shoot> shootIterator = shots.listIterator();
        while(shootIterator.hasNext()){
            Shoot tempSh = shootIterator.next();
            tempSh.update(this);
            if(tempSh.getHp() <= 0) {
                shootIterator.remove();
            }
        }
    }

    /**
     * Atualiza la lógica de los botones del menú de pausa.
     */
    private void updateMenuLogic() {
        //Botones tocados
        resumeButton.isTouched();
        menuButton.isTouched();
        quitButton.isTouched();

        //Acciones
        if (resumeButton.isJustClicked()){
            pauseFlag = !pauseFlag;
        }
        else if (menuButton.isJustClicked()) {
            game.setScreen(new MenuScreen(game));
        }
        else if (quitButton.isJustClicked()) {
            Gdx.app.exit();
            System.exit(0);
        }
    }

    /**
     * Dibuja las entidades.
     */
    private void draw() {
        game.entityBatch.begin();
        game.entityBatch.draw(map.getTexture(), 0, 0);

        //Objetos y enemigos
        for (GameObject object:objects) {
            if (object.getAnimation() != null) game.entityBatch.draw(object.getCurrentAnimation(animationTimer), object.getX(), object.getY());

            else if (object.getType().equals("Shoot") && !object.isInInventory(this)) {
                game.entityBatch.draw(new TextureRegion(object.getTexture()), object.getX(), object.getY(),
                        8, 9,18, 16, 1, 1, (int)((Attacker) object).getAngle() - 45, true);
            }

            else game.entityBatch.draw(object.getTexture(), object.getX(), object.getY());
        }
        for (Enemy enemy:enemies) {
            game.entityBatch.draw(enemy.getCurrentAnimation(animationTimer), enemy.getX(), enemy.getY());
        }

        //Barras de vida y disparos
        for (GameObject object:objects) {
            if (object.hpBar != null) {
                game.entityBatch.draw(object.hpBar.getBackground(), object.hpBar.getX(), object.hpBar.getY(), 14, 2);
                game.entityBatch.draw(object.hpBar.getForeground(), object.hpBar.getX(), object.hpBar.getY(), object.hpBar.getCurrentWidth(), 2);
            }
        }
        for (Enemy enemy: enemies) {
            if (enemy.hpBar != null) {
                game.entityBatch.draw(enemy.hpBar.getBackground(), enemy.hpBar.getX(), enemy.hpBar.getY(), 14, 2);
                game.entityBatch.draw(enemy.hpBar.getForeground(), enemy.hpBar.getX(), enemy.hpBar.getY(), enemy.hpBar.getCurrentWidth(), 2);
            }
        }
        for(Shoot shoot: shots){
            game.entityBatch.draw(shoot.getTexture(),shoot.getX(),shoot.getY());
        }

        //Inventario
        game.entityBatch.draw(drawingInv.getTexture(), drawingInv.getX(), drawingInv.getY());
        game.entityBatch.draw(allObjectsButton.getCurrentTexture(), allObjectsButton.getX(), allObjectsButton.getY());
        game.entityBatch.draw(attackerButton.getCurrentTexture(), attackerButton.getX(), attackerButton.getY());
        game.entityBatch.draw(defenderButton.getCurrentTexture(), defenderButton.getX(), defenderButton.getY());
        game.entityBatch.draw(trapButton.getCurrentTexture(), trapButton.getX(), trapButton.getY());


        //Objetos del inventario
        for (GameObject object:drawingInv.getObjects()) {
            if (object != null) {
                if (object.getAnimation() == null) game.entityBatch.draw(object.getTexture(), object.getX(), object.getY());
                else game.entityBatch.draw(object.getCurrentAnimation(animationTimer), object.getX(), object.getY());
            }
        }

        //Oro
        if (currentRound != 0){
            font.draw(game.entityBatch, "ORO : " + gold, 5, 175);
        } else {
            gold = gold + 100000;
            if (Settings.s.isTutorialCheck()) game.entityBatch.draw(tutoBut.getCurrentTexture(),tutoBut.getX(),tutoBut.getY());
            font.draw(game.entityBatch, "ORO INFINITY: " , 5, 175);
            font.draw(game.entityBatch, msg1 , 5, 68);
            font.draw(game.entityBatch, msg2 , 5, 55);
        }


        //Botones
        game.entityBatch.draw(pauseButton.getCurrentTexture(), pauseButton.getX(), pauseButton.getY());
        game.entityBatch.draw(deleteButton.getCurrentTexture(), deleteButton.getX(), deleteButton.getY());

        //Menu de pausa
        if (pauseFlag) {
            game.entityBatch.draw(new Texture("pauseMenu.png"), 50, 26);
            game.entityBatch.draw(resumeButton.getCurrentTexture(), resumeButton.getX(), resumeButton.getY());
            game.entityBatch.draw(menuButton.getCurrentTexture(), menuButton.getX(), menuButton.getY());
            game.entityBatch.draw(quitButton.getCurrentTexture(), quitButton.getX(), quitButton.getY());
        }

        game.entityBatch.end();
    }

    /**
     * Cambia el inventario visible al que está en uso.
     */
    private void inventorySwap() {
        allObjectsButton.isTouched();
        attackerButton.isTouched();
        defenderButton.isTouched();
        trapButton.isTouched();

        if (allObjectsButton.isJustClicked()) drawingInv = fullInv.sortInventory();
        if (attackerButton.isJustClicked()) drawingInv = attackInv.sortInventory();
        if (defenderButton.isJustClicked()) drawingInv = defInv.sortInventory();
        if (trapButton.isJustClicked()) drawingInv = trapInv.sortInventory();
    }

    /**
     * Asigna a un objeto Enemy de tipo 1 los valores comunes y posicionales, y lo carga al ArrayList de juego.
     * @param larry Enemy a cargar
     * @param fields Array de parámetros posicionales
     */
    private void loadEnemy(Enemy larry, String[] fields) {
        larry.setX(Integer.parseInt(fields[0]));
        larry.setY(Integer.parseInt(fields[1]));
        larry.setStartTime(Integer.parseInt(fields[2]));
        larry.hpBar.setMaxHP(larry.getHp());
        larry.setFocus(beacon.getX(), beacon.getY());
        larry.loadAnimations();
        enemies.add(larry);
    }

    private void loadRound1(){
        try {
            DBManager.dbManager.connect("Databases/base.db");
        } catch (DBException e) {
            log( Level.INFO, "Error en la conexion a la base de datos", null );
        }
        Enemy larry;
        try {
            try {
                Scanner sc = new Scanner(new FileInputStream("core/assets/ronda1.csv"));
                while (sc.hasNext()) {
                    String line = sc.next();
                    String[] fields = line.split(";");

                    larry = DBManager.dbManager.getEnemy(0);
                    loadEnemy(larry, fields);
                }
                sc.close();


            } catch (FileNotFoundException e) {
                log( Level.INFO, "No se ha podido abrir el fichero", null );
            }


        } catch (DBException e) {
            log( Level.INFO, "No se ha podido obtener el enemigo", null );
        }
        try {
            DBManager.dbManager.disconnect();
        } catch (DBException ignored) {

        }
        obstacles.add(new Obstacle(63,0,32,18));
        obstacles.add(new Obstacle(63,55,32,18));
        obstacles.add(new Obstacle(63,108,32,18));
        obstacles.add(new Obstacle(63,163,32,18));

    }

    private void loadRound2(){
        try {
            DBManager.dbManager.connect("Databases/base.db");
        } catch (DBException e) {
            log( Level.INFO, "Error en la conexion a la base de datos", null );
        }
        Enemy larry;
        try {
            try {
                Scanner sc= new Scanner(new FileInputStream("core/assets/ronda1.csv"));
                while (sc.hasNext()) {
                    String line = sc.next();
                    String[] fields = line.split(";");
                    if (enemies.size()<6){
                        larry = DBManager.dbManager.getEnemy(0);
                    } else {
                        larry = DBManager.dbManager.getEnemy(1);
                    }
                    larry.loadAnimations();

                    loadEnemy(larry, fields);
                }
                sc.close();


            } catch (FileNotFoundException e) {
                log( Level.INFO, "No se ha podido abrir el fichero", null );
            }
        } catch (DBException e) {
            log( Level.INFO, "No se ha podido obtener el enemigo", null );
        }
        try {
            DBManager.dbManager.disconnect();
        } catch (DBException ignored) {

        }
    }

    private void loadRound3() {

    }
    private void loadRound4() {

    }
    private void loadRound5() {

    }

    /**
     * Extrae los objetos de la Base de Datos y los introduce en los inventarios.
     */
    public void createObjects(){
        try {
            DBManager.dbManager.connect("Databases/base.db");
        } catch (DBException e) {
            log( Level.INFO, "Error en la conexion a la base de datos", null );
        }

        for (int i = 0; i < 4; i++){
            try {
                Defender def = DBManager.dbManager.getDefender(i);
                def.hpBar.setMaxHP(def.getHp());
                def.loadTextures();
                objects.add(def);
                fullInv.insert(def);
                defInv.insert(def);
            } catch (DBException e) {
                log( Level.INFO, "No se ha podido obtener el defender", null );
            }
        }

        for (int i = 0; i < 5; i++){
            try {
                Trap trap = DBManager.dbManager.getTrap(i);
                trap.hpBar.setMaxHP(trap.getHp());
                trap.loadTextures();
                objects.add(trap);
                fullInv.insert(trap);
                trapInv.insert(trap);
            } catch (DBException e) {
                log( Level.INFO, "No se ha podido obtener la trampa", null );
            }
        }

        for (int i = 0; i < 4; i++){
            try {
                Attacker att = DBManager.dbManager.getAttacker(i);
                att.hpBar.setMaxHP(att.getHp());
                att.loadTextures();
                objects.add(att);
                fullInv.insert(att);
                attackInv.insert(att);
            } catch (DBException e) {
                log( Level.INFO, "No se ha podido obtener el objeto de ataque", null );
            }
        }

        try{
            DBManager.dbManager.disconnect();
        } catch (DBException ignored) {

        }

        beacon = new Defender(144, 90, 16, 18, -1, "Beacon", -1, false, 9000);
        beacon.hpBar.setMaxHP(beacon.getHp());
        beacon.loadTextures();
        objects.add(beacon);
    }

    @Override
    public void dispose() {
        game.entityBatch.dispose();

        drawingInv.getTexture().dispose();
        fullInv.getTexture().dispose();
        attackInv.getTexture().dispose();
        defInv.getTexture().dispose();
        trapInv.getTexture().dispose();

        map.getTexture().dispose();
        font.dispose();

        deleteButton.disposeButton();
        pauseButton.disposeButton();
        resumeButton.disposeButton();
        menuButton.disposeButton();
        quitButton.disposeButton();

        allObjectsButton.disposeButton();
        attackerButton.disposeButton();
        defenderButton.disposeButton();
        trapButton.disposeButton();

        tutoBut.disposeButton();

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

    private void log(Level level, String msg, Throwable exception) {
        if (!LOGGING) return;
        if (logger == null) {  // Logger por defecto local:
            logger = Logger.getLogger("GameScreen");  // Nombre del logger - el de la clase
            logger.setLevel(Level.ALL);  // Loguea todos los niveles
        }
        if (exception == null)
            logger.log(level, msg);
        else
            logger.log(level, msg, exception);
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
