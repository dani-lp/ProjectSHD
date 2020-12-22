package tests.entity;

import com.a02.entity.Enemy;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnemyTest {

    Enemy enemyTest = new Enemy(1, 2, 20, 30, 3, 4, 5, 6, 7, 8, "Wpath", "Apath", "Dpath");


    @Test
    public void getId() {
        assertEquals(3, enemyTest.getId());
    }

    @Test
    public void getHp() {
        assertEquals(4, enemyTest.getHp());
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
    public void getWalkpath() {
        assertEquals("Wpath", enemyTest.getWalkpath());
    }

    @Test
    public void getAttackpath() {
        assertEquals("Apath", enemyTest.getAttackpath());
    }

    @Test
    public void getDeathpath() {
        assertEquals("Dpath", enemyTest.getDeathpath());
    }

    @Test
    public void getStartTime() {
        assertEquals(7, enemyTest.getStartTime());
    }

}