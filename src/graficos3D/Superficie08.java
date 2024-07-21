package graficos3D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Superficie08 extends JFrame{

	private BufferedImage buffer, pantalla, fondo, aux;
	private Graphics2D gPantalla, gFondo, gAux;
	private int[] punto = {100,100,-400};
	private int[] plano = {-100,100,-850};
	private Superficie superficie;
	private Vertice3D[] vertices;
	
	public Superficie08() {
		this.setResizable(false);
		this.setVisible(true);
		this.setTitle("Rotacion sobre el eje");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBounds(450,100,500,500);
		buffer = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
		fondo = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_RGB);
		gFondo = (Graphics2D) fondo.createGraphics();
		pantalla = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_ARGB);
		gPantalla = (Graphics2D) pantalla.createGraphics();
		aux = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_ARGB);
		gAux = (Graphics2D) aux.createGraphics();
		inicializarGraficos();
		crearFondo();
	}
	
	public static void main(String[] args) {
		Superficie08 ventana = new Superficie08();
		ventana.superficie();
		ventana.iniciarHilo();
		
	}
	
	public void iniciarHilo() {
		new Hilo().start();
	}
	
	public void superficie() {
        double tMin = -Math.PI;
        double tMax = Math.PI;
        double tAumento = .1; 
        
        int numIteraciones = (int) ((tMax - tMin) / tAumento)+1;
        System.out.print(numIteraciones);
        vertices = new Vertice3D[numIteraciones];
        int j = 0;
        for(double t = tMin; t<=tMax; t+=tAumento) {
        	int x = (int) (100*Math.pow(Math.cos(t), 3))+350;
        	int y = (int) (100*Math.pow(Math.sin(t), 3))+100;
        	int z = 0;
        	vertices[j]= new Vertice3D(x, y, z);
        	j++;
        }
        superficie = new Superficie(vertices,'z',100,20, Color.black);
        superficie.dibujar(CurvaExplicita.PARALELA, gPantalla, plano);
        finalizar();
	}
	
	public void finalizar() {
		gAux.drawImage(fondo, 0,0,this);
		gAux.drawImage(pantalla, 0, 0, this);
		this.getGraphics().drawImage(aux, 0, 0, this);
		pantalla = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_ARGB);
		gPantalla = (Graphics2D) pantalla.createGraphics();
	}
	
	public void dibujarFondo() {
		this.getGraphics().drawImage(fondo, 0,0,this);
	}
	
	public void crearFondo() {
		Color c = Color.white;
		for(int i = 0; i<getHeight(); i++) dibujarLineaHorizontal(0, fondo.getWidth()-1, i, c, gFondo);
		dibujarLineaHorizontal(0, 500, 250, Color.red, gFondo);
		dibujarLineaVertical(250, 0, 500, Color.green, gFondo);
		dibujarLinea(0,500, 500,0, Color.yellow, gFondo);
		
	}
	
	private void dibujarLinea(int x1, int y1, int x2, int y2, Color c, Graphics g) {
		int dx = Math.abs(x2 - x1);
	    int dy = Math.abs(y2 - y1);
		if(dx==0) {
			dibujarLineaVertical(x1,y1,y2,c,g);
			return;
		}
		
		if(dy==0) {
			dibujarLineaHorizontal(x1,x2,y1,c,g);
			return;
		}
		

	    int sx = (x1 < x2) ? 1 : -1;
	    int sy = (y1 < y2) ? 1 : -1;

	    int error = dx - dy;

	    while (x1 != x2 || y1 != y2) {
	        ponerPixel(x1, y1,c,g);
	        int error2 = 2 * error;

	        if (error2 > -dy) {
	            error -= dy;
	            x1 += sx;
	        }

	        if (error2 < dx) {
	            error += dx;
	            y1 += sy;
	        }
	    }

	    ponerPixel(x2, y2,c,g);

	}
	
	private void dibujarLineaVertical(int x, int y, int yF, Color c, Graphics g) {
		if(yF<y) {
			int aux = y;
			y=yF;
			yF=aux;
		}
		for(int i = 0; i <= Math.abs(yF-y); i++)ponerPixel(x, y+i, c,g);
	}
	
	private void dibujarLineaHorizontal(int x, int xF, int y, Color c, Graphics g) {
		if(xF<x) {
			int aux = x;
			x=xF;
			xF=aux;
		}
		for(int i = 0; i <= Math.abs(xF-x); i++)  ponerPixel(x+i, y, c,g);			
	}

	
	public void ponerPixel(int x, int y, Color c) {
		buffer.setRGB(0, 0, c.getRGB());
		gPantalla.drawImage(buffer, x, y,null);
	}
	
	public void ponerPixel(int x, int y, Color c, Graphics g) {
		buffer.setRGB(0, 0, c.getRGB());
		g.drawImage(buffer, x, y,null);

	}
	
	public static void inicializarGraficos() {
		try {
		    Thread.sleep(40);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	}
	
private class Hilo extends Thread {
		
		@Override
		public void run() {
			while(true) {
				//superficie.rotarSobreEje(5, 'x');
				//superficie.rotarSobreEje(5, 'y');
				superficie.rotarSobreEje(2, 'z');
				superficie.dibujar(Cubo.PARALELA, gPantalla, plano);
				finalizar();
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}



}
