package Starhammer;

import java.awt.Graphics;
import java.util.LinkedList;

import Objects.GameObject;

public class Handler {
	LinkedList<GameObject> objectList = new LinkedList<GameObject>();
	
	public void tick() {
		for(int i=0; i < objectList.size(); i++) {
			GameObject tempObject = objectList.get(i);
			tempObject.tick();
		}
	}
	
	public void render( Graphics g ) {
		for(int i=0; i < objectList.size(); i++) {
			GameObject tempObject = objectList.get(i);
			tempObject.render( g );
		}
	}
	
	public GameObject addObject( GameObject object ) {
		this.objectList.add( object );
		return object;
	}
	
	public void removeObject( GameObject object ) {
		this.objectList.remove( object ); 
	}
	
	public int size() {
		return objectList.size();
	}
	
	public GameObject get( int i ) {
		return objectList.get(i);
	}
}
