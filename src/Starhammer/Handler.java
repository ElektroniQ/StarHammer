package Starhammer;

import java.awt.Graphics;
import java.util.LinkedList;

import Objects.GameObject;

public class Handler {
	
	LinkedList<GameObject> objectList = new LinkedList<GameObject>();
	Menu menu;

	public void tick() {
		GameObject tempObject;
		for(int i=0; i < objectList.size(); i++) {
			tempObject = objectList.get(i);
			tempObject.tick();
		}
	}
	
	public void render( Graphics g ) {
		GameObject tempObject;
		for(int i=0; i < objectList.size(); i++) {
			tempObject = objectList.get(i);
			tempObject.render( g );
		}
	}
	
	public GameObject addObject( GameObject object ) {
		this.objectList.add( object );
		return object;
	}
	
	public void removeObject( GameObject object ) {
		this.objectList.remove( object );
		boolean isThereTeam1 = false; //Winning condition starts here
		boolean isThereTeam2 = false;
		GameObject tempObject;
		
		for(int i=0; i < objectList.size(); i++) {
			tempObject = objectList.get(i);
			if( tempObject.getTeam() == 1 )
				isThereTeam1 = true;
			if( tempObject.getTeam() == 2 )
				isThereTeam2 = true;
		}
		if( !isThereTeam1 ) {
			if( menu.player[0].getTeam() == 1 )
				menu.player[0].setWinner( true );
			else
				menu.player[1].setWinner( true );
			menu.setGameState( State.GG );
		}
		if( !isThereTeam2 ) {
			if( menu.player[0].getTeam() == 1 )
				menu.player[0].setWinner( true );
			else
				menu.player[1].setWinner( true );
			menu.setGameState( State.GG );
		}
	}
	
	public int size() {
		return objectList.size();
	}
	
	public GameObject get( int i ) {
		return objectList.get(i);
	}
}
