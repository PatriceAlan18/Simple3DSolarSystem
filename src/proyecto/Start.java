package proyecto;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Start{
	
	private BufferedImage buffer;
	private int x;
	private int y;
	private final Color OUTSIDE = new Color(124,122,121);
	private final Color CENTER = new Color(218,215,213);
	private final Color UNION = new Color(179,175,174);
	private final static Color NEGRO = new Color(0,0,1);
	
	public Start(int centerX, int centerY) {
		this.x = centerX;
		this.y = centerY;
		buffer = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
	}
	
	
	public void drawStart(Graphics g) {
		putPixel(g, x, y, CENTER);
		putPixel(g, x+1, y, UNION);
		putPixel(g, x-1, y, UNION);
		putPixel(g, x, y+1, UNION);
		putPixel(g, x, y-1, UNION);
		putPixel(g, x+1, y+1, OUTSIDE);
		putPixel(g, x-1, y+1, OUTSIDE);
		putPixel(g, x+1, y-1, OUTSIDE);
		putPixel(g, x-1, y-1, OUTSIDE);
		putPixel(g, x+2, y, OUTSIDE);
		putPixel(g, x-2, y, OUTSIDE);
		putPixel(g, x, y+2, OUTSIDE);
		putPixel(g, x, y-2, OUTSIDE);
	}
	
	public void twinkle(Graphics g) {
		putPixel(g, x+3, y, OUTSIDE);
		putPixel(g, x-3, y, OUTSIDE);
		putPixel(g, x, y+3, OUTSIDE);
		putPixel(g, x, y-3, OUTSIDE);
		putPixel(g, x+4, y, OUTSIDE);
		putPixel(g, x-4, y, OUTSIDE);
		putPixel(g, x, y+4, OUTSIDE);
		putPixel(g, x, y-4, OUTSIDE);
	}
	
	public void stopTwinkle(Graphics g) {
		putPixel(g, x+3, y, NEGRO);
		putPixel(g, x-3, y, NEGRO);
		putPixel(g, x, y+3, NEGRO);
		putPixel(g, x, y-3, NEGRO);
		putPixel(g, x+4, y, NEGRO);
		putPixel(g, x-4, y, NEGRO);
		putPixel(g, x, y+4, NEGRO);
		putPixel(g, x, y-4, NEGRO);
	}
	
	
	
	public void putPixel(Graphics g, int x, int y, Color c) {
		buffer.setRGB(0, 0, c.getRGB());
		g.drawImage(buffer, x, y,null);

	}
	
}
