package Objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Terrain extends GameObject{

	
	public Terrain(int x, int y, ID id) {
		super(x, y, id);
		this.width = 32;
		this.height = 32;
	}

	@Override
	public void tick() {

	}

	@Override
	public void render( Graphics g ) {
		g.setColor( Color.darkGray );
		g.fillRect(x, y, width, height);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle( x, y, width, height );
	}

	@Override
	public void move(int goalX, int goalY) {
	}
	
}
