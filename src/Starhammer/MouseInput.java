package Starhammer;

import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import Objects.Barracks;
import Objects.GameObject;
import Objects.ID;
import Objects.Nexus;




public class MouseInput implements MouseListener {

	private Menu menu;
	private Map map;
	private Handler handler;
	private Camera camera;
	private Robot robot;
	private ClickField clickField;
	
	public MouseInput( Handler handl, Camera cam, Robot robot, ClickField clickF, Map map, Menu menu ) {
		this.handler = handl;
		this.camera = cam;
		this.robot = robot;
		this.clickField = clickF;
		this.map = map;
		this.menu = menu;
	}
	
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if( menu.getGameState() == State.Game ) {
			int x = e.getX() - camera.getX();
			int y = e.getY() - camera.getY();
	
			if( e.getButton() == MouseEvent.BUTTON1 ) {
				clickField.setX( x );
				clickField.setY( y );
				clickField.setGoalX( x+1 );
				clickField.setGoalY( y+1 );
				clickField.setClicked( true );
				
				
				//to jest czesc odpowiedzialna za attackMove
				if( x < 0 ) x = 0;
				else if( x > Starhammer.mapRes ) x = Starhammer.mapRes-1;  //zapobieganie naciskaniu poza mape
				if( y < 0 ) y = 0;
				else if ( y > Starhammer.mapRes ) y = Starhammer.mapRes-1;
				
				
				for( int i=0; i<handler.size(); ++i ) {
					GameObject object = handler.get(i);
					if( menu.buildMode && object.isClicked() && object.getTeam() == menu.player[0].getTeam() && object.getID() == ID.Worker ) { //budowanie
						if( menu.buildingNexus && menu.player[0].getMinerals() >= ID.Nexus.getMineralCost() ) {
							if( x/64-1 >= 0 && x/64+1 <= 50 && y/64-1 >= 0 && y/64+1 <= 50 ) {
								handler.addObject( object = new Nexus( (x/64-1)*64, (y/64-1)*64, menu.player[0].getTeam(), handler, map, menu) );
								menu.player[0].addMinerals( -ID.Nexus.getMineralCost() );
							}
							menu.buildingNexus = false;
							menu.buildMode = false;
						}
						else if( menu.buildingBarracks && menu.player[0].getMinerals() >= ID.Barracks.getMineralCost() ) {
							if( x/64-1 >= 0 && x/64 <= 50 && y/64-1 >= 0 && y/64+1 <= 50 ) {
								handler.addObject( object = new Barracks( (x/64-1)*64, (y/64-1)*64, menu.player[0].getTeam(), handler, map) );
								menu.player[0].addMinerals( -ID.Barracks.getMineralCost() );
							}
							menu.buildingBarracks = false;
							menu.buildMode = false;
						}
					}
					
					else if( menu.aMove && object.isClicked() && object.isMoveable() && menu.player[0].getTeam() == object.getTeam() ) {
						LinkedList<MapVertex> path;						//ta czesc to polozenie obiektu                                                ,a ta to polozenie kursora
						path = PathFinder.findPath(map, map.mapGrid[(object.getY()+object.getHeight()/2)/64][( object.getX()+object.getWidth()/2 )/64], map.mapGrid[y/64][x/64]);

					object.setPath( path );
					object.setGoal( x, y );
					object.setAttackMove( true );
					object.setLookingForEnemy( true );
					object.move();
					}
				}
				
			}
			

			//TODO przydaloby sie tez klikanie dla grup
			else if( e.getButton() == MouseEvent.BUTTON3 ) {
				
				if( x < 0 ) x = 0;
				else if( x > Starhammer.mapRes ) x = Starhammer.mapRes-1;  //zapobieganie naciskaniu poza mape
				if( y < 0 ) y = 0;
				else if ( y > Starhammer.mapRes ) y = Starhammer.mapRes-1;
				
				
				for( int i=0; i<handler.size(); ++i ) {
					GameObject object = handler.get(i);
					
					if( object.isClicked() && object.isMoveable() ) {
						LinkedList<MapVertex> path;						//ta czesc to polozenie obiektu                                                ,a ta to polozenie kursora
						path = PathFinder.findPath(map, map.mapGrid[(object.getY()+object.getHeight()/2)/64][( object.getX()+object.getWidth()/2 )/64], map.mapGrid[y/64][x/64]);
					object.setPath( path );
					object.setGoal( x, y );
					object.setAttackMove( false );
					object.move();
	
					}
					
					else if( object.isClicked() && object.getID().isBuilding() ) {
						GameObject temp;
						GameObject newTarget = null;
						
						for( int j=0; j<handler.size(); ++j ) {
							temp = handler.get(j);
							if( temp.getBounds().contains(x, y) )
								newTarget = temp;
						}
						
						object.setTarget( newTarget );
						object.setGoalX( x );
						object.setGoalY( y );
					}
				}
			}
			
		}
		/**********************************************************************MENU************************************************************************/
		else if( menu.getGameState() == State.Menu ) {
			int x = e.getX();
			int y = e.getY();
			
			if( menu.isOverButtonStart(x, y) ) { //button start
				menu.startGame();
				menu.setGameState(State.Game);
			}
			if( menu.isOverButtonEnd(x, y) ) { //button exit
				System.exit(1);
			}
			
		}
		else {
			int x = e.getX();
			int y = e.getY();
			
			if( menu.isOverButtonStart(x, y) ) { //button start
				menu.setGameState(State.Game);
				
			}
			if( menu.isOverButtonEnd(x, y) ) { //button exit
				System.exit(1);
			}
		}
		//System.out.println("x"+x+" "+"y" + y);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if( e.getButton() == MouseEvent.BUTTON1 && !menu.aMove && menu.getGameState() == State.Game ) {
			for( int j=0; j<handler.size(); ++j ) {
				GameObject tempObject = handler.get(j);
						
				if( tempObject.getTeam() == menu.player[0].getTeam() && tempObject.isClickable() && tempObject.getBounds().intersects( clickField.getBounds() )) {
					if( tempObject.isClicked() && e.isControlDown() )
						tempObject.setClicked( false );
					else
						tempObject.setClicked( true );
					
						
				}
				else if( tempObject.isClicked() && !e.isControlDown() && !e.isShiftDown() ) {
						tempObject.setClicked( false );
				}
						
			}

		}
		menu.aMove = false;
		clickField.setX( 0 ); // to teoretycznie powinno byc w ifie ale powstawaly jakeis dziwne anomalie wiec stamtad to zabralem
		clickField.setY( 0 );
		clickField.setGoalX( 0 );
		clickField.setGoalY( 0 );
		clickField.setClicked( false );
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//camera movement starts here
		int x = e.getX();
		int y = e.getY();
		int trueX = e.getXOnScreen();
		int trueY = e.getYOnScreen();
		if( x <= 0 )
			robot.mouseMove(trueX - x + 1, trueY);
		if( y <= 0 )
			robot.mouseMove(trueX, trueY - y + 1); //1 potrzebne jest gdy wycodzimy za ekran po skosie
		if( x >= Starhammer.trueWIDTH )
			robot.mouseMove(trueX - x + Starhammer.trueWIDTH - 1, trueY);
		if( y >= Starhammer.trueHEIGHT )
			robot.mouseMove(trueX, trueY - y - 1 + Starhammer.trueHEIGHT );
		//camera end
	}
	
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		return super.toString();
	}


	@Override
	protected void finalize() throws Throwable {
	}

}


