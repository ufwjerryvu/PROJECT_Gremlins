package gremlins;

import processing.core.*;
import processing.data.*;

import java.util.*;
import java.lang.*;
import java.io.*;

public abstract class Sprite{
    /*
    SECTION 1: DECLARATION AND INITIALIZATION OF CLASS ATTRIBUTES
        AND PARAMETERS.
    */
    private int x;
    private int y;

    private PImage representation;

    /*
    SECTION 2: CONSTRUCTORS
    */
    public Sprite(){
        /*
        NOTE:
            - A value of negative one means the coordinates haven't been initialized
            yet.
         */
        this.x = -1;
        this.y = -1;
        this.representation = null;
    }

    public Sprite(int x, int y){
        this.setCoordinate(x, y);
        this.representation = null;
    }

    public Sprite(PImage representation, int x, int y){
        this.setCoordinate(x, y);
        this.representation = representation;
    }

    /* 
    SECTION 3: SETTER AND GETTER FUNCTIONS
     */
    public boolean setCoordinate(int x, int y){
        return this.setX(x) && this.setY(y);
    }

    public void setRepresentation(PImage representation){
        this.representation = representation;
    }

    public boolean setX(int x){
        if(x < 0 || x > App.WINDOW_WIDTH - App.SPRITESIZE){
            /*
            NOTE:
                - Validation to make sure that the coordinates being set are
                within the dimensions of the window.
            */
            return false;
        }
        this.x = x;
        return true;
    }

    public boolean setY(int y){
        if(y < 0 || y > App.WINDOW_WIDTH - App.BOTTOM_BAR - App.SPRITESIZE){
            /*
            NOTE:
                - Validation to make sure that the coordinates being set are
                within the dimensions of the window.
            */
            return false;
        }
        this.y = y;
        return true;
    }

    public int getX(){ return this.x;}

    public int getY(){ return this.y;}
    public PImage getRepresentation(){ return this.representation; }

    public int[] getBounds(){
        /*
        NOTE:
            - This function is to get the bounds of a sprite. If another
            object comes into contact with the bounds, there will be some 
            sort of interaction.
         */
        int x_left_bound = this.x;
        int x_right_bound = this.x + App.SPRITESIZE;
        int y_upper_bound = this.y;
        int y_lower_bound = this.y + App.SPRITESIZE;

        int[] rarr = {x_left_bound, x_right_bound, y_upper_bound, y_lower_bound};

        return rarr;
    }

    /*
    SECTION 4: OTHER FUNCTIONS
     */
    public void draw(PApplet application){
        /*
        NOTE:
            - This function is used to display individual sprite intances.
         */
        application.image(this.representation, this.getX(), this.getY());
    }
}