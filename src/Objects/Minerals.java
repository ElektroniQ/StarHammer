package Objects;

import java.awt.Color;
import java.awt.Graphics;

public class Minerals extends GameObject {

	Color color = new Color( 0.1f, 0.5f, 1f, .3f );
	
	public Minerals(int x, int y, int team) {
		super(x, y, team);
		this.id = ID.Minerals;
		this.width = 64;
		this.height = 64;
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		g.setColor( color );
		g.fillRect(x, y, width, height);
		
	}

}
