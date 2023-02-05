package gremlins;

import processing.core.*;
import processing.data.*;

import java.io.*;
import java.util.*;
import java.lang.*;

public class Game{
    /*
    SECTION 1: DECLARATION AND INITIALIZATION OF CLASS ATTRIBUTES
        AND GAME PARAMETERS.
    */
    JSONObject game_config;
    JSONArray levels_config;

    private ArrayList<Stonewall> stonewalls;
    private ArrayList<Brickwall> brickwalls;
    private ArrayList<Gremlin> gremlins;
    private ArrayList<Fireball> fireballs;
    private ArrayList<Slime> slimes;
    private ArrayList<Teleportal> portals;
    private Wizard wizard;
    private Door door;
    private ArrayList<Sprite> walls;
    private Powerup powerup;

    private PImage rep_stonewall;
    private PImage[] rep_brickwall_stages;
    private PImage rep_gremlin;
    private PImage rep_fireball;
    private PImage rep_slime;
    private PImage[] rep_wizard_directions;
    private PImage rep_door;
    private PImage rep_powerup;
    private PImage[] rep_portal_status;

    private int current_level;

    private int current_key_pressed;

    private int player_speed;
    private int gremlin_speed;

    private int player_cooldown_counter = 0;
    private int player_cooldown_frames;

    private int powerup_spawn_counter;
    private int powerup_counter;
    private int powerup_duration;

    private int portal_effect_counter = 0;

    private int gremlin_cooldown_frames;
    private int initial_number_of_gremlins;

    private int lives;

    private boolean won = false;

    private int end_screen_counter = 0;

    /*
    SECTION 2: CONSTRUCTOR
    */
    public Game(JSONObject game_config, PImage stonewall, PImage[] brickwall_stages, PImage gremlin, PImage[] wizard_directions, 
     PImage door, PImage fireball, PImage slime, PImage powerup, PImage[] portal_status){
        /*
        NOTE:
            - This is the only constructor because we want to make sure 
            all the sprite's representations are set up properly in one go.
         */
        this.stonewalls = new ArrayList<Stonewall>();
        this.brickwalls = new ArrayList<Brickwall>();
        this.gremlins = new ArrayList<Gremlin>();
        this.fireballs = new ArrayList<Fireball>();
        this.slimes = new ArrayList<Slime>();
        this.portals = new ArrayList<Teleportal>();
        this.wizard = new Wizard();
        this.door = new Door();
        /*
        NOTE:
            - Assigning the configuration as an attribute of the Game class
            so we don't have to pass it in again later.
         */
        this.game_config = game_config;
        this.levels_config = game_config.getJSONArray("levels");
        this.lives = game_config.getInt("lives");

        this.current_level = 0;

        /*
        NOTE:
            - Similarly, we don't want to pass the values
            in again and again.
         */
        this.rep_stonewall = stonewall;
        this.rep_brickwall_stages = brickwall_stages;
        this.rep_gremlin = gremlin;
        this.rep_fireball = fireball;
        this.rep_slime = slime;
        this.rep_wizard_directions = wizard_directions;
        this.rep_door = door;
        this.rep_powerup = powerup;
        this.rep_portal_status = portal_status;
    }
    
    /* 
    SECTION 3: SETTER AND GETTER FUNCTIONS
    */
    public void setStonewalls(ArrayList<Stonewall> stonewalls){ this.stonewalls = stonewalls;}
    public void setBrickwalls(ArrayList<Brickwall> brickwalls){ this.brickwalls = brickwalls;}
    public void setGremlins(ArrayList<Gremlin> gremlins){ this.gremlins = gremlins;}
    public void setFireballs(ArrayList<Fireball> fireballs){ this.fireballs = fireballs;}
    public void setSlimes(ArrayList<Slime> slimes){ this.slimes = slimes;}
    public void setPortals(ArrayList<Teleportal> portals){ this.portals = portals;}
    public void setWizard(Wizard wizard){ this.wizard = wizard;}
    public void setDoor(Door exit){ this.door = exit;}
    public void setPowerup(Powerup powerup){ this.powerup = powerup;}

    public ArrayList<Stonewall> getStonewalls(){return this.stonewalls;}
    public ArrayList<Brickwall> getBrickwalls(){ return this.brickwalls; }
    public ArrayList<Gremlin> getGremlins(){ return this.gremlins;}
    public ArrayList<Fireball> getFireballs(){ return this.fireballs;}
    public ArrayList<Slime> getSlimes(){ return this.slimes;}
    public ArrayList<Teleportal> getPortals(){ return this.portals;}
    public Wizard getWizard(){ return this.wizard;}
    public Door getDoor(){ return this.door;}
    public Powerup getPowerup(){ return this.powerup;}

