package Objects;

import java.awt.Color;
import java.awt.Graphics;

import Starhammer.Handler;
import Starhammer.Starhammer;

public class Marine extends GameObject{

	Handler handler;
	
	public Marine(int x, int y, int team, Handler handler) {
		super(x, y, team);
		
		this.id = ID.Marine;
		this.handler = handler;
		this.clickable = true;
		this.lookingForEnemy = true;
		
		this.width = 64;
		this.height = 64;
		this.hp = 150;
		this.movementSpeed = 3;
		this.attackRange = 192; //3*this
		this.attackDMG = 30;
		this.attackSpeed = 0.5f;
	}
	
	@Override
	public void tick() {
		if( velX != 0 && velY != 0 ) {
			x += Math.round(this.velX * 0.71);
			y += Math.round(this.velY * 0.71);
		}
		else {
			x += this.velX;
			y += this.velY;
		}
		
		x = Starhammer.boarder( x, 0, Starhammer.mapRes );
		y = Starhammer.boarder( y, 0, Starhammer.mapRes );
		
		collision( handler );

		if( lookingForEnemy ) {
			lookForEnemy( handler );
		}
		
		if( attacking ) {
			if( checkIfInRange(target) ){ //niebezpieczenstwo NULLA
				if( System.currentTimeMillis() - timeOfLastAttack > 1000*attackSpeed ) {
					target.hp -= attackDMG;
					timeOfLastAttack = System.currentTimeMillis();
					System.out.println(target.hp);
					if( target.hp <= 0) {
						attacking = false;
						lookingForEnemy = true;
						handler.removeObject( target );
					}
						
				}
			}
			else {
				attacking = false;
				lookingForEnemy = true;
			}
		}
		
		if( moves ) {
			lookingForEnemy = false;
			attacking = false;
			checkIfCloseToDestination();
		}
			
	}
	
	@Override
	public void render( Graphics g ) {
		g.setColor(Color.white);
		g.fillRect(x, y, width, height);
		if( clicked ) {
			g.setColor(Color.green);
			g.drawRect(x-1, y-1, width+1, height+1);
			//g.setColor(Color.green);

			g.fillRect(x+8, y+8, (width-16)*hp/150, 8); //hpbar
			g.setColor(Color.red);
			g.fillRect(x+width-8, y+8, ((width-16)*(hp-150)/150), 8);
		}
	}
	

}
