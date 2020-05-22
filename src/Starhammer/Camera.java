package Starhammer;


public class Camera {
	private int x, y;

	public Camera( int x, int y ) {
		this.x = x;
		this.y = y;
	}

	public void tick() {
		x = Starhammer.boarder( x, 0, Starhammer.trueWIDTH - 64 );
		y = Starhammer.boarder( y, 0, Starhammer.trueHEIGHT - 64 );
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	
}
