package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    final MainGame game;

    public static boolean buying;

    List<GameObject> objects = new ArrayList<GameObject>(); //Objetos en el juego
    List<Texture> textures = new ArrayList<Texture>();  //Texturas de los objetos del juego
    List<Enemy> enemies = new ArrayList<Enemy>(); // Enemigos del juego
    List<Texture> enemyTextures = new ArrayList<Texture>();  //Texturas de los enemigos del juego

    Texture imgL;
    Texture imgWall;
    Texture imgFire;        //Las texturas
    Texture imgElec;
    Texture imgB;
    Texture inventoryTexture;
    Texture mapTexture;

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

    int secTimer;   //Contador de segundos. Suma 1 cada fotograma.
    float animationTimer;   //Contador para animaciones

    public GameScreen(MainGame game) {
        this.game = game;
        buying = false;

        secTimer = 0;
        animationTimer = 0;

        larry = new Enemy(12, 90, 16, 16, "Test2.png", 1, "Larry", 200, 100, 30);
        larry2 = new Enemy(45, -25, 16, 16, "Test2.png", 2, "Jeremy", 200, 100, 30);
        larry3 = new Enemy(100, 200, 16, 16, "Test2.png", 1, "Larry", 200, 100, 30);
        larry4 = new Enemy(450, 20, 16, 16, "Test2.png", 2, "Jeremy", 200, 100, 30);

        beacon = new Defender(145,90,16,16,"beacon.png",0,"Beacon","Beacon", 1000, true,1000,false,true);
        wall = new Defender(260,135,16,18,"Muro.png",0,"Wall","Defense", 1000, true,1000,true,true);
        elec = new Attacker(280,135,16,18,"Electricidad.png",2,"Electricity","Attack",100,true,1000,true,true,"Spark",1);
        fire = new Trap(260,115,16,18,"Fuego.png",3,"Fire","Trap",1000,true,1000,true,true,"Burn",15);

        enemies.add(larry);
        enemies.add(larry2);
        enemies.add(larry3);
        enemies.add(larry4);

        objects.add(wall);
        objects.add(elec);
        objects.add(fire);
        objects.add(beacon);

        imgL = new Texture(Gdx.files.internal(larry.getSprite()));
        imgWall = new Texture(Gdx.files.internal(wall.getSprite()));
        imgElec = new Texture(Gdx.files.internal(elec.getSprite()));
        imgFire = new Texture(Gdx.files.internal(fire.getSprite()));
        imgB = new Texture(Gdx.files.internal(beacon.getSprite()));

        enemyTextures.add(imgL);

        textures.add(imgWall);
        textures.add(imgElec);
        textures.add(imgFire);
        textures.add(imgB);

        inventory = new Inventory();
        inventoryTexture = new Texture(Gdx.files.internal(inventory.getSprite()));
        inventory.insert(wall);
        inventory.insert(elec);
        inventory.insert(fire);

        map = new Map("map1.png");
        mapTexture = new Texture(Gdx.files.internal(map.getSprite()));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 320, 180);
        game.entityBatch = new SpriteBatch();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //Reset de OpenGL
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        secTimer += 1;
        animationTimer += Gdx.graphics.getDeltaTime();

        //Actualiza cámara
        camera.update();
        game.entityBatch.setProjectionMatrix(camera.combined);

        wall.grabObject(map,objects,textures);
        elec.grabObject(map,objects,textures);
        fire.grabObject(map,objects,textures);

        larry.update(beacon.getX(), beacon.getY(), objects, enemies, secTimer);
        //elec.update(objects, enemies, secTimer);

        if (secTimer > 500){
            larry2.update(beacon.getX(), beacon.getY(), objects, enemies, secTimer);
        }
        if (secTimer > 800){
            larry3.update(beacon.getX(), beacon.getY(), objects, enemies, secTimer);
        }
        if (secTimer > 1400){
            larry4.update(beacon.getX(), beacon.getY(), objects, enemies, secTimer);
        }

        ArrayList<GameObject> copy = new ArrayList<GameObject>();
        ArrayList<Texture> copyt = new ArrayList<Texture>();

        for (GameObject object:objects) {
            if (object.getHp() > 0) {
                copy.add(object);
                copyt.add(textures.get(objects.indexOf(object)));
            }
            else {
                map.getOccGrid()[(int) object.getX()/16][(int) object.getY()/18] = false;
            }
        }

        objects = copy;
        textures = copyt;

        ArrayList<Enemy> copyn = new ArrayList<Enemy>();
        ArrayList<Texture> copynt = new ArrayList<Texture>();

        for (Enemy enemy : enemies) {
            if (enemy.getHp() > 0){
                copyn.add(enemy);
            }
        }

        enemies = copyn;
        enemyTextures = copynt;

        TextureRegion currentLarryWalk = larry.walkAnimation.getKeyFrame(animationTimer, true);

        game.entityBatch.begin();

        game.entityBatch.draw(mapTexture, 0, 0);
        game.entityBatch.draw(mapTexture, 0, 0);

        if (larry.state != Enemy.State.DEAD) game.entityBatch.draw(currentLarryWalk, larry.getX(), larry.getY());
        if (larry2.state != Enemy.State.DEAD) game.entityBatch.draw(currentLarryWalk, larry2.getX(), larry2.getY());
        if (larry3.state != Enemy.State.DEAD) game.entityBatch.draw(currentLarryWalk, larry3.getX(), larry3.getY());
        if (larry4.state != Enemy.State.DEAD) game.entityBatch.draw(currentLarryWalk, larry4.getX(), larry4.getY());

        game.entityBatch.draw(inventoryTexture, inventory.getX(), inventory.getY());            //Dibujado de objetos

        for (GameObject object:objects) {
            game.entityBatch.draw(textures.get(objects.indexOf(object)), object.getX(), object.getY());
        }

        game.entityBatch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            System.exit(0);
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
        game.entityBatch.dispose();
        imgL.dispose();
        imgB.dispose();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
