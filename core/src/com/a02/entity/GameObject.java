package com.a02.entity;

import com.a02.component.HealthBar;
import com.a02.component.Map;
import com.a02.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.a02.game.Utils.*;

public abstract class GameObject extends Entity {
    private int id;
    private String type;
    private int price;
    private boolean unlocked;
    private int hp;
    private int maxHp;
    private boolean grabbed;
    private Texture texture;
    private Vector2 ogPos;
    private Animation<TextureRegion> animation;
    private boolean isSelected;

    private static Logger logger = Logger.getLogger(GameObject.class.getName());

    public GameObject(float x, float y, int width, int height, int id,String type,
                      int price, boolean unlocked, int hp) {
        super(x, y, width, height);
        this.id = id;
        this.type = type;
        this.price = price;
        this.unlocked = unlocked;
        this.hp = hp;
        this.maxHp= hp;
        this.hpBar = new HealthBar(this, hp);
        this.ogPos = new Vector2();
        this.isSelected=false;
    }

    public GameObject(GameObject other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight());
        this.id = other.id;
        this.type = other.type;
        this.price = other.price;
        this.unlocked = other.unlocked;
        this.hp = other.hp;
        this.ogPos = other.ogPos;
    }

    public GameObject() {
        super();
        this.id = -1;
        this.type = "";
        this.price = 0;
        this.unlocked = false;
        this.hp = 0;
        this.hpBar = new HealthBar(this, hp);
        this.ogPos = new Vector2();
        this.isSelected=false;
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

    public int getMaxHp() {
        return maxHp;
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
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
                '}';
    }

    /**
     * Actualiza el estado de los GameObject.
     * @param gs Screen del juego
     */
    public abstract void update(GameScreen gs);

    /**
     * Carga las texturas y/o animaciones de los objetos.
     */
    public abstract void loadTextures();

    /**
     * Comprueba y gestiona el agarre y colocación de los objetos.
     * @param gs Screen de juego
     */
    public void grabObject(GameScreen gs) {  //Agarra el objeto y lo suelta
        Vector3 touchPos = getRelativeMousePos();
        if (Gdx.input.isTouched() && this.overlapsPoint(touchPos.x, touchPos.y) && (gs.state == GameScreen.State.PLAYING)) {
            this.grabbed = true;
            gs.state = GameScreen.State.BUYING;
            this.ogPos.x = this.getX();
            this.ogPos.y = this.getY();
            Logger.getLogger("").setLevel(Level.INFO);
            Logger.getLogger("").getHandlers()[0].setLevel(Level.INFO);
            logger.info("Objeto comprado");
        }
        if (this.grabbed) {
            this.setX((int) touchPos.x - 8);
            this.setY((int) touchPos.y - 8);

            if (!Gdx.input.isTouched()) {
                this.grabbed = false;
                this.setX(ogPos.x);
                this.setY(ogPos.y);

                gs.state = GameScreen.State.PLAYING;

                if (GameScreen.getGold() >= this.price)
                    this.setObjectInGrid(gs.map, gs.objects);
                else
                    logger.warning("No hay suficiente oro");
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
                logger.info("Objeto colocado");
            }
        }
    }

    /**
     * Copia el objeto introducido, dependiendo de su tipo.
     * @return Copia del objeto
     */
    public GameObject copyObject() {
        if (this instanceof Attacker) {
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
     * Comprueba si el objeto está en algún inventario.
     * @param gs Screen de juego
     * @return True si el objeto pertenece a algún inventario
     */
    public boolean isInInventory(GameScreen gs){
        return gs.defInv.contains(this) ||
                gs.drawingInv.contains(this) ||
                gs.trapInv.contains(this) ||
                gs.attackInv.contains(this) ||
                gs.fullInv.contains(this);
    }

    /**
     * Devuelve las coordenadas de la casilla en la que está el objeto que realiza la llamada.
     * @param map Objeto Map
     * @return Vector2 con las coordenadas
     */
    protected Vector2 mapGridCollision(Map map) {
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

    /**
     * Devuelve el enemigo con el que el objeto que hace la llamada hace colisión.
     * @param gs Screen de juego principal
     * @return Enemy colisionado
     */
    protected Enemy overlappedEnemy(GameScreen gs) {
        if (this.isGrabbed()) return null;
        for (Enemy enemy : gs.enemies) {
            if (this.getX() < enemy.getX() + enemy.getWidth() && this.getX() + this.getWidth() > enemy.getX() &&
                    this.getY() < enemy.getY() + enemy.getHeight() && this.getY() + this.getHeight() > enemy.getY() &&
                    !this.isInInventory(gs)) {
                return enemy;
            }
        }
        return null;
    }

    /**
     * Devuelve el frame actual de la animación correspondiente.
     * @param animationTimer Timer de animaciones de GameScreen
     * @return TextureRegion del frame requerido
     */
    public TextureRegion getCurrentAnimation(float animationTimer) {
        return this.getAnimation().getKeyFrame(animationTimer, true);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
