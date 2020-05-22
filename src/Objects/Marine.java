package Objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import Starhammer.Handler;
import Starhammer.Starhammer;

public class Marine extends GameObject{

	Handler handler;
	
	public Marine(int x, int y, ID id, Handler handler) {
		super(x, y, id);
		this.handler = handler;
		this.width = 64;
		this.height = 64;
		this.clickable = true;
	}
	
	@Override
	public void tick() {
		x += this.velX;
		y += this.velY;
		
		x = Starhammer.boarder( x, 0, Starhammer.trueWIDTH - width );
		y = Starhammer.boarder( y, 0, Starhammer.trueHEIGHT - height );
		
		for( int i=0; i < handler.size(); i++ ) { //collision start
			GameObject temp = handler.get(i);
			//TODO do poprawy
			if( temp.getBounds().intersects( getBounds() ) && this!=temp )
				if( temp.getID() == ID.Terrain || temp.getID() == ID.Marine )
					if( temp.getX() < x )
						if( temp.getBounds().intersectsLine(x, y+6, x, y+height-6) )  //this random number must be higher than maxspeed of unit
							x = temp.getX() + temp.getWidth();
						else if ( temp.getY() > y )
							y = temp.getY() - height;
						else
							y = temp.getY() + temp.getHeight();
					else
						if( temp.getBounds().intersectsLine(x+width, y+6, x+width, y+height-6) )  //same here
							x = temp.getX() - width;
						else if ( temp.getY() > y )
							y = temp.getY() - height;
						else
							y = temp.getY() + temp.getHeight(); //collision ends
						
		}
		if( moves ) {
			checkIfCloseToDestination();
		}
	}
	
	@Override
	public void render( Graphics g ) {
		g.setColor(Color.white);
		g.fillRect(x, y, width, height);
		if( clicked ) {
			g.setColor(Color.green);
			g.drawRect(x-1, y-1, width+1, height+1);
		}
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle( x, y, width, height );
	}
	
	@Override
	public void move( int goalxPassed, int goalyPassed ){
		goalX = goalxPassed;
		goalY = goalyPassed;
		if( x+(width/2) < goalX ) 
			velX = 4;
		else 
			velX = -4;
		
		if( y+(height/2) < goalY ) 
			velY = 4;
		else 
			velY = -4;
		
		moves = true;
	}

}
