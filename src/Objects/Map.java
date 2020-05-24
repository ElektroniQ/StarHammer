package Objects;

import java.io.File;
import java.util.Scanner;

import Starhammer.Handler;

public class Map {
	
	protected MapVertex[][] mapGrid = new MapVertex[50][50];
	
	public Map( Handler handler, String mapName ){
		ID[] idValues = ID.values();
		
		for(int i=0; i<50; ++i ) {
			for(int j=0; j<50; ++j ) {
				mapGrid[i][j] = new MapVertex();
				mapGrid[i][j].x = i; //wiem ze odwrotnie powinno patrzec sie na tablice 2d ale to moja tablica i bede patrzyl jak chcial
				mapGrid[i][j].y = j; //czy to sie wgl do czegos przyda?
			}
		}
		
		try {
		Scanner scan = new Scanner( new File( mapName ));
		for( int i=0; scan.hasNextInt() && i < 50; i++ ) {
			for( int j=0; scan.hasNextInt() && j < 50; j++ ) {
				mapGrid[i][j].id = idValues[scan.nextInt()];
			}
		}
		scan.close();
		} catch( Exception e) { System.out.println("Nie udalo sie wczytac pliku"); };
		
		if( mapGrid[49][49].id == null )
			System.out.println("Niepelny plik mapy");
		
		for(int i=0; i<50; ++i ) {
			for(int j=0; j<50; ++j ) {
				switch (mapGrid[i][j].id) {
				case Terrain:
					handler.addObject( new Terrain(j*64, i*64, 0));
					mapGrid[i][j].cost = 999;
					break;
				default:
					break;
				}
					
			}
		}

	}

}
