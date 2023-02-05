package gremlins;

import processing.core.*;
import processing.data.*;

import java.util.*;
import java.lang.reflect.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestSlime {
    @Test 
    public void testConstructor1(){
        /*
        NOTE: 
            - Testing the constructor that only accepts the 
            coordinates.
        */
        Slime test_1 = new Slime(100, 100);
        assertEquals(test_1.getX(), 100);
        assertEquals(test_1.getY(), 100);
        assertNull(test_1.getRepresentation());
    }
    
    @Test
    public void testConstructor2(){
        /*
        NOTE:
            - Testing the constructor that doesn't take in 
            any arguments.
         */
        Slime test_2 = new Slime();
        assertEquals(test_2.getX(), -1);
        assertEquals(test_2.getY(), -1);
        assertNull(test_2.getRepresentation());
    }

    @Test
    public void testConstructor3(){
        /*
        NOTE:
            - Testing for the constructor that takes in 
            all arguments
         */
        PImage test = new PImage();
        Slime test_3 = new Slime(test, 100, 100);
        assertEquals(test_3.getX(), 100);
        assertEquals(test_3.getY(), 100);
        assertNotNull(test_3.getRepresentation());
    }

    @Test
    public void testSetDirection(){ 
        /*
        NOTE:
            - We're testing the direction that the slime
            is traveling in. Reflections are used to test
            this because there are no getter functions.
         */
        try{
            Direction current = Direction.DOWN;
            Slime test = new Slime();
            test.setDirection(current);

            Field cd = Slime.class.getDeclaredField("current_direction");
            cd.setAccessible(true);
            Direction current_direction = (Direction)cd.get(test);

            assertEquals(current_direction, current);
        }catch(IllegalAccessException e){
            assert false;
        }catch(NoSuchFieldException e){
            assert false;
        }
    }

    @Test
    public void testMove(){
        /*
        NOTE:
            - Testing if the slime is moving correctly
            and is moving 4 pixels per frame.
         */
        Slime test = new Slime(40, 40);

        test.setDirection(Direction.NONE);
        test.move();
        assertTrue(test.getX() == 40 && test.getY() == 40);

        test.setCoordinate(40, 40);
        test.setDirection(Direction.UP);
        test.move();
        assertEquals(test.getX(), 40);
        assertEquals(test.getY(), 36);
        
        test.setCoordinate(40, 40);
        test.setDirection(Direction.DOWN);
        test.move();
        assertEquals(test.getX(), 40);
        assertEquals(test.getY(), 44);

        test.setCoordinate(40, 40);
        test.setDirection(Direction.LEFT);
        test.move();
        assertEquals(test.getX(), 36);
        assertEquals(test.getY(), 40);

        test.setCoordinate(40, 40);
        test.setDirection(Direction.RIGHT);
        test.move();
        assertEquals(test.getX(), 44);
        assertEquals(test.getY(), 40);
    }

    @Test
    public void testCollidedWalls(){
        /*
        NOTE:
            - Testing if the collided(walls) function works
            and returns correct values. We move the slime around
            the walls instead of moving the walls.
         */
        Slime test = new Slime(20, 40);
        ArrayList<Sprite> walls = new ArrayList<Sprite>();
        walls.add(new Brickwall(100, 200));

        assertFalse(test.collided(walls));

        walls.add(new Brickwall(20, 40));
        assertTrue(test.collided(walls));

        walls.add(new Brickwall(22, 41));
        assertTrue(test.collided(walls));

        walls.add(new Brickwall(19,39));
        assertTrue(test.collided(walls));

        walls.add(new Brickwall(40, 40));
        assertTrue(test.collided(walls));

        walls.add(new Brickwall(0, 40));
        assertTrue(test.collided(walls));

        walls.add(new Brickwall(20, 60));
        assertTrue(test.collided(walls));
    }

    @Test
    public void testCollidedPlayer(){
        /*
        NOTE:
            - Testing if the slime has correctly collided
            with the player.
         */
        Wizard player = new Wizard(20, 20);

        Slime test = new Slime(20, 20);
        assertTrue(test.collided(player));

        test.setCoordinate(0, 0);
        assertFalse(test.collided(player));

        test.setCoordinate(20, 40);
        assertFalse(test.collided(player));

        test.setCoordinate(40, 20);
        assertFalse(test.collided(player));

        test.setCoordinate(0, 20);
        assertFalse(test.collided(player));

        test.setCoordinate(20, 0);
        assertFalse(test.collided(player));
    }

}
