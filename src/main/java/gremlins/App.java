package gremlins;

import processing.core.*;
import processing.data.*;

import java.util.*;
import java.lang.*;
import java.io.*;

public class App extends PApplet {
    /*
    SECTION 1: DECLARATION AND INTIALIZATION OF CLASS ATTRIBUTES
        AND GAME PARAMETERS
    */
    public static final int WINDOW_WIDTH = 720;
    public static final int WINDOW_HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int BOTTOM_BAR = 60;

    public static final int FPS = 60;

    public String config_path;
    
    /*
    NOTE:  
        - These attributes are the image instances that are
        going to be representing the sprites.
    */
    public PImage stonewall, gremlin, slime, fireball, door, powerup;
    
    public PImage portal_available, portal_unavailable;

    public PImage brickwall_normal, brickwall_destroyed_0, brickwall_destroyed_1, 
        brickwall_destroyed_2, brickwall_destroyed_3;

    public PImage wizard_left, wizard_right, wizard_up, wizard_down;

    /*
    NOTE:
        - All the game display, logic, and sequences are implemented
        in the `Game` class and is going to be carried out by the only
        `game` instance.
    */
    public Game game;

    /*
    SECTION 2: CONSTRUCTOR(S)
    */
    public App() {
        this.config_path = "config.json";
    }

    /*
    SECTION 3: OVERLOADING/OVERRIDING PApplet METHODS
     */
    public void settings() {
        /*
        NOTE:
            - Specifying the size of the game window.
         */
        size(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public void setup() {

        frameRate(FPS);

        /*
        NOTE:
            - The hairy block below loads all the images of all the sprites 
            from their respective .png files.
        */
        this.stonewall = loadImage(this.getClass().getResource("stonewall.png").getPath().replace("%20", " "));
        this.gremlin = loadImage(this.getClass().getResource("gremlin.png").getPath().replace("%20", " "));
        this.slime = loadImage(this.getClass().getResource("slime.png").getPath().replace("%20", " "));
        this.fireball = loadImage(this.getClass().getResource("fireball.png").getPath().replace("%20", " "));
        this.door = loadImage(this.getClass().getResource("door.png").getPath().replace("%20", " "));
        this.powerup = loadImage(this.getClass().getResource("powerup.png").getPath().replace("%20", " "));

        this.portal_available = loadImage(this.getClass().getResource("portal_available.png").getPath().replace("%20", " "));
        this.portal_unavailable = loadImage(this.getClass().getResource("portal_unavailable.png").getPath().replace("%20", " "));
        PImage[] portal_status = {this.portal_available, this.portal_unavailable};

        this.brickwall_normal = loadImage(this.getClass().getResource("brickwall.png").getPath().replace("%20", " "));
        this.brickwall_destroyed_0 = loadImage(this.getClass().getResource("brickwall_destroyed0.png").getPath().replace("%20", " "));
        this.brickwall_destroyed_1 = loadImage(this.getClass().getResource("brickwall_destroyed1.png").getPath().replace("%20", " "));
        this.brickwall_destroyed_2 = loadImage(this.getClass().getResource("brickwall_destroyed2.png").getPath().replace("%20", " "));
        this.brickwall_destroyed_3 = loadImage(this.getClass().getResource("brickwall_destroyed3.png").getPath().replace("%20", " "));
        PImage[] brickwall_stages = {this.brickwall_normal, this.brickwall_destroyed_0, this.brickwall_destroyed_1, 
            this.brickwall_destroyed_2, this.brickwall_destroyed_3};

        this.wizard_left = loadImage(this.getClass().getResource("wizard0.png").getPath().replace("%20", " "));
        this.wizard_right = loadImage(this.getClass().getResource("wizard1.png").getPath().replace("%20", " "));
        this.wizard_up = loadImage(this.getClass().getResource("wizard2.png").getPath().replace("%20", " "));
        this.wizard_down = loadImage(this.getClass().getResource("wizard3.png").getPath().replace("%20", " "));
        PImage[] wizard_directions = {this.wizard_left, this.wizard_right, this.wizard_up, this.wizard_down};


        /*
        NOTE:
            - Reading in the configuration from the JSON config file. 

            - The function Game.loadMap() with all the parameters must be called
            before its overloaded version with no parameters because the Game
            class needs all of the intiliazed image representations.
        */
        JSONObject game_config = loadJSONObject(new File(this.config_path));

        game = new Game(game_config, this.stonewall, brickwall_stages, this.gremlin, wizard_directions, this.door,
            this.fireball, this.slime, this.powerup, portal_status);
        game.loadMap();
    }

    private boolean released;
    private int current_keycode;
    private boolean shoot;
    public void keyPressed(){
        /*
        NOTE:
            - This ensures simultaneity.
         */
        if(this.keyCode == ' '){
            this.shoot = true;
            return;
        }

        this.released = false;
        this.current_keycode = this.keyCode;

        //System.out.println("+ current: " + this.current_keycode + "; kc: " + this.keyCode + "; released: " + released);
    }

    public void keyReleased(){
        /*
        NOTE:
            - This implementation is paramount as it ensures the 
            smoothness of the character's movements. Without it, the
            operating system's keyboard delay would take over.
            
            - Do not remove.
         */
        if(this.current_keycode != this.keyCode && !this.released){
            this.released = false;
        }else{
            this.released = true;
        }

        if(this.keyCode == ' '){
            this.shoot = false;
        }

    }

    public void draw() {
        /*
        NOTE:
            - The code that is commented out below 
            tests the performance of the game logic
            and the draw function. The average is 
            1ms.
         */
        //long start_time = System.currentTimeMillis();

        background(0xCEA277);
        game.blit(this.current_keycode, this.released, this.shoot);
        game.draw(this);

        //System.out.println(System.currentTimeMillis()-start_time);
    }

    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
}
