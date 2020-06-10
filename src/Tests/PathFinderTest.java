package Tests;

import org.junit.Test;

import Starhammer.Handler;
import Starhammer.Map;
import Starhammer.MapVertex;
import Starhammer.PathFinder;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;


public class PathFinderTest{
	Handler handler = new Handler();
	Map map = new Map(handler, "testmap.txt");

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
    
}