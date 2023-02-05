package gremlins;

import processing.core.*;
import processing.data.*;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Slime extends Sprite{
    /*
    SECTION 1: DECLARATION AND INITIALIZATION OF CLASS ATTRIBUTES
        AND PARAMETERS.
    */
    Direction current_direction = Direction.NONE;
    /*
    SECTION 2: CONSTRUCTORS
    */
    public Slime() {
        super();
        super.setRepresentation(null);
    }

    public Slime(int x, int y) {
        super(x, y);
        super.setRepresentation(null);
    }

    public Slime(PImage representation, int x, int y) {
        super(representation, x, y);
    }
    /* 
    SECTION 3: SETTER AND GETTER FUNCTIONS
    */
    public void setDirection(Direction direction){ this.current_direction = direction; }
    /*
    SECTION 4: IMPLEMENTING MOVEMENTS
    */
    public void move(){
        final int PIXELS_PER_FRAME = 4;
        if(this.current_direction == Direction.LEFT){
            super.setCoordinate(super.getX() - PIXELS_PER_FRAME, super.getY());
        }else if(this.current_direction == Direction.RIGHT){
            super.setCoordinate(super.getX() + PIXELS_PER_FRAME, super.getY());
        }else if(this.current_direction == Direction.UP){
            super.setCoordinate(super.getX(), super.getY() - PIXELS_PER_FRAME);
        }else if(this.current_direction == Direction.DOWN){
            super.setCoordinate(super.getX(), super.getY() + PIXELS_PER_FRAME);
        }
    }

    /*
    SECTION 5: IMPLEMENTING COLLISIONS
    */
    public boolean collided(ArrayList<Sprite> walls){
        int[] slime_bounds = super.getBounds();

        boolean flag = false;
        for(int index = 0; index < walls.size(); index++){
            int[] wall_bounds = walls.get(index).getBounds();

            if(((wall_bounds[0] < slime_bounds[1] && wall_bounds[0] >= slime_bounds[0])
            && (wall_bounds[2] < slime_bounds[3] && wall_bounds[2] >= slime_bounds[2]))
            || ((wall_bounds[1] > slime_bounds[0] && wall_bounds[1] <= slime_bounds[1])
            && (wall_bounds[3] > slime_bounds[2] && wall_bounds[3] <= slime_bounds[3]))){
                /*
                NOTE:
                    - It does nothing because the gremlins' slimes
                    can't destroy any type of wall.
                 */
                return true;
            }
        }

        return false;
    }

    public boolean collided(Wizard player){
        int[] slime_bounds = super.getBounds();

        int[] player_bounds = player.getBounds();

        if(((player_bounds[0] < slime_bounds[1] && player_bounds[0] >= slime_bounds[0])
        && (player_bounds[2] < slime_bounds[3] && player_bounds[2] >= slime_bounds[2]))
        || ((player_bounds[1] > slime_bounds[0] && player_bounds[1] <= slime_bounds[1])
        && (player_bounds[3] > slime_bounds[2] && player_bounds[3] <= slime_bounds[3]))){
            /*
            NOTE:
                - However, the slime can send the player
                to the backrooms.
            */
            return true;
        }
        
        return false;
    }
}