package Objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import Starhammer.Handler;
import Starhammer.Map;
import Starhammer.MapVertex;
import Starhammer.PathFinder;


public abstract class GameObject {

	protected int x;
	protected int y;
	protected int width, height;
	protected ID id;
    protected int velX, velY;
    protected int goalX, goalY, goal2X, goal2Y;
    protected int hp;
	protected int movementSpeed;
	protected int attackRange , attackDMG;
	protected float attackSpeed;
	protected int team;
	private int lastPositionX, lastPositionY;
	private int stuckCounter;

	GameObject target;
	protected LinkedList<MapVertex> path;
	
	protected boolean moveable;
  	protected boolean clickable, clicked;
	protected boolean moves;
	protected boolean lookingForEnemy;
	protected boolean attacking, producing;
	protected boolean attackMove;
	protected boolean working;
	protected boolean[] produce;
	protected boolean build;
	private boolean lastMove;

	long timeOfLastPosition;
	long timeOfLastAttack;
	long timeOfStart;
	
	public GameObject( int x, int y, int team ) {
		this.x = x;
		this.y = y;
		this.team = team;
	}

	public abstract void tick();
	public abstract void render( Graphics g );

	
	
	public void setX( int x ) {
		this.x = x;
	}
	public int getX() {
		return x;
	}
	public void setY( int y ) {
		this.y = y;
	}
	public int getY() {
		return y;
	}
	public void setID( ID id ) {
		this.id = id;
	}
	public ID getID() {
		return id;
	}
	public void setVelX( int vel ) {
		this.velX = vel;
	}
	public int getVelX() {
		return velX;
	}
	public void setVelY( int vel ) {
		this.velY = vel;
	}
	public int getVelY() {
		return velY;
	}
	public int getGoalX() {
		return goalX;
	}
	public void setGoalX(int goalX) {
		this.goalX = goalX;
	}
	public int getGoalY() {
		return goalY;
	}
	public void setGoalY(int goalY) {
		this.goalY = goalY;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setTarget(GameObject target) {
		this.target = target;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	
	public boolean isAttacking() {
		return attacking;
	}

	public boolean isClickable() {
		return clickable;
	}
	public boolean isClicked() {
		return clicked;
	}
	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}
	public boolean isMoves() {
		return moves;
	}
	public void setMoves(boolean moves) {
		this.moves = moves;
	}
	
	public boolean isWorking() {
		return working;
	}

	public void setWorking(boolean working) {
		this.working = working;
	}

	public void setAttackMove(boolean attackMove) {
		this.attackMove = attackMove;
	}
	public boolean isBuild() {
		return build;
	}

	public void setBuild(boolean build) {
		this.build = build;
	}

	public boolean isMoveable() {
		return this.moveable;
	}
	
	public void setLookingForEnemy(boolean lookingForEnemy) {
		this.lookingForEnemy = lookingForEnemy;
	}
	
	public void setTimeOfStart( long time ) {
		this.timeOfStart = time;
	}

	public void setPath( LinkedList<MapVertex> passedPath ) {
		this.path = passedPath;
	}
	
	public void setProduce( int product ) {
		this.produce[product] = true;
	}
	
	public int getTeam() {
		return team;
	}

	public boolean isProducing() {
		return this.producing;
	}
	public void setProducing( boolean isProd ) {
		this.producing = isProd;
	}
	
	public void setGoal( int x, int y ) {
		this.goal2X = x;
		this.goal2Y = y;
	}

	public Rectangle getBounds() {
		return new Rectangle( x, y, width, height );
	}
	
	public void collision( Handler handler ) {
		for( int i=0; i < handler.size(); i++ ) { //collision start
			GameObject temp = handler.get(i);
			if( temp.getBounds().intersects( getBounds() ) && this!=temp )
				if( temp.getID() == ID.Terrain || temp.getID().isBuilding() ) {
					if( temp.getX() < x )
						if( temp.getBounds().intersectsLine(x, y+movementSpeed+2, x, y+height-(movementSpeed+2)) )  //this "2" is just safety number
							x = temp.getX() + temp.getWidth();
						else if ( temp.getY() > y )
							y = temp.getY() - height;
						else
							y = temp.getY() + temp.getHeight();
					else
						if( temp.getBounds().intersectsLine(x+width, y+movementSpeed+2, x+width, y+height-(movementSpeed+2) ))  //same here
							x = temp.getX() - width;
						else if ( temp.getY() > y )
							y = temp.getY() - height;
						else
							y = temp.getY() + temp.getHeight(); //collision ends
				}
				else if( temp.getID().isUnit() ) {
					if( temp.getX() < x )
						if( temp.getBounds().intersectsLine(x, y+movementSpeed+20, x, y+height-(movementSpeed+20)) )  //this "20" is just safety number
							temp.setX( temp.getX()-1 );
						else if ( temp.getY() > y )
							temp.setY( temp.getY()+1 );
						else
							temp.setY( temp.getY()-1 );
					else
						if( temp.getBounds().intersectsLine(x+width, y+movementSpeed+20, x+width, y+height-(movementSpeed+20) ))  //same here
							temp.setX( temp.getX()+1 );
						else if ( temp.getY() > y )
							temp.setY( temp.getY()+1 );
						else
							temp.setY( temp.getY()-1 ); //collision ends
				}
						
		}
	}
	public void checkIfCloseToDestination(){
		if( velX > 0 && goalX <= x+(width/2) )
			velX = 0;
		else if( velX < 0 && goalX >= x+(width/2) )
			velX = 0;
		if( velY > 0 && goalY <= y+(height/2) )
			velY = 0;
		else if( velY < 0 && goalY >= y+(height/2) )
			velY = 0;
		
		
		if( System.currentTimeMillis() - timeOfLastPosition > 300 ) { //przeciw stuckowaniu
			lastPositionX = x;
			lastPositionY = y;
			timeOfLastPosition = System.currentTimeMillis();
		}
		
		//TODO to jest zalosne rozwiazanie ale dziala... tak prawie. Sprobuj rozwiazanie z linia nad jakim myslales
		if( lastPositionX - x == 0 && lastPositionY - y == 0 ) { //przeciw stuckowaniu
			stuckCounter++;
			if( stuckCounter == 10 ) {
				velX = 0;
				velY = 0;
				lastMove = true;
			}
			else {
				x = x+1;
				y = y+1;
				moveToGoal();
			}
			
		} //koniec stuckowania
		

		
		if( velX == 0 && velY == 0 ) { //lastmove wykonuje sie po przejsciu calej tras i sluzy do dokladnego ustawienia jednostki
			if( !lastMove ) {
				move();
				stuckCounter = 0;
			}
			else {
				moves = false;
				attackMove = false;
				lookingForEnemy = true;
				lastMove = false;
			}
		}
		
		
	}
	
	public void moveToGoal() {
		
		if( x+(width/2) < goalX ) 
			velX = movementSpeed;
		else 
			velX = -movementSpeed;
		
		if( y+(height/2) < goalY ) 
			velY = movementSpeed;
		else 
			velY = -movementSpeed;
	}
	
	public void move(){
		
		if( !path.isEmpty() ) {
			lastMove = false;
			goalX = path.getFirst().x*64+32;
			goalY = path.getFirst().y*64+32;
	
			path.removeFirst();
		}
		else {
			lastMove = true;
			goalX = goal2X;
			goalY = goal2Y;
		}
			
			moveToGoal();
			
			moves = true;

	}
	
	public void lookForEnemy( Handler handler ){ //znajdz najlbizszego wroga i wydaj komende ataku
		long tempDistance = 0;
		long distance = 9999;
		int distX;
		int distY;
		GameObject object = this;
		GameObject tempObject;
		
		
		for( int i=0; i < handler.size(); i++ ) {
			tempObject = handler.get(i);
			if( tempObject.team != this.team && ( tempObject.id.isUnit() || tempObject.id.isBuilding() ) ) {
				distX = (tempObject.x + tempObject.width/2) - (x + width/2);
				distY = (tempObject.y + tempObject.height/2) - (y + height/2);
				tempDistance = Math.round( Math.sqrt( distX*distX + distY*distY ));
				
				if( distance > tempDistance ) { 
					distance = tempDistance;
					object = tempObject;
				}
			}
		}
		if( distance <= attackRange && object!=this ) {
			target = object;
			attacking = true;
			lookingForEnemy = false;
			moves = false;
		}
	}
	
	public boolean checkIfInRange( GameObject target ) {

		int distX;
		int distY;
		long distance = 0;
		distX = (target.x + target.width/2) - (x + width/2);
		distY = (target.y + target.height/2) - (y + height/2);
		distance = Math.round( Math.sqrt( distX*distX + distY*distY ));
		if( distance <= attackRange )
			return true;
		else
			return false;
	}
	
	public boolean checkIfInRectRange( GameObject object, int range ) {
		int distX;
		int distY;
		distX = (object.x + object.width/2) - (x + width/2);
		distY = (object.y + object.height/2) - (y + height/2);
		if( distX <= range && distX >= -range && distY <= range && distY >= -range )
			return true;
		else
			return false;
		
	}
	
	public LinkedList<MapVertex> lookForClosestNexus( Handler handler, Map map ) {
		long tempDistance = 0;
		long distance = 9999;
		int distX;
		int distY;
		GameObject nexus = this;
		
		for( int i=0; i < handler.size(); i++ ) {
			GameObject tempNexus = handler.get(i);
			if( tempNexus.id == ID.Nexus && tempNexus.team == this.team ) {
				distX = (tempNexus.x + tempNexus.width/2) - (x + width/2);
				distY = (tempNexus.y + tempNexus.height/2) - (y + height/2);
				tempDistance = Math.round( Math.sqrt( distX*distX + distY*distY ));
				
				if( distance > tempDistance ) { 
					distance = tempDistance;
					nexus = tempNexus;
				}
			}
		}
		if( nexus != this )
			if( x+width/2 > nexus.x+nexus.width/2 )//wiemy ze jestesmy po prawej stronie nexusa
				if( x+width/2 - (nexus.x+nexus.width/2) > Math.abs(nexus.y+nexus.height/2 - (y+height/2)) )
					return PathFinder.findPath(map, map.mapGrid[ (y+height/2) / 64][ (x+width/2)/64 ], map.mapGrid[ (nexus.y+nexus.height/2) / 64 ][ (nexus.x+nexus.width+32) / 64 ]); //prawo
				else if( nexus.y+nexus.height/2 - (y+height/2) > 0 )
					return PathFinder.findPath(map, map.mapGrid[ (y+height/2) / 64][ (x+width/2)/64 ], map.mapGrid[ (nexus.y-32) / 64 ][ (nexus.x+nexus.width/2) / 64 ]); //gora
				else
					return PathFinder.findPath(map, map.mapGrid[ (y+height/2) / 64][ (x+width/2)/64 ], map.mapGrid[ (nexus.y+nexus.height+32) / 64 ][ (nexus.x+nexus.width/2) / 64 ]); //dol
			else
				if( nexus.x+nexus.width/2 - (x+width/2) > Math.abs(nexus.y+nexus.height/2 - (y+height/2)) )
					return PathFinder.findPath(map, map.mapGrid[ (y+height/2) / 64][ (x+width/2)/64 ], map.mapGrid[ (nexus.y+nexus.height/2) / 64 ][ (nexus.x-32) / 64 ]); //lewo
				else if( nexus.y+nexus.height/2 - (y+height/2) > 0 )
					return PathFinder.findPath(map, map.mapGrid[ (y+height/2) / 64][ (x+width/2)/64 ], map.mapGrid[ (nexus.y-32) / 64 ][ (nexus.x+nexus.width/2) / 64 ]); //gora
				else
					return PathFinder.findPath(map, map.mapGrid[ (y+height/2) / 64][ (x+width/2)/64 ], map.mapGrid[ (nexus.y+nexus.height+32) / 64 ][ (nexus.x+nexus.width/2) / 64 ]); //dol
		else
			return null;
	}
	
	
}
