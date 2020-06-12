package Starhammer;

import Objects.GameObject;

public class Camera {
	private int x, y;

	public Camera( int x, int y ) {
		this.x = x;
		this.y = y;
	}

	public void tick() {
		x = GameObject.boarder( x, -2020, 100 ); //mapa 3200x3200 nie pytajcie dlaczego
		y = GameObject.boarder( y, -2580, 100 ); //Starhammer.height - Starhammer.mapRes;
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
