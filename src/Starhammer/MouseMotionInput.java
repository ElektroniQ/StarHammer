package Starhammer;

import java.awt.event.MouseMotionListener;

import Objects.GameObject;
import Objects.ID;

import java.awt.event.MouseEvent;
import java.awt.Robot;
import Objects.GameObject;

public class MouseMotionInput implements MouseMotionListener {

	Handler handler;
	Camera  camera;
	Robot robot;
	
	public MouseMotionInput( Handler hand, Camera cam, Robot rob ) {
		this.handler = hand;
		this.camera = cam;
		this.robot = rob;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX() - camera.getX();
		int y = e.getY() - camera.getY();
		
		for( int i=0; i < handler.size(); ++i ) {
			GameObject object = handler.get(i);
			if( object.getID() == ID.ClickField && object.isClicked() ) {
				object.setGoalX(x);
				object.setGoalY(y);
				object.setMoves( true );
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//whole camera movement
		int x = e.getX();
		int y = e.getY();
		int trueX = e.getXOnScreen();
		int trueY = e.getYOnScreen();
		
		
		switch (x){
		case 0:
			x = (int)camera.getX();
			camera.setX( x += 2 );
			robot.mouseMove(trueX+2, trueY);
			break;
		case 1:
			x = (int)camera.getX();
			camera.setX( x += 2 );
			robot.mouseMove(trueX+1, trueY);
			break;
		case Starhammer.trueWIDTH-1:
			x = (int)camera.getX();
			camera.setX( x -= 2 );
			robot.mouseMove(trueX-1, trueY);
			break;
		case Starhammer.trueWIDTH:
			x = (int)camera.getX();
			camera.setX( x -= 2 );
			robot.mouseMove(trueX-2, trueY);
			break;
		}
		switch (y) {
		case 0:
			y = (int)camera.getY();
			camera.setY( y += 2 );
			robot.mouseMove(trueX, trueY+2);
			break;
		case 1:
			y = (int)camera.getY();
			camera.setY( y += 2 );
			robot.mouseMove(trueX, trueY+1);
			break;
		case Starhammer.trueHEIGHT-1:
			y = (int)camera.getY();
			camera.setY( y -= 2 );
			robot.mouseMove(trueX, trueY-1);
			break;
		case Starhammer.trueHEIGHT:
			y = (int)camera.getY();
			camera.setY( y -= 2 );
			robot.mouseMove(trueX, trueY-2);
			break;
		}
		
	}

}
