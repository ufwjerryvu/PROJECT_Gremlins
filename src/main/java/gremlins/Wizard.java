package gremlins;

import processing.core.*;
import processing.data.*;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Wizard extends Sprite implements Movement, Collision, Shooting{ 
    /*
    SECTION 1: DECLARATION AND INITIALIZATION OF CLASS ATTRIBUTES
        AND PARAMETERS.
    */
    private PImage facing_left;
    private PImage facing_up;
    private PImage facing_right;
    private PImage facing_down;

    private PImage facing_direction;
    private Direction current_direction;

    private boolean unable_left, unable_right, unable_up, unable_down;

    private boolean refresh = false;
    /*
    SECTION 2: CONSTRUCTORS
    */
    public Wizard(){
        super();
        super.setRepresentation(null);
    }
    public Wizard(int x, int y){
        super(x, y);
        super.setRepresentation(null);
    }
    public Wizard(PImage[] directions, int x, int y){
        super(x, y);
        super.setRepresentation(null);
        
        this.facing_left = directions[0];
        this.facing_right = directions[1];
        this.facing_up = directions[2];
        this.facing_down = directions[3];

        this.facing_direction = this.facing_right;
        this.current_direction = Direction.RIGHT;

        this.unable_left = false;
        this.unable_right = false;
        this.unable_up = false;
        this.unable_down = false;
    }
    /* 
    SECTION 3: SETTER AND GETTER FUNCTIONS
    */
    public void setRefresh(boolean refresh){ this.refresh = refresh;}
    public boolean getRefresh(){ return this.refresh;}

    /*
    SECTION 4: IMPLEMENTING MOVEMENTS
    */
    public void clearUnable(){
        this.unable_down = false;
        this.unable_up = false;
        this.unable_right = false;
        this.unable_left = false;
    }
    public void move(){
        /*
        NOTE:
            - Doing nothing because we can't really do
            anything if the player doesn't provide any input.
         */
    }

    public void move(int player_input, int speed){
        final int LEFT = 37, UP = 38, RIGHT = 39, DOWN = 40;
        final int PIXELS_PER_FRAME = speed;

        if(player_input == LEFT){
            this.facing_direction = facing_left;
            this.current_direction = Direction.LEFT;
            /*
            NOTE:
                - Checking if there are any obstacles on the left.
             */
            if(!unable_left){
                super.setCoordinate(super.getX() - PIXELS_PER_FRAME, super.getY());
            }
        }else if(player_input == RIGHT){
            this.facing_direction = facing_right;
            this.current_direction = Direction.RIGHT;
            /*
            NOTE:
                - Checking if there are any obstacles on the right.
             */
            if(!unable_right){
                super.setCoordinate(super.getX() + PIXELS_PER_FRAME, super.getY());
            }
        }else if(player_input == UP){
            this.facing_direction = facing_up;
            this.current_direction = Direction.UP;
            /*
            NOTE:
                - Checking if there are any obstacles up.
            */
            if(!unable_up){
                super.setCoordinate(super.getX(), super.getY() - PIXELS_PER_FRAME);
            }
        }else if(player_input == DOWN){
            this.facing_direction = facing_down;
            this.current_direction = Direction.DOWN;
            /*
            NOTE:
                - Checking if there are any obstacles down.
            */
            if(!unable_down){
                super.setCoordinate(super.getX(), super.getY() + PIXELS_PER_FRAME);
            }
        }
    }

    public void stop(){
        super.setCoordinate(super.getX(), super.getY());
    }
    /*
    SECTION 5: IMPLEMENTING COLLISIONS
    */

    public boolean collided(ArrayList<Sprite> walls){
        int[] player_bounds = super.getBounds();

        boolean flag = false;
        for(int index = 0; index < walls.size(); index++){
            int[] wall_bounds = walls.get(index).getBounds();
            /*
            NOTE:
                - The if statement below is saying if the 
                right bound of the player is equal to the 
                left bound of the wall and the player
                has the same y-coordinate as the wall
                then they won't be able to move in the right
                direction.
             */
            if(wall_bounds[0] == player_bounds[1] 
            && wall_bounds[2] == player_bounds[2]){
                this.unable_right = true;
                flag = true;
            }
            /*
            NOTE:
                - Similarly, this is saying if the left bound
                of the player is equal to the right bound of
                the wall then the player cannot move left.
             */
            if(wall_bounds[1] == player_bounds[0] 
            && wall_bounds[2] == player_bounds[2]){
                this.unable_left = true;
                flag = true;
            }
            /*
            NOTE:
                - Same, but for upper and lower bounds.
                However, we have to make sure the x-axis
                is the same.
             */
            if(wall_bounds[2] == player_bounds[3]
            && wall_bounds[0] == player_bounds[0]){
                this.unable_down = true;
                flag = true;
            }
            /*
            NOTE:
                - And again.
             */
            if(wall_bounds[3] == player_bounds[2]
            && wall_bounds[0] == player_bounds[0]){
                this.unable_up = true;
                flag = true;
            }
        }
        
        if(flag && !refresh){
            return flag;
        }

        /*
        NOTE:
            - If the player isn't colliding with any brick
            or stonewall in the current frame then all of the
            unable flags are set to false.
         */
        this.clearUnable();

        return false;
    }

    public boolean collided(Door door){
        int[] player_bounds = super.getBounds();
        int[] door_bounds = door.getBounds();

        if(((door_bounds[0] < player_bounds[1] && door_bounds[0] >= player_bounds[0])
        && (door_bounds[2] < player_bounds[3] && door_bounds[2] >= player_bounds[2]))){
            /*
            NOTE:
                - We only need to know if the wizard has 
                collided with the door unlike the walls.
            */
            return true;
        }

        return false;
    }

    public boolean collided(Powerup powerup){
        int[] player_bounds = super.getBounds();
        int[] powerup_bounds = powerup.getBounds();

        if(((powerup_bounds[0] < player_bounds[1] && powerup_bounds[0] >= player_bounds[0])
        && (powerup_bounds[2] < player_bounds[3] && powerup_bounds[2] >= player_bounds[2]))){
            /*
            NOTE:
                - Similarly, we only need to know whether
                the wizard has collided with a powerup. 
                We don't need to set unable flags in the 
                directions of travel.
             */
            return true;
        }

        return false;
    }

    /*
    SECTION 6: IMPLEMENTING SHOOTING 
    */
    public void shoot(){

    }

    public void shoot(ArrayList<Fireball> fireballs, PImage rep_fireball){
        Fireball temp = new Fireball(rep_fireball, super.getX(), super.getY());
        temp.setDirection(this.current_direction);
        fireballs.add(temp);
    }
    /*
    SECTION 7: OTHER FUNCTIONS
     */
    public void draw(PApplet application){
        application.image(this.facing_direction, super.getX(), super.getY());
    }
}