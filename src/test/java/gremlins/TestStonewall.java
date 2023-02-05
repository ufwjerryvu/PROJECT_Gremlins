package gremlins;

import processing.core.*;
import processing.data.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestStonewall {
    
    @Test
    public void testConstructor(){
        /*
        NOTE:
            - Testing two constructors in one. The other constructor
            has been tested elsewhere.
         */
        Stonewall test_1 = new Stonewall(100, 260);
        assertTrue(test_1.getX() == 100 && test_1.getY() == 260);
        assertNull(test_1.getRepresentation());

        Stonewall test_2 = new Stonewall();
        assertTrue(test_2.getX() == -1 && test_2.getY() == -1);
        assertNull(test_2.getRepresentation());
    }
}
