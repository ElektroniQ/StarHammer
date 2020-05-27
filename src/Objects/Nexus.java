package Objects;

import java.awt.Color;
import java.awt.Graphics;

import Starhammer.Handler;
import Starhammer.Map;

public class Nexus extends GameObject {

	Handler handler;
	long timeToProduce;
	long startTimeOfProduction;
	long completionPercent;
	
	public Nexus(int x, int y, int team, Handler handler, Map map ) {
		super(x, y, team);
		
		this.id = ID.Building;
		this.handler = handler;
		this.clickable = true;
		this.width = 192;
		this.height = 192;
		this.hp = 3000;
		this.produce = new boolean[1];
		this.timeToProduce = 15;
		
		
		for(int i=0; i<3; ++i ) {
			for(int j=0; j<3; ++j ) {
				map.mapGrid[y/64+i][x/64+j].cost = 999;
			}
		}
	}

	@Override
	public void tick() {
		if( produce[0] ) {
			completionPercent = (System.currentTimeMillis() - timeOfStart) / (10*timeToProduce);
			if( completionPercent >= 100 ) {
				handler.addObject( new Worker( x+width/2, y+height+1, team, handler) );
				produce[0] = false;
				completionPercent = 0;
			}
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(x, y, width, height);
		if( clicked ) {
			g.setColor(Color.green);
			g.drawRect(x-1, y-1, width+1, height+1);

			g.fillRect(x+32, y+32, (width-64)*hp/3000, 8); //hpbar
			g.setColor(Color.red);
			g.fillRect(x+width-32, y+32, ((width-64)*(hp-3000)/3000), 8);
			
			g.setColor(Color.DARK_GRAY);
			g.drawString("'e' aby zbudowac robotnikow", x+32, y+190);
		}
		if( produce[0] )
			//g.setColor( Color.red );
			g.drawRect( x+16, y+48, width-32, 16);
			g.setColor( Color.BLUE );
			g.fillRect(x+16, y+48, (width-32)*(int)completionPercent/100, 16);
	}
	

}
