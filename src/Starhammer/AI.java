package Starhammer;

import java.util.LinkedList;

import Objects.Barracks;
import Objects.GameObject;
import Objects.ID;

public class AI {
	private Menu menu;
	private Handler handler;
	private Map map;
	private long timeOfLastTick;
	private long reactionTime;
	private int resourceCheckTimer;
	private int startPositionX;
	private int startPositionY;
	private int lastMinerals;
	private int lastGold;
	private int howBigArmy;
	boolean somethingToBuild;
	boolean firstBarracks;
	boolean twoMarines;
	boolean someoneOnGold;
	
	public AI( Menu menu, Handler handler, Map map ) {
		this.menu = menu;
		this.handler = handler;
		this.map = map;
		this.timeOfLastTick = System.currentTimeMillis();
		this.reactionTime = 1;
		this.startPositionX = 2880;
		this.startPositionY = 2880;
		this.resourceCheckTimer = 0;
	}
	
	public void tick() {
		if( System.currentTimeMillis() - timeOfLastTick > 1000*reactionTime ) {
			++resourceCheckTimer;
			somethingToBuild = false;
			howBigArmy = 0;
			for( int i=0; i < handler.size(); i++ ) {
				GameObject object = handler.get(i); //wysylanie robotnikow do pracy
				if( object.getTeam() == 2 ) {
					if( object.getID() == ID.Worker && !object.isMoves() && !object.isBuild() && !someoneOnGold ) {
						lookForClosestResources( object, ID.Gold);
						someoneOnGold = true;
					}
					if (object.getID() == ID.Worker && !object.isWorking() && !object.isMoves() && !object.isBuild())
						lookForClosestResources( object, ID.Minerals);
					if( (object.getID() == ID.Nexus || object.getID() == ID.Barracks) && !object.isBuild() )
						somethingToBuild = true;
					if( object.getID() == ID.Barracks && menu.player[1].getMinerals() >= ID.Marine.getMineralCost() && menu.player[1].getGold() >= ID.Marine.getGoldCost() ) {
						menu.player[1].addMinerals(-ID.Marine.getMineralCost());
						menu.player[1].addGold(-ID.Marine.getGoldCost());
						object.setProducing( true );
						object.setProduce( 0 ); //produkuj Marine
						object.setTimeOfStart( System.currentTimeMillis() );
					}
					if( object.getID() == ID.Marine )
						++howBigArmy;
				
				}		
			}
			
			if( !somethingToBuild ) {
				for( int i=0; i < handler.size(); i++ ) {
					GameObject object = handler.get(i);
					if( object.getTeam() == 2 && object.getID() == ID.Worker && object.isBuild() )
						object.setBuild( false );
				}
			}
			if( menu.player[1].getMinerals() >= ID.Barracks.getMineralCost() && !firstBarracks ) { //budowa barakow
				int baracksPlaceX = (startPositionX/64-5)*64;
				int baracksPlaceY = (startPositionY/64-2)*64;
				handler.addObject( new Barracks( baracksPlaceX, baracksPlaceY, menu.player[1].getTeam(), handler, map) );
				menu.player[1].addMinerals( -ID.Barracks.getMineralCost() );
				
				for( int i=0; i < handler.size(); i++ ) {
					GameObject object = handler.get(i);
					if( object.getTeam() == 2 && object.getID() == ID.Worker ) {
						LinkedList<MapVertex> path;
						path = PathFinder.findPath(map, map.mapGrid[ (object.getY()+object.getHeight()/2) / 64][ (object.getX() + object.getWidth()/2) / 64 ], map.mapGrid[ baracksPlaceY/64][ baracksPlaceX/64+2 ]);
						object.setPath( path );
						object.setGoal( baracksPlaceX + 170, baracksPlaceY );
						object.move();
						object.setBuild( true );
						break; //wystarczy nam jeden robotnik przy budowie
					}
				}
				firstBarracks = true;
			}
			if( resourceCheckTimer >= 20 ) {
				if( menu.player[1].getGold() == lastGold )
					someoneOnGold = false;
				//if( menu.player[1]).getMinerals() == lastMinerals)
					//buildWorker()
				resourceCheckTimer -= 20;
				lastGold = menu.player[1].getGold();
				lastMinerals = menu.player[1].getMinerals();
			}
			if( howBigArmy >= 2 ) {
				for( int i=0; i < handler.size(); i++ ) {
					GameObject object = handler.get(i);
					if( object.getTeam() == 2 && object.getID() == ID.Marine && !object.isMoves() && !object.isAttacking() )
						lookForEnemy( object );
				}
			}
			
			timeOfLastTick = System.currentTimeMillis();
		}
	}
	
	
	private int lookForClosestResources( GameObject thisObject, ID resourceID ){
		long tempDistance = 0;
		long distance = 9999;
		int distX;
		int distY;
		GameObject object = thisObject;
		for( int i=0; i < handler.size(); i++ ) {
			GameObject tempObject = handler.get(i);
			if( tempObject.getID() == resourceID ) {
				distX = (tempObject.getX() + tempObject.getWidth()/2) - (thisObject.getX() + thisObject.getWidth()/2);
				distY = (tempObject.getY() + tempObject.getHeight()/2) - (thisObject.getY() + thisObject.getHeight()/2);
				tempDistance = Math.round( Math.sqrt( distX*distX + distY*distY ));
				
				if( distance > tempDistance ) { 
					distance = tempDistance;
					object = tempObject;
				}
			}
		}
		if( object != thisObject ) {
			LinkedList<MapVertex> path;
			int x,y;
			x = object.getX() + object.getWidth()/2;
			y = object.getY() + object.getHeight()/2;
			path = PathFinder.findPath(map, map.mapGrid[ (thisObject.getY()+thisObject.getHeight()/2) / 64][ (thisObject.getX() + thisObject.getWidth()/2) / 64 ], map.mapGrid[ y / 64][ x / 64 ]);
			thisObject.setPath( path );
			thisObject.setGoal( x, y );
			thisObject.setAttackMove( false );
			thisObject.move();
			return 1;
		}
		else
			return -1;
	}
	
