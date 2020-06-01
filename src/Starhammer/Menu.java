package Starhammer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;

import Objects.GameObject;
import Objects.Marine;
import Objects.Nexus;
import Objects.Worker;


public class Menu {

	Camera camera;
	Handler handler;
	Map map;
	public static Player[] player;
	
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
		GameObject object;
		Random rd = new Random();
		player = new Player[2];
		int random = (rd.nextInt() % 2); // 0 lub 1
		//if( random == 0 ) {
			player[0] = new Player( 1 );
			player[1] = new Player( 2 );
		//}
		//else {
		//	player[0] = new Player( 2 );
		//	player[1] = new Player( 1 );
		//}
		if( player[0].getTeam() == 1 ) {
			camera.setX(0);
			camera.setY(0);
		}
		else {
			camera.setX(-1920);
			camera.setY(-2300);
		}

		handler.addObject( object = new Nexus( 128, 128, 1, handler, map));
		object.setHp(3000);
		object.setBuild(true);
		handler.addObject( new Worker( 300, 400, 1, handler, map ) );
		handler.addObject( new Worker( 364, 400, 1, handler, map ) );
		handler.addObject( new Worker( 300, 464, 1, handler, map ) );
		handler.addObject( new Marine( 300, 400, 1, handler ) );

		
		handler.addObject( object = new Nexus( 2880, 2880, 2, handler, map ));
		object.setHp(3000);
		object.setBuild(true);
		handler.addObject( new Worker( 2800, 2800, 2, handler, map ) );
		handler.addObject( new Worker( 2864, 2800, 2, handler, map ) );
		handler.addObject( new Worker( 2928, 2800, 2, handler, map ) );
		handler.addObject( new Marine( 2700, 2700, 2, handler ) );

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
