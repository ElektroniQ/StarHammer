package Objects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

import Starhammer.Handler;
import Starhammer.MapVertex;


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
	GameObject target;
	protected int team;

	protected LinkedList<MapVertex> path;
	
  	protected boolean clickable, clicked;
	protected boolean moves;
	protected boolean lookingForEnemy;
	protected boolean attacking;
	private boolean lastMove;

	long timeOfLastAttack;
	
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
	public void setPath( LinkedList<MapVertex> passedPath ) {
		this.path = passedPath;
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
			//TODO do poprawy
			if( temp.getBounds().intersects( getBounds() ) && this!=temp )
				if( temp.getID() == ID.Terrain || temp.getID() == ID.Marine )
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
	}
	
	public void checkIfCloseToDestination(){
		if( goalX - 4 < x+(width/2) && x+(width/2) < goalX + 4 )
			x+=4;

		
		if( goalY - 4 < y+(height/2) && y+(height/2) < goalY + 4 ) 
			velY = 0;
		
		
		if( velX == 0 && velY == 0 ) {
			if( !lastMove ) {
				move();
			}
			else {
				moves = false;
				lookingForEnemy = true;
				lastMove = false;
			}
		}
		
	}
	
	public void move(){
		if( !path.isEmpty() ) {
			goalX = path.getFirst().x*64+32;
			goalY = path.getFirst().y*64+32;
	
			path.removeFirst();
		}
		else {
			lastMove = true;
			goalX = goal2X;
			goalY = goal2Y;
		}
			
			if( x+(width/2) < goalX ) 
				velX = movementSpeed;
			else 
				velX = -movementSpeed;
			
			if( y+(height/2) < goalY ) 
				velY = movementSpeed;
			else 
				velY = -movementSpeed;
			
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
			if( tempObject.team != this.team && tempObject.id != ID.Terrain ) {
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
		}
	}
	
	public boolean checkIfInRange( GameObject target ) {
		/*if( target == this )
			return false;*/
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
	
	/*public void attack( GameObject objectToAttack ) {
		
		objectToAttack.hp = objectToAttack.hp - attackDMG;
		System.out.println(objectToAttack.hp);
		
	}*/
	
}
