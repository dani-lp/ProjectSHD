package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import static com.a02.utils.Utils.*;

public abstract class GameObject extends Entity {
    private int id;
    private String name;
    private String type;
    private int price;
    private boolean unlocked;
    private boolean buyable = true;
    private boolean selected = true;
    private int hp;
    private boolean grabbed;

    public GameObject(float x, float y, int width, int height, String sprite, int id, String name,String type,
                      int price, boolean unlocked, int hp, boolean buyable, boolean selected) {
        super(x, y, width, height, sprite);
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.unlocked = unlocked;
        this.buyable = buyable;
        this.hp = hp;
        this.selected = selected;
    }

    public GameObject(GameObject other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(), other.getSprite());
        this.id = other.id;
        this.name = other.name;
        this.type = other.type;
        this.price = other.price;
        this.unlocked = other.unlocked;
        this.buyable = other.buyable;
        this.hp = other.hp;
        this.selected = other.selected;
    }

    public GameObject() {
        super();
        this.id = -1;
        this.name = "";
        this.type = "";
        this.price = 0;
        this.unlocked = false;
        this.buyable = true;
        this.selected = true;
        this.hp = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isBuyable() {
        return buyable;
    }

    public void setBuyable(boolean buyable) {
        this.buyable = buyable;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isGrabbed() {
        return grabbed;
    }

    @Override
    public String toString() {
        return "ASD [id=" + id + ", name=" + name + ", type=" + type + ", x=" + this.getX() + ", y=" + this.getY() + ", sprite=" + getSprite() + ", price=" + price
                + ", unlock=" + unlocked + ", buyable=" + buyable + ", hp=" + hp+"]";
    }

    float x = this.getX();
    float y = this.getY();

    /*
    public void buy(GameScreen game, ArrayList<GameObject> objects, ArrayList<Texture> textures, Inventory inventory, Map map) {
        //Coge posición del ratón y la escala al tamaño de la cámara
        Vector3 touchPos = getRelativeMousePos();

        if (Gdx.input.isTouched() && this.overlapsPoint(touchPos.x, touchPos.y) && !grabbed && buyable && objects.get(0).isSelected()) {
            grabbed = true;
            objects.get(0).setSelected(false);  //TODO: porqué get(0)?
        }
        if (grabbed) {//El objeto ya ha sido "cogido"
            this.setBuyable(false);
            //Ajusta la posición del sprite a la del mouse
            this.setX((int) (touchPos.x - 16 / 2));
            this.setY((int) (touchPos.y - 16 / 2));

            //Al "soltar" el objeto:
            if (!Gdx.input.isTouched()) {
                Vector2 temp = this.mapGridCollisionMouse(map); //Pos. del mouse
                if (touchPos.x < 255) {
                    if (!map.getOccGrid()[(int) temp.x / 16][(int) temp.y / 18]) {    //Comprueba si la casilla está libre
                        this.setBuyable(true);

                        GameObject object = new GameObject(this);
                        if (this instanceof Trap) {
                            object = new Trap( (Trap) this);       //Objeto que va a ser colocado
                        }
                        else if (this instanceof Attacker) {
                            object = new Attacker( (Attacker) this);
                        }

                        Texture textu = new Texture(Gdx.files.internal(object.getSprite())); //Textura del objeto copia
                        object.setX(temp.x);   //Fija la posición copia
                        object.setY(temp.y);
                        objects.add(object);
                        textures.add(textu);

                        map.getOccGrid()[(int) temp.x / 16][(int) temp.y / 18] = true;
                    }
                }
                this.setX(x); //Devuelve a su posición inicial al objeto original
                this.setY(y); //260 135
                objects.get(0).setSelected(true); //Para que al pasar por encima de otros objetos del inventario no se seleccione mas de uno
            }
        }
        if (!Gdx.input.isTouched()) {
            grabbed = false;
        }
    }
    */

    public abstract void update(List<GameObject> objects, List<Enemy> enemies, float secTimer);

    public void grabObject(Map map, List<GameObject> objects, List<Texture> textures) {  //Agarra el objeto y lo suelta
        //4.- Volver a colocar en lugar original
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

                if (GameScreen.getGold() >= this.price) this.setObjectInGrid(map, objects, textures);
            }
        }
    }

    public void setObjectInGrid(Map map, List<GameObject> objects, List<Texture> textures) {  //Coloca el objeto en su posición asignada, si está libre
        Vector3 touchPos = getRelativeMousePos();
        if (touchPos.x < 255) {
            Vector2 tempPos = this.mapGridCollisionMouse(map);
            if (!map.getOccGrid()[(int) tempPos.x / 16][(int) tempPos.y / 18]) {
                GameObject copy = this.copyObject();
                copy.setX(tempPos.x);
                copy.setY(tempPos.y);
                map.getOccGrid()[(int) tempPos.x / 16][(int) tempPos.y / 18] = true;
                objects.add(copy);
                textures.add(new Texture(Gdx.files.internal(copy.getSprite())));
                GameScreen.setGold(GameScreen.getGold() - this.price);
            }
        }
    }

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

}
