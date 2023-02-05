package gremlins;

import processing.core.*;
import processing.data.*;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Brickwall extends Sprite{ 
    /*
    SECTION 1: DECLARATION AND INITIALIZATION OF CLASS ATTRIBUTES
        AND PARAMETERS.
    */
    
    private PImage[] stages;
    private PImage current_representation;
    private int current_stage;

    private boolean initiated_destruction = false;
    private int destruction_counter = 0;

    /*
    SECTION 2: CONSTRUCTORS
    */
    public Brickwall(){
        super();
        super.setRepresentation(null);
    }
    public Brickwall(int x, int y){
        super(x, y);
        super.setRepresentation(null);
    }
    public Brickwall(PImage[] representations, int x, int y){
        super(x, y);
        super.setRepresentation(null);

        this.stages = representations;

        this.current_stage = 0;
        this.current_representation = representations[current_stage];
    }
    /* 
    SECTION 3: SETTER AND GETTER FUNCTIONS
    */
    
    public boolean isBeingDestroyed(){ return this.initiated_destruction;}
    /*
    SECTION 4: OTHER FUNCTIONS
    */

    public void initiateDestruction(){
        initiated_destruction = true;
    }

    public boolean nextStage(){
        int FRAMES_PER_STAGE = 4;

        if(this.initiated_destruction){
            this.destruction_counter++;

            if(this.destruction_counter % FRAMES_PER_STAGE == 0){
                this.current_stage++;
                this.destruction_counter = 0;
            }

            if(this.current_stage == this.stages.length){
                /*
                NOTE:
                    - Returns true if the current stage is the last
                    stage of the brickwall.
                 */
                return true;
            }

            this.current_representation = this.stages[this.current_stage];
        }
        return false;
    }
    public void draw(PApplet application){
        application.image(this.current_representation, super.getX(), super.getY());
    }
}