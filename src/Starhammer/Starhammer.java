package Starhammer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.image.BufferStrategy;

import Objects.ClickField;
import Objects.ID;
import Objects.Marine;
import Objects.Terrain;

public class Starhammer extends Canvas implements Runnable{

	private static final long serialVersionUID = -5671504739751849190L;
	public static final int WIDTH = 640, HEIGHT = WIDTH * 9 / 16; //623 i 320
	public static final int trueWIDTH = WIDTH - 17, trueHEIGHT = HEIGHT - 40;
	private Thread thread;
	private boolean running = false;
	private Handler handler;
	Camera camera;
	Robot robot;
	
	
	public Starhammer() {
		handler = new Handler();
		camera = new Camera( 0, 0 );
		
		try {
		robot = new Robot();
		}
		catch( Throwable e ) { System.exit(1);}
		
		this.addMouseListener( new MouseInput( handler, camera, robot ) );
		this.addMouseMotionListener( new MouseMotionInput( handler, camera, robot ) );
		this.addKeyListener( new KeyInput( handler, camera ) );
		
		new Window(WIDTH, HEIGHT, "Lets do this", this);

		handler.addObject( new ClickField( 50, 50, ID.ClickField ) );
		handler.addObject( new Marine( 0, 0, ID.Marine, handler ) );
		handler.addObject( new Marine( 200, 200, ID.Marine, handler ) );		
		handler.addObject( new Terrain( 100, 100, ID.Terrain ));
		handler.addObject( new Terrain( 132, 100, ID.Terrain ));
		handler.addObject( new Terrain( 164, 100, ID.Terrain ));
		handler.addObject( new Terrain( 196, 132, ID.Terrain ));

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
		handler.tick();
		camera.tick();
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
		
		g.translate( camera.getX(), camera.getY() );
		handler.render(g);
		//g.translate( -camera.getX(), -camera.getY() );
		
		g.dispose();
		bs.show();
	}
	
	
	public static int boarder( int var, int min, int max ) { //zakaz wychodzenia za plansze
		if( var >= max ) {
			var = max;
			return var = max;
		}
		if( var <= min )
			return var = min;
		else
			return var;
	}
	
	public static void main(String args[]) {
		new Starhammer();
	}
	
}
