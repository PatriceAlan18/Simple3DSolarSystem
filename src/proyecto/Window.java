package proyecto;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Window extends JFrame implements Runnable{
	
	private BufferedImage screen, aux, background, start, rocket;
	private Graphics gScreen, gAux, gStart;
	private Sphere sun, planet1, planet2, planet3, planet4, planet5;
	private Start[] starts, twinkles;
	
	public Window() {
		
		this.setResizable(false);
		this.setTitle("Universe");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setVisible(true);
		this.setExtendedState(MAXIMIZED_BOTH);
		screen = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_ARGB);
		gScreen = (Graphics2D) screen.createGraphics();
		start = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_ARGB);
		rocket = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_ARGB);
		gStart = (Graphics2D) start.createGraphics();
		aux = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_RGB);
		gAux = (Graphics2D) aux.createGraphics();
		startGraphics();
	}
	
	public static void main(String[] args) {
		Window window = new Window();
		window.drawBackground();
		window.drawStarts();
		window.drawRocket();
		window.drawSpheres();
		window.draw();
		window.starts();
		window.run();
	}
	
	public void drawBackground() {
		background = new Background(getWidth(),getHeight()).getBackground();
	}
	
	public void starts() {
		new startThread().start();
	}
	
	public void drawRocket() {
		Rocket roc = new Rocket(new Color(214,214,214));
		roc.drawRocket(rocket.createGraphics(), rocket);
	}
	
	public void drawStarts() {
		starts = new Start[getRandom(400, 550)];
		for(int i = 0; i<starts.length; i++) {
			starts[i] = new Start(getRandom(5, this.getWidth()-5),getRandom(5, this.getHeight()-5));
			starts[i].drawStart(gStart);
		}
	}
	
	public void twinkle() {
		
		twinkles = new Start[getRandom(20, starts.length-1)];
		for(int i = 0; i<twinkles.length; i++) {
			twinkles[i] = starts[getRandom(0, starts.length-1)];
			twinkles[i].twinkle(gStart);
		}
	}
	
	public void stopTwinkle() {
		
		for(int i = 0; i<twinkles.length; i++) {
			twinkles[i].stopTwinkle(gStart);
		}
	}
	
	public void drawSpheres() {
		sun = new Sun(new Vertex3D(400,400,0),60,Color.yellow);
		sun.drawParallelSphere(screen, gScreen);
		
		planet1 = new Sphere(new Vertex3D(170,170,0),15,new Color(192,247,204));		
		planet1.drawParallelSphere(screen,gScreen);
		
		planet2 = new Sphere(new Vertex3D(140,340,5000),17,Color.red);		
		planet2.drawParallelSphere(screen,gScreen);	
		
		planet3 = new Sphere(new Vertex3D(120,380,5000),15,Color.blue);		
		planet3.drawParallelSphere(screen,gScreen);
		
		planet4 = new Sphere(new Vertex3D(620,400,5000),12,Color.white);		
		planet4.drawParallelSphere(screen,gScreen);
		
		planet5 = new Sphere(new Vertex3D(570,520,5000),19,Color.MAGENTA);		
		planet5.drawParallelSphere(screen,gScreen);

	}
	
	public void draw() {
		gAux.drawImage(background, 0,0,this);
		gAux.drawImage(start, 0,0,this);
		gAux.drawImage(rocket,730,200,this);
		gAux.drawImage(screen, 0,0,this);
		this.getGraphics().drawImage(aux, 0,0,this);
		screen = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_ARGB);
		gScreen = (Graphics2D) screen.createGraphics();
	}
	
	
	public static void startGraphics() {
		try {
		    Thread.sleep(40);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	}
	
	public int getRandom(int min, int max) {
	    return (int) (Math.random() * (max - min + 1) + min);
	}
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				Thread.sleep(100);
				sun.rotate(5, 'y');
				
				planet1.drawParallelSphere(screen,gScreen);
				planet1.bothRotates(new Vertex3D(400,400,0),2,5,'z','x');

				planet3.bothRotates(new Vertex3D(400,400,0),2,5,'z','x');
				planet3.drawParallelSphere(screen,gScreen);

				planet2.bothRotates(new Vertex3D(400,400,0),3,5,'z','x');
				planet2.drawParallelSphere(screen,gScreen);
				
				planet4.bothRotates(new Vertex3D(400,400,0),6,4,'z','x');
				planet4.drawParallelSphere(screen,gScreen);
				
				planet5.bothRotates(new Vertex3D(400,400,0),3,4,'z','x');
				planet5.drawParallelSphere(screen,gScreen);

				sun.drawParallelSphere(screen,gScreen);
				draw();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
			
		
		
	}

	
	private class startThread extends Thread{
		public void run() {
			while(true) {
				try {
					Thread.sleep(400);
					twinkle();
					Thread.sleep(400);
					stopTwinkle();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	
}
