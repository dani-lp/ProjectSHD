package tests.entity;

import com.a02.entity.Enemy;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnemyTest {

    Enemy enemyTest = new Enemy(2, 2, 20, 30, 1, 23, 5, 6, 7, 8);


    @Test
    public void getId() {
        assertEquals(1, enemyTest.getId());
    }

    @Test
    public void getHp() {
        assertEquals(23, enemyTest.getHp());
    }

    @Test
    public void getEffectTimer() {
    }

    @Test
    public void getAttackDamage() {
        assertEquals(5,enemyTest.getAttackDamage());
    }

    @Test
    public void getSpeed() {
        assertEquals(6, enemyTest.getSpeed(),0);
    }

    @Test
    public void getGoldValue() {
        assertEquals(8,enemyTest.getGoldValue());
    }

    @Test
    public void getStartTime() {
        assertEquals(7, enemyTest.getStartTime());
    }

}