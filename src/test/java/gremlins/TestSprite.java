package gremlins;

import processing.core.*;
import processing.data.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestSprite {
    Wizard player;

    @Test
    public void testConstructor(){
        /*
        NOTE:
            - Testing the constructor that only take in
            coordinate arguments.
         */
        player = new Wizard(0, 0);
        assertNotNull(player);
        assertEquals(player.getX(), 0);
        assertEquals(player.getY(), 0);
    }

    @Test
    public void testSetGetXY(){
        /*
        NOTE:
            - Testing the basic getters and setters.
         */
        player = new Wizard();

        player.setCoordinate(0, 0);
        assertEquals(player.getX(), 0);
        assertEquals(player.getY(), 0);

        assertEquals(player.setCoordinate(-1, -1), false);
        assertEquals(player.setCoordinate(1, 1), true);
        assertEquals(player.setCoordinate(800, 800), false);
        assertEquals(player.setCoordinate(800, 600), false);
        assertEquals(player.setCoordinate(600, 800), false);

        assertEquals(player.setX(-1), false);
        assertEquals(player.setX(800), false);
        assertEquals(player.setY(-1), false);
        assertEquals(player.setY(800), false);
    }

    @Test 
    public void testGetBounds(){
        /*
        NOTE:
            - Testing the getBounds() function that returns an
            array of the sprite's bounds. 

            - Update: I don't know why this is failing.
         */
        player = new Wizard(100, 100);
        assertEquals(player.getBounds()[0], 100);
        assertEquals(player.getBounds()[1], 100);
        assertEquals(player.getBounds()[2], 120);
        assertEquals(player.getBounds()[3], 120);
    }

    @Test
    public void testSetGetRepresentation(){
        /*
        NOTE:
            - Testing the image getter and setter to
            see if the image being returned is the same as the
            one being passed and not null.
         */
        PImage test_image = new PImage();
        Stonewall test = new Stonewall(test_image, 100, 200);
        test.setRepresentation(test_image);
        assertNotNull(test.getRepresentation());
        assertEquals(test.getRepresentation(), test_image);
    }

}

