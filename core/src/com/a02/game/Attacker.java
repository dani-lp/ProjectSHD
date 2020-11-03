package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Attacker extends GameObject {
    private float attackDamage;
    private String attackType;

    private enum State {
        IDLE, ATTACKING, DYING, DEAD
    }

    State state;

    public Attacker(float x, float y, int width, int height, String sprite, int id, String name, String type, int price,
                    boolean unlocked, int hp, boolean buyable, boolean selected, String attackType, float attackDamage) {
        super(x, y, width, height, sprite, id, name, type, price, unlocked, hp, buyable, selected);
        this.attackType = attackType;
        this.attackDamage = attackDamage;
        this.state = State.IDLE;
    }

    public Attacker() {
        super();
        this.attackType = "";
        this.attackDamage = 0;
    }

    public Attacker(Attacker other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(), other.getSprite(), other.getId(),
                other.getName(), other.getType(), other.getPrice(), other.isUnlocked(), other.getHp(), other.isBuyable(), other.isSelected());
        this.attackType = other.getAttackType();
        this.attackDamage = other.getAttackDamage();
    }


    public float getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(float attackDamage) {
        this.attackDamage = attackDamage;
    }

    public String getAttackType() {
        return attackType;
    }

    public void setAttackType(String attackType) {
        this.attackType = attackType;
    }

    protected void update(ArrayList<GameObject> objects, ArrayList<Enemy> enemies, float secTimer) {
        switch (this.state) {
            case IDLE:
                if (this.getHp() <= 0) {
                    this.state = State.DYING;
                } else if (this.overlapsArrayEnemies(enemies)) {
                    this.state = State.ATTACKING;
                }
                break;

            case ATTACKING:
                if (this.overlapsArrayEnemies(enemies)) {
                    Enemy tempEnemy = this.overlappedEnemy(enemies);

                    if (tempEnemy.getHp() > 0 && secTimer % 60 == 0) {
                        tempEnemy.setHp((int) (tempEnemy.getHp() - this.attackDamage));
                    } else if (tempEnemy.getHp() <= 0) {
                        this.state = State.IDLE;
                    }
                }
                break;

            case DYING:
                try {
                    objects.remove(this);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                this.state = State.DEAD;
                break;

        }
    }

    float x = this.getX();
    float y = this.getY();
    static boolean temp = false;

    public void buy(GameScreen game, ArrayList<GameObject> objects, ArrayList<Texture> textures, Inventory
            inventory, Map map) {
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        game.camera.unproject(touchPos);
        if (Gdx.input.isTouched() && this.overlapsPoint(touchPos.x, touchPos.y) && !temp && isBuyable() && objects.get(0).isSelected()) {
            temp = true;
            objects.get(0).setSelected(false);
        }
        if (temp) { //El objeto ya ha sido "cogido"
            this.setBuyable(false);
            //Ajusta la posición del sprite a la del mouse
            this.setX((int) (touchPos.x - 16 / 2));
            this.setY((int) (touchPos.y - 16 / 2));
            //Al "soltar" el objeto:
            if (!Gdx.input.isTouched()) {
                Vector2 temp = this.mapGridCollisionMouse(map, touchPos.x, touchPos.y); //Pos. del mouse
                if (touchPos.x < 255) {
                    if (!map.getOccGrid()[(int) temp.x / 16][(int) temp.y / 18]) {    //Comprueba si la casilla está libre
                        this.setBuyable(true);
                        Attacker object = new Attacker(this);   //Objeto que va a ser colocado
                        Texture textu = new Texture(Gdx.files.internal(object.getSprite())); //Textura del objeto copia
                        object.setX(temp.x);   //Fija la posición copia
                        object.setY(temp.y);

                        objects.add(object);
                        textures.add(textu);

                        map.getOccGrid()[(int) temp.x / 16][(int) temp.y / 18] = true;
                    }
                }
                this.setX(x); //Devuelve a su posición inicial al objeto original
                this.setY(y); //280 135
                objects.get(0).setSelected(true);
            }
        }
        if (!Gdx.input.isTouched()) {
            temp = false;
        }
    }

    protected boolean overlapsArrayEnemies(ArrayList<Enemy> enemies) { //Devuelve true si la Entity que llama colisiona con la Entity parámetro
        for (Enemy enemy : enemies) {
            if ((this.getY() + this.getHeight() < enemy.getY()) || (this.getY() > enemy.getY() + enemy.getHeight())) {
                continue;
            } else if ((this.getX() + this.getWidth() < enemy.getX()) || (this.getX() > enemy.getX() + enemy.getWidth())) {
                continue;
            } else {
                return true;
            }
        }
        return false;
    }

    protected Enemy overlappedEnemy(ArrayList<Enemy> enemies) { //Devuelve true si la Entity que llama colisiona con la Entity parámetro
        for (Enemy enemy : enemies) {
            if ((this.getY() + this.getHeight() < enemy.getY()) || (this.getY() > enemy.getY() + enemy.getHeight())) {
                continue;
            } else if ((this.getX() + this.getWidth() < enemy.getX()) || (this.getX() > enemy.getX() + enemy.getWidth())) {
                continue;
            } else {
                return enemy;
            }
        }
        return null;
    }

    public void attack(Enemy thing) {
        if (this.overlaps(thing)) {      //Si estan en contacto empieza a restarle vida
            thing.setHp((int) (thing.getHp() - this.getAttackDamage()));
            System.out.println(thing.getHp());
        }
    }

}


