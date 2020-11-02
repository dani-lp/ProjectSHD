package tests;

import com.a02.game.GameObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameObjectTest {
    GameObject testObject = new GameObject(1,2,3,4,"test.png",5,"test",6,true,7,false,true);

    @Test
    public void getId() {
        assertEquals(5,testObject.getId());
    }

//    @Test
//    public void setId() {
//    }
//
//    @Test
//    public void getName() {
//    }
//
//    @Test
//    public void setName() {
//    }
//
//    @Test
//    public void getPrice() {
//    }
//
//    @Test
//    public void setPrice() {
//    }
//
//    @Test
//    public void isUnlocked() {
//    }
//
//    @Test
//    public void setUnlocked() {
//    }
//
//    @Test
//    public void isBuyable() {
//    }
//
//    @Test
//    public void setBuyable() {
//    }
//
//    @Test
//    public void getHp() {
//    }
//
//    @Test
//    public void setHp() {
//    }
//
//    @Test
//    public void isSelected() {
//    }
//
//    @Test
//    public void setSelected() {
//    }
//
//    @Test
//    public void testToString() {
//    }
//
//    @Test
//    public void buy() {
//    }
//
//    @Test
//    public void mapGridCollision() {
//    }
//
//    @Test
//    public void mapGridCollisionMouse() {
//    }
}