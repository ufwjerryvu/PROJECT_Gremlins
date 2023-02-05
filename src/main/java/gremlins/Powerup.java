package gremlins;

import processing.core.*;
import processing.data.*;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Powerup extends Sprite {
        /*
    SECTION 1: DECLARATION AND INITIALIZATION OF CLASS ATTRIBUTES
        AND PARAMETERS.
    */
    /*
    SECTION 2: CONSTRUCTORS
    */
    public Powerup() {
        super();
        super.setRepresentation(null);
    }

    public Powerup(int x, int y) {
        super(x, y);
        super.setRepresentation(null);
    }

    public Powerup(PImage representation, int x, int y) {
        super(representation, x, y);
    }
}