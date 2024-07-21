package graficos3D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class ProyeccionPerpendicular02 extends JFrame{

	private BufferedImage buffer, pantalla;
	private Graphics gPantalla;
	private int[] punto = {0,0,-500};
	
	public ProyeccionPerpendicular02() {
		this.setResizable(false);
		this.setVisible(true);
		this.setTitle("Proyeccion perspectiva");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBounds(450,100,500,500);
		buffer = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
		pantalla = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_ARGB);
		gPantalla = (Graphics2D) pantalla.createGraphics();
		inicializarGraficos();
	}
	
	public static void main(String[] args) {
		ProyeccionPerpendicular02 ventana = new ProyeccionPerpendicular02();
		Color c = new Color(0,0,255);
		ventana.cubo3D(125, 125, 25, 50, c);



		ventana.finalizar();
	}

	
	public void cubo3D(int x, int y, int z, int longitud, Color c) {
		int mitad = longitud/2;
		int x1 = x-mitad;
		int x2 = x+mitad;
		int y1 = y-mitad;
		int y2 = y+mitad;
		int z1 = z-mitad;
		int z2 = z+mitad;
		Linea3D(x1,y1,z1,x2,y1,z1, c);
		Linea3D(x1,y1,z1,x1,y2,z1, c);
		Linea3D(x1,y2,z1,x2,y2,z1, c);
		Linea3D(x2,y2,z1,x2,y1,z1, c);
		
		Linea3D(x1,y1,z1,x1,y1,z2, c);
		Linea3D(x1,y2,z1,x1,y2,z2, c);
		Linea3D(x2,y1,z1,x2,y1,z2, c);
		Linea3D(x2,y2,z1,x2,y2,z2, c);

		Linea3D(x1,y1,z2,x1,y2,z2, c);
		Linea3D(x1,y1,z2,x2,y1,z2, c);
		Linea3D(x1,y2,z2,x2,y2,z2, c);
		Linea3D(x2,y1,z2,x2,y2,z2, c);
	}
	
	public void finalizar() {
		this.getGraphics().drawImage(pantalla, 0, 0, this);
	}
	
	public void Linea3D(Vertice3D v1, Vertice3D v2, Color c) {
		int x0 = v1.getX();
		int y0 = v1.getY();
		int z0 = v1.getZ();
		int x1 = v2.getX();
		int y1 = v2.getY();
		int z1 = v2.getZ();
		int x2,x3,y2,y3;
		x2 = punto[0]-((punto[2]*(x0-punto[0]))/(z0-punto[2]));
		y2 = punto[1] - ((punto[2]*(y0-punto[1]))/(z0-punto[2]));
		
		x3 = punto[0]-((punto[2]*(x1-punto[0]))/(z1-punto[2]));
		y3 = punto[1] - ((punto[2]*(y1-punto[1]))/(z1-punto[2]));
		dibujarLinea(x2, y2, x3, y3, c);
		System.out.println("Punto 2D 1: (" + x2 + ", " + y2 + ")");
		System.out.println("Punto 2D 2: (" + x3 + ", " + y3 + ")\n\\\\\\\\\\\\\\\\\\\\n");

	}
	
	public void Linea3D(int x0, int y0, int z0, int x1, int y1, int z1, Color c) {
		int x2,x3,y2,y3;
		x2 = punto[0]-((punto[2]*(x0-punto[0]))/(z0-punto[2]));
		y2 = punto[1] - ((punto[2]*(y0-punto[1]))/(z0-punto[2]));
		
		x3 = punto[0]-((punto[2]*(x1-punto[0]))/(z1-punto[2]));
		y3 = punto[1] - ((punto[2]*(y1-punto[1]))/(z1-punto[2]));
		dibujarLinea(x2, y2, x3, y3, c);
	}
	
	public void dibujarLinea(int x1, int y1, int x2, int y2, Color c) {
		int dx = Math.abs(x2 - x1);
	    int dy = Math.abs(y2 - y1);
		if(dx==0) {
			dibujarLineaVertical(x1,y1,y2,c);
			return;
		}
		
		if(dy==0) {
			dibujarLineaHorizontal(x1,x2,y1,c);
			return;
		}
		

	    int sx = (x1 < x2) ? 1 : -1;
	    int sy = (y1 < y2) ? 1 : -1;

	    int error = dx - dy;

	    while (x1 != x2 || y1 != y2) {
	        ponerPixel(x1, y1,c);
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

	    ponerPixel(x2, y2,c);

	}

	private void dibujarLineaVertical(int x, int y, int yF, Color c) {
		if(yF<y) {
			int aux = y;
			y=yF;
			yF=aux;
		}
		for(int i = 0; i <= Math.abs(yF-y); i++)ponerPixel(x, y+i, c);
	}
	
	private void dibujarLineaHorizontal(int x, int xF, int y, Color c) {
		if(xF<x) {
			int aux = x;
			x=xF;
			xF=aux;
		}
		for(int i = 0; i <= Math.abs(xF-x); i++)  ponerPixel(x+i, y, c);			
	}
	
	public void ponerPixel(int x, int y, Color c) {
		buffer.setRGB(0, 0, c.getRGB());
		gPantalla.drawImage(buffer, x, y,null);

	}
	
	public static void inicializarGraficos() {
		try {
		    Thread.sleep(40);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	}
	
}
