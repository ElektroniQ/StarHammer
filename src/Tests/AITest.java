package Tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import Objects.GameObject;
import Objects.ID;
import Objects.Nexus;
import Objects.Worker;
import Starhammer.AI;
import Starhammer.Camera;
import Starhammer.Handler;
import Starhammer.Map;
import Starhammer.Menu;

public class AITest {
	Handler handler = new Handler();
	Map map = new Map( handler, "testmap.txt" );
	Camera camera = new Camera( 0, 0 );
	Menu menu = new Menu( camera, handler, map );
	AI testAI = new AI( menu, handler, map );
	
	@Before
	public void Init() {
		GameObject object;
		handler.addObject( new Worker( 1600, 1500, 2, handler, map, menu ) );
		handler.addObject( new Worker( 1664, 1500, 2, handler, map, menu ) );
		handler.addObject( object = new Nexus( 1600, 1600, 2, handler, map, menu ));
	}
	
	@Test
	public void LookingForResources(){
		GameObject test = handler.get(0);
		assertEquals(1, testAI.lookForClosestResources(test, ID.Minerals) );
		assertEquals(0, test.getPath().getLast().x );
		assertEquals(48, test.getPath().getLast().y );
		
		assertEquals(1, testAI.lookForClosestResources(test, ID.Gold) );
		assertEquals(0, test.getPath().getLast().x );
		assertEquals(49, test.getPath().getLast().y );
	}
	
}
