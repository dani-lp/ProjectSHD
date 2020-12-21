package tests.component;

import com.a02.component.HealthBar;
import com.a02.entity.Attacker;
import com.a02.entity.Entity;
import org.junit.Test;

import static org.junit.Assert.*;

public class HealthBarTest {

    Entity entityTest = new  Attacker(1,2,3,4, 5,"electrico",6,true,7,"Electrico",1.1f);
    HealthBar hBarTest = new HealthBar(entityTest, 100);

    @Test
    public void getX() {
        assertEquals(1,hBarTest.getX(),0.0);
    }

    @Test
    public void getY() {
        assertEquals(2,hBarTest.getY(),0.0);
    }

    @Test
    public void getMaxHP() {
        assertEquals(100,hBarTest.getMaxHP(),0.0);
    }

    @Test
    public void getCurrentHP() {
        hBarTest.update(entityTest,20);
        assertEquals(20,hBarTest.getCurrentHP(),0.0);
    }

    @Test
    public void getCurrentWidth() {
        assertEquals((int)(hBarTest.getCurrentHP()*14 / hBarTest.getMaxHP()),hBarTest.getCurrentWidth());
    }

}