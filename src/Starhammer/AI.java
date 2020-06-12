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
	//private int lastMinerals;
	private int lastGold;
	private int howBigArmy;
	private int howManyWorkers;
	private int howBigEnemyArmy;
	boolean somethingToBuild;
	boolean firstBarracks, secondBarracks;
	boolean twoMarines;
	boolean someoneOnGold;
	boolean attack;
	boolean weAreAttacked;
	boolean weHaveNexus;
	GameObject mainNexus;
	
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
			howBigEnemyArmy = 0;
			weAreAttacked = false;
			for( int i=0; i < handler.size(); i++ ) {
				GameObject object = handler.get(i); //wysylanie robotnikow do pracy
				if( object.getTeam() == 2 ) {
					if( object.getID() == ID.Worker && !object.isMoves() && !object.isBuild() && !object.isWorking() && ( !someoneOnGold || menu.player[1].getMinerals() > menu.player[1].getGold() + 500 )) {
						lookForClosestResources( object, ID.Gold);
						someoneOnGold = true;
					}
					if( object.getID() == ID.Worker && !object.isWorking() && !object.isMoves() && !object.isBuild() )
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
					if( object.getID() == ID.Worker )
						++howManyWorkers;
					if( object.isBeingAttacked() )
						weAreAttacked = true;
				}
				else if( object.getTeam() == 1 && object.getID() == ID.Marine )
					++howBigEnemyArmy;
			}
			
			for( int i=0; i < handler.size(); i++ ) { //druga petla
				GameObject object = handler.get(i);
				if( object.getID() == ID.Nexus && object.getTeam() == 2 && menu.player[1].getMinerals() >= ID.Worker.getMineralCost() && !object.isProducing() && (howManyWorkers < 3 || menu.player[1].getMinerals() > 700) ) {
					menu.player[1].addMinerals(-ID.Worker.getMineralCost());
					object.setProducing( true );
					object.setProduce( 0 ); //produkuj Worker
					object.setTimeOfStart( System.currentTimeMillis() );
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
				BuildBarracks( (startPositionX/64-5)*64, (startPositionY/64-2)*64 );
				firstBarracks = true;
			}
			if( menu.player[1].getMinerals() >= ID.Barracks.getMineralCost() + 500 && !secondBarracks ) {
				BuildBarracks( (startPositionX/64-2)*64, (startPositionY/64-5)*64 );
				secondBarracks = true;
			}
			
			if( resourceCheckTimer >= 20 ) {
				if( menu.player[1].getGold() == lastGold )
					someoneOnGold = false;
				//if( menu.player[1]).getMinerals() == lastMinerals)
					//buildWorker()
				resourceCheckTimer -= 20;
				lastGold = menu.player[1].getGold();
				//lastMinerals = menu.player[1].getMinerals();
			}
			
			if( howBigArmy >= 2 || weAreAttacked ) { //atack
				for( int i=0; i < handler.size(); i++ ) {
					GameObject object = handler.get(i);
					if( object.getTeam() == 2 && object.getID() == ID.Marine && !object.isMoves() && !object.isAttacking() )
						lookForEnemy( object );
				}
				attack = true;
			}
			else if( attack && howBigEnemyArmy > howBigArmy && !weAreAttacked ) { //retreat
				for( int i=0; i < handler.size(); i++ ) {
					GameObject object = handler.get(i);
					if( object.getTeam() == 2 && object.getID() == ID.Marine && !object.isAttacking() )
						Retreat( object );
				}
				attack = false;
			}
			
			timeOfLastTick = System.currentTimeMillis();
		}
	}
	
	
	public int lookForClosestResources( GameObject thisObject, ID resourceID ){
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
		return -1;
	}
	
	public int lookForEnemy( GameObject thisObject ) {
		long tempDistance = 0;
		long distance = 9999;
		int distX;
		int distY;
		GameObject target = thisObject;
		for( int i=0; i < handler.size(); i++ ) {
			GameObject tempObject = handler.get(i);
			if( tempObject.getTeam() == 1 && tempObject.getID().isUnit() ) {
				distX = (tempObject.getX() + tempObject.getWidth()/2) - (thisObject.getX() + thisObject.getWidth()/2);
				distY = (tempObject.getY() + tempObject.getHeight()/2) - (thisObject.getY() + thisObject.getHeight()/2);
				tempDistance = Math.round( Math.sqrt( distX*distX + distY*distY ));
				
				if( distance > tempDistance ) { 
					distance = tempDistance;
					target = tempObject;
				}
			}
			else if( tempObject.getTeam() == 1 && howBigEnemyArmy == 0 ) {
				target = tempObject;
			}
		}
		
		if( target != thisObject  ) {
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
		return 0;
	}
	
	public int Retreat( GameObject thisObject ) { //wycofaj sie w poblize nexusa
		for( int i=0; i < handler.size(); i++ ) {
			GameObject tempNexus = handler.get(i);
			if( tempNexus.getID() == ID.Nexus && tempNexus.getTeam() == 2 ) {
				LinkedList<MapVertex> path;
				int x = tempNexus.getX() + tempNexus.getWidth()/2;
				int y = tempNexus.getY() + tempNexus.getHeight()/2;
				path = PathFinder.findPath(map, map.mapGrid[ (thisObject.getY()+thisObject.getHeight()/2) / 64][ (thisObject.getX() + thisObject.getWidth()/2) / 64 ], map.mapGrid[ y / 64 - 2 ][ x / 64 + 1 ]);
				thisObject.setPath( path );
				thisObject.setGoal( x, y );
				thisObject.setAttackMove( false );
				thisObject.move();
				return 1;
			}
		}
		return 0;
	}
	
	public int BuildBarracks( int x, int y ) {
		int baracksPlaceX = x;
		int baracksPlaceY = y;
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
				return 1; //wystarczy nam jeden robotnik przy budowie
			}
		}
		return 0;
	}
		
}
