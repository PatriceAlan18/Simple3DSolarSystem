package proyecto;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Background extends MyGraphics{
	
	private BufferedImage background;
	private Graphics gBackground;
	private final static Color BLACK = new Color(0,0,1);
	private Color[] space = {BLACK,BLACK,BLACK,BLACK,new Color(24,28,34), new Color(15,10,10) };
	
	public Background(int Width, int Height) {
		background = new BufferedImage(Width, Height,BufferedImage.TYPE_INT_ARGB);
		gBackground = background.createGraphics();
		paintBackground();
	}
	
	public void paintBackground() {
		setGraphics(gBackground);
		rectangle(0, 0, background.getWidth()-1, background.getHeight()-1, BLACK);
		Color fondo = new Color(background.getRGB(100, 100));		
		paint2D(background,100,100,fondo, space);
	}
	
	
	
	public BufferedImage getBackground() {
		return background;
	}
	
}
