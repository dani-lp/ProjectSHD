package tests.component;

import com.a02.entity.Shoot;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShootTest {

    Shoot shootTest = new Shoot(1,2,50,100,3,20,"TestSprite",5, "0", 1);
    @Test
    public void getX() {
        assertEquals(1,shootTest.getX(), 0.0f);
    }

    @Test
    public void getY() {
        assertEquals(2,shootTest.getY(), 0.0f);
    }

    @Test
    public void getSpeed() {
        assertEquals(3,shootTest.getSpeed(),0);
    }

    @Test
    public void getAttackdamage() {
        assertEquals(20,shootTest.getAttackdamage(), 0.0f);
    }

    @Test
    public void getWidth() {
        assertEquals(100,shootTest.getWidth());
    }

    @Test
    public void getHeight() {
        assertEquals(50,shootTest.getHeight());
    }

    @Test
    public void getHp() {
        assertEquals(4,shootTest.getHp());
    }
}