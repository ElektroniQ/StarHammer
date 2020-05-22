package Objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class ClickField extends GameObject {
	
	Color color = new Color( 0f, 1f, 1f, .2f );
	
	public ClickField(int x, int y, ID id) {
		super(x, y, id);
		this.goalX = 0;
		this.goalY = 0;

	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		g.setColor( color );
		g.fillRect( x, y, goalX-x, goalY-y );
		
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle( x, y, goalX-x, goalY-y );
	}

	@Override
	public void move(int goalX, int goalY) {
		
	}

}