    /* 
    SECTION 4: OTHER METHODS
    */
    public void loadMap(){
        /*
        NOTE:
            - This reads in the map text file using JSON.
         */
        MapParsing file_in = new MapParsing(this.levels_config.getJSONObject(this.current_level).get("layout").toString());

        /*
        NOTE:
            - Reading in the maps from the layout text files via the JSON config
            and setting up all necessary assets for the level.
        */
        this.setStonewalls(file_in.parseStonewalls(this.rep_stonewall));
        this.setBrickwalls(file_in.parseBrickwalls(this.rep_brickwall_stages));
        this.setGremlins(file_in.parseGremlins(this.rep_gremlin));
        this.setWizard(file_in.parseWizard(this.rep_wizard_directions));
        this.setDoor(file_in.parseDoor(this.rep_door));

        /*
        NOTE:
            - Making sure no projectiles remain from the previous 
            load and everything that needs to be reset gets reset.
         */
        this.fireballs = new ArrayList<Fireball>();
        this.portals = new ArrayList<Teleportal>();
        this.slimes = new ArrayList<Slime>();
        this.powerup = null;

        this.player_cooldown_frames = (int)(App.FPS * this.levels_config.getJSONObject(this.current_level).getFloat("wizard_cooldown"));
        this.gremlin_cooldown_frames = (int)(App.FPS * this.levels_config.getJSONObject(this.current_level).getFloat("enemy_cooldown"));
    
        this.initial_number_of_gremlins = this.gremlins.size();

        this.powerup_spawn_counter = 0;
        this.powerup_counter = 0;
        this.powerup_duration = 7;

        this.portal_effect_counter = 0;

        this.gremlin_speed = 1;
        this.player_speed = 2;

        /* 
        NOTE:
            - Setting up the portals that can teleport the player and making sure it doesn't
            clash with any other object in the game. The number of portals is 
            set to 4 by default. Also, the distance between the portals is at least 4 tiles.
        */
        ArrayList<Sprite> all = new ArrayList<Sprite>();

        all.addAll(this.stonewalls);
        all.addAll(this.brickwalls);
        all.addAll(this.gremlins);
        all.add(this.wizard);
        all.add(this.door);

        int number_of_portals = 4;
        for(int i = 0; i < number_of_portals; ){
            Random rand = new Random();
            boolean can_set_portal = true;

            int portal_x = rand.nextInt(35) * App.SPRITESIZE;
            int portal_y = rand.nextInt(32) * App.SPRITESIZE;

            /*
            NOTE:
                - We don't want the portals to be set in the same coordinate as the walls.
             */
            for(int j = 0; j < all.size(); j++){
                if(portal_x == all.get(j).getX() && portal_y == all.get(j).getY()){
                    can_set_portal = false;
                    break;
                }
            }

            /*
            NOTE:
                - And we also don't want the portals to be too close to each other.
             */
            int minimum_distance = 4;
            for(int j = 0; j < this.portals.size(); j++){
                if((this.portals.get(j).getX() <= (portal_x + minimum_distance * App.SPRITESIZE)
                    && this.portals.get(j).getX() >= (portal_x - minimum_distance * App.SPRITESIZE))
                    || (this.portals.get(j).getY() <= (portal_y + minimum_distance * App.SPRITESIZE)
                    && this.portals.get(j).getY() >= (portal_x - minimum_distance * App.SPRITESIZE))){
                        can_set_portal = false;
                        break;
                }
            }

            if(can_set_portal){
                this.portals.add(new Teleportal(this.rep_portal_status, portal_x, portal_y));
                i++;
            }else{
                continue;
            }
        }
    }

