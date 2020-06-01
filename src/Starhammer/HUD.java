package Starhammer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import Objects.GameObject;

public class HUD {
	
	int x,y;
	Font myFont = new Font( "arial", 0, 15 );
	Color myColor = new Color( 0.9f, 0.9f, 0.7f, .1f);
	Map map;
	
	public HUD( Map map ) {
		this.map = map;
	}
	
	public void tick() {
		
	}
	
	
	public void render( Graphics g) {
		String str = String.valueOf( Menu.player[0].getMinerals() );
		g.setColor(Color.BLUE);
		g.fillRoundRect(1150, 3, 20, 20, 20, 20);
		g.setColor(Color.YELLOW);
		g.fillRoundRect(1150, 28, 20, 20, 20, 20);
		g.fillRect(1170, 53, 20, 20);
		g.setColor(Color.WHITE);
		g.setFont( myFont );
		g.drawString(str, 1180, 17);
		str = String.valueOf( Menu.player[0].getGold() );
		g.drawString(str, 1180, 43);
		str = String.valueOf( Menu.player[0].getSupply() );
		str += "/200";
		g.drawString(str, 1200, 68);
		
		if( KeyInput.buildMode ) {
			g.setColor( myColor );
			g.fillRect(1113, 580, 150, 100);
			g.setColor( Color.WHITE );
			g.drawRect(1113, 580, 75, 50);
			g.drawRect(1188, 580, 75, 50);
			g.drawRect(1113, 630, 75, 50);
			g.drawRect(1188, 630, 75, 50);
			g.drawString("Q:Nexus", 1120, 610);
			
		}
		

	}
	
}
