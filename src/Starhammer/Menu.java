package Starhammer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import Objects.Marine;
import Objects.Nexus;
import Objects.Worker;


public class Menu {

	Camera camera;
	Handler handler;
	Map map;
	
	public Menu( Camera cam, Handler hand, Map map ) {
		this.camera = cam;
		this.handler = hand;
		this.map = map;
	}
	
	public void render( Graphics g ) {
		Font myFont = new Font( "arial", 0, 50 );
		g.setFont( myFont );
		
		if( Starhammer.gameState == State.Menu ) {
			g.setColor( Color.red );
			g.drawRect( 480, 100, 300, 100 );
			g.drawRect( 480,  300,  300,  100 );
			g.setColor(Color.white);
			g.drawString("Jazda", 565, 165);
			g.drawString("Exit", 590, 365);
		}
		else {
			g.setColor( Color.cyan );
			g.drawRect( 480-camera.getX(), 100-camera.getY(), 300, 100 );
			g.drawRect( 480-camera.getX(),  300-camera.getY(),  300,  100 );
			g.setColor(Color.white);
			g.drawString("Resume", 540-camera.getX(), 165-camera.getY() );
			g.drawString("Exit", 590-camera.getX(), 365-camera.getY());
		}
	}
	
	public void startGame() {
		handler.addObject( new Nexus( 128, 128, 1, handler, map));
		handler.addObject( new Worker( 300, 400, 1, handler ) );
		handler.addObject( new Worker( 364, 400, 1, handler ) );
		handler.addObject( new Worker( 300, 464, 1, handler ) );
		handler.addObject( new Worker( 364, 464, 1, handler ) );
		handler.addObject( new Marine( 300, 400, 1, handler ) );
		handler.addObject( new Marine( 300, 400, 1, handler ) );
		handler.addObject( new Marine( 300, 400, 1, handler ) );
		handler.addObject( new Marine( 800, 400, 2, handler ) );
		handler.addObject( new Marine( 800, 400, 2, handler ) );
		handler.addObject( new Marine( 800, 400, 2, handler ) );
	}
	
	public static boolean isOverButtonStart(int x, int y) {
		if( x > 480 && x < 780 && y > 100 && y < 200 )
			return true;
		else
			return false;
	}
	
	public static boolean isOverButtonEnd(int x, int y) {
		if( x > 480 && x < 780 && y > 300 && y < 400 )
			return true;
		else
			return false;
	}
	
}
