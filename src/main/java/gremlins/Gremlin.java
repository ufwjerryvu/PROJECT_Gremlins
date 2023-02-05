package gremlins;

import processing.core.*;
import processing.data.*;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Gremlin extends Sprite implements Movement, Collision, Shooting{
    /*
    SECTION 1: DECLARATION AND INITIALIZATION OF CLASS ATTRIBUTES
        AND PARAMETERS.
    */
    private boolean unable_left, unable_right, unable_up, unable_down;
    private Direction current_direction;

    public int cooldown_counter = 0;
    public boolean has_shot = false;
    /*
    SECTION 2: CONSTRUCTORS
    */
    public Gremlin() {
        super();
        super.setRepresentation(null);
    }

    public Gremlin(int x, int y) {
        super(x, y);
        super.setRepresentation(null);
    }

    public Gremlin(PImage representation, int x, int y) {
        super(representation, x, y);

        this.unable_left = false;
        this.unable_right = false;
        this.unable_up = false;
        this.unable_down = false;

        /*
        NOTE:
            - The `current_direction` variable for 
            a Gremlin instance is different from that
            of Wizard's. 
         */
        this.current_direction = Direction.NONE;
    }
    /* 
    SECTION 3: SETTER AND GETTER FUNCTIONS
    */
    public int getCooldownCounter(){ return this.cooldown_counter;}
    public void setCooldownCounter(int cooldown_counter){ this.cooldown_counter = cooldown_counter;}
    public boolean getHasShot(){ return this.has_shot;}
    public void setHasShot(boolean has_shot){ this.has_shot = has_shot; }

    private Direction correspondingDirection(int value){
        /*
        NOTE:
            - This function is private because it's useless
            and would make no sense outside of the class.
            However, this function is used for readability in the
            Gremlin.getRandomDirection() method.
         */
        if(value == 0){
            return Direction.LEFT;
        }else if(value == 1){
            return Direction.RIGHT;
        }else if(value == 2){
            return Direction.UP;
        }else if(value == 3){
            return Direction.DOWN;
        }else{
            return Direction.NONE;
        }
    }
    /*
    SECTION 4: IMPLEMENTING MOVEMENTS
    */ 
    public void move(){

    }
    
    public void move(int gremlin_speed){
        final int PIXELS_PER_FRAME = gremlin_speed;

        Random rand = new Random();

        if(this.current_direction == Direction.NONE){
            this.current_direction = this.correspondingDirection(rand.nextInt(4));
        }

        /*
        NOTE:
            - This is setting a vertical direction for the gremlin just
            in case it hits a wall. All horizontal directions are
            disabled.
         */
        if((this.current_direction == Direction.LEFT && this.unable_left)||
            (this.current_direction == Direction.RIGHT && this.unable_right)){

            if(this.unable_up && this.unable_down){
                this.current_direction = Direction.NONE;
                return;
            }

            int random_vertical = rand.nextInt(2);
            if(random_vertical == 0 && !this.unable_up){
                this.current_direction = Direction.UP;
            }else if(random_vertical == 1 && !this.unable_down){
                this.current_direction = Direction.DOWN;
            }
        }

       /*
        NOTE:
            - On the other hand, this is setting a random vertical direction
            for the gremlin as the gremlin cannot go back to the direction
            it came from. 
         */
        if((this.current_direction == Direction.UP && this.unable_up)||
            (this.current_direction == Direction.DOWN && this.unable_down)){
            
            if(this.unable_left && this.unable_right){
                this.current_direction = Direction.NONE;
                return;
            }

            int random_horizontal = rand.nextInt(2);
            if(random_horizontal == 0 && !this.unable_left){
                this.current_direction = Direction.LEFT;
            }else if(random_horizontal == 1 && !this.unable_right){
                this.current_direction = Direction.RIGHT;
            }
        }

        /*
        NOTE:
            - This section actually moves the gremlin.
         */
        if(this.current_direction == Direction.LEFT && !this.unable_left){
            super.setCoordinate(super.getX() - PIXELS_PER_FRAME, super.getY());
        }else if(this.current_direction == Direction.RIGHT && !this.unable_right){
            super.setCoordinate(super.getX() + PIXELS_PER_FRAME, super.getY());
        }else if(this.current_direction == Direction.UP && !this.unable_up){
            super.setCoordinate(super.getX(), super.getY() - PIXELS_PER_FRAME);
        }else if(this.current_direction == Direction.DOWN && !this.unable_down){
            super.setCoordinate(super.getX(), super.getY() + PIXELS_PER_FRAME);
        }

    }
    public void stop(){

    }
    /*
    SECTION 5: IMPLEMENTING COLLISIONS
     */
    public boolean collided(ArrayList<Sprite> walls){
        int[] gremlin_bounds = super.getBounds();

        boolean flag = false;
        for(int index = 0; index < walls.size(); index++){
            int[] wall_bounds = walls.get(index).getBounds();
            /*
            NOTE:
                - The if statement below is saying if the 
                right bound of the gremlin is equal to the 
                left bound of the wall and the gremlin
                has the same y-coordinate as the wall
                then they won't be able to move in the right
                direction.
             */
            if(wall_bounds[0] == gremlin_bounds[1] 
            && wall_bounds[2] == gremlin_bounds[2]){
                this.unable_right = true;
                flag = true;
            }
            /*
            NOTE:
                - Similarly, this is saying if the left bound
                of the gremlin is equal to the right bound of
                the wall then the gremlin cannot move left.
             */
            if(wall_bounds[1] == gremlin_bounds[0] 
            && wall_bounds[2] == gremlin_bounds[2]){
                this.unable_left = true;
                flag = true;
            }
            /*
            NOTE:
                - Same, but for upper and lower bounds.
                However, we have to make sure the x-axis
                is the same.
             */
            if(wall_bounds[2] == gremlin_bounds[3]
            && wall_bounds[0] == gremlin_bounds[0]){
                this.unable_down = true;
                flag = true;
            }
            /*
            NOTE:
                - And again.
             */
            if(wall_bounds[3] == gremlin_bounds[2]
            && wall_bounds[0] == gremlin_bounds[0]){
                this.unable_up = true;
                flag = true;
            }
        }
        
        if(flag){
            return flag;
        }

        /*
        NOTE:
            - If the gremlin isn't colliding with any brick
            or stonewall in the current frame then all of the
            unable flags are set to false.
         */
        this.unable_left = false;
        this.unable_right = false;
        this.unable_up = false; 
        this.unable_down = false;

        return false;
    }

    public boolean collided(Wizard player){
        int[] player_bounds = player.getBounds();
        int[] gremlin_bounds = super.getBounds();

        if(((player_bounds[0] < gremlin_bounds[1] && player_bounds[0] >= gremlin_bounds[0])
        && (player_bounds[2] < gremlin_bounds[3] && player_bounds[2] >= gremlin_bounds[2]))
        || ((player_bounds[1] > gremlin_bounds[0] && player_bounds[1] <= gremlin_bounds[1])
        && (player_bounds[3] > gremlin_bounds[2] && player_bounds[3] <= gremlin_bounds[3]))){
            return true;
        }

        return false;
    }

    public void shoot(){

    }

    public void shoot(ArrayList<Slime> slimes, PImage rep_slime){
        Slime temp = new Slime(rep_slime, super.getX(), super.getY());
        temp.setDirection(this.current_direction);
        slimes.add(temp);
    }
    /*
    SECTION 6: OTHER FUNCTIONS
    */ 
}