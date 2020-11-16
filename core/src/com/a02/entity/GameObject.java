package com.a02.entity;

import com.a02.component.HealthBar;
import com.a02.component.Map;
import com.a02.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.List;

import static com.a02.game.Utils.*;

public abstract class GameObject extends Entity {
    private int id;
    private String type;
    private int price;
    private boolean unlocked;
    private int hp;
    private boolean grabbed;
    private Texture texture;

    public GameObject(float x, float y, int width, int height, int id,String type,
                      int price, boolean unlocked, int hp) {
        super(x, y, width, height);
        this.id = id;
        this.type = type;
        this.price = price;
        this.unlocked = unlocked;
        this.hp = hp;
        this.hpBar = new HealthBar(this, hp);
    }

    public GameObject(GameObject other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight());
        this.id = other.id;
        this.type = other.type;
        this.price = other.price;
        this.unlocked = other.unlocked;
        this.hp = other.hp;
    }

    public GameObject() {
        super();
        this.id = -1;
        this.type = "";
        this.price = 0;
        this.unlocked = false;
        this.hp = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public boolean isGrabbed() {
        return grabbed;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public String toString() {
        return "GameObject{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", unlocked=" + unlocked +
                ", hp=" + hp +
                ", grabbed=" + grabbed +
                ", texture=" + texture +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    float x = this.getX();
    float y = this.getY();

    public abstract void update(List<GameObject> objects, List<Enemy> enemies, float secTimer);

    /**
     * Comprueba y gestiona el agarre y colocación de los objetos.
     * @param map mapa en el que colocar el objeto
     * @param objects lista de objetos en las que guardar el objeto colocado
     */
    public void grabObject(Map map, List<GameObject> objects) {  //Agarra el objeto y lo suelta
        Vector3 touchPos = getRelativeMousePos();

        if (Gdx.input.isTouched() && this.overlapsPoint(touchPos.x, touchPos.y) && !GameScreen.isBuying()) {
            this.grabbed = true;
            GameScreen.setBuying(true);
        }
        if (this.grabbed) {
            this.setX((int) touchPos.x - 8);
            this.setY((int) touchPos.y - 8);

            if (!Gdx.input.isTouched()) {
                this.grabbed = false;
                this.setX(x);
                this.setY(y);

                GameScreen.setBuying(false);

                if (GameScreen.getGold() >= this.price) this.setObjectInGrid(map, objects);
            }
        }
    }

    /**
     * Coloca el objeto en su posición asignada, si ésta está libre
     * @param map Mapa en el que colocar el objeto
     * @param objects Lista de objetos a la que añadir el objeto colocado
     */
    public void setObjectInGrid(Map map, List<GameObject> objects) {
        Vector3 touchPos = getRelativeMousePos();
        if (touchPos.x < 255) {
            Vector2 tempPos = this.mapGridCollisionMouse(map);
            if (!map.getOccGrid()[(int) tempPos.x / 16][(int) tempPos.y / 18]) {
                GameObject copy = this.copyObject();
                copy.setX(tempPos.x);
                copy.setY(tempPos.y);
                map.getOccGrid()[(int) tempPos.x / 16][(int) tempPos.y / 18] = true;
                objects.add(copy);
                GameScreen.setGold(GameScreen.getGold() - this.price);
            }
        }
    }

    /**
     * Copia el objeto introducido, dependiendo de su tipo.
     * @return Copia del objeto
     */
    public GameObject copyObject() {
        if (this instanceof Attacker) { //TODO: Es posible que haya que crear constructores copia nuevos
            return new Attacker((Attacker) this);
        }
        else if (this instanceof Defender) {
            return new Defender((Defender) this);
        }
        else if (this instanceof Trap) {
            return new Trap((Trap) this);
        }
        else {
            return null;
        }
    }

    /**
     * Devuelve las coordenadas de la casilla en la que está el objeto que realiza la llamada.
     * @param map Objeto Map
     * @return
     */
    protected Vector2 mapGridCollision(Map map) {
        int x, y;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 10; j++) {
                if (this.overlaps( map.getCoordGrid()[i][j] , 18, 16)) {
                    return new Vector2(map.getCoordGrid()[i][j].x, map.getCoordGrid()[i][j].y);
                }
            }
        }
        return null;
    }

    /**
     * Devuelve el número de casilla con la que colisiona del mapa recibido como parámetro, null si no lo hace,
     * con la colisión adaptada a la posición del ratón
     * @param map mapa introducido
     * @return índice de la casilla o null
     */
    protected Vector2 mapGridCollisionMouse(Map map) {
        Vector3 touchPos = getRelativeMousePos();
        touchPos.x = (int) touchPos.x;
        touchPos.y = (int) touchPos.y;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 10; j++) {
                if(map.getCoordGrid()[i][j].x <= touchPos.x &&
                        map.getCoordGrid()[i][j].x + 16 >= touchPos.x &&
                        map.getCoordGrid()[i][j].y <= touchPos.y &&
                        map.getCoordGrid()[i][j].y + 18 >= touchPos.y){
                    return new Vector2(map.getCoordGrid()[i][j].x, map.getCoordGrid()[i][j].y);
                }
            }
        }
        return null;
    }

    protected boolean overlapsArrayEnemies(List<Enemy> enemies) { //Devuelve true si la Entity que llama colisiona con la Entity parámetro
        for (Enemy enemy : enemies) {
            if (this.getX() < enemy.getX() + enemy.getWidth() && this.getX() + this.getWidth() > enemy.getX() &&
                    this.getY() < enemy.getY() + enemy.getHeight() && this.getY() + this.getHeight() > enemy.getY()) {
                return true;
            }
        }
        return false;
    }

    protected Enemy overlappedEnemy(List<Enemy> enemies) { //Devuelve true si la Entity que llama colisiona con la Entity parámetro
        if (this.isGrabbed()) return null;
        for (Enemy enemy : enemies) {
            if (this.getX() < enemy.getX() + enemy.getWidth() && this.getX() + this.getWidth() > enemy.getX() &&
                    this.getY() < enemy.getY() + enemy.getHeight() && this.getY() + this.getHeight() > enemy.getY()) {
                return enemy;
            }
        }
        return null;
    }
}
