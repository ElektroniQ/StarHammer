package Starhammer;

import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import Objects.GameObject;



public class MouseInput implements MouseListener {

	private Map map;
	private Handler handler;
	private Camera camera;
	private Robot robot;
	private ClickField clickField;
	
	public MouseInput( Handler handl, Camera cam, Robot robot, ClickField clickF, Map map ) {
		this.handler = handl;
		this.camera = cam;
		this.robot = robot;
		this.clickField = clickF;
		this.map = map;
	}
	
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX() - camera.getX();
		int y = e.getY() - camera.getY(); 

		if( e.getButton() == MouseEvent.BUTTON1 ) {
			clickField.setX( x );
			clickField.setY( y );
			clickField.setGoalX( x+1 );
			clickField.setGoalY( y+1 );
			clickField.setClicked( true );
			
		}
		else if( e.getButton() == MouseEvent.BUTTON3 ) {
			for( int i=0; i<handler.size(); ++i ) {
				GameObject object = handler.get(i);
				if( object.isClicked() ) {
					//object.move( x, y );
					LinkedList<MapVertex> path;
					path = PathFinder.findPath(map, map.mapGrid[object.getY()/64][object.getX()/64], map.mapGrid[y/64][x/64]);
					for(int j=0; j < path.size(); j++) {
						System.out.println("x"+path.get(j).x+" "+"y" + path.get(j).y);
					}
					object.setPath( path );
					object.setGoal( x, y );
					object.move();

				}
			}
		}
		System.out.println("x"+x+" "+"y" + y);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if( e.getButton() == MouseEvent.BUTTON1 ) {
			for( int j=0; j<handler.size(); ++j ) {
				GameObject tempObject = handler.get(j);
						
				if( tempObject.isClickable() && tempObject.getBounds().intersects( clickField.getBounds() ))
					tempObject.setClicked( true );
				else
					tempObject.setClicked( false );
						
			}
			clickField.setX( 0 );
			clickField.setY( 0 );
			clickField.setGoalX( 0 );
			clickField.setGoalY( 0 );
			clickField.setClicked( false );
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//camera movement starts here
		int x = e.getX();
		int y = e.getY();
		int trueX = e.getXOnScreen();
		int trueY = e.getYOnScreen();
		if( x <= 0 )
			robot.mouseMove(trueX - x + 1, trueY);
		if( y <= 0 )
			robot.mouseMove(trueX, trueY - y + 1);//1 potrzebne jest gdy wycodzimy za ekran po skosie
		if( x >= Starhammer.trueWIDTH )
			robot.mouseMove(trueX - x + Starhammer.trueWIDTH - 1, trueY);
		if( y >= Starhammer.trueHEIGHT )
			robot.mouseMove(trueX, trueY - y - 1 + Starhammer.trueHEIGHT );
		//camera end, what a shity code
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		return super.toString();
	}


	@Override
	protected void finalize() throws Throwable {
		//super.finalize();
	}

}


