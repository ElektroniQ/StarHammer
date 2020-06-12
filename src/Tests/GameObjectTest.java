package Tests;

import org.junit.Test;

import Objects.GameObject;
import Objects.Marine;
import Starhammer.Handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;


public class GameObjectTest{
	
	Handler handler = new Handler();
	@Before
	public void Init() {
		handler.addObject( new Marine( 1000, 1000, 1, handler ) );
		handler.addObject( new Marine( 1150, 1000, 2, handler ) );
		handler.addObject( new Marine( 1150, 1150, 2, handler ) );
	}

    @Test
    public void RangeTest() {
        GameObject target = handler.get(1);
        GameObject target2 = handler.get(2);
        GameObject marine = handler.get(0);


        assertFalse(marine.checkIfInRange(target2));
        assertTrue(marine.checkIfInRectRange(target2, marine.getAttackRange() ));
        assertTrue(marine.checkIfInRange(target));
    }
    
   @Test
    public void AttackTest() {
    	GameObject target2 = handler.get(2);
        GameObject target = handler.get(1);
        GameObject marine = handler.get(0);
        target.setMoves(true);

        handler.tick();
        assertTrue( target.getHp() != marine.getHp() );
        assertEquals( 150, target2.getHp() );
        
        target.setAttackMove(true);
        target.setLookingForEnemy(true);
        handler.tick();
        assertTrue( target.getHp() == marine.getHp() );
        
    }
    
   @Test
   public void CollisionTest() {
   	GameObject test = handler.get(0);
   	GameObject test2 = handler.get(1);
   	test.setX( test2.getX() );

   	handler.tick();
   	assertFalse( test.getX() == test2.getX() );
   	
   	test.setX(-100);
   	test.setY(-100);
   	handler.tick();
   	assertEquals(0, test.getX() );
   	assertEquals(0, test.getY() );
   	test.setX(Starhammer.Starhammer.mapRes + 100);
   	test.setY(Starhammer.Starhammer.mapRes + 100);
   	handler.tick();
   	assertEquals(Starhammer.Starhammer.mapRes, test.getX() + test.getWidth() );
   	assertEquals(Starhammer.Starhammer.mapRes, test.getY() + test.getHeight() );
   }
    
}
