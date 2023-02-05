package gremlins;

import processing.core.*;
import processing.data.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestBrickwall {
    @Test 
    public void testConstructor1(){
        /*
        NOTE:
            - Testing for the constructor that takes in 
            all arguments
         */
        PImage[] test = {new PImage(), new PImage()};
        Brickwall brickwall = new Brickwall(test, 20, 60);
        assertNotNull(brickwall);

        assertEquals(brickwall.getX(), 20);
        assertEquals(brickwall.getY(), 60);
    }

    @Test
    public void testConstructor2(){
        /*
        NOTE:
            - Testing for the constructor that takes in
            the coordinates only.
         */
        Brickwall brickwall = new Brickwall(20, 20);
        assertNotNull(brickwall);

        assertEquals(brickwall.getX(), 20);
        assertEquals(brickwall.getY(), 20);
        assertNull(brickwall.getRepresentation());
    }

    @Test
    public void testConstructor3(){
        /*
        NOTE:
            - Testing the constructor that doesn't take in 
            any arguments.
         */
        Brickwall brickwall = new Brickwall();
        assertNotNull(brickwall);

        assertEquals(brickwall.getX(), -1);
        assertEquals(brickwall.getY(), -1);
        assertNull(brickwall.getRepresentation());
    }

    @Test 
    public void testDestruction(){
        /*
        NOTE:
            - Testing for the destruction sequence.
         */
        PImage[] test = {new PImage(), new PImage(), new PImage()};
        Brickwall brickwall = new Brickwall(test, 20, 60);
        brickwall.initiateDestruction();
        assertEquals(brickwall.isBeingDestroyed(), true);
    }

    @Test
    public void testNextStage(){
        /*
        NOTE:
            - Testing if the destruction sequence takes place
            accordingly.
         */
        PImage[] test = {new PImage(), new PImage()};
        Brickwall brickwall = new Brickwall(test, 20, 60);
        
        assertEquals(brickwall.nextStage(), false);
        brickwall.initiateDestruction();
        assertEquals(brickwall.nextStage(), false);
        assertEquals(brickwall.nextStage(), false);
        assertEquals(brickwall.nextStage(), false);
    }
}

