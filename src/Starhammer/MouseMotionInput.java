package Starhammer;

import java.awt.event.MouseMotionListener;


import java.awt.event.MouseEvent;
import java.awt.Robot;


public class MouseMotionInput implements MouseMotionListener {

	@SuppressWarnings("unused")
	private Handler handler;
	private Camera  camera;
	private Robot robot;
	private ClickField clickField;
	private HUD hud;
	
	public MouseMotionInput( Handler handl, Camera cam, Robot robot, ClickField clickF, HUD hud ) {
		this.handler = handl;
		this.camera = cam;
		this.robot = robot;
		this.clickField = clickF;
		this.hud = hud;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX() - camera.getX();
		int y = e.getY() - camera.getY();
		
		if( clickField.isMouse1Pressed() ) {
			clickField.setGoalX( x );
			clickField.setGoalY( y );
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//whole camera movement
		int x = e.getX();
		int y = e.getY();
		int trueX = e.getXOnScreen();
		int trueY = e.getYOnScreen();
		
		if( KeyInput.buildMode ) {
			clickField.setBuildX( x - camera.getX() );
			clickField.setBuildY( y - camera.getY() );
		}
		
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
