package Objects;

import java.awt.Color;
import java.awt.Graphics;

public class Gold extends GameObject {

	Color color = new Color( 0.9f, 0.7f, 0.15f, .3f );
	
	public Gold(int x, int y, int team) {
		super(x, y, team);
		
		this.id = ID.Gold;
		this.width = 64;
		this.height = 64;
		this.hp = 200;
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
