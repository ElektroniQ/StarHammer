package Starhammer;

import java.util.LinkedList;
import java.util.Vector;

public class PathFinder  {
	
	//ACHTUNG pojawia sie problemy jezeli dostep bedzie tu mialo kilka watkow
	//ACHTUNG 2 jesli pojawia sie problemy to znaczy ze trzeba wyzerowywac mape albo ja clonowac
	public static LinkedList<MapVertex> findPath( Map map, MapVertex start, MapVertex goal ) {
		//Map map = (Map)passedMap.clone();

		Vector<MapVertex> closedSet = new Vector<MapVertex>();
		Vector<MapVertex> openSet = new Vector<MapVertex>();
		Vector<MapVertex> neighbours = new Vector<MapVertex>();
		int i, j, k;
		MapVertex tempVertex;
		MapVertex tempNeigh;

		openSet.add( start );
		start.g_score = 0;
		
		while( !openSet.isEmpty() ) {
			tempVertex = openSet.get(0);

			for( i=1; i < openSet.size(); ++i ) {
				if( tempVertex.f_score > openSet.get(i).f_score )
					tempVertex = openSet.get(i);
			}
			
			if( tempVertex == goal ) {
				for( i=0; i<50; ++i ) {
					for( j=0; j<50; ++j ) {
						map.mapGrid[i][j].g_score = 0;
						map.mapGrid[i][j].f_score = 0;
						map.mapGrid[i][j].h_score = 0;
					}
				}
				return path( start, goal );
				
			}
			
			openSet.remove( tempVertex );
			closedSet.add( tempVertex );
			neighbours = findNeighbours( map, tempVertex, neighbours );
			for( k=0; k < neighbours.size(); ++k ) {
				tempNeigh = neighbours.get(k);
				for( i=0; i < closedSet.size() && closedSet.get(i) != tempNeigh; ++i );
				if( i == closedSet.size() ) {
					int temp_g_score = tempVertex.g_score + tempNeigh.cost;
					if( tempNeigh.x != tempVertex.x && tempNeigh.y != tempVertex.y )
						temp_g_score += 5;
					boolean is_better = false;
					for( j=0; j < openSet.size() && openSet.get(j) != tempNeigh; ++j );
					if( j == openSet.size() ) {
						openSet.add( tempNeigh );
						tempNeigh.h_score = Math.abs( goal.x - tempNeigh.x ) + Math.abs( goal.y - tempNeigh.y );
						is_better = true;
					}
					else if( temp_g_score < tempNeigh.g_score )
						is_better = true;
					if( is_better ) {
						tempNeigh.prev = tempVertex;
						tempNeigh.g_score = temp_g_score;
						tempNeigh.f_score = tempNeigh.g_score + tempNeigh.h_score;
					}
				}
			}
			
			
		}
		return path( start, start );
		
	}
	
	private static Vector<MapVertex> findNeighbours( Map map, MapVertex tmp, Vector<MapVertex> neighbours ){
		
		neighbours.clear();
	   /*if (tmp.x - 1 >= 0 ) {
	        neighbours.add(map.mapGrid[tmp.y][tmp.x - 1]);
	        if(tmp.y - 1 >= 0) {
	        	neighbours.add(map.mapGrid[tmp.y - 1][tmp.x]);
	        	neighbours.add(map.mapGrid[tmp.y - 1][tmp.x - 1]);
	        }
	    }
	    else if( tmp.y - 1 >= 0 )
	    	neighbours.add(map.mapGrid[tmp.y - 1][tmp.x]);
	    
	    if( tmp.x + 1 <= 50) {
	    	neighbours.add(map.mapGrid[tmp.y][tmp.x + 1]);
	    	if (tmp.y + 1 <= 50)
	    		neighbours.add(map.mapGrid[tmp.y + 1][tmp.x]);
	    		neighbours.add(map.mapGrid[tmp.y + 1][tmp.x + 1]);
	    }
	    else if( tmp.y + 1 <= 50 )
	    	neighbours.add(map.mapGrid[tmp.y + 1][tmp.x]);
	    
	    if( tmp.x - 1 >= 0 && tmp.y + 1 <= 50 )
	    	neighbours.add(map.mapGrid[tmp.y + 1][tmp.x - 1]);
	    if( tmp.y - 1 >= 0 && tmp.x + 1 <= 50 )
	    	neighbours.add(map.mapGrid[tmp.y - 1][tmp.x + 1]);*/
		
	    if (tmp.x - 1 >= 0 )
	    	neighbours.add(map.mapGrid[tmp.y][tmp.x - 1]);
	    if (tmp.x + 1 <= 50)
	    	neighbours.add(map.mapGrid[tmp.y][tmp.x + 1]);
	    if (tmp.y + 1 <= 50)
	    	neighbours.add(map.mapGrid[tmp.y + 1][tmp.x]);
	    if (tmp.y - 1 >= 0)
	    	neighbours.add(map.mapGrid[tmp.y - 1][tmp.x]);
		if( tmp.x - 1 >= 0 && tmp.y + 1 <= 50 )
	    	neighbours.add(map.mapGrid[tmp.y + 1][tmp.x - 1]);
	    if( tmp.y - 1 >= 0 && tmp.x + 1 <= 50 )
	    	neighbours.add(map.mapGrid[tmp.y - 1][tmp.x + 1]);
	    if( tmp.x - 1 >= 0 && tmp.y - 1 >= 0 )
		    neighbours.add(map.mapGrid[tmp.y - 1][tmp.x - 1]);
		if( tmp.y + 1 <= 50 && tmp.x + 1 <= 50 )
		    neighbours.add(map.mapGrid[tmp.y + 1][tmp.x + 1]);
	    	
	    
		return neighbours;
	}
	
	private static LinkedList<MapVertex> path( MapVertex start, MapVertex goal ){
		MapVertex tmp = goal;

		LinkedList<MapVertex> path = new LinkedList<MapVertex>();
		while( tmp != start ) {
			path.addFirst( tmp );
			tmp = tmp.prev;
		}
		return path;
	}
}
