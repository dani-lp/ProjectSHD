package tests.component;

import com.a02.component.Inventory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryTest {

    Inventory inventoryTest = new Inventory("Inventory/fullInv.png");

    @Test
    public void getX() {
        assertEquals(256,inventoryTest.getX());
    }

    @Test
    public void getY() {
        assertEquals(0,inventoryTest.getY());
    }

    @Test
    public void getTexture() {
        String sprite = "inventory.png";
        Texture textureTest = new Texture(Gdx.files.internal(sprite));
        assertEquals(textureTest, inventoryTest.getTexture());
    }


    @Test
    public void getSprite() {
        assertEquals("inventory.png", inventoryTest.getSprite());
    }
}