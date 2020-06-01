package Starhammer;

import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

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
		if( Starhammer.gameState == State.Game ) {
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
					if( KeyInput.buildMode && object.isClicked() && object.getTeam() == Menu.player[0].getTeam() && object.getID() == ID.Worker ) { //budowanie
						if( KeyInput.buildingNexus && Menu.player[0].getMinerals() >= 1000 ) {
							handler.addObject( object = new Nexus( (x/64-1)*64, (y/64-1)*64, Menu.player[0].getTeam(), handler, map) );
							Menu.player[0].addMinerals( -1000 );
							KeyInput.buildingNexus = false;
							KeyInput.buildMode = false;
						}
					}
					
					else if( KeyInput.aMove && object.isClicked() && object.isMoveable() && Menu.player[0].getTeam() == object.getTeam() ) {
						LinkedList<MapVertex> path;						//ta czesc to polozenie obiektu                                                ,a ta to polozenie kursora
						path = PathFinder.findPath(map, map.mapGrid[(object.getY()+object.getHeight()/2)/64][( object.getX()+object.getWidth()/2 )/64], map.mapGrid[y/64][x/64]);

					object.setPath( path );
					object.setGoal( x, y );
					object.setAttackMove( true );
					object.setLookingForEnemy( true );
					object.move();
					//notAttackMoveCommand = false;
					}
				}
				
			}
			

			//TODO zrob wyjatki dla naciskania poza mapa, przydaloby sie tez klikanie dla grup
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
						/*for(int j=0; j < path.size(); j++) {
							System.out.println("x"+path.get(j).x+" "+"y" + path.get(j).y);
					}*/
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
		else if( Starhammer.gameState == State.Menu ) {
			int x = e.getX();
			int y = e.getY();
			
			if( Menu.isOverButtonStart(x, y) ) { //button start
				menu.startGame();
				Starhammer.gameState = State.Game;
			}
			if( Menu.isOverButtonEnd(x, y) ) { //button exit
				System.exit(1);
			}
			
		}
		else {
			int x = e.getX();
			int y = e.getY();
			
			if( Menu.isOverButtonStart(x, y) ) { //button start
				Starhammer.gameState = State.Game;
				
			}
			if( Menu.isOverButtonEnd(x, y) ) { //button exit
				System.exit(1);
			}
		}
		//System.out.println("x"+x+" "+"y" + y);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if( e.getButton() == MouseEvent.BUTTON1 && !KeyInput.aMove && Starhammer.gameState == State.Game ) {
			for( int j=0; j<handler.size(); ++j ) {
				GameObject tempObject = handler.get(j);
						
				if( tempObject.getTeam() == Menu.player[0].getTeam() && tempObject.isClickable() && tempObject.getBounds().intersects( clickField.getBounds() )) {
					if( tempObject.isClicked() && e.isShiftDown() )
						tempObject.setClicked( false );
					else
						tempObject.setClicked( true );
					
						
				}
				else if( tempObject.isClicked() && !e.isShiftDown() ) {
						tempObject.setClicked( false );
				}
						
			}

		}
		KeyInput.aMove = false;
		clickField.setX( 0 ); // to teoretycznie powinno byc w ifie ale powstawaly jakeis dziwne anomalie wiec stamtad to zabralem
		clickField.setY( 0 );
		clickField.setGoalX( 0 );
		clickField.setGoalY( 0 );
		clickField.setClicked( false );
		//notAttackMoveCommand = true;
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
			robot.mouseMove(trueX, trueY - y + 1);//1 potrzebne jest gdy wycodzimy za ekran po skosie
		if( x >= Starhammer.trueWIDTH )
			robot.mouseMove(trueX - x + Starhammer.trueWIDTH - 1, trueY);
		if( y >= Starhammer.trueHEIGHT )
			robot.mouseMove(trueX, trueY - y - 1 + Starhammer.trueHEIGHT );
		//camera end, what a shity code
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
		//super.finalize();
	}

}


