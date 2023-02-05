package gremlins;

import processing.core.*;
import processing.data.*;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Fireball extends Sprite {
    /*
    SECTION 1: DECLARATION AND INITIALIZATION OF CLASS ATTRIBUTES
        AND PARAMETERS.
    */
    Direction current_direction = Direction.NONE;
    /*
    SECTION 2: CONSTRUCTORS
    */
    public Fireball() {
        super();
        super.setRepresentation(null);
    }

    public Fireball(int x, int y) {
        super(x, y);
        super.setRepresentation(null);
    }

    public Fireball(PImage representation, int x, int y) {
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
    public boolean collided(ArrayList<Brickwall> brickwalls, ArrayList<Stonewall> stonewalls, 
     ArrayList<Gremlin> gremlins, ArrayList<Slime> slimes){
        int[] fireball_bounds = super.getBounds();

        boolean flag = false;
        for(int index = 0; index < brickwalls.size(); index++){
            int[] wall_bounds = brickwalls.get(index).getBounds();

            if(brickwalls.get(index).isBeingDestroyed()){
                continue;
            }

            if(((wall_bounds[0] < fireball_bounds[1] && wall_bounds[0] >= fireball_bounds[0])
            && (wall_bounds[2] < fireball_bounds[3] && wall_bounds[2] >= fireball_bounds[2]))
            || ((wall_bounds[1] > fireball_bounds[0] && wall_bounds[1] <= fireball_bounds[1])
            && (wall_bounds[3] > fireball_bounds[2] && wall_bounds[3] <= fireball_bounds[3]))){
                /*
                NOTE:
                    - This distinction is important because
                    it removes the brickwall object that is
                    colliding with the fireball.
                */
                brickwalls.get(index).initiateDestruction();
                return true;
            }
        }

        for(int index = 0; index < stonewalls.size(); index++){
            int[] wall_bounds = stonewalls.get(index).getBounds();

            if(((wall_bounds[0] < fireball_bounds[1] && wall_bounds[0] >= fireball_bounds[0])
            && (wall_bounds[2] < fireball_bounds[3] && wall_bounds[2] >= fireball_bounds[2]))
            || ((wall_bounds[1] > fireball_bounds[0] && wall_bounds[1] <= fireball_bounds[1])
            && (wall_bounds[3] > fireball_bounds[2] && wall_bounds[3] <= fireball_bounds[3]))){
                /*
                NOTE:
                    - Does nothing here, as opposed to the loop
                    above because we cannot destroy stonewalls.
                */
                return true;
            }
        }

        for(int index = 0; index < gremlins.size(); index++){
            int[] gremlin_bounds = gremlins.get(index).getBounds();

            if(((gremlin_bounds[0] < fireball_bounds[1] && gremlin_bounds[0] >= fireball_bounds[0])
            && (gremlin_bounds[2] < fireball_bounds[3] && gremlin_bounds[2] >= fireball_bounds[2]))
            || ((gremlin_bounds[1] > fireball_bounds[0] && gremlin_bounds[1] <= fireball_bounds[1])
            && (gremlin_bounds[3] > fireball_bounds[2] && gremlin_bounds[3] <= fireball_bounds[3]))){
                /*
                NOTE:
                    - The gremlin that comes into contact with the 
                    fireball gets sent away to the backrooms.
                */
                gremlins.remove(index);
                return true;
            }
        }

        for(int index = 0; index < slimes.size(); index++){
            int[] slime_bounds = slimes.get(index).getBounds();

            if(((slime_bounds[0] < fireball_bounds[1] && slime_bounds[0] >= fireball_bounds[0])
            && (slime_bounds[2] < fireball_bounds[3] && slime_bounds[2] >= fireball_bounds[2]))
            || ((slime_bounds[1] > fireball_bounds[0] && slime_bounds[1] <= fireball_bounds[1])
            && (slime_bounds[3] > fireball_bounds[2] && slime_bounds[3] <= fireball_bounds[3]))){
                /*
                NOTE:
                    - The slime that comes into contact with the 
                    fireball gets sent away to the backrooms.
                */
                slimes.remove(index);
                return true;
            }
        }

        return false;
    }
    /*
    SECTION 6: OTHER FUNCTIONS
    */
}