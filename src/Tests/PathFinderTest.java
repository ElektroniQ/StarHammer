package Tests;

import org.junit.Test;

import Objects.GameObject;
import Objects.Marine;
import Objects.Nexus;
import Starhammer.Camera;
import Starhammer.Handler;
import Starhammer.Map;
import Starhammer.MapVertex;
import Starhammer.Menu;
import Starhammer.PathFinder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;


public class PathFinderTest{
	Handler handler = new Handler();
	Map map = new Map(handler, "testmap.txt");
	Camera camera = new Camera( 0, 0 );
	Menu menu = new Menu( camera, handler, map );

    @Test
    public void pathFindingTest() {
    	LinkedList<MapVertex> path;
    	MapVertex[] mv = new MapVertex[5];
    	for(int i=0; i<5; ++i ) {
    		mv[i] = new MapVertex();
    	}
    	mv[0].x = 6;
    	mv[0].y = 5;
    	mv[1].x = 10;
    	mv[1].y = 5;
    	
    	mv[2].x = 0;
    	mv[2].y = 1;
    	mv[3].x = 1;
    	mv[3].y = 5;
    	mv[4].x = 2;
    	mv[4].y = 2;
    
    	
		path = PathFinder.findPath( map, map.mapGrid[ 5 ][ 5 ], map.mapGrid[5][10] );
		assertEquals(mv[0].x, path.getFirst().x);
		assertEquals(mv[0].y, path.getFirst().y);
		assertEquals(mv[1].x, path.getLast().x);
		assertEquals(mv[1].y, path.getLast().y);
		
		path = PathFinder.findPath( map, map.mapGrid[ 0 ][ 0 ], map.mapGrid[2][2] );
		assertEquals(mv[2].x, path.getFirst().x);
		assertEquals(mv[2].y, path.getFirst().y);
		assertEquals(mv[3].x, path.get(4).x);
		assertEquals(mv[3].y, path.get(4).y);
		assertEquals(mv[4].x, path.getLast().x);
		assertEquals(mv[4].y, path.getLast().y);
		
    }
    
    @Test
    public void LookingForClosestNexusTest() {
    	GameObject test = new Marine( 1500, 1280, 1, handler); //prawo
    	GameObject test2 = new Marine( 1200, 1280, 1, handler); //lewo
    	GameObject test3 = new Marine( 1380, 1180, 1, handler); //gora
    	GameObject test4 = new Marine( 1280, 1500, 1, handler); //dol
    	GameObject nexus;
    	LinkedList<MapVertex> path;
		handler.addObject( nexus = new Nexus( 1280, 1280, 1, handler, map, menu));
		path = test.lookForClosestNexus(handler, map);
		assertNull( path );
		
		nexus.setBuild(true);
		path = test.lookForClosestNexus(handler, map);
		assertNotNull(path);
		assertTrue( path.size() < 5);
		assertEquals(23, path.getLast().x );
		assertEquals(21, path.getLast().y );
		
		path = test2.lookForClosestNexus(handler, map);
		assertTrue( path.size() < 5 );
		assertEquals(19, path.getLast().x );
		assertEquals(21, path.getLast().y );
		
		path = test3.lookForClosestNexus(handler, map);
		assertTrue( path.size() < 5 );
		assertEquals(21, path.getLast().x );
		assertEquals(19, path.getLast().y );
		
		path = test4.lookForClosestNexus(handler, map);
		assertTrue( path.size() < 5 );
		assertEquals(21, path.getLast().x );
		assertEquals(23, path.getLast().y );
		
		
    }
    
}