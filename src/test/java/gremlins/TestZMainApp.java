package gremlins;

import processing.core.*;
import processing.data.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestZMainApp {
    @Test
    public void testSetup(){
        /*
        NOTE:
            - Checking if the App.setup() function runs and the images
            being loaded in are not null.
         */
        App test = new App();
        PApplet.runSketch(new String[] {"App"}, test);
        test.setup();
        test.delay(1000);

        assertNotNull(test.stonewall);
        assertNotNull(test.gremlin);
        assertNotNull(test.slime);
        assertNotNull(test.fireball);
        assertNotNull(test.door);
        assertNotNull(test.powerup);

        assertNotNull(test.brickwall_normal);
        assertNotNull(test.brickwall_destroyed_0);
        assertNotNull(test.brickwall_destroyed_1);
        assertNotNull(test.brickwall_destroyed_2);
        assertNotNull(test.brickwall_destroyed_3);

        assertNotNull(test.wizard_left);
        assertNotNull(test.wizard_right);
        assertNotNull(test.wizard_up);
        assertNotNull(test.wizard_down);

        assertNotNull(test.game);
    }
    
    @Test 
    public void testKeyPressed(){
        /*
        NOTE:
            - To be tested later.
         */
        App test = new App();
        PApplet.runSketch(new String[] {"App"}, test);
        test.delay(1000);
        test.keyPressed();
    }

    @Test
    public void testKeyReleased(){
        /*
        NOTE: 
            - To be tested later.
        */
        App test = new App();
        PApplet.runSketch(new String[]{"App"}, test);
        test.delay(1000);
        test.keyReleased();
    }
}
