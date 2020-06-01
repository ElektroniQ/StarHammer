package Starhammer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.image.BufferStrategy;




public class Starhammer extends Canvas implements Runnable{

	/*private String ip = "localhost";
	private int port = 20022;
	private Socket socket;
	public static DataOutputStream dos;
	public static DataInputStream dis;
	private ServerSocket serverSocket;
	private boolean accepted;
	private boolean unableToCommunicateWithOpponent = false;
	private boolean won = false;
	private boolean enemyWon = false;
	private Scanner scanner = new Scanner( System.in );*/
	
	
	private static final long serialVersionUID = -5671504739751849190L;
	public static final int WIDTH = 1280, HEIGHT = WIDTH * 9 / 16; //1280 i 720
	public static final int trueWIDTH = WIDTH - 17, trueHEIGHT = HEIGHT - 40;
	public static final int mapRes = 3200;
	protected static State gameState = State.Menu;
	private Thread thread;
	private boolean running = false;
	private Handler handler;
	private ClickField clickField;
	private Camera camera;
	private Robot robot;
	private Map map;
	private Menu menu;
	private HUD hud;
	
	
	
	public Starhammer() {
		/*System.out.println("Podaj ip");
		ip = scanner.nextLine();
		System.out.println("Please input the port: ");
		port = scanner.nextInt();*/
		
		handler = new Handler();
		camera = new Camera( 0, 0 );
		clickField = new ClickField();
		
		//if (!connect()) initializeServer();
		
		try {
		robot = new Robot();
		}
		catch( Throwable e ) { System.exit(1);}
		
		map = new Map( handler, "map.txt" );
		menu = new Menu( camera, handler, map );
		hud = new HUD( map );
		new Window(WIDTH, HEIGHT, "Starhammer", this);

		this.addMouseListener( new MouseInput( handler, camera, robot, clickField, map, menu ) );
		this.addMouseMotionListener( new MouseMotionInput( handler, camera, robot, clickField, hud ) );
		this.addKeyListener( new KeyInput( handler, camera, hud ) );
		



		//handler.addObject( new Marine( 0, 0, 1, handler ) );
		//handler.addObject( new Marine( 400, 400, 1, handler ) );	
		/*handler.addObject( new Marine( 64, 0, 1, handler ) );
		handler.addObject( new Marine( 500, 400, 1, handler ) );	
		handler.addObject( new Marine( 128, 0, 1, handler ) );
		handler.addObject( new Marine( 300, 400, 1, handler ) );
		handler.addObject( new Marine( 0, 0, 1, handler ) );
		handler.addObject( new Marine( 400, 400, 1, handler ) );	
		handler.addObject( new Marine( 64, 0, 1, handler ) );
		handler.addObject( new Marine( 500, 400, 1, handler ) );	
		handler.addObject( new Marine( 128, 0, 1, handler ) );
		handler.addObject( new Marine( 300, 400, 1, handler ) );
		handler.addObject( new Marine( 0, 0, 1, handler ) );
		handler.addObject( new Marine( 400, 400, 1, handler ) );	
		handler.addObject( new Marine( 64, 0, 1, handler ) );
		handler.addObject( new Marine( 500, 400, 1, handler ) );	
		handler.addObject( new Marine( 128, 0, 1, handler ) );
		handler.addObject( new Marine( 1000, 1000, 2, handler ) );*/

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
		if( gameState == State.Game ) {
			handler.tick();
			camera.tick();
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
		
		switch (gameState){
		case Game:
			g.translate( camera.getX(), camera.getY() );
			g.setColor(Color.orange);
			g.fillRect(-50, -50, 50, 3300);
			g.fillRect(3200, -50, 50, 3300); //to sa ograniczenia mapy
			g.fillRect(0, -50, 3200, 50);
			g.fillRect(0, 3200, 3200, 50);
			
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
		}
		
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
	
	/*public void listenForServerRequest() {
		Socket socket = null;
		try {
			socket = serverSocket.accept();
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			accepted = true;
			System.out.println("CLIENT HAS REQUESTED TO JOIN, AND WE HAVE ACCEPTED");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean connect() {
		try {
			socket = new Socket(ip, port);
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			accepted = true;
		} catch (IOException e) {
			System.out.println("Unable to connect to the address: " + ip + ":" + port + " | Starting a server");
			return false;
		}
		System.out.println("Successfully connected to the server.");
		return true;
	}

	private void initializeServer() {
		try {
			serverSocket = new ServerSocket(port, 8, InetAddress.getByName(ip));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/
	
	
	
}
