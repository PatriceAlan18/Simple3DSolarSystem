package graficos3D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Transformaciones extends JFrame{
	
	private BufferedImage buffer, pantalla;
	private Graphics gPantalla;
	private int dx, dy, dz,sx, sy,sz;
	private int[][] translacion = {{1,0,0,dx},{0,1,0,dy},{0,0,1,dz},{0,0,0,1}};
	private double[][] escalacion = {{sx,0,0,0},{0,sy,0,0},{0,0,sz,0},{0,0,0,1}};
	//private double[][] rotacion = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
	
	public Transformaciones() {
		this.setResizable(false);
		this.setVisible(true);
		this.setTitle("Proyeccion perpendicular");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBounds(450,100,500,500);
		buffer = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
		pantalla = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_ARGB);
		gPantalla = (Graphics2D) pantalla.createGraphics();
		inicializarGraficos();
	}
	
	public static void main(String[] args) {
		Transformaciones ventana = new Transformaciones();
		//ventana.transladar(55, 70, 50);
		ventana.escalar(90,90,90);
		//ventana.rotar(10, 'z');
	}

	public void escalar(double sx, double sy, double sz) {
		sx = sx/100;
		sy = sy/100;
		sz = sz/100;
		escalacion[0][0] = sx;
		escalacion[1][1] = sy;
		escalacion[2][2] = sz;
		int [] puntos = {250,250,25,1};
		verMatriz(multiplicarMatrices(puntos,escalacion));
	}
	
	
	public void rotar(int angulo, char eje) {
		double radianes = Math.toRadians(angulo);
		double seno = Math.sin(radianes);
		double coseno = Math.cos(radianes);
		double[][] rotacion = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};

		switch(eje) {
		case 'x':
			rotacion[1][1] = coseno;
			rotacion[1][2] = seno*(-1);
			rotacion[2][1] = seno;
			rotacion[2][2] = coseno;
			break;
		case'y':
			rotacion[0][0] = coseno;
			rotacion[0][2] = seno*(-1);
			rotacion[2][0] = seno;
			rotacion[2][2] = coseno;
			break;
		case 'z':
			rotacion[0][0] = coseno;
			rotacion[0][1] = seno*(-1);
			rotacion[1][0] = seno;
			rotacion[1][1] = coseno;
			break;
			default: 
				System.out.print("Eje incorrecto");
				return;
		}	
		int [] puntos = {100,100,100,1};
		verMatriz(multiplicarMatrices(puntos,rotacion));
	}
	
	public void transladar(int dx, int dy, int dz) {
		translacion[0][3] = dx;
		translacion[1][3] = dy;
		translacion[2][3] = dz;
		int [] puntos = {100,100,100,1};
		verMatriz(multiplicarMatrices(puntos,translacion));
		
	}
	
	public void verMatriz(int[] matriz) {
		for(int i = 0; i<matriz.length; i++) {
			System.out.print(matriz[i]+"  ");

		}
	}
	
	public void verMatriz(int[][] matriz) {
		for(int i = 0; i<matriz.length; i++) {
			for(int j = 0; j<matriz[i].length; j++) {
				System.out.print(matriz[i][j]+"  ");
			}
			System.out.print("\n");
		}
	}
	
	public void verMatriz(double[][] matriz) {
		for(int i = 0; i<matriz.length; i++) {
			for(int j = 0; j<matriz[i].length; j++) {
				System.out.print(matriz[i][j]+"  ");
			}
			System.out.print("\n");
		}
	}
	
	public int[] multiplicarMatrices(int[] matriz1,int[][] matriz2) {
		int[] nueva = new int[matriz1.length];
		for(int i = 0; i<matriz1.length; i++) {
			for(int j = 0; j<matriz2[i].length; j++) {
				nueva[i]=nueva[i]+matriz1[j]*matriz2[i][j];
			}
		}
		return nueva;
	}
	
	public int[] multiplicarMatrices(int[] matriz1,double[][] matriz2) {
		int[] nueva = new int[matriz1.length];
		for(int i = 0; i<matriz1.length; i++) {
			for(int j = 0; j<matriz2[i].length; j++) {
				nueva[i]=(int) (nueva[i]+matriz1[j]*matriz2[i][j]);
			}
		}
		return nueva;
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
