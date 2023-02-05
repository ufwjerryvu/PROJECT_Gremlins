package gremlins;

import processing.core.*;
import processing.data.*;

import java.lang.reflect.*;
import java.util.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestGremlin {
    @Test
    public void testConstructor(){
        /*
        NOTE:
            - Testing the constuctor that takes in no arguments.
            The other constructors aren't tested because they
            have already been used in other test caes.
         */
        Gremlin test = new Gremlin();
        assertEquals(test.getX(), -1);
        assertEquals(test.getY(), -1);
    }

    @Test
    public void testCorrespondingDirection(){
        /*
        NOTE:
            - Using reflections to test because this is a private function.
            This tests whether the corresponding directions agree with 
            the integers being passed.
         */
        try{
            Gremlin test = new Gremlin();

            Method method = Gremlin.class.getDeclaredMethod("correspondingDirection", int.class);
            method.setAccessible(true);
            
            Direction current = (Direction)method.invoke(test, 0);
            assertEquals(current, Direction.LEFT);

            current = (Direction)method.invoke(test, 1);
            assertEquals(current, Direction.RIGHT);

            current = (Direction)method.invoke(test, 2);
            assertEquals(current, Direction.UP);

            current = (Direction)method.invoke(test, 3);
            assertEquals(current, Direction.DOWN);

            current = (Direction)method.invoke(test, -1295);
            assertEquals(current, Direction.NONE);


        }catch(IllegalAccessException e){
            assert false;
        }catch(InvocationTargetException e){
            assert false;
        }catch(NoSuchMethodException e){
            assert false;
        }

    }

    
    @Test 
    public void testMoveA(){
        /*
        NOTE:
            - Again, using reflections to test but
            this time we test private variables instead
            of testing for functions.

            - Here we test if the gremlins are moving 
            in the direction they are supposed to given there
            are no constraints. Coordinates are checked to see
            if the the position of the gremlin equals the 
            direction that the gremlin is moving in after the
            function is called.
         */
        Gremlin test = new Gremlin(40, 40);

        try{
            Field p_u_down = Gremlin.class.getDeclaredField("unable_down");
            Field p_u_up = Gremlin.class.getDeclaredField("unable_up");
            Field p_u_left = Gremlin.class.getDeclaredField("unable_left");
            Field p_u_right = Gremlin.class.getDeclaredField("unable_right");
            Field p_cd = Gremlin.class.getDeclaredField("current_direction");

            p_u_down.setAccessible(true);
            p_u_up.setAccessible(true);
            p_u_left.setAccessible(true);
            p_u_right.setAccessible(true);
            p_cd.setAccessible(true);

            p_cd.set(test, Direction.NONE);
            test.move(1);
            assertTrue(test.getX() == 41 || test.getX() == 40 || test.getX() == 39);
            assertTrue(test.getY() == 41 || test.getY() == 40 || test.getY() == 39);
            
            p_u_down.set(test, true);
            p_cd.set(test, Direction.DOWN);
            test.setCoordinate(40, 40);
            test.move(1);
            assertTrue(test.getX() == 41 || test.getX() == 40 || test.getX() == 39);
            assertTrue(test.getY() == 41 || test.getY() == 40 || test.getY() == 39);
    
            p_u_up.set(test, true);
            p_cd.set(test, Direction.UP);
            test.move(1);
            test.setCoordinate(40, 40);
            test.move(1);
            assertTrue(test.getX() == 41 || test.getX() == 40 || test.getX() == 39);
            assertTrue(test.getY() == 41 || test.getY() == 40 || test.getY() == 39);
    
            p_u_left.set(test, true);
            p_cd.set(test, Direction.LEFT);
            test.move(1);
            
            /*
            NOTE:
                - Resetting to the initial coordinates
             */
            test.setCoordinate(40, 40);
            test.move(1);
            assertTrue(test.getX() == 41 || test.getX() == 40 || test.getX() == 39);
            assertTrue(test.getY() == 41 || test.getY() == 40 || test.getY() == 39);
    
            p_u_right.set(test, true);
            p_cd.set(test, Direction.RIGHT);
            test.move(1);
            test.setCoordinate(40, 40);
            test.move(1);
            assertTrue(test.getX() == 41 || test.getX() == 40 || test.getX() == 39);
            assertTrue(test.getY() == 41 || test.getY() == 40 || test.getY() == 39);

        }catch(NoSuchFieldException e){
            assert false;
        }catch(IllegalAccessException e){
            assert false;
        }catch(IllegalArgumentException e){
            assert false;
        }
    }
    
    @Test 
    public void testMoveB1(){
        /*
        NOTE:
            - Reflections, again.

            - Testing the movement of the gremlins with walls
            obstructing in all three directions and there's only one way out.
         */
        try{
            Gremlin test = new Gremlin(100, 100);

            Field p_u_down = Gremlin.class.getDeclaredField("unable_down");
            Field p_u_up = Gremlin.class.getDeclaredField("unable_up");
            Field p_u_left = Gremlin.class.getDeclaredField("unable_left");
            Field p_u_right = Gremlin.class.getDeclaredField("unable_right");
            Field p_cd = Gremlin.class.getDeclaredField("current_direction");

            p_u_down.setAccessible(true);
            p_u_up.setAccessible(true);
            p_u_left.setAccessible(true);
            p_u_right.setAccessible(true);
            p_cd.setAccessible(true);
            
            /*
            CASE 1: Only horizontal available.
             */
            p_u_down.set(test, true);
            p_u_up.set(test, true);
            p_u_left.set(test, true);
            p_u_right.set(test, false);
            p_cd.set(test, Direction.LEFT);

            test.move(1);
            assertEquals(test.getX(), 100);
            assertEquals(test.getY(), 100);

            /*
            CASE 2: Only vertical available
             */
            p_u_down.set(test, true);
            p_u_up.set(test, false);
            p_u_left.set(test, true);
            p_u_right.set(test, true);
            p_cd.set(test, Direction.RIGHT);
            test.move(1);
            assertEquals(test.getX(), 100);
            assertTrue(test.getY() >= 99 && test.getY() <= 101);

            p_cd.set(test, Direction.UP);
            test.move(1);

            /*
            CASE 3: All available directions.
             */
            test.setCoordinate(100, 100);
            p_u_down.set(test, false);
            p_u_up.set(test, false);
            p_u_left.set(test, false);
            p_u_right.set(test, false);

            p_cd.set(test, Direction.LEFT);
            test.move(1);
            assertEquals(test.getX(), 99);
            assertEquals(test.getY(), 100);

            p_cd.set(test, Direction.RIGHT);
            test.move(1);
            assertEquals(test.getX(), 100);
            assertEquals(test.getY(), 100);

            p_cd.set(test, Direction.UP);
            test.move(1);
            assertEquals(test.getX(), 100);
            assertEquals(test.getY(), 99);

            p_cd.set(test, Direction.DOWN);
            test.move(1);
            assertEquals(test.getX(), 100);
            assertEquals(test.getY(), 100);

        }catch(NoSuchFieldException e){
            assert false;
        }catch(IllegalAccessException e){
            assert false;
        }catch(IllegalArgumentException e){
            assert false;
        }
    }

    @Test 
    public void testMoveB2(){
        try{
            /*
            NOTE:
                - Testing with only one wall obstructing the
                gremlin in the direction of its travel.
             */
            Gremlin test = new Gremlin(100, 100);

            Field p_u_down = Gremlin.class.getDeclaredField("unable_down");
            Field p_u_up = Gremlin.class.getDeclaredField("unable_up");
            Field p_u_left = Gremlin.class.getDeclaredField("unable_left");
            Field p_u_right = Gremlin.class.getDeclaredField("unable_right");
            Field p_cd = Gremlin.class.getDeclaredField("current_direction");

            p_u_down.setAccessible(true);
            p_u_up.setAccessible(true);
            p_u_left.setAccessible(true);
            p_u_right.setAccessible(true);
            p_cd.setAccessible(true);
        
            p_cd.set(test, Direction.LEFT);
            p_u_left.set(test, true);
            test.move(1);
            assertTrue(test.getX() >= 99 && test.getX() <= 101);
            assertTrue(test.getY() >= 99 && test.getY() <= 101);

        }catch(NoSuchFieldException e){
            assert false;
        }catch(IllegalAccessException e){
            assert false;
        }catch(IllegalArgumentException e){
            assert false;
        }
    }

    @Test 
    public void testMoveB3(){
        try{
            /*
            NOTE:
                - Testing with any two walls obstructing the
                gremlin in the direction of its travel and adjacent to it.
             */
            Gremlin test = new Gremlin(100, 100);

            Field p_u_down = Gremlin.class.getDeclaredField("unable_down");
            Field p_u_up = Gremlin.class.getDeclaredField("unable_up");
            Field p_u_left = Gremlin.class.getDeclaredField("unable_left");
            Field p_u_right = Gremlin.class.getDeclaredField("unable_right");
            Field p_cd = Gremlin.class.getDeclaredField("current_direction");

            p_u_down.setAccessible(true);
            p_u_up.setAccessible(true);
            p_u_left.setAccessible(true);
            p_u_right.setAccessible(true);
            p_cd.setAccessible(true);
        
            p_cd.set(test, Direction.UP);
            p_u_up.set(test, true);
            p_u_left.set(test, true);
            p_u_right.set(test, true);

            test.move(1);
            assertTrue(test.getX() >= 99 && test.getX() <= 101);
            assertTrue(test.getY() >= 99 && test.getY() <= 101);

        }catch(NoSuchFieldException e){
            assert false;
        }catch(IllegalAccessException e){
            assert false;
        }catch(IllegalArgumentException e){
            assert false;
        }
    }

    @Test 
    public void testMoveB4(){
        try{
            /*
            NOTE:
                - Testing with any wall obstructing in the direction of travel.
                This is an extension of the B2 testing.
             */
            Gremlin test = new Gremlin(100, 100);

            Field p_u_down = Gremlin.class.getDeclaredField("unable_down");
            Field p_u_up = Gremlin.class.getDeclaredField("unable_up");
            Field p_u_left = Gremlin.class.getDeclaredField("unable_left");
            Field p_u_right = Gremlin.class.getDeclaredField("unable_right");
            Field p_cd = Gremlin.class.getDeclaredField("current_direction");

            p_u_down.setAccessible(true);
            p_u_up.setAccessible(true);
            p_u_left.setAccessible(true);
            p_u_right.setAccessible(true);
            p_cd.setAccessible(true);
        
            p_cd.set(test, Direction.LEFT);
            p_u_left.set(test, true);
            assertEquals(test.getX(), 100);
            assertTrue(test.getY() >= 99 && test.getY() <= 101);

            p_u_up.set(test, false);
            test.move(1);
            assertEquals(test.getX(), 100);
            assertTrue(test.getY() >= 98 && test.getY() <= 101);
            
            p_u_up.set(test, true);
            p_u_down.set(test, true);
            test.move(1);
            assertTrue(test.getX() >= 99 || test.getX() <= 101);
            assertTrue(test.getY() >= 99 && test.getY() <= 101);

        }catch(NoSuchFieldException e){
            assert false;
        }catch(IllegalAccessException e){
            assert false;
        }catch(IllegalArgumentException e){
            assert false;
        }
    }
    
    @Test 
    public void runEmptyFunctions(){
        /*
        NOTE:   
            - These functions are overloaded 
            so they're empty.
         */
        Gremlin test = new Gremlin();
        test.shoot();
        test.move();
        test.stop();
    }
    
    @Test
    public void testCollidedWallsExtensive(){
        Gremlin test= new Gremlin(20, 40);
        test.move(1);

        ArrayList<Sprite> walls = new ArrayList<Sprite>();
        walls.add(new Brickwall(0, 40));
        test.move(0);
        assertTrue(test.collided(walls));

        walls.add(new Brickwall(40, 40));
        walls.remove(0);
        test.move(1);
        assertTrue(test.collided(walls));

        walls.add(new Brickwall(20, 20));
        walls.remove(0);
        test.move(1);
        assertTrue(test.collided(walls));

        /*
        NOTE:
            - No nearby walls.
         */
        walls.add(new Brickwall(80, 80));
        walls.remove(0);
        test.move(1);
        assertFalse(test.collided(walls));
    }

    @Test
    public void testCollidedWalls(){
        /*
        NOTE:
            - Testing if the collided(walls) function returns the
            correct boolean variable if the gremlin
            has collided with any wall.
         */
        Gremlin test = new Gremlin(20, 40);
        ArrayList<Sprite> walls = new ArrayList<Sprite>();
        test.collided(walls);
        walls.add(new Brickwall(100, 200));

        assertFalse(test.collided(walls));

        walls.add(new Brickwall(20, 40));
        assertFalse(test.collided(walls));

        walls.add(new Brickwall(22, 41));
        assertFalse(test.collided(walls));

        walls.add(new Brickwall(19,39));
        assertFalse(test.collided(walls));

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
            - Testing if the collided(player) function
            returns the correct boolean variable. This is
            important because the boolean variable is 
            used to check if the player has lost a life in
            the Game class.
         */
        Wizard player = new Wizard(20, 20);

        Gremlin test = new Gremlin(20, 20);
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
    @Test
    public void testGetSet(){
        /*
        NOTE:
            - Testing if the setter and getter functions
            are working as expected.
         */
        Gremlin test = new Gremlin();
        test.setCooldownCounter(10);
        test.setHasShot(true);

        assertTrue(test.getHasShot());
        assertEquals(test.getCooldownCounter(), 10);
    }

    @Test
    public void testShoot(){
        /*
        NOTE:
            - Testing the shoot function and making sure
            it produces a new slime object. This is obvious 
            in the fact that the array list for slimes 
            would have an extra element.
         */
        Gremlin test = new Gremlin();
        ArrayList<Slime> slimes = new ArrayList<Slime>();
        int before = slimes.size();
        test.shoot(slimes, new PImage());
        assertEquals(before + 1, slimes.size());
    }

}
