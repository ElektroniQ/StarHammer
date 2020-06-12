package Objects;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import Starhammer.Handler;
import Starhammer.Map;
import Starhammer.MapVertex;
import Starhammer.Menu;
import Starhammer.PathFinder;
import Starhammer.Starhammer;

public class Worker extends GameObject {

	private Handler handler;
	private Map map;
	private int timeToMine;
	private boolean hasResource;
	private boolean hasMinerals;
	private Menu menu;
	
	public Worker( int x, int y, int team, Handler handler, Map map, Menu menu ) {
		super(x, y, team);

		this.id = ID.Worker;
		this.map = map;
		this.handler = handler;
		this.menu = menu;
		this.clickable = true;
		this.moveable = true;
		this.lookingForEnemy = true;
		
		this.width = 64;
		this.height = 64;
		this.hp = 50;
		this.movementSpeed = 4;
		this.attackRange = 70; //3*this
		this.attackDMG = 10;
		this.attackSpeed = 1f;
		this.timeToMine = 7;
	}

	@Override
	public void tick() {
		if( velX != 0 && velY != 0 ) {
			x += Math.round(this.velX * 0.71);
			y += Math.round(this.velY * 0.71);
		}
		else {
			x += this.velX;
			y += this.velY;
		}
		
		x = GameObject.boarder( x, 0, Starhammer.mapRes-width );
		y = GameObject.boarder( y, 0, Starhammer.mapRes-height );
		
		collision( handler );

		if( moves ) {
			if( !attackMove ) {
				lookingForEnemy = false;
				attacking = false;
			}
			working = false;
			checkIfCloseToDestination();
		}
		
		
		if( lookingForEnemy ) {
			lookForEnemy( handler );
		}
		
		if( attacking ) {
			velX = 0;
			velY = 0;
			if( checkIfInRange(target) ){ //niebezpieczenstwo NULLA
				if( System.currentTimeMillis() - timeOfLastAttack > 1000*attackSpeed ) {
					target.hp -= attackDMG;
					target.beingAttacked = true;
					timeOfLastAttack = System.currentTimeMillis();
					System.out.println(target.hp);
					if( target.hp <= 0) {
						handler.removeObject( target );
						target = null;
						attacking = false;
						lookingForEnemy = true;
						if( attackMove ) {
							moveToGoal();
							moves = true;
						}
					}
				}
			}
			else {
				target.beingAttacked = false;
				target = null;
				attacking = false;
				lookingForEnemy = true;
				
				if( attackMove ) {
					moveToGoal();
					moves = true;
				}
			}
		}
		
		
		if( !working && !moves && !attacking ) { //rozpoczecie pracy
			for( int i=0; i < handler.size(); i++ ) {
				GameObject tempResources = handler.get(i);
				if( (tempResources.getID() == ID.Minerals || tempResources.getID() == ID.Gold) && tempResources.getX() == ((x+width/2)/64)*64 && tempResources.getY() == ((y+height/2)/64)*64 ) {
					setTarget( tempResources );
					if( tempResources.getID() == ID.Minerals )
						hasMinerals = true;
					else
						hasMinerals = false;
					working = true;
					lookingForEnemy = false;
					timeOfStart = System.currentTimeMillis();
				}
			}
		}
		
		if( working ) {
			if( System.currentTimeMillis() - timeOfStart > 1000*timeToMine ) { //procedura zebrania mineralu i znalezienia drogi do nexusa
				hasResource = true;
				if( target != null ) {
					target.hp -= 50;
					if( target.hp <= 0 ) {
						handler.removeObject( target );
						map.mapGrid[target.y/64][target.x/64].id = ID.Void;
					}
				}
				LinkedList<MapVertex> path;
				path = lookForClosestNexus(handler, map);
				working = false;
				
				if( path != null ) {
					this.setPath( path );
					this.setGoal( x+width/2, y+width/2 );
					this.setAttackMove( false );
					this.move();
				}
				else {
					lookingForEnemy = true;
				}
			}
		}
		
		
		if( hasResource ) { //odnoszenie materialu do nexusa
			for( int i=0; i < handler.size(); i++ ) {
				GameObject tempNexus = handler.get(i);
				if( tempNexus.getID() == ID.Nexus && tempNexus.getTeam() == team && checkIfInRectRange( tempNexus, 150 )) {
					
					hasResource = false;
					if( menu.player[0].getTeam() == team )
						if( hasMinerals )
							menu.player[0].addMinerals( 50 );
						else
							menu.player[0].addGold( 25 );
					else
						if( hasMinerals )
							menu.player[1].addMinerals( 50 );
						else
							menu.player[1].addGold( 25 );
					
					LinkedList<MapVertex> path;
					path = PathFinder.findPath(map, map.mapGrid[ (y+height/2)/64 ][ (x+width/2)/64 ], map.mapGrid[ goal2Y / 64 ][ goal2X / 64]); //goal2Y i 2X jest ustawiane w momencie wywolania this.setGoal
					this.setPath( path );
					this.setAttackMove( false );
					this.move();
				}
					
			}
		}
		
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.blue);
		g.fillRect(x, y, width, height);
		if( clicked ) {
			g.setColor(Color.green);
			g.drawRect(x-1, y-1, width+1, height+1);

			g.fillRect(x+8, y+8, (width-16)*hp/50, 8); //hpbar
			g.setColor(Color.red);
			g.fillRect(x+width-8, y+8, ((width-16)*(hp-50)/50), 8);
		
		}
	}

}
