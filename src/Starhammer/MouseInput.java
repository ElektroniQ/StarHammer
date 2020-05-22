package Starhammer;

import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import Objects.GameObject;
import Objects.ID;


public class MouseInput implements MouseListener {

	Handler handler;
	Camera camera;
	Robot robot;
	
	public MouseInput( Handler handl, Camera cam, Robot robot ) {
		this.handler = handl;
		this.camera = cam;
		this.robot = robot;
	}
	
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX() - camera.getX();
		int y = e.getY() - camera.getY(); 

		if( e.getButton() == MouseEvent.BUTTON1 ) {
			for( int i=0; i<handler.size(); ++i ) {
				GameObject object = handler.get(i);
					if( object.isClickable() )
						if( object.getBounds().contains(x,y) )
							object.setClicked( true );
						else
							object.setClicked( false );
					if( object.getID() == ID.ClickField ) {
						object.setX( x );
						object.setY( y );
						object.setGoalX(x);
						object.setGoalY(y);
						object.setClicked( true );
					}
			}
		}
		else if( e.getButton() == MouseEvent.BUTTON3 ) {
			for( int i=0; i<handler.size(); ++i ) {
				GameObject object = handler.get(i);
				if( object.isClicked() )
					object.move( x, y );
			}
		}
		System.out.println("x"+x+" "+"y" + y);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for( int i=0; i<handler.size(); ++i ) {
			GameObject object = handler.get(i);
			if( object.getID() == ID.ClickField && object.isMoves() ) {
				for( int j=0; j<handler.size(); ++j ) {
					GameObject tempObject = handler.get(j);
					
					if( tempObject.isClickable() && object.getBounds().intersects( tempObject.getBounds() ) && object != tempObject )
						tempObject.setClicked( true );
					else
						tempObject.setClicked( false );
					
				}
				
				object.setX( 0 );
				object.setY( 0 );
				object.setGoalX(0);
				object.setGoalY(0);
				object.setClicked( false );
				object.setMoves( false );
			}
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


