package Starhammer;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import Objects.GameObject;
import Objects.ID;

public class KeyInput extends KeyAdapter {
	
	private Handler handler;
	private Camera camera;
	private HUD hud;
	public static boolean aMove = false;
	public static boolean buildMode = false;
	public static boolean buildingNexus = false;
	
	public KeyInput( Handler handl, Camera cam, HUD hud ) {
		this.handler = handl;
		this.camera = cam;
		this.hud = hud;
	}
	
	public void keyPressed( KeyEvent e ) {
		int key = e.getKeyCode();
		
		/*for( int i=0; i < handler.objectList.size(); ++i ) {
			GameObject object = handler.objectList.get(i);

			if( object.getID() == ID.Marine ) {
				if( key == KeyEvent.VK_W ) object.setVelY(-4);
				if( key == KeyEvent.VK_S ) object.setVelY(4);
				if( key == KeyEvent.VK_A ) object.setVelX(-4);
				if( key == KeyEvent.VK_D ) object.setVelX(4);
			}
			
		}*/
		
		switch( key ) {
		case KeyEvent.VK_ESCAPE:
			if( Starhammer.gameState == State.Game) {
				if( buildMode ) {
					buildMode = false;
					buildingNexus = false;
				}
				else
					Starhammer.gameState = State.Paused;
			}
			break;
		case KeyEvent.VK_RIGHT:
			camera.setX( camera.getX() - 8 );
			break;
		case KeyEvent.VK_LEFT:
			camera.setX( camera.getX() + 8 );
			break;
		case KeyEvent.VK_UP:
			camera.setY( camera.getY() + 8 );
			break;
		case KeyEvent.VK_DOWN:
			camera.setY( camera.getY() - 8 );
			break;
		case KeyEvent.VK_A:
			aMove = true;
			break;
		case KeyEvent.VK_E:
			for( int i=0; i < handler.objectList.size(); ++i ) {
				GameObject object = handler.objectList.get(i);
				if( object.isClicked() && object.getID() == ID.Nexus && !object.isProducing() ) {
					object.setProducing( true );
					object.setProduce( 0 ); //produkuj robotnik
					object.setTimeOfStart( System.currentTimeMillis() );
				}
			}
			break;
		case KeyEvent.VK_B:
			for( int i=0; i < handler.objectList.size(); ++i ) {
				GameObject object = handler.objectList.get(i);
				if( object.isClicked() && object.getID() == ID.Worker && object.getTeam() == Menu.player[0].getTeam() ) { //wybranie zaznaczonego robotnika
					buildMode = true;
				}
			}
			break;
		case KeyEvent.VK_Q:
			if( buildMode ) {
				buildingNexus = true;
			}
			break;
			
		}
	}
	
	public void keyReleased( KeyEvent e ) {
		int key = e.getKeyCode();
		
		for( int i=0; i<handler.objectList.size(); ++i ) {
			GameObject object = handler.objectList.get(i);

			if( object.getID() == ID.Marine ) {
				if( key == KeyEvent.VK_W ) object.setVelY(0);
				if( key == KeyEvent.VK_S ) object.setVelY(0);
				if( key == KeyEvent.VK_A ) object.setVelX(0);
				if( key == KeyEvent.VK_D ) object.setVelX(0);
			}
			
		}
	}
	
}
