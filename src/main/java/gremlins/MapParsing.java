package gremlins;

import processing.core.*;
import processing.data.*;

import java.io.*;
import java.util.*;
import java.lang.*;

public class MapParsing{
    /*
    SECTION 1: DECLARATION AND INITIALIZATION OF CLASS ATTRIBUTES
        AND GAME PARAMETERS.
    */
    /*
    NOTE:
        - A tile/sprite is 20 pixels in dimension. This
        attribute is declared again because I'm lazy and I
        don't want to make this class file dependent on the
        main App.java file, although it is already declared there.
     */
    private static final int SPRITE_DIMENSION = App.SPRITESIZE;
    private String layout_file_path;

    /*
    SECTION 2: CONSTRUCTORS
    */
    public MapParsing(){
        layout_file_path = null;
    }

    public MapParsing(String layout_file_path){
        this.layout_file_path = layout_file_path;
    }

    /*
    SECTION 3: PARSING FUNCTIONS

    IMPORTANT NOTE:
        - All the functions below follow the same or have highly similar logic for
        reading files and returning results but each of them return something different 
        and there are slight tweaks in a couple of them so there's no other way but to 
        be repetitive.
        
        - With that said, I've looked into functions with generic types to try to shorten the
        code down but it seems like working with generics is impossible in this case as allocating
        a new generic variable on the heap is not allowed by Java.
    */
    public ArrayList<Stonewall> parseStonewalls(PImage representation){
        /*
        NOTE:
            - The ArrayList instance rarrl stands for
            'return array list'.
         */
        ArrayList<Stonewall> rarrl = new ArrayList<Stonewall>();
        /*
        NOTE:
            - The specifcation states that the stonewalls are listed
            as 'X' in the text files.
         */
        final char IDENTIFIER = 'X';
        try{
            File file_in = new File(this.layout_file_path);
            Scanner jvfin = new Scanner(file_in);
            /*
            NOTE:
                - We are reading the level files here. 
                Since it's two dimensional, we're using
                nested loops.
             */
            for(int i = 0; jvfin.hasNextLine(); i++){
                /*
                NOTE:  
                    - The outer loop deals with the rows
                 */
                String line = jvfin.nextLine();
                for(int j = 0; j < line.length(); j++){
                    /*
                    NOTE:
                        - And the nested loop deals with
                        the columns
                     */
                    if(line.charAt(j) == IDENTIFIER){
                        rarrl.add(new Stonewall(representation, j * SPRITE_DIMENSION, i * SPRITE_DIMENSION));
                    }
                }
            }
        }catch(FileNotFoundException e){
            System.err.println("Error: file not found. Message from FileHandling.parseStonewalls().");
            return null;
        }

        return rarrl;
    }

    public ArrayList<Brickwall> parseBrickwalls(PImage[] stages){
        /*
        NOTE:
            - The ArrayList instance rarrl stands for
            'return array list'.
         */
        ArrayList<Brickwall> rarrl = new ArrayList<Brickwall>();
        /*
        NOTE:
            - The specifcation states that the brickwalls are listed
            as 'B' in the text files.
         */
        final char IDENTIFIER = 'B';
        try{
            File file_in = new File(this.layout_file_path);
            Scanner jvfin = new Scanner(file_in);
            /*
            NOTE:
                - We are reading the level files here. 
                Since it's two dimensional, we're using
                nested loops.
             */
            for(int i = 0; jvfin.hasNextLine(); i++){
                /*
                NOTE:  
                    - The outer loop deals with the rows
                 */
                String line = jvfin.nextLine();
                for(int j = 0; j < line.length(); j++){
                    /*
                    NOTE:
                        - And the nested loop deals with
                        the columns.
                     */
                    if(line.charAt(j) == IDENTIFIER){
                        rarrl.add(new Brickwall(stages, j * SPRITE_DIMENSION, i * SPRITE_DIMENSION));
                    }
                }
            }
        }catch(FileNotFoundException e){
            System.err.println("Error: file not found. Message from FileHandling.parseBrickwalls().");
            return null;
        }

        return rarrl;
    }

