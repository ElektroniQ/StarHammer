package Objects;

import java.awt.Color;
import java.awt.Graphics;

public class Gold extends GameObject {

	private Color color = new Color( 0.8f, 0.8f, 0.2f, .75f );
	
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
