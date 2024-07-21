package proyecto;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sun extends Sphere{
	
	private Color colors[] = new Color[2];
	
	public Sun(Vertex3D centro, int R, Color c) {
		super(centro, R, c);
		Color[] aux = {c, new Color(230,214,16)};
		this.colors = aux;
		// TODO Auto-generated constructor stub
	}
	@Override
	public void drawParallelSphere(BufferedImage bf ,Graphics g) {
		setGraphics(g);
		setPlane(0, 0, 5);	
	    for(int i = 0; i<vertex.length-1; i++) {
	    	for(int j =0; j<vertex[i].length-1; j++) {
	    		paintParallelPolygon(bf, vertex[i][j], vertex[i][j+1], vertex[i+1][j], vertex[i+1][j+1], colors[getRandom(0, 1)]);
	    	}
	    }
	    
	    
	    
	}
	
	@Override
	protected void paintParallelPolygon(BufferedImage bf, Vertex3D v1,Vertex3D v2,Vertex3D v3,Vertex3D v4, Color c){
		
		double x0 = v1.getX()+v2.getX()+v3.getX()+v4.getX();
		double y0 = v1.getY()+v2.getY()+v3.getY()+v4.getY();
		double z0 = v1.getZ()+v2.getZ()+v3.getZ()+v4.getZ();

		x0=x0/4;
		y0=y0/4;
		z0=z0/4;

		if(z0<center.getZ())return;
		
		draw3DParallelLine(v1, v2, c);
		draw3DParallelLine(v1, v3, c);
		draw3DParallelLine(v3, v4, c);
		draw3DParallelLine(v2, v4, c);
	}
	
}
