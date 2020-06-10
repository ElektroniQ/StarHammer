package Tests;

import org.junit.Test;

import Objects.GameObject;
import Objects.Marine;
import Starhammer.Handler;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class GameObjectTest{

    @Test
    public void RangeTest() {
    	//assertTrue(true);
    	Handler handler = new Handler();
        GameObject target = new Marine( 300, 400, 1, handler);
        GameObject target2 = new Marine( 550, 400, 1, handler);// MyClass is tested
        GameObject marine = new Marine( 350, 400, 2, handler);

        //assert statements
        assertFalse(marine.checkIfInRange(target2));
        assertTrue(marine.checkIfInRange(target));
    }
    
}
