package gremlins;

import processing.core.*;
import processing.data.*;

import java.io.*;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.*;

public class TestMapParsing{
    @Test 
    public void testConstructor1(){
        MapParsing test = new MapParsing();
        assertNotNull(test);
    }

    @Test
    public void testConstructor2(){
        MapParsing test = new MapParsing("level1.txt");
        assertNotNull(test);
    }

    @Test
    public void testParseBrickwalls(){
        /*
        NOTE:
            - Testing for the map parsing of brickwalls
        */
        MapParsing test = new MapParsing("wrong_file.txt");
        assertNull(test.parseBrickwalls(null));

        test = new MapParsing("level1.txt");
        PImage[] arr = {new PImage(), new PImage(), new PImage()};
        test.parseBrickwalls(arr);
        assertNotNull(test.parseBrickwalls(arr));
    }

    @Test
    public void testParseStonewalls(){
        /*
        NOTE:
            - Testing for the map parsing of stonewalls
        */
        MapParsing test = new MapParsing("wrong_file.txt");
        assertNull(test.parseStonewalls(null));

        test = new MapParsing("level1.txt");
        test.parseStonewalls(null);

        assertNotNull(test.parseStonewalls(new PImage()));
    }

    @Test
    public void testParseGremlins(){
        /*
        NOTE:
            - Testing for the map parsing of gremlins
        */
        MapParsing test = new MapParsing("wrong_file.txt");
        assertNull(test.parseGremlins(null));

        test = new MapParsing("level1.txt");
        test.parseGremlins(null);
        assertNotNull(test.parseGremlins(new PImage()));
    }

    @Test
    public void testParseDoor(){
        /*
        NOTE:
            - Testing for the map parsing of the door
        */
        MapParsing test = new MapParsing("wrong_file.txt");
        assertNull(test.parseDoor(null));

        test = new MapParsing("level1.txt");
        assertNotNull(test.parseDoor(new PImage()));
    }

    @Test
    public void testParseWizard(){
        /*
        NOTE:
            - Testing for the map parsing of the wizard.
        */
        MapParsing test = new MapParsing("wrong_file.txt");
        assertNull(test.parseWizard(null));

        test = new MapParsing("level1.txt");
        PImage[] arr = {new PImage(), new PImage(), new PImage(), new PImage()};
        assertNotNull(test.parseWizard(arr));
    }

}

