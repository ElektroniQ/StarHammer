package Starhammer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.image.BufferStrategy;

public class Starhammer extends Canvas implements Runnable{

	private static final long serialVersionUID = -5671504739751849190L;
	public static final int WIDTH = 1280, HEIGHT = WIDTH * 9 / 16; //1280 i 720
	public static final int trueWIDTH = WIDTH - 17, trueHEIGHT = HEIGHT - 40;
	public static final int mapRes = 3200;
	private Thread thread;
	private boolean running = false;
	private Handler handler;
	private ClickField clickField;
	private Camera camera;
	private Robot robot;
	private Map map;
	private Menu menu;
	private HUD hud;
	private AI artInt;
	
	
	public Starhammer() {
		
		handler = new Handler();
		camera = new Camera( 0, 0 );
		try {
		robot = new Robot();
		}
		catch( Throwable exce ) { System.exit(1);}
		
		map = new Map( handler, "map.txt" );
		menu = new Menu( camera, handler, map );
		clickField = new ClickField( menu );
		hud = new HUD( menu );
		artInt = new AI( menu, handler, map);
		
		new Window(WIDTH, HEIGHT, "Starhammer", this);
		this.addMouseListener( new MouseInput( handler, camera, robot, clickField, map, menu ) );
		this.addMouseMotionListener( new MouseMotionInput( handler, camera, robot, clickField, hud, menu ) );
		this.addKeyListener( new KeyInput( handler, camera, hud, menu ) );
		

		this.requestFocusInWindow();
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop() {
		try{
			thread.join();
			running = false;
		}catch(Exception excep) {
			excep.printStackTrace();
		}
	}
	
	public void run() {
        long lastTime = System.nanoTime();
        double tickAmount = 1000000000 / 60.0;
        double fpsAmount  = 1000000000 / 144.0;
        double delta = 0;
        double fpsdelta = 0;
        //long timer = System.currentTimeMillis();
        //int frames = 0;
        //if( !accepted )
        //	listenForServerRequest();
        while(running)
        {
	        long now = System.nanoTime();
	        delta += (now - lastTime) / tickAmount;
	        fpsdelta += (now - lastTime) /fpsAmount;
	        lastTime = now;
	        while(delta >=1)
	        {
		        tick();
		        delta--;
	        }
	        
	        if( fpsdelta >=1 && running ) {
		        render();
		        fpsdelta--;
	        	//frames++;
	        }

	                            
	        /*if(System.currentTimeMillis() - timer > 1000)
	        {
		        timer += 1000;
		        System.out.println("FPS: "+ frames);
		        frames = 0;
	        }*/
        }
        stop();
	}
	
	public void tick() {
		if( menu.getGameState() == State.Game ) {
			handler.tick();
			camera.tick();
			artInt.tick();
		}

	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if( bs == null ) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();

		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		switch (menu.getGameState()){
		case Game:
			g.translate( camera.getX(), camera.getY() );
			
			g.setColor(Color.orange);
			g.fillRect(-50, -50, 50, mapRes+100);
			g.fillRect(mapRes, -50, 50, mapRes+100); //to sa ograniczenia mapy
			g.fillRect(0, -50, mapRes, 50);
			g.fillRect(0, mapRes, mapRes, 50);
			handler.render(g);
			clickField.render(g);
			
			g.translate( -camera.getX(), -camera.getY() );
			hud.render(g);
			break;
			
		case Menu:
			menu.render(g);
			break;
		
		case Paused:
			g.translate( camera.getX(), camera.getY() );
			handler.render(g);
			clickField.render(g);
			menu.render(g);
			break;
		
		case GG:
			handler.render(g);
			menu.render(g);

		}
		
		g.dispose();
		bs.show();
	}

	public static void main(String args[]) {
		new Starhammer();
	}
	
	
	
}
