package tests;

import com.a02.game.Attacker;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttackerTest {
    Attacker attackerTest = new Attacker(1,2,3,4,"test1.png", 5,"test",6,true,7,false,true,"testType",1.1f);
    @Test
    public void getAttackDamage() {
        assertEquals(1.1f,attackerTest.getAttackDamage());
    }


    @Test
    public void getAttackType() {
    }


    @Test
    public void buy() {
    }
}