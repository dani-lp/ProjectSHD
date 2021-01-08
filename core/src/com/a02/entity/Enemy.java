package com.a02.entity;

import com.a02.pathfinding.Node;
import com.a02.screens.GameScreen;
import com.a02.component.HealthBar;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import static com.a02.game.Utils.*;

public class Enemy extends Entity {
    private int id;
    private int hp;
    private int attackDamage;
    private float speed;
    private int goldValue;
    private int startTime;  //Tiempo de inicio de movimiento
    private int effectTimer;    //Timer de efectos de las trampas
    private int deathTimer; //Timer de animación de muerte
    private Vector2 focus;
    protected Animation<TextureRegion> walkAnimation;
    protected Animation<TextureRegion> attackAnimation;
    protected Animation<TextureRegion> deathAnimation;
    public Texture idleTexture;

    public Node focusNode; //Nodo al que se dirige el enemigo

    public enum State {
        IDLE, WALKING, ATTACKING, DYING
    }
    public enum TrapEffect {
        BURNING, FREEZE, CONFUSED, NEUTRAL
    }

    public State state;
    public TrapEffect trapEffect;

    public Enemy(float x, float y, int width, int height, int id, int hp,
                 int attackDamage, float speed, int startTime, int goldValue) {  //Constructor de enemigos
        super(x, y, width, height);
        this.id = id;
        this.hp = hp;
        this.attackDamage = attackDamage;
        this.speed = speed;
        this.state = State.IDLE;
        this.trapEffect = TrapEffect.NEUTRAL;
        this.startTime = startTime;
        this.goldValue = goldValue;
        this.hpBar = new HealthBar(this, hp);
        this.focus = new Vector2(0,0);

    }

    public Enemy() {    //Constructor vacio de enemigos
        super();
        this.id = -1;
        this.hp = 0;
        this.attackDamage = 0;
        this.speed = 0;
        this.state = State.IDLE;
        this.trapEffect = TrapEffect.NEUTRAL;
        this.hpBar = new HealthBar(this, 0);
        this.goldValue = 50;
        this.focus = new Vector2(0,0);
    }

    public void loadAnimations() {
        switch (this.getId()){
            case 0:
                this.walkAnimation = createAnimation("Enemies/e1-walk.png",3,1,0.2f);
                this.attackAnimation = createAnimation("Enemies/e1-attack.png",2,2,0.2f);
                this.deathAnimation = createAnimation("Enemies/e1-death.png",2,1, 0.2f);
                break;
            case 1:
                this.walkAnimation = createAnimation("Enemies/e2-walk.png",2,2,0.2f);
                this.attackAnimation = createAnimation("Enemies/e2-attack.png",5,1,0.2f);
                this.deathAnimation = createAnimation("Enemies/e2-death.png",2,1, 0.2f);
                break;
            case 2:
                this.walkAnimation = createAnimation("Enemies/e3-walk.png",2,2,0.2f);
                this.attackAnimation = createAnimation("Enemies/e3-attack.png",5,1,0.2f);
                this.deathAnimation = createAnimation("Enemies/e3-death.png",2,1, 0.2f);
                break;
            case 3:
                this.walkAnimation = createAnimation("Enemies/e4-walk.png",2,2,0.2f);
                this.attackAnimation = createAnimation("Enemies/e4-attack.png",2,2,0.125f);
                this.deathAnimation = createAnimation("Enemies/e4-death.png",2,1, 0.2f);
                break;
            case 4:
                this.walkAnimation = createAnimation("Enemies/e5-walk.png",2,2,0.5f); //MUY LENTO (el movimiento)
                this.attackAnimation = createAnimation("Enemies/e5-attack.png",5,1,0.2f);
                this.deathAnimation = createAnimation("Enemies/e5-death.png",2,1, 0.2f);
                break;
            case 5:
                this.walkAnimation = createAnimation("Enemies/e6-walk.png",2,2,0.2f);
                this.attackAnimation = createAnimation("Enemies/e6-attack.png",5,1,0.2f);
                this.deathAnimation = createAnimation("Enemies/e6-death.png",2,1, 0.2f);
                break;
            case 6:
                this.walkAnimation = createAnimation("Enemies/e7-walk.png",2,2,0.2f);
                this.attackAnimation = createAnimation("Enemies/e7-attack.png",2,2,0.125f);
                this.deathAnimation = createAnimation("Enemies/e7-death.png",2,1, 0.2f);
                break;
            case 7: //BOSS
                this.walkAnimation = createAnimation("Enemies/e8-walk.png",2,2,0.2f);
                this.attackAnimation = createAnimation("Enemies/e8-attack.png",2,2,0.25f);
                this.deathAnimation = createAnimation("Enemies/e8-death.png",3,1, 0.15f);
                break;
        }
    }

