package Starhammer;

import java.io.File;
import java.util.Scanner;

import Objects.Gold;
import Objects.ID;
import Objects.Minerals;
import Objects.Terrain;

public class Map {
	/*mapa bedzie 50x50 blokow 64x64, z pliku wczytamy po kolei 2500liczb ktore beda ID terenu znajdujacego sie w danym miejscu. Po wczytaniu ID terenu
	 * przelecimy cala tablice sprawdzajac w jakich miejscach pojawilo sie ID terenu i tam wpakujemy go do handlera. W konstruktorach terenu bedzie umieszczone
	 * dalsze wypelnienie mapBoard. Pametaj zeby w notatniku nie budowac scian "super ukosnych" bo PathFinder ma z tym problemy
	 */
	
	public MapVertex[][] mapGrid = new MapVertex[50][50];
	
	public Map( Handler handler, String mapName ){
		ID[] idValues = ID.values();
		
		for(int i=0; i<50; ++i ) {
			for(int j=0; j<50; ++j ) {
				mapGrid[i][j] = new MapVertex();
				mapGrid[i][j].x = j;
				mapGrid[i][j].y = i;
				mapGrid[i][j].cost = 10;
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
				//TODO dodaj wiecej case albo zmien to na ifa
				case Terrain:
					handler.addObject( new Terrain(j*64, i*64, 0));
					mapGrid[i][j].cost = 999;
					break;
				case Minerals:
					handler.addObject( new Minerals(j*64, i*64, 0) );
					break;
				case Gold:
					handler.addObject( new Gold(j*64, i*64, 0) );
					break;
				default:
					break;
				}
					
			}
		}

	}
	
	/*public Map( Map passedMap ){
		
		for(int i=0; i<50; ++i ) {
			for(int j=0; j<50; ++j ) {
				mapGrid[i][j] = new MapVertex();
				mapGrid[i][j].x = j; //wiem ze odwrotnie powinno patrzec sie na tablice 2d ale to moja tablica i bede patrzyl jak chcial
				mapGrid[i][j].y = i; //czy to sie wgl do czegos przyda?
			}
		}
		
		for( int i=0; i < 50; i++ ) {
			for( int j=0; j < 50; j++ ) {
				mapGrid[i][j].id = passedMap.;
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

	}*/
	
	/*public Object clone() throws CloneNotSupportedException{
		Map t = (Map)super.clone();
		t.mapGrid = mapGrid.clone();
		return t;
	}*/

}
