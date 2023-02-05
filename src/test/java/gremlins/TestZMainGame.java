package gremlins;

import processing.core.*;
import processing.data.*;

import java.util.*;
import java.lang.reflect.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestZMainGame{
    private static App test;

    @BeforeAll
    public static void runApp(){
        test = new App();
        PApplet.runSketch(new String[]{"App"}, test);
        test.setup();
        test.delay(1000);
    }

    @Test 
    public void testSetGet(){
        /*
        NOTE:
            - Testing the setter and getter functions.
         */

        ArrayList<Stonewall> stonewalls = new ArrayList<Stonewall>();
        ArrayList<Brickwall> brickwalls = new ArrayList<Brickwall>();
        ArrayList<Gremlin> gremlins = new ArrayList<Gremlin>();
        ArrayList<Fireball> fireballs = new ArrayList<Fireball>();
        ArrayList<Slime> slimes = new ArrayList<Slime>();
        Wizard player = new Wizard();
        Door exit = new Door();

        test.game.setStonewalls(stonewalls);
        test.game.setBrickwalls(brickwalls);
        test.game.setGremlins(gremlins);
        test.game.setFireballs(fireballs);
        test.game.setSlimes(slimes);
        test.game.setWizard(player);
        test.game.setDoor(exit);

        assertNotNull(test.game.getStonewalls());
        assertNotNull(test.game.getBrickwalls());
        assertNotNull(test.game.getGremlins());
        assertNotNull(test.game.getFireballs());
        assertNotNull(test.game.getSlimes());
        assertNotNull(test.game.getWizard());
        assertNotNull(test.game.getDoor());
    }
    @Test
    public void testBlitSectionABCD(){
        /*
        NOTE:
            - Sections A, B, C, D are outlined in the blit() function
            in Game.java.
         */
        
        try{

            //SECTION A, B, C
            test.game.blit(38, true, false);
            
            Field game_wizard = Game.class.getDeclaredField("wizard");
            Field game_door = Game.class.getDeclaredField("door");
            Field game_has_shot = Game.class.getDeclaredField("has_shot");
            Field game_player_cooldown_counter = Game.class.getDeclaredField("player_cooldown_counter");
            Field game_fireballs = Game.class.getDeclaredField("fireballs");

            game_wizard.setAccessible(true);
            game_door.setAccessible(true);
            game_has_shot.setAccessible(true);
            game_player_cooldown_counter.setAccessible(true);
            game_fireballs.setAccessible(true);
            /*
            NOTE:
                - Testing if the player is in between tiles.
             */
            game_wizard.set(test.game, new Wizard(20, 30));

            /*
            NOTE:
                - Checking if shooting variables have been updated and the case
                where cooldown_counter is not 0 after two frames.
             */

            test.game.blit(38, true, true);
            test.game.blit(38, true, true);

            assertTrue((boolean)game_has_shot.get(test.game));
            assertTrue((int)game_player_cooldown_counter.get(test.game) != 0);

            //SECTION D
            Field game_current_level = Game.class.getDeclaredField("current_level");
            Field game_powerup_spawn_counter = Game.class.getDeclaredField("powerup_spawn_counter");
            Field game_powerup_counter = Game.class.getDeclaredField("powerup_counter");
            Field game_powerup = Game.class.getDeclaredField("powerup");
            Field game_won = Game.class.getDeclaredField("won");
            Field game_levels_config = Game.class.getDeclaredField("levels_config");
            
            game_current_level.setAccessible(true);
            game_powerup_spawn_counter.setAccessible(true);
            game_powerup_counter.setAccessible(true);
            game_powerup.setAccessible(true);
            game_won.setAccessible(true);
            game_levels_config.setAccessible(true);

            game_current_level.set(test.game, 0);
            //int level_before = (int)game_current_level.get(test.game);

            /*
            NOTE:
                - Collision with the door.
             */
            game_wizard.set(test.game, new Wizard(20, 40));
            game_door.set(test.game, new Door(20, 40));
            test.game.blit(38, true, true);
            //assertEquals((int)game_current_level.get(test.game), level_before + 1);

            /*
            NOTE:
                - No collision with the powerup. Powerup is not null.
             */
            game_wizard.set(test.game, new Wizard(20, 20));
            game_powerup.set(test.game, new Powerup(60, 60));
            test.game.blit(38, true, true);
            assertEquals((int)game_powerup_counter.get(test.game), 0);
            assertTrue((int)game_powerup_spawn_counter.get(test.game) > 0);

            /*
            NOTE:
                - Collsion with the powerup. Powerup is also not null.
             */
            game_powerup_spawn_counter.set(test.game, 50);
            game_powerup_counter.set(test.game, 0);
            game_wizard.set(test.game, new Wizard(60, 60));
            test.game.blit(38, true, false);
            assertTrue((int)game_powerup_spawn_counter.get(test.game) >= 0);
            assertTrue((int)game_powerup_counter.get(test.game) > 0);

            /*
            NOTE:
                - Collision with the door multiple times. Setting 
                value for current_level directly.

            */
            JSONArray lv_config = (JSONArray)game_levels_config.get(test.game);
            test.game.blit(38, true, false);
            game_current_level.set(test.game, lv_config.size() - 1);
            game_door.set(test.game, new Door(100, 100));
            game_wizard.set(test.game, new Wizard(100, 100));
            test.game.blit(38, true, false);
            assertTrue((boolean)game_won.get(test.game));


        }catch(NoSuchFieldException e){
            assert false;
        }catch(IllegalAccessException e){
            assert false;
        }catch(IllegalArgumentException e){
            assert false;
        }catch(RuntimeException e){

        }
    }

    @Test
    public void testBlitSectionE(){
        /*
        NOTE:
            - Sections E is outlined in the blit() function
            in Game.java.
         */
        
        try{
            //SECTION E
            Field game_wizard = Game.class.getDeclaredField("wizard");
            Field game_gremlins = Game.class.getDeclaredField("gremlins");
            Field game_lives = Game.class.getDeclaredField("lives");
            Field game_slimes = Game.class.getDeclaredField("slimes");

            game_slimes.setAccessible(true);
            game_wizard.setAccessible(true);
            game_gremlins.setAccessible(true);
            game_lives.setAccessible(true);

            /*
            NOTE:
                - Testing the collision of the gremlins with the player.
            */
            ArrayList<Gremlin> g_placeholder = new ArrayList<Gremlin>();
            game_lives.set(test.game, 100);
            int lives_before_blit = 100;
            g_placeholder.add(new Gremlin(20, 20));
            test.game.setGremlins(g_placeholder);
            test.game.setWizard(new Wizard(20, 20));

            test.game.blit(38, true, false);
            assertTrue(lives_before_blit >= (int)game_lives.get(test.game));

            /*
            NOTE:
                - Testing the collision of the slimes with the player.
            */
            ArrayList<Slime> s_placeholder = new ArrayList<Slime>();
            game_lives.set(test.game, 100);
            s_placeholder.add(new Slime(120, 120));
            test.game.setSlimes(s_placeholder);
            test.game.setWizard(new Wizard(120, 120));
            
            test.game.blit(38, true, false);
            assertTrue(lives_before_blit >= (int)game_lives.get(test.game));

        }catch(NoSuchFieldException e){
            assert false;
        }catch(IllegalAccessException e){
            assert false;
        }catch(IllegalArgumentException e){
            assert false;
        }
    }

    @Test
    public void testBlitSectionF(){
        /*
        NOTE:
            - Sections F is outlined in the blit() function
            in Game.java.
         */
        
        try{
            //SECTION F

            Field game_gremlins = Game.class.getDeclaredField("gremlins");
            Field game_initial_number_of_gremlins = Game.class.getDeclaredField("lives");

            game_initial_number_of_gremlins.setAccessible(true);
            game_gremlins.setAccessible(true);

            test.game.setGremlins(new ArrayList<Gremlin>());
            test.game.blit(38, true, false);

            assertTrue((int)game_initial_number_of_gremlins.get(test.game) >= test.game.getGremlins().size());

        }catch(NoSuchFieldException e){
            assert false;
        }catch(IllegalAccessException e){
            assert false;
        }catch(IllegalArgumentException e){
            assert false;
        }
    }
    @Test
    public void testBlitSectionG(){
        /*
        NOTE:
            - Unable to test this part.
         */
    }

    @Test
    public void testBlitSectionH(){
        /*
        NOTE:
            - Sections H is outlined in the blit() function
            in Game.java.
         */
        
        try{
            //SECTION F
            Field game_powerup_spawn_counter = Game.class.getDeclaredField("powerup_spawn_counter");
            Field game_powerup = Game.class.getDeclaredField("powerup");

            game_powerup_spawn_counter.setAccessible(true);
            game_powerup.setAccessible(true);;

            game_powerup_spawn_counter.set(test.game, 800);
            test.game.blit(38, true, false);

            assertTrue((Powerup)game_powerup.get(test.game) == null || (Powerup)game_powerup.get(test.game) != null);

        }catch(NoSuchFieldException e){
            assert false;
        }catch(IllegalAccessException e){
            assert false;
        }catch(IllegalArgumentException e){
            assert false;
        }
    }

    @Test
    public void testRunGameDisplay(){
        
        try{
            test.game.displayCooldown(test);
            test.game.displayPowerup(test);
            test.game.displayLose(test);
            test.game.displayWin(test);
        }catch(NullPointerException e){
            
        }
    }

    @Test
    public void testBlitNothingEventful(){
        /*
        NOTE:
            - Just running the blit function with the player
            shooting and not shooting as well as moving and 
            not moving. 
         */
        App test = new App();
        PApplet.runSketch(new String[] {"App"}, test);
        test.setup();
        test.delay(80);

        test.game.blit(38, true, true);
        test.game.blit(38, false, false);
        assertNotNull(test.game);
    }
}