    public ArrayList<Gremlin> parseGremlins(PImage representation){
        /*
        NOTE:
            - The ArrayList instance rarrl stands for
            'return array list'.
         */
        ArrayList<Gremlin> rarrl = new ArrayList<Gremlin>();
        /*
        NOTE:
            - The specifcation states that the gremlins/enemies are listed
            as 'G' in the text files.
         */
        final char IDENTIFIER = 'G';
        try{
            File file_in = new File(this.layout_file_path);
            Scanner jvfin = new Scanner(file_in);
            /*
            NOTE:
                - We are reading the level files here. 
                Since it's two dimensional, we're using
                nested loops.
             */
            for(int i = 0; jvfin.hasNextLine(); i++){
                /*
                NOTE:  
                    - The outer loop deals with the rows.
                 */
                String line = jvfin.nextLine();
                for(int j = 0; j < line.length(); j++){
                    /*
                    NOTE:
                        - And the nested loop deals with
                        the columns.
                     */
                    if(line.charAt(j) == IDENTIFIER){
                        rarrl.add(new Gremlin(representation, j * SPRITE_DIMENSION, i * SPRITE_DIMENSION));
                    }
                }
            }
        }catch(FileNotFoundException e){
            System.err.println("Error: file not found. Message from FileHandling.parseGremlins().");
            return null;
        }

        return rarrl;
    }

    public Wizard parseWizard(PImage[] directions){
        /*
        NOTE:
            - The specifcation states that the wizard is listed
            as 'W' in the text files.
         */
        Wizard player = new Wizard();
        final char IDENTIFIER = 'W';
        try{
            File file_in = new File(this.layout_file_path);
            Scanner jvfin = new Scanner(file_in);
            /*
            NOTE:
                - We are reading the level files here. 
                Since it's two dimensional, we're using
                nested loops.
             */
            for(int i = 0; jvfin.hasNextLine(); i++){
                /*
                NOTE:  
                    - The outer loop deals with the rows.
                 */
                String line = jvfin.nextLine();
                for(int j = 0; j < line.length(); j++){
                    /*
                    NOTE:
                        - And the nested loop deals with
                        the columns.
                     */
                    if(line.charAt(j) == IDENTIFIER){
                        player = new Wizard(directions, j * SPRITE_DIMENSION, i * SPRITE_DIMENSION);
                        return player;
                    }
                }
            }
        }catch(FileNotFoundException e){
            System.err.println("Error: file not found. Message from FileHandling.parseWizard().");
            return null;
        }

        return player;
    }
    public Door parseDoor(PImage representation){
        Door exit = new Door();
        /*
        NOTE:
            - The specifcation states that the doors are listed
            as 'E' in the text files.
         */
        final char IDENTIFIER = 'E';
        try{
            File file_in = new File(this.layout_file_path);
            Scanner jvfin = new Scanner(file_in);
            /*
            NOTE:
                - We are reading the level files here. 
                Since it's two dimensional, we're using
                nested loops.
             */
            for(int i = 0; jvfin.hasNextLine(); i++){
                /*
                NOTE:  
                    - The outer loop deals with the rows
                 */
                String line = jvfin.nextLine();
                for(int j = 0; j < line.length(); j++){
                    /*
                    NOTE:
                        - And the nested loop deals with
                        the columns.
                     */
                    if(line.charAt(j) == IDENTIFIER){
                        exit = new Door(representation, j * SPRITE_DIMENSION, i * SPRITE_DIMENSION);
                        return exit;
                    }
                }
            }
        }catch(FileNotFoundException e){
            System.err.println("Error: file not found. Message from FileHandling.parseDoor().");
            return null;
        }

        return exit;
    }
}