	private int lookForEnemy( GameObject thisObject ) {
		for( int i=0; i < handler.size(); i++ ) {
			GameObject target = handler.get(i);
			if( target.getTeam() == 1 ) {
				LinkedList<MapVertex> path;
				int x,y;
				x = target.getX() + target.getWidth()/2;
				y = target.getY() + target.getHeight()/2;
				path = PathFinder.findPath(map, map.mapGrid[ (thisObject.getY()+thisObject.getHeight()/2) / 64][ (thisObject.getX() + thisObject.getWidth()/2) / 64 ], map.mapGrid[ y / 64 ][ x / 64 ]);
				thisObject.setPath( path );
				thisObject.setGoal( x, y );
				thisObject.setAttackMove( true );
				thisObject.setLookingForEnemy( true );
				thisObject.move();
				return 1;
			}
		}
		return 0;
	}
	
		
}

/*public LinkedList<MapVertex> lookForClosestNexus( Handler handler, Map map ) {
	long tempDistance = 0;
	long distance = 9999;
	int distX;
	int distY;
	GameObject nexus = this;
	
	for( int i=0; i < handler.size(); i++ ) {
		GameObject object = handler.get(i);
		if( object.id == ID.Nexus && object.team == this.team ) {
			distX = (object.x + object.width/2) - (x + width/2);
			distY = (object.y + object.height/2) - (y + height/2);
			tempDistance = Math.round( Math.sqrt( distX*distX + distY*distY ));
			
			if( distance > tempDistance ) { 
				distance = tempDistance;
				nexus = object;
			}
		}
	}
	if( nexus != this )*/