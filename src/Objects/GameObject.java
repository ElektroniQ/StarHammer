package Objects;

import java.awt.Graphics;
import java.awt.Rectangle;


public abstract class GameObject {

	protected int x,y;
	protected int width, height;
	protected ID id;
	protected int velX, velY;
	protected int goalX, goalY;

	boolean clickable, clicked;
	boolean moves;
	
	public GameObject( int x, int y, ID id ) {
		this.x = x;
		this.y = y;
		this.id = id;
	}

	public abstract void tick();
	public abstract void render( Graphics g );
	public abstract Rectangle getBounds();
	public abstract void move( int goalX, int goalY );
	
	
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

	public void checkIfCloseToDestination(){
		if( goalX - 8 < x+(width/2) && x+(width/2) < goalX + 8 )
			velX = 0;
		if( goalY - 8 < y+(height/2) && y+(height/2) < goalY + 8 )
			velY = 0;
		
		if( velX == 0 && velY == 0 )
			moves = false;
	}
	
}
