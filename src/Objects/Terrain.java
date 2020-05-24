package Objects;

import java.awt.Color;
import java.awt.Graphics;

public class Terrain extends GameObject{

	
	public Terrain(int x, int y, int team ) {
		super(x, y, team);
		this.id = ID.Terrain;
		this.width = 64;
		this.height = 64;

	}

	@Override
	public void tick() {

	}

	@Override
	public void render( Graphics g ) {
		g.setColor( Color.darkGray );
		g.fillRect(x, y, width, height);
	}
	
}
