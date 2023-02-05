package gremlins;

import processing.core.*;
import processing.data.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestPowerup{
    @Test 
    public void testConstructor1(){
        /*
        NOTE:
            - Only coordinates
         */
        Powerup test_1 = new Powerup(100, 100);
        test_1.getBounds();
        assertEquals(test_1.getX(), 100);
        assertEquals(test_1.getY(), 100);
        assertNull(test_1.getRepresentation());
    }
    
    @Test
    public void testConstructor2(){
        /*
        NOTE:
            - No arguments.
         */
        Powerup test_2 = new Powerup();
        test_2.getBounds();
        assertEquals(test_2.getX(), -1);
        assertEquals(test_2.getY(), -1);
        assertNull(test_2.getRepresentation());
    }

    @Test
    public void testConstructor3(){
        /*
        NOTE:
            - Full arguments.
         */
        PImage test = new PImage();
        Powerup test_3 = new Powerup(test, 100, 100);
        test_3.getBounds();
        assertEquals(test_3.getX(), 100);
        assertEquals(test_3.getY(), 100);
        assertNotNull(test_3.getRepresentation());
    }
}