    /*
    THE GAME LOGIC:
        - Otherwise known as the tick() function. But I just prefer to call it
        blit() to be different.
     */
    private boolean has_shot = false;
    public void blit(int player_input, boolean released, boolean shoot){
        /* 
        RESTART/GAME OVER: 
            - Pressing keys to restart.
        */
        if(this.lives <= 0 || this.won){
            if(this.end_screen_counter >= App.FPS * 2){
                if((player_input >= 0 || player_input == ' ') && !released){
                
                    this.won = false;
                    this.current_level = 0;

                    this.lives = game_config.getInt("lives");

                    this.end_screen_counter = 0;
                    this.loadMap();

                }
            }else{
                this.end_screen_counter++;
            }
        }
        /*
        SECTION A:
            - This updates the list of walls for every frame.
        */
        this.walls = new ArrayList<Sprite>();
        this.walls.addAll(this.brickwalls);
        this.walls.addAll(this.stonewalls);

        /*
        SECTION B:
            - The following is the logical sequence for the 
            player's movements. The whole point of this is to
            make sure the player moves within cells and is nowhere
            between the cells.
         */
        boolean player_is_between_tiles = (this.wizard.getX() % App.SPRITESIZE != 0 
        || this.wizard.getY() % App.SPRITESIZE != 0);

        /*
        SECTION C:
            - Executing the player's shooting logic and the fireball
            collision logic.
        */

        if(this.has_shot){
            this.player_cooldown_counter++;
            if(this.player_cooldown_counter >= this.player_cooldown_frames){
                this.player_cooldown_counter = 0;
                this.has_shot = false;
            }
        }

        /*
        NOTE:
            - Making sure there is an interval in between so
            the player doesn't shoot a continuous stream of fireballs.
         */
        if(shoot && this.player_cooldown_counter == 0){
            this.wizard.shoot(this.fireballs, this.rep_fireball);
            this.has_shot = true;
        }
        
        /*
        NOTE:
            - The block below runs through each fireball
            and checks if they have collided with any given wall
        */
        int bwall_size_before_shoot = this.brickwalls.size();
        for(int index = 0; index < this.fireballs.size(); index++){
            this.fireballs.get(index).move();
            if(this.fireballs.get(index).collided(this.brickwalls, this.stonewalls, this.gremlins, this.slimes)){
                this.fireballs.remove(index);
            }
        }
        for(int index = 0; index < this.brickwalls.size(); index++){
            if(this.brickwalls.get(index).nextStage()){
                this.brickwalls.remove(index);
            }
        }
        int bwall_size_after_shoot = this.brickwalls.size();

        /*
        SECTION D:
            - Executing the player's collision logic with the door, walls and powerups.
        */
        if(bwall_size_before_shoot != bwall_size_after_shoot){
            /*
            NOTE:
                - The Wizard method below exists because of a lack of
                design. The collision logic for the player should have been
                done discretely instead of continuously. My bad.

                - It's sort of a band-aid solution.
             */
            this.wizard.setRefresh(true);
        }else{
            this.wizard.setRefresh(false);
        }

        this.wizard.collided(this.walls);
        
        if(this.wizard.collided(this.door)){
            /*
            NOTE:
                - If the player collides with a door then a new level
                gets set.
             */
            this.current_level++;
            if(this.current_level >= this.levels_config.size()){
                this.won = true;
                return;
            }else{
                this.loadMap();
                return;
            }
        }

        if(this.powerup != null){
            /*
            NOTE:
                - If the player collides with a powerup then all
                the gremlins are stopped for 10 seconds.
             */
            if(this.wizard.collided(this.powerup)){
                this.gremlin_speed = 0;
                this.powerup = null;

                this.powerup_counter = App.FPS * this.powerup_duration;
                this.powerup_spawn_counter = 0;
            }
        }

        if(this.powerup_counter > 0){
            powerup_counter--;
        }else{
            this.gremlin_speed = 1;
        }

        /*
        SECTION E:
            - Executing the gremlins' collision logic with the walls and the player and
            random shooting of slimes.
         */
        /*
        NOTE:
            - This is the collision
         */
        for(int index = 0; index < this.gremlins.size(); index++){
            gremlins.get(index).collided(this.walls);

            if(gremlins.get(index).collided(this.wizard)){
                this.lives--;
                this.loadMap();
                return;
            }

            gremlins.get(index).move(this.gremlin_speed);
        }

        /*
        NOTE:
            - And this is the random shooting
         */

        for(int index = 0; index < this.gremlins.size(); index++){
            if(gremlins.get(index).cooldown_counter >= gremlin_cooldown_frames){
                gremlins.get(index).cooldown_counter = 0;
            }

            if(gremlins.get(index).cooldown_counter == 0){
                gremlins.get(index).has_shot = false;
                gremlins.get(index).cooldown_counter = 0;

                Random rand = new Random();
                int POOL = 7;
                /*
                NOTE:
                    - The higher the value for `POOL`, the less
                    likely any given gremlin is going to shoot.
                 */
                if(rand.nextInt(POOL) == 0){
                    /*
                    NOTE:
                        - Setting the bound of the random variable high
                        so the probability of a gremlin shooting becomes lower.
                     */
                    gremlins.get(index).shoot(this.slimes, this.rep_slime);
                    gremlins.get(index).has_shot = true;
                }
            }

            if(gremlins.get(index).has_shot){
                gremlins.get(index).cooldown_counter++;
            }

        }

        for(int index = 0; index < this.slimes.size(); index++){
            this.slimes.get(index).move();
            if(this.slimes.get(index).collided(this.wizard)){
                /*
                NOTE:
                    - We load the map again if the player
                    crashes into a slimeball. And the player
                    loses a life.
                 */
                this.lives--;
                this.loadMap();
                return;
            }
            if(this.slimes.get(index).collided(this.walls)){
                /*
                NOTE:
                    - If the slime hits a wall then it's just
                    going to disappear.
                 */
                this.slimes.remove(index);
            }
        }

        /*
        SECTION F:
            - Gremlins' random respawn.
        */
        if(this.gremlins.size() < this.initial_number_of_gremlins){
            while(true){
                Random rand = new Random();
                
                int respawn_x = rand.nextInt(35) * App.SPRITESIZE;
                int respawn_y = rand.nextInt(32) * App.SPRITESIZE;

                boolean can_respawn = true;

                for(int index = 0; index < this.walls.size(); index++){
                    /*
                    NOTE:
                        - This is checking to see if the gremlin is
                        being spawned within ten tiles radius from
                        the player.
                     */
                    boolean within_ten_tiles_radius = 
                            ((respawn_x < this.wizard.getX() + 10 * App.SPRITESIZE
                            && respawn_x > this.wizard.getX() - 10 * App.SPRITESIZE)
                            || (respawn_y < this.wizard.getY() + 10 * App.SPRITESIZE
                            && respawn_y > this.wizard.getY() - 10 * App.SPRITESIZE));

                    if(respawn_x == this.walls.get(index).getX()
                    && respawn_y == this.walls.get(index).getY()
                    || within_ten_tiles_radius){
                        /*
                        NOTE:
                            - If the coordinates for the gremlin being 
                            respawned is colliding with a wall or is
                            within ten tiles radius of the player then
                            `can_respawn` is false and the whole loop runs 
                            again.
                         */
                        can_respawn = false;
                    }
                }

                if(can_respawn){
                    this.gremlins.add(new Gremlin(this.rep_gremlin, respawn_x, respawn_y));
                    break;
                }
            }
        }

        /*
        SECTION G
            - Making sure the player is moving within discrete tiles
            and not between tiles.
        */
        if(player_is_between_tiles){
            this.wizard.move(this.current_key_pressed, this.player_speed);
        }else{
            this.current_key_pressed = player_input;
            if(released){
                this.wizard.stop();
            }else{
                this.wizard.move(this.current_key_pressed, this.player_speed);
            }
        }

        /*
        SECTION H:
            - Powerup random spawning
         */

        Random rand = new Random();
        boolean random_spawn = false;
        if(rand.nextInt(4) == 0){
            /*
            NOTE:
                - One out of four chances of spawning a
                powerup.
             */
            random_spawn = true;
        }
        if(this.powerup_spawn_counter > App.FPS * 10 && random_spawn && this.powerup == null){
            this.powerup_spawn_counter = 0;
            while(true){
                rand = new Random();
                
                int spawn_x = rand.nextInt(35) * App.SPRITESIZE;
                int spawn_y = rand.nextInt(32) * App.SPRITESIZE;
    
                boolean can_spawn = true;
    
                for(int index = 0; index < this.walls.size(); index++){
                    if(spawn_x == this.walls.get(index).getX()
                    && spawn_y == this.walls.get(index).getY()){
                        /*
                        NOTE:
                            - Spawning the powerups has the same logic
                            as spawning the gremlins except the powerups
                            can spawn within the player's ten-tile radius.
                         */
                        can_spawn = false;
                    }
                }
    
                if(can_spawn){
                    this.powerup = new Powerup(rep_powerup, spawn_x, spawn_y);
                    break;
                }
            }
        }else{
            if(this.powerup_counter == 0){
                this.powerup_spawn_counter++;
            }
        }

        /*
        SECTION I: 
            - The extension where the player gets teleported to a random
            portal.
         */
        if(this.portal_effect_counter <= 0){
            for(int index = 0; index < this.portals.size(); index++){
                /*
                NOTE:
                    - This loop switches the image of the portals to the 
                    available representation.
               */
                this.portals.get(index).setAvailable();
            }

            for(int index = 0; index < this.portals.size(); index++){
                /*
                NOTE:
                    - This loop chooses a random portal for the player.
                */ 
                if(this.portals.get(index).collided(this.wizard)){
                    this.portal_effect_counter = App.FPS * 5;

                    Random randport = new Random();
                    int next_portal = randport.nextInt(this.portals.size());
                    while(next_portal == index){
                        next_portal = randport.nextInt(this.portals.size());
                    }

                    this.wizard.setCoordinate(this.portals.get(next_portal).getX(), this.portals.get(next_portal).getY());

                    /*
                    NOTE:
                        - Making sure the player doesn't clip through any walls.
                     */
                    this.wizard.clearUnable();
                    this.wizard.collided(this.walls);

                    break;
                }
            }
        }else{
            for(int index = 0; index < this.portals.size(); index++){
                /*
                NOTE:
                    - And this switches the portals to a different
                    representation showing that it's not available for
                    use.
                 */
                this.portals.get(index).setUnavailable();
            }
            this.portal_effect_counter--;
        }

    }

