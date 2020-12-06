package tests;

import com.a02.entity.Attacker;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttackerTest {

    Attacker attackerTest = new Attacker(1,2,3,4, 5,"electrico",6,true,7,"Electrico",1.1f);

    @Test
    public void getAttackDamage() {
        assertEquals(1.1f,attackerTest.getAttackDamage(), 0);
    }


    @Test
    public void getAttackType() {
    }


    @Test
    public void update() {
    }
}