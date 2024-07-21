package graficos3D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Escalamiento04 extends JFrame implements KeyListener{

	private BufferedImage buffer, pantalla, fondo, aux;
	private Graphics2D gPantalla, gFondo, gAux;
	private int[] plano = {-1,1,2};
	private Vertice3D centro;
	private Color color = new Color(0,0,255);
	private int[] punto = {0,0,-300};
	private Cubo cubo;
	
	public Escalamiento04() {
		this.setResizable(false);
		this.setVisible(true);
		this.setTitle("Escalamiento");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBounds(450,100,500,500);
		centro = new Vertice3D(250,250,25);
		buffer = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
		fondo = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_RGB);
		gFondo = (Graphics2D) fondo.createGraphics();
		pantalla = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_ARGB);
		gPantalla = (Graphics2D) pantalla.createGraphics();
		aux = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_ARGB);
		gAux = (Graphics2D) aux.createGraphics();
		inicializarGraficos();
		crearFondo();
		this.addKeyListener(this);
		cubo = new Cubo(centro, 50, color);
	}
	
	public static void main(String[] args) {
		Escalamiento04 ventana = new Escalamiento04();
		ventana.Cubo();
	}
	
	public void Cubo() {
		cubo.dibujar(Cubo.PERSPECTIVA, gPantalla, punto);
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
		
	}
	
	public void rectangulo(int x0, int y0, int x1, int y1, Color c, Graphics g) {
		dibujarLineaVertical(x0,y0,y1,c,g);
		dibujarLineaHorizontal(x0,x1,y1,c,g);
		dibujarLineaVertical(x1,y0,y1,c,g);
		dibujarLineaHorizontal(x0,x1,y0,c,g);
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
	
	public Vertice3D getCentro() {
		return centro;
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

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int sx=100, sy=100,sz=100;
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			sx=90;
			break;
		case KeyEvent.VK_RIGHT:
			sx=110;
			break;
		case KeyEvent.VK_UP:
			sy=90;
			break;
		case KeyEvent.VK_DOWN:
			sy=110;
			break;
		case KeyEvent.VK_M:
			sz=90;
			break;
		case KeyEvent.VK_N:
			sz=110;
			break;
		case KeyEvent.VK_V:
			sx=110;
			sy=110;
			sz=110;
			break;
		case KeyEvent.VK_B:
			sx=90;
			sy=90;
			sz=90;
			break;
		}
		cubo.escalar(sx, sy, sz);
		cubo.dibujar(Cubo.PERSPECTIVA, gPantalla, punto);
		finalizar();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