    public void displayLose(PApplet application){
        /*
        NOTE:
            - This is if the player loses.
            */
        application.textSize(45);
        application.textAlign(application.CENTER);
        application.text("You've lost miserably!", 720/2, 720/2 - 60);
        application.textSize(25);
        application.text("Press any key to restart.", 720/2, 720/2 + 40);
        application.textSize(10);
        application.text("Thank you for playing the game.\nMade by Tran Minh Cuong Vu (Jerry)", 720/2, 720/2 + 100);
        application.fill(0, 0, 0);

    }

    public void displayWin(PApplet application){
        /*
        NOTE:
            - This happens when there are
            no more rounds to clear.
            */
        application.textSize(45);
        application.textAlign(application.CENTER);
        application.text("You've won!", 720/2, 720/2 - 60);
        application.textSize(25);
        application.text("Press any key to restart.", 720/2, 720/2 + 40);
        application.textSize(10);
        application.text("Thank you for playing the game.\nMade by Tran Minh Cuong Vu (Jerry)", 720/2, 720/2 + 100);
        application.fill(0, 0, 0);
    }

    public void displayLives(PApplet application){

        if(this.lives > 5){
            /*
            NOTE:
                - If the number of live is larger than 5 then we just
                display the number.
             */
            application.textSize(20);
            application.textAlign(application.LEFT);
            application.text("Lives: " + ((Integer)this.lives).toString(), 20, 720 - App.BOTTOM_BAR/3 - 5);
            application.fill(0, 0, 0);
        }else{
            /*
            NOTE:
                - If it's equal to or less than 5 then we display
                the wizard icons.
             */
            application.textSize(20);
            application.textAlign(application.LEFT);
            application.text("Lives:", 20, 720 - App.BOTTOM_BAR/3 - 5);
            application.fill(0, 0, 0);
            for(int index = 0; index < this.lives; index++){
                application.image(this.rep_wizard_directions[0], 60 + (index+1) * 25, 720 - 42);
            }
        }
    }

