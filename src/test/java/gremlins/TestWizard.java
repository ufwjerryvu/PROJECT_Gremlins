package gremlins;

import processing.core.*;
import processing.data.*;

import java.lang.reflect.*;
import java.util.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestWizard{
    @Test
    public void testConstructor(){
        /*
        NOTE:
            - This has been tested in a different branch.
         */
    }
    
    @Test
    public void runEmptyFunctions(){
        /*
        NOTE:
            - These functions are overloaded 
            and do not contain anything in them.
         */
        Wizard player = new Wizard();
        player.stop();
        player.move();
        player.shoot();
    }

    @Test 
    public void testSetGet(){
        /*
        NOTE:
            - The wizard class only has 1 setter and
            1 getter method. This is testing for them.
         */
        Wizard player = new Wizard();

        player.setRefresh(false);
        assertFalse(player.getRefresh());

        player.setRefresh(true);
        assertTrue(player.getRefresh());
    }

    @Test 
    public void testMoveNoConstraints(){
        try{
            /*
            NOTE:
                - Testing the wizard's move() function with
                no walls and the wizard can freely move.
             */
            Wizard test = new Wizard(40, 40);

            Field p_u_down = Wizard.class.getDeclaredField("unable_down");
            Field p_u_up = Wizard.class.getDeclaredField("unable_up");
            Field p_u_left = Wizard.class.getDeclaredField("unable_left");
            Field p_u_right = Wizard.class.getDeclaredField("unable_right");
            Field p_cd = Wizard.class.getDeclaredField("current_direction");

            p_u_down.setAccessible(true);
            p_u_up.setAccessible(true);
            p_u_left.setAccessible(true);
            p_u_right.setAccessible(true);
            p_cd.setAccessible(true);

            // LEFT
            test.move(37, 2);
            assertEquals(test.getX(), 38);
            assertEquals(test.getY(), 40);

            // UP
            test.move(38, 2);
            assertEquals(test.getX(), 38);
            assertEquals(test.getY(), 38);
            
            // RIGHT
            test.move(39, 2);
            assertEquals(test.getX(), 40);
            assertEquals(test.getY(), 38);

            // DOWN
            test.move(40, 2);
            assertEquals(test.getX(), 40);
            assertEquals(test.getY(), 40);

        }catch(NoSuchFieldException e){
            assert false;
        }catch(IllegalArgumentException e){
            assert false;
        }
    }

    @Test 
    public void testMoveWithConstraints(){
        try{
            /*
            NOTE:  
                - Testing the wizard with some walls.
             */
            Wizard test = new Wizard(40, 40);

            Field p_u_down = Wizard.class.getDeclaredField("unable_down");
            Field p_u_up = Wizard.class.getDeclaredField("unable_up");
            Field p_u_left = Wizard.class.getDeclaredField("unable_left");
            Field p_u_right = Wizard.class.getDeclaredField("unable_right");
            Field p_cd = Wizard.class.getDeclaredField("current_direction");

            p_u_down.setAccessible(true);
            p_u_up.setAccessible(true);
            p_u_left.setAccessible(true);
            p_u_right.setAccessible(true);
            p_cd.setAccessible(true);

            // Wall on the LEFT
            p_u_left.set(test, true);
            test.move(37, 2);
            assertEquals(test.getX(), 40);
            assertEquals(test.getY(), 40);

            // Wall ABOVE
            p_u_up.set(test, true);
            test.move(38, 2);
            assertEquals(test.getX(), 40);
            assertEquals(test.getY(), 40);

            // Wall on the RIGHT
            p_u_right.set(test, true);
            test.move(39, 2);
            assertEquals(test.getX(), 40);
            assertEquals(test.getY(), 40);

            // Wall BELOW
            p_u_down.set(test, true);
            test.move(40, 2);
            assertEquals(test.getX(), 40);
            assertEquals(test.getY(), 40);

            /*
            NOTE:
                - Testing with an invalid value.
             */
            test.move(50, 2);

        }catch(NoSuchFieldException e){
            assert false;
        }catch(IllegalArgumentException e){
            assert false;
        }catch(IllegalAccessException e){
            assert false;
        }
    }

    @Test 
    public void testCollidedDoorAndPowerup1(){
        /*
        NOTE:
            - Testing if the collision function
            is working as expected. In here,
            we're testing both the collision with
            the door and the powerup. 
                
            - Coordinate testing.
         */
        Wizard test = new Wizard();

        Door d = new Door(60, 60);
        Powerup pu = new Powerup(60, 60);

        test.setCoordinate(20, 40);
        assertFalse(test.collided(d));
        assertFalse(test.collided(pu));

        test.setCoordinate(65, 60);
        assertFalse(test.collided(d));
        assertFalse(test.collided(pu));

        test.setCoordinate(65, 65);
        assertFalse(test.collided(d));
        assertFalse(test.collided(pu));

        test.setCoordinate(60, 65);
        assertFalse(test.collided(d));
        assertFalse(test.collided(pu));

        test.setCoordinate(60, 55);
        assertTrue(test.collided(d));
        assertTrue(test.collided(pu));

    }

    @Test
    public void testCollidedDoorAndPowerup2(){
        /*
        NOTE:
            - Similarly, testing if the collision function
            is working as expected. But this is:

            - Border testing.
         */
        Wizard test = new Wizard();

        Door d = new Door(60, 60);
        Powerup pu = new Powerup(60, 60);

        test.setCoordinate(60, 60);
        assertTrue(test.collided(d));
        assertTrue(test.collided(pu));

        test.setCoordinate(45, 60);
        assertTrue(test.collided(d));
        assertTrue(test.collided(pu));

        test.setCoordinate(60, 55);
        assertTrue(test.collided(d));
        assertTrue(test.collided(pu));

        test.setCoordinate(60, 45);
        assertTrue(test.collided(d));
        assertTrue(test.collided(pu));

    }

    @Test
    public void testCollidedWallsNoConstraints(){
        /*
        NOTE:
            - Testing the collision of the wizard with 
            the walls. There should be no collision in this
            case because there are no walls.
         */
        Wizard test = new Wizard(40, 40);
        ArrayList<Sprite> walls = new ArrayList<Sprite>();

        assertFalse(test.collided(walls));
    }

    @Test
    public void testCollidedWallsLeftConstraint(){
        /*
        NOTE:
            - Testing the collision of the wizard with a
            single wall on the left.
         */
        Wizard test = new Wizard(40, 40);
        ArrayList<Sprite> walls = new ArrayList<Sprite>();
        walls.add(new Brickwall(20, 40));
        assertTrue(test.collided(walls));
    }

    @Test
    public void testCollidedWallsRightConstraint(){
        /*
        NOTE:
            - Testing the collision of the wizard with a 
            single wall on the right.
         */
        Wizard test = new Wizard(40, 40);
        ArrayList<Sprite> walls = new ArrayList<Sprite>();
        walls.add(new Brickwall(60, 40));
        assertTrue(test.collided(walls));
    }

    @Test
    public void testCollidedWallsUpConstraint(){
        /*
        NOTE:
            - Testing the collision of the wizard with a
            single wall above.
         */
        Wizard test = new Wizard(40, 40);
        ArrayList<Sprite> walls = new ArrayList<Sprite>();
        walls.add(new Brickwall(40, 20));
        assertTrue(test.collided(walls));
    }

    @Test
    public void testCollidedWallsDownConstraint(){
        /*
        NOTE:
            - Testing the collision of the wizard with a
            single wall below.
         */
        Wizard test = new Wizard(40, 40);
        ArrayList<Sprite> walls = new ArrayList<Sprite>();
        walls.add(new Brickwall(40, 60));
        assertTrue(test.collided(walls));
    }

    @Test
    public void testCollidedMultipleWalls(){
        /*
        NOTE:  
            - Testing the wizard's collision with multiple
            walls at a time.
         */
        Wizard test = new Wizard(40, 40);
        ArrayList<Sprite> walls = new ArrayList<Sprite>();

        walls.add(new Brickwall(20, 40));
        walls.add(new Brickwall(20, 60));
        walls.add(new Brickwall(60, 40));
        walls.add(new Brickwall(60,  20));
        
        assertTrue(test.collided(walls));
    }

    @Test 
    public void testCollidedRefresh(){
        /*
        NOTE:
            - The refresh() function basically disables the 
            return value for the collided() function, causing
            it to return false.
         */
        Wizard test = new Wizard(40, 40);
        ArrayList<Sprite> walls = new ArrayList<Sprite>();

        walls.add(new Brickwall(20, 40));
        test.setRefresh(true);

        assertFalse(test.collided(walls));
    }

    @Test 
    public void testShoot(){
        /*
        NOTE:
            - Testing if the shoot() function has 
            created an extra fireball.
         */
        Wizard test = new Wizard();
        ArrayList<Fireball> fireballs = new ArrayList<Fireball>();

        int size_before = fireballs.size();
        test.shoot(fireballs, new PImage());
        assertEquals(size_before + 1, fireballs.size());

    }
}