    /**
     * Carga la textura Idle del enemigo.
     */
    public void loadIdleTexture() {
        if (this.getId() >= 0 && this.getId() <= 7) this.idleTexture = new Texture("Enemies/e" + (this.getId() + 1) + "-idle.png");
        else this.idleTexture = new Texture(Gdx.files.internal("empty.png"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setEffectTimer(int effectTimer) {
        this.effectTimer = effectTimer;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getGoldValue() {
        return goldValue;
    }

    public void setGoldValue(int goldValue) {
        this.goldValue = goldValue;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setDeathTimer(int deathTimer) {
        this.deathTimer = deathTimer;
    }

    public void setFocus(float x, float y) {
        this.focus.x = x;
        this.focus.y = y;
    }

    public void setFocus(Node node) {
        this.focus.x = node.getX();
        this.focus.y = node.getY();
    }

    public int getDeathTimer() {
        return deathTimer;
    }

    /**
     * Actualiza la posición, estado y efectos de un enemigo.
     * @param gs GameScreen utilizada
     */
    public void update(GameScreen gs) {
        switch (this.state) {
            case IDLE:
                if (gs.secTimer >= this.startTime){
                    if (this.trapEffect != TrapEffect.FREEZE) {
                        this.state = State.WALKING;
                    }
                }
                break;

            case WALKING: //Movimiento a beacon
                if (this.getId() != 3) this.updatePathfinding(gs);
                else this.move();

                if (this.getHp() <= 0) {
                    this.state = State.DYING;
                    this.deathTimer = gs.secTimer + 60;
                }
                else if (this.overlappedObject(gs) != null && !this.overlappedObject(gs).isGrabbed() && this.getId() != 2) {
                    if (!(this.overlappedObject(gs) instanceof Trap && this.getId() == 3)) this.state = State.ATTACKING;
                }
                else if (this.overlappedArea(gs) != null && !this.overlappedArea(gs).isGrabbed() && this.getId() == 2){
                    this.state = State.ATTACKING;
                }
                break;

            case ATTACKING:

                if (this.overlappedObject(gs) != null && this.getId() != 2) {
                    GameObject tempObj = this.overlappedObject(gs);    //Objeto siendo colisionado
                    if (tempObj.getHp() > 0 && !tempObj.isGrabbed() && gs.secTimer % 60 == 0) { //Pegar 1 vez por segundo
                        tempObj.setHp(tempObj.getHp() - this.attackDamage);
                    } else if (tempObj.getHp() <= 0) {
                        this.state = State.WALKING;
                    }
                }
                else if (this.overlappedArea(gs) != null && this.getId() == 2){
                    if (gs.secTimer % 60 == 0) {
                        double angle = ((Math.atan2(this.getY() - overlappedArea(gs).getY(),
                                this.getX() - overlappedArea(gs).getX()) * 180) / Math.PI + 90);
                        EnemyShoot shoot = new EnemyShoot(this.getX() + 18, this.getY() + 9, 2, 2, 2,
                                this.getAttackDamage(), "spectralShoot.png", 5, angle); //TODO Cambiar color del disparo
                        gs.enemyShots.add(shoot);
                    }
                }
                else {
                    this.state = State.WALKING;
                }

                if (this.getHp() <= 0) {
                    this.state = State.DYING;
                    this.deathTimer = gs.secTimer + 60;
                }

                break;

            case DYING:
                this.hpBar = null;
                if (gs.secTimer > this.deathTimer) {
                    gs.setGold(gs.getGold() + this.goldValue);
                    break;
                }
                break;
        }

        this.updateEffect(gs);

        if (this.hpBar != null) this.hpBar.update(this, this.getHp());
    }

    public boolean flipped = false; //Usado para saber si un objeto debe estar dado la vuelta al animarlo

    protected void move() {
        double angle = Math.toDegrees(-Math.atan((this.getY() - this.focus.y) / (this.getX() - this.focus.x)));
        float dirX = (float) Math.sin(angle) * Gdx.graphics.getDeltaTime() * this.speed;
        float dirY = (float) Math.cos(angle) * Gdx.graphics.getDeltaTime() * this.speed;
        this.flipped = dirX < 0;
        this.setX((float) round(this.getX() + dirX, 4));
        this.setY((float) round(this.getY() + dirY, 4));
    }

    void updatePathfinding(GameScreen gs) {
        if (this.focusNode == null) {
            this.focusNode = getNearestValidNode(gs);
            this.setFocus(focusNode.getX(), focusNode.getY());
            return;
        }
        if (this.overlapsPoint(focusNode.getX(), focusNode.getY(), 8)) {
            if (this.focusNode.getNextNode() != null) this.focusNode = this.focusNode.getNextNode();
            this.setFocus(focusNode.getX(), focusNode.getY());
        }
        this.move();
    }

    /**
     * Devuelve el nodo válido más cercano.
     * @param gs Screen de juego
     * @return Nodo válido más cercano
     */
    public Node getNearestValidNode(GameScreen gs) {
        ArrayList<Node> validNodes = new ArrayList<>();
        //Calcular nodos válidos
        for (Node node : gs.nodes) {
            if (this.isNodeValid(node, gs)) validNodes.add(node);
        }
        //Calcular nodo válido más cercano
        int minDistanceIndex = 0;
        double minDistance = 400; //Distancia mayor a la diagonal máxima (368 aprox.)
        for (int node = 0; node < validNodes.size(); node++) {
            double distance = validNodes.get(node).distanceToNode(this);
            if (distance < minDistance ) {
                minDistance = distance;
                minDistanceIndex = node;
            }
        }
        return validNodes.get(minDistanceIndex);
    }

    public boolean isNodeValid(Node node, GameScreen gs) {
        boolean anyColl = false;
        if (gs.obstacles.size() < 1) return true;
        for (Obstacle obstacle : gs.obstacles) {
            //Abajo izquierda
            boolean coll1 = lineRectCollision(
                    this.getX(),this.getY(),
                    node.getX(),node.getY(),
                    obstacle.getX(),obstacle.getY(),obstacle.getWidth(), obstacle.getHeight());
            //Abajo derecha
            boolean coll2 = lineRectCollision(
                    this.getX() + this.getWidth(),this.getY(),
                    node.getX() + this.getWidth(),node.getY(),
                    obstacle.getX(),obstacle.getY(),obstacle.getWidth(), obstacle.getHeight());
            //Arriba izquierda
            boolean coll3 = lineRectCollision(
                    this.getX(),this.getY() + this.getHeight(),
                    node.getX(),node.getY() + this.getHeight(),
                    obstacle.getX(),obstacle.getY(),obstacle.getWidth(), obstacle.getHeight());
            //Arriba derecha
            boolean coll4 = lineRectCollision(
                    this.getX() + this.getWidth(),this.getY() + this.getHeight(),
                    node.getX() + this.getWidth(),node.getY() + this.getHeight(),
                    obstacle.getX(),obstacle.getY(),obstacle.getWidth(), obstacle.getHeight());

            anyColl = coll1 || coll2 || coll3 || coll4;
        }
        return !anyColl;
    }

    /**
     * Colisión entre 2 líneas.
     */
    private static boolean lineLineCollision(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        //Calcular la dirección de las líneas
        float div = ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
        float uA = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / div;
        float uB = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3)) / div;

        //Si uA y uB están entre 0 y 1, existe colisión
        return uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1;
    }

    /**
     * Colisión entre línea FINITA y rectángulo.
     */
    public static boolean lineRectCollision(float x1, float y1, float x2, float y2, float rx, float ry, float rw, float rh) {
        //Colisión de cada lado del rectángulo
        boolean left =   lineLineCollision(x1,y1,x2,y2, rx,ry,rx, ry+rh);
        boolean right =  lineLineCollision(x1,y1,x2,y2, rx+rw,ry, rx+rw,ry+rh);
        boolean top =    lineLineCollision(x1,y1,x2,y2, rx,ry, rx+rw,ry);
        boolean bottom = lineLineCollision(x1,y1,x2,y2, rx,ry+rh, rx+rw,ry+rh);

        //Si alguna de las colisiones superiores es cierta, ha habido colisión con el obstáculo
        return left || right || top || bottom;
    }

    private void updateEffect(GameScreen gs) {
        switch (this.trapEffect) {
            case BURNING:
                if ((gs.secTimer % 30 == 0) && (gs.secTimer < this.effectTimer + 180)){
                    this.hp -= 30;
                } else if (gs.secTimer > this.effectTimer + 180) this.trapEffect = TrapEffect.NEUTRAL;
                break;
            case FREEZE:
                this.state = State.IDLE;
                if (gs.secTimer > this.effectTimer + 300) {
                    this.trapEffect = TrapEffect.NEUTRAL;
                    this.state = State.WALKING;
                }
                break;
            case CONFUSED:
                if (gs.secTimer == this.effectTimer) {
                    this.focus = getRandomFocus(gs);
                    this.state = State.WALKING;
                }
                if (gs.secTimer > this.effectTimer + 280) {
                    this.trapEffect = TrapEffect.NEUTRAL;
                    this.setFocus(this.focusNode);
                    this.move();
                    this.state = State.WALKING;
                }
            case NEUTRAL:
                break;
        }
    }

    private Vector2 getRandomFocus(GameScreen gs) {
        float x = 0;
        float y = 0;
        switch (gs.getCurrentRound()) {
            case 1: //Limitado a delante del puente
                x = (float)(Math.random() * 300 + 96); //TODO: y si se confunde antes de cruzar el puente?
                y = (float)(Math.random() * 1800 - 900); //TODO igual es buena idea bloquar la zona oeste del puente
                break;
            case 2: //Limitado al barranco
                x = (float)(Math.random() * 159 + 48);
                y = (float)(Math.random() * 1800 - 900);
                break;
            case 3: //TODO
                x = (float)(Math.random());
                y = (float)(Math.random());
                break;
            case 4: //Ilimitado
                x = (float)(Math.random() * 3200 - 1600);
                y = (float)(Math.random() * 1800 - 900);
                break;
            case 5: //Limitado hacia detrás
                x = (float)(320 - Math.random() * 300);
                y = (float)(Math.random() * 180);
                break;
        }
        return new Vector2(x, y);
    }

    /**
     * Comprueba si existe colisión con algún objeto dentro de una Lista.
     * @param objects List de objetos con los que es posible hallar colisión.
     * @return True si hay colisión, false si no la hay.
     */
    protected boolean overlapsArray(List<GameObject> objects) {
        for (GameObject object: objects) {
            if (!(object instanceof Trap) && !object.isGrabbed()) {
                if (this.getX() < object.getX() + object.getWidth() && this.getX() + this.getWidth() > object.getX() &&
                        this.getY() < object.getY() + object.getHeight() && this.getY() + this.getHeight() > object.getY()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Halla qué objeto está colisionando con el enemigo, excluyendo los objetos del inventario.
     * @param gs GameScreen utilizada
     * @return Objecto colisionado.
     */
    protected GameObject overlappedObject(GameScreen gs) {
        for (GameObject object: gs.objects) {
            if (this.getX() < object.getX() + object.getWidth() && this.getX() + this.getWidth() > object.getX() &&
                    this.getY() < object.getY() + object.getHeight() && this.getY() + this.getHeight() > object.getY() &&
                    !object.isInInventory(gs)) {
                return object;
            }
        }
        return null;
    }

    /**
     * Devuelve un TextureRegion con la animación correspondiente al estado
     * @param animationTimer Reloj de la animación
     * @return El TextureRegion de animación
     */
    public TextureRegion getCurrentAnimation(float animationTimer) {
        switch (this.state) {
            case IDLE:
                return new TextureRegion(this.idleTexture);
            case WALKING:
                return this.walkAnimation.getKeyFrame(animationTimer, true);
            case ATTACKING:
                return this.attackAnimation.getKeyFrame(animationTimer, true);
            case DYING:
                return this.deathAnimation.getKeyFrame(animationTimer, true);
        }
        return null;
    }

    protected Obstacle overlappedObstacle(GameScreen gs) {
        for (Obstacle obstacle: gs.obstacles) {
            if (this.getX() < obstacle.getX() + obstacle.getWidth() && this.getX() + this.getWidth() > obstacle.getX() &&
                    this.getY() < obstacle.getY() + obstacle.getHeight() && this.getY() + this.getHeight() > obstacle.getY() ) {
                return obstacle;
            }
        }
        return null;
    }

    protected GameObject overlappedArea(GameScreen gs) { //TODO: no selecciona por proximidad, sino por primer enemigo encontrado
        for (GameObject obj : gs.objects) {
            if ((obj.getX() < this.getX() + 50 && obj.getX() > this.getX() - 50)
                    && (obj.getY() < this.getY() + 50 && obj.getY() > this.getY() - 50) && !(obj instanceof Trap)) {
                return obj;
            }
        }
        return null;
    }
}
