package Starhammer;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import Objects.GameObject;
import Objects.ID;

public class KeyInput extends KeyAdapter {
	
	private Handler handler;
	private Camera camera;
	private HUD hud;
	private Menu menu;
	
	public KeyInput( Handler handl, Camera cam, HUD hud, Menu menu ) {
		this.handler = handl;
		this.camera = cam;
		this.hud = hud;
		this.menu = menu;
	}
	
	public void keyPressed( KeyEvent e ) {
		int key = e.getKeyCode();
		
		switch( key ) {
		case KeyEvent.VK_ESCAPE:
			if( menu.getGameState() == State.Game) {
				if( menu.buildMode ) {
					menu.buildMode = false;
					menu.buildingNexus = false;
					menu.buildingBarracks = false;
				}
				else
					menu.setGameState(State.Paused);
			}
			else if( menu.getGameState() == State.GG ) {
				menu.setGameState(State.Menu);
			}
			break;
			
		case KeyEvent.VK_RIGHT:
			camera.setX( camera.getX() - 40 );
			break;
			
		case KeyEvent.VK_LEFT:
			camera.setX( camera.getX() + 40 );
			break;
			
		case KeyEvent.VK_UP:
			camera.setY( camera.getY() + 40 );
			break;
			
		case KeyEvent.VK_DOWN:
			camera.setY( camera.getY() - 40 );
			break;
			
		case KeyEvent.VK_A:
			menu.aMove = true;
			break;
			
		case KeyEvent.VK_E:
			for( int i=0; i < handler.objectList.size(); ++i ) {
				GameObject object = handler.objectList.get(i);
				if( object.isClicked() && object.getID() == ID.Nexus && !object.isProducing() && menu.player[0].getMinerals() >= ID.Worker.getMineralCost() ) {
					menu.player[0].addMinerals(-ID.Worker.getMineralCost());
					object.setProducing( true );
					object.setProduce( 0 ); //produkuj robotnik
					object.setTimeOfStart( System.currentTimeMillis() );
				}
			}
			break;
			
		case KeyEvent.VK_R:
			for( int i=0; i < handler.objectList.size(); ++i ) {
				GameObject object = handler.objectList.get(i);
				if( object.isClicked() && object.getID() == ID.Barracks && !object.isProducing() && menu.player[0].getMinerals() >= ID.Marine.getMineralCost() && menu.player[0].getGold() >= ID.Marine.getGoldCost() ) {
					menu.player[0].addMinerals(-ID.Marine.getMineralCost());
					menu.player[0].addGold(-ID.Marine.getGoldCost());
					object.setProducing( true );
					object.setProduce( 0 ); //produkuj Marine
					object.setTimeOfStart( System.currentTimeMillis() );
				}
			}
			break;
			
		case KeyEvent.VK_B:
			for( int i=0; i < handler.objectList.size(); ++i ) {
				GameObject object = handler.objectList.get(i);
				if( object.isClicked() && object.getID() == ID.Worker && object.getTeam() == menu.player[0].getTeam() ) { //wybranie zaznaczonego robotnika
					menu.buildMode = true;
				}
			}
			break;
			
		case KeyEvent.VK_Q:
			if( menu.buildMode ) {
				menu.buildingBarracks = false;
				menu.buildingNexus = true;
			}
			break;
			
		case KeyEvent.VK_W:
			if( menu.buildMode ) {
				menu.buildingNexus = false;
				menu.buildingBarracks = true;
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
