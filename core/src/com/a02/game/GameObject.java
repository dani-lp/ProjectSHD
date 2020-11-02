package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class GameObject extends Entity{
    private int id;
    private String name;
    private String type;
    private int price;
    private boolean unlocked;
    private boolean buyable=true;
    private boolean selected=true;
    private int hp;

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

    public GameObject(GameObject other){
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

    @Override
    public String toString() {
        return "ASD [id=" + id + ", name=" + name + ", type=" + type + ", x=" + this.getX() + ", y=" + this.getY() + ", sprite=" + getSprite() + ", price=" + price
                + ", unlock=" + unlocked + ", buyable=" + buyable + ", hp=" + hp+"]";
    }

    float x= this.getX();
    float y = this.getY();
    static boolean temp = false;
    public void buy(GameScreen game, ArrayList<GameObject> objects, ArrayList<Texture> textures, Inventory inventory, Map map){
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        game.camera.unproject(touchPos);

        if (Gdx.input.isTouched() && this.overlapsPoint(touchPos.x, touchPos.y) && !temp && buyable && objects.get(0).isSelected()) {
            temp = true;
            objects.get(0).setSelected(false);
        }
        if (temp) { //El objeto ya ha sido "cogido"
            //Ajusta la posición del sprite a la del mouse
            this.setX((int) (touchPos.x - 16 / 2));
            this.setY((int) (touchPos.y - 16 / 2));
            //Al "soltar" el objeto:
            if (!Gdx.input.isTouched()){
                Vector2 temp = this.mapGridCollisionMouse(map, touchPos.x, touchPos.y); //Pos. del mouse

                if (!map.getOccGrid()[(int)temp.x/16][(int)temp.y/18]) {    //Comprueba si la casilla está libre

                    GameObject object = new GameObject(this);   //Objeto que va a ser colocado
                    Texture textu = new Texture(Gdx.files.internal(object.getSprite())); //Textura del objeto copia

                    object.setX(temp.x);   //Fija la posición copia
                    object.setY(temp.y);
                    objects.add(object);
                    textures.add(textu);

                    map.getOccGrid()[(int)temp.x/16][(int)temp.y/18] = true;
                }
                this.setX(x); //Devuelve a su posición inicial al objeto original
                this.setY(y); //260 135
                objects.get(0).setSelected(true); //Para que al pasar por encima de otros objetos del inventario no se seleccione mas de uno
            }

        }
        if (!Gdx.input.isTouched()) {
            temp = false;
        }


    }
    //Devuelve el número de casilla con la que colisiona del mapa recibido como parámetro, null si no lo hace
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
     * @param map   mapa introducido
     * @param xMouse    x del ratón
     * @param yMouse    y delratón
     * @return  índice de la casilla o null
     */
    protected Vector2 mapGridCollisionMouse(Map map, float xMouse, float yMouse) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 10; j++) {
                if(map.getCoordGrid()[i][j].x <= xMouse &&
                        map.getCoordGrid()[i][j].x + 16 >= xMouse &&
                        map.getCoordGrid()[i][j].y <= yMouse &&
                        map.getCoordGrid()[i][j].y + 18 >= yMouse){
                    return new Vector2(map.getCoordGrid()[i][j].x, map.getCoordGrid()[i][j].y);
                }
            }
        }
        return null;
    }

}
