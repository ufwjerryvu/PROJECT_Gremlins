package gremlins;

import processing.core.*;
import processing.data.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestDoor {
    @Test 
    public void testConstructor1(){
        /*
        NOTE:
            - Testing for the constructor that takes in
            the coordinates only.
         */
        Door test_1 = new Door(100, 100);
        assertEquals(test_1.getX(), 100);
        assertEquals(test_1.getY(), 100);
    }
    
    @Test
    public void testConstructor2(){
        /*
        NOTE:
            - Testing the constructor that doesn't take in 
            any arguments.
         */
        Door test_2 = new Door();
        assertEquals(test_2.getX(), -1);
        assertEquals(test_2.getY(), -1);
    }

    @Test
    public void testConstructor3(){
        /*
        NOTE:
            - Testing for the constructor that takes in 
            all arguments
         */
        PImage test = new PImage();
        Door test_3 = new Door(test, 100, 100);

        assertEquals(test_3.getX(), 100);
        assertEquals(test_3.getY(), 100);
        assertNotNull(test_3);
        
    }
}
