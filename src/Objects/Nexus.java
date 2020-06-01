package Objects;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import Starhammer.Handler;
import Starhammer.Map;
import Starhammer.MapVertex;
import Starhammer.PathFinder;

public class Nexus extends GameObject {

	private final int maxHp = 3000;
	private final int fullBuildPercent = 90;
	Handler handler;
	Map map;
	long timeToProduce;
	long startTimeOfProduction;
	long completionPercent;
	
	
	public Nexus(int x, int y, int team, Handler handler, Map map ) {
		super(x, y, team);
		
		this.id = ID.Nexus;
		this.handler = handler;
		this.clickable = true;
		this.width = 192;
		this.height = 192;
		this.hp = 300;
		this.produce = new boolean[1];
		this.build = false;
		this.timeToProduce = 1;
		this.completionPercent = 9;
		this.goalX = x+width/3 + 32;
		this.goalY = y+height+32; //ten goal x,y wskazuja spod nexusa
		this.map = map;
		
		
		for(int i=0; i<3; ++i ) {
			for(int j=0; j<3; ++j ) {
				map.mapGrid[y/64+i][x/64+j].cost = 999;
			}
		}
	}

	@Override
	public void tick() {
		if( build ) {
			if( target != null ) {
				goalX = target.getX() + target.getWidth()/2;
				goalY = target.getY() + target.getHeight()/2;
			}
			
			if( produce[0] ) {
				completionPercent = (System.currentTimeMillis() - timeOfStart) / (10*timeToProduce);
				if( completionPercent >= 100 ) {
					GameObject temp = handler.addObject( new Worker( x+width/3, y+height+1, team, handler, map) );
					LinkedList<MapVertex> path;
					path = PathFinder.findPath( map, map.mapGrid[ (y+height + 32) / 64 ][ (x+width/3 + 32) / 64 ], map.mapGrid[goalY/64][goalX/64] );
		
					
					produce[0] = false;
					producing = false;
					completionPercent = 0;
					temp.setPath( path );
					temp.setGoal( goalX, goalY );
					temp.setAttackMove( false );
					temp.move();
					
				}
			}
		}
		else {
			for( int i=0; i < handler.size(); i++ ) {
				GameObject object = handler.get(i);
				if( object.getID() == ID.Worker && object.getTeam() == team && checkIfInRectRange( object, 150 ) )
					if( System.currentTimeMillis() - timeOfStart > 1000 ) {
						hp = hp + 100;
						completionPercent+=3;
						timeOfStart = System.currentTimeMillis();
						if( completionPercent >= fullBuildPercent ) //90 poniewaz brakuje nam do fullHp 2700hp
							build = true;
					}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		if( build ) {
			g.setColor(Color.GRAY); //body
			g.fillRect(x, y, width, height);
			if( clicked ) {
				g.setColor(Color.DARK_GRAY);
				g.drawLine(x+width/2, y+width/2, goalX, goalY); //rallypoint
				
				g.setColor(Color.GREEN);
				g.drawRect(x-1, y-1, width+1, height+1); //oznaczenie ze zaznaczony
	
				g.fillRect(x+32, y+32, (width-64)*hp/maxHp, 8); //hpbar
				g.setColor(Color.RED);
				g.fillRect(x+width-32, y+32, ((width-64)*(hp-maxHp)/maxHp), 8);
				
				g.setColor(Color.DARK_GRAY);
				g.drawString("'e' aby zbudowac robotnikow", x+32, y+190);
			}
			if( produce[0] ) {
				g.drawRect( x+16, y+48, width-32, 16);
				g.setColor(Color.BLUE);
				g.fillRect(x+16, y+48, (width-32)*(int)completionPercent/100, 16);
			}
		}
		else { //jesli jest niezbudowany
			g.setColor(Color.GRAY); //body
			g.fillRect(x+width/2 - (width/2)*(int)completionPercent/fullBuildPercent, y+height/2 - (height/2)*(int)completionPercent/fullBuildPercent, width*(int)completionPercent/fullBuildPercent, height*(int)completionPercent/fullBuildPercent);
			
			g.setColor(Color.GREEN);
			g.fillRect(x+32, y+32, (width-64)*hp/maxHp, 8); //hpbar
			g.setColor(Color.RED);
			g.fillRect(x+width-32, y+32, ((width-64)*(hp-maxHp)/maxHp), 8);
		}
	}
	

}
