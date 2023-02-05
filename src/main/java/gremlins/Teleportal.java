package gremlins;

import processing.core.*;
import processing.data.*;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Teleportal extends Sprite{
    /*
    SECTION 1: DECLARATION AND INITIALIZATION OF CLASS ATTRIBUTES
        AND PARAMETERS.
    */
    private PImage available;
    private PImage unavailable;

    private PImage current_status;
    /*
    SECTION 2: CONSTRUCTORS
    */
    public Teleportal(){
        super();
        super.setRepresentation(null);
    }
    public Teleportal(int x, int y){
        super(x, y);
        super.setRepresentation(null);
    }
    public Teleportal(PImage[] representations, int x, int y){
        super(x, y);
        super.setRepresentation(null);
        
        this.available = representations[0];
        this.unavailable = representations[1];

        this.current_status = available;
    }
    /* 
    SECTION 3: SETTER AND GETTER FUNCTIONS
    */
    public void setAvailable(){
        this.current_status = this.available;
    }
    public void setUnavailable(){
        this.current_status = this.unavailable;
    }
    /*
    SECTION 4: IMPLEMENTING COLLISIONS
    */
    public boolean collided(Wizard player){
        int[] player_bounds = player.getBounds();
        int[] portal_bounds = super.getBounds();

        if(((portal_bounds[0] == player_bounds[0] && portal_bounds[1] == player_bounds[1])
        && (portal_bounds[2] == player_bounds[2] && portal_bounds[3] == player_bounds[3]))){
            /*
            NOTE:
                - We only need to know whether
                the wizard has collided with a portal.
                Returns true if this happens
            */
            return true;
        }
        return false;
    }
    /*
    NOTE:
     */
    public void draw(PApplet application){
        application.image(this.current_status, super.getX(), super.getY());
    }
}