    public void displayLevel(PApplet application){
        String current_level_str = ((Integer)(this.current_level + 1)).toString();
        application.textSize(15);
        application.text("Level: " + current_level_str, 620, 720 - App.BOTTOM_BAR/3 - 6);
        application.fill(0, 0, 0);
    }

    public void displayCooldown(PApplet application){
        /*
        NOTE:
            - This first one is the original bar
         */
        application.fill(255, 255, 255);
        application.rect(500, 685,  100, 5);

        /*
        NOTE:
            - This second one is the bar that loads up.
         */
        application.fill(0, 0, 0);
        application.rect(500, 685, (float)this.player_cooldown_counter * 100 / (float)(this.player_cooldown_frames), 5);
    }

    public void displayPowerup(PApplet application){
        /*
        NOTE:
            - The bar for remaining powerup.
        */
        application.fill(148,0,211);
        application.rect(350, 685, (float)this.powerup_counter * 100 /(float)(App.FPS * this.powerup_duration), 5);
        application.fill(0, 0, 0);
    }

    public void draw(PApplet application){
        if(this.won){
            this.displayWin(application);
            return;
        }

        if(this.lives <= 0){
            this.displayLose(application);
            return;
        }

        this.displayLives(application);
        this.displayCooldown(application);
        if(this.powerup_counter > 0){
            this.displayPowerup(application);
        }
        this.displayLevel(application);

        /*
        NOTE:
            - As there could be multiple sprites of 
            other types, we must loop through each of them
            in their respective lists in order to display 
            everything.
         */
        
        ArrayList<Sprite> all = new ArrayList<Sprite>();

        all.addAll(this.stonewalls);
        all.addAll(this.brickwalls);
        all.addAll(this.portals);
        if(this.powerup != null){
            all.add(this.powerup);
        }
        all.add(this.door);
        all.addAll(this.gremlins);
        all.addAll(this.fireballs);
        all.addAll(this.slimes);
        all.add(this.wizard);

        for(int index = 0; index < all.size(); index++){
            all.get(index).draw(application);
        }
    }
}