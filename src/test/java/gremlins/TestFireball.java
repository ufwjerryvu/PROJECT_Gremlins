package gremlins;

import processing.core.*;
import processing.data.*;

import java.util.*;
import java.lang.reflect.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestFireball {
    @Test 
    public void testConstructor1(){
        /*
        NOTE: 
            - Testing the constructor that only accepts the 
            coordinates.
         */
        Fireball test_1 = new Fireball(100, 100);
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
        Fireball test_2 = new Fireball();
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
        Fireball test_3 = new Fireball(test, 100, 100);
        assertEquals(test_3.getX(), 100);
        assertEquals(test_3.getY(), 100);
    }   

    @Test
    public void testSetDirection(){
        /*
        NOTE:
            - Testing the getter function but reflections
            are needed here because there aren't any getter
            methods.
         */
        Direction current = Direction.DOWN;
        Fireball test = new Fireball();
        test.setDirection(current);

        try{
            Field cd = Fireball.class.getDeclaredField("current_direction");

            cd.setAccessible(true);
            Direction sample = (Direction)cd.get(test);
            assertEquals(sample, current);

            test.setDirection(Direction.UP);
            sample = (Direction)cd.get(test);
            assertEquals(sample, Direction.UP);
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
            - Testing if the move() function actually
            moves the fireballs. The fireball moves
            4 pixels per frame.
         */
        Fireball test = new Fireball(60, 60);
        test.setDirection(Direction.UP);
        test.move();
        assertEquals(test.getX(), 60);
        assertEquals(test.getY(), 56);
        
        test.setDirection(Direction.DOWN);
        test.move();
        assertEquals(test.getX(), 60);
        assertEquals(test.getY(), 60);

        test.setDirection(Direction.LEFT);
        test.move();
        assertEquals(test.getX(), 56);
        assertEquals(test.getY(), 60);

        test.setDirection(Direction.RIGHT);
        test.move();
        assertEquals(test.getX(), 60);
        assertEquals(test.getY(), 60);

        test.setDirection(Direction.NONE);
        test.move();
        assertEquals(test.getX(), 60);
        assertEquals(test.getY(), 60);
    }

    @Test 
    public void testCollided(){
        /*
        NOTE: 
            - This tests whether the collided() function works
            as expected. It should return a false if the fireball
            hasn't collided with anything and a true if the fireball
            has.
         */
        ArrayList<Brickwall> br = new ArrayList<Brickwall>();
        ArrayList<Stonewall> sw = new ArrayList<Stonewall>();
        ArrayList<Gremlin> gr = new ArrayList<Gremlin>();
        ArrayList<Slime> sl = new ArrayList<Slime>();
       
        Fireball test = new Fireball();
        
        assertFalse(test.collided(br, sw, gr, sl));
    
        br.add(new Brickwall(20, 40));
        
        sw.add(new Stonewall(20, 40));

        gr.add(new Gremlin(20, 40));
        gr.add(new Gremlin(60, 60));

        sl.add(new Slime(20, 40));
        sl.add(new Slime(60, 60));

        for(int i = 0; i < 80; i++){
            for(int j = 0; j < 80; j++){
                test.setCoordinate(i, j);
                test.collided(br, sw, gr, sl);
            }
        }

        /* 
        NOTE:
            - Just moving the fireball around instead
            of moving the individual objects.
        */
        test.setCoordinate(20, 40);
        assertTrue(test.collided(br, sw, gr, sl));

        test.setCoordinate(0, 40);
        assertFalse(test.collided(br, sw, gr, sl));

        test.setCoordinate(0, 0);
        assertFalse(test.collided(br, sw, gr, sl));

        test.setCoordinate(40, 40);
        assertFalse(test.collided(br, sw, gr, sl));

        test.setCoordinate(20, 20);
        assertFalse(test.collided(br, sw, gr, sl));

        test.setCoordinate(20, 60);
        assertFalse(test.collided(br, sw, gr, sl));

    }
}
