package tests.component;

import com.a02.component.Map;
import com.a02.entity.GameObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapTest {

    private boolean[][] occGrid;
    private GameObject[][] entityGrid;
    private Vector2[][] coordGrid;
    private String sprite = "Test";
    private Texture texture;

    protected final int GRID_WIDTH = 16;
    protected final int GRID_HEIGHT = 18;
    Map mapTest = new Map(sprite);

    @Test
    public void getOccGrid() {
    }

    @Test
    public void getEntityGrid() {
    }

    @Test
    public void getSprite() {
        assertEquals("Test", mapTest.getSprite());
    }

    @Test
    public void getCoordGrid() {
    }

    @Test
    public void getTexture() {
    }
}