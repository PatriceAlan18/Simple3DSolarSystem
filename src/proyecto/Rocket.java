package proyecto;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Rocket extends MyGraphics{

	private Vertex3D[] vertex, vW,vW2, aux1, aux2;
	private Vertex3D[][] cone;
	private Color c;
	
	public Rocket(Color c) {
		this.c = c;
		makeRocket();
	}
	
	public void makeRocket(){
		int R = 10;
	    double tIncrease = .1; 
		double tMin = -Math.PI+9*tIncrease;
	    double tMax = Math.PI/2-12*tIncrease;
	    //Siempre se le ha restado .8
	    int tIterations = (int) ((tMax - tMin) / tIncrease)+1;
	    vertex = new Vertex3D[tIterations];    
	    aux1 = new Vertex3D[tIterations];   
	    aux2 = new Vertex3D[tIterations];   
	    int j = 0;
	    for(double t = tMin; t<=tMax; t+=tIncrease) {
	    	int x =  (int) (10*( R*Math.cos(t))+300);//300
        	int y =  (int) (10*(R*Math.sin(t))+860);//600
        	int z =  (int) (100);
	        vertex[j]= new Vertex3D(x, y, z);
	        j++;
	   }		
	    
	   

	}
	
	public void makeWindow() {
		int R = 50;
	    double tIncrease = 0.1;
	    double tMin = -Math.PI;
	    double tMax = Math.PI;
	    int tIterations = (int) ((tMax - tMin) / tIncrease) + 1;
	    vW = new Vertex3D[tIterations+1];
	    vW2 = new Vertex3D[tIterations+1];
	    int j = 0;
	    for (double t = tMin; t <= tMax; t += tIncrease) {

	        double x = R * Math.cos(t) + 550; 
	        double y = R * Math.sin(t) + 500; 
	        double z = 100; 
	        vW[j] = new Vertex3D(x,  y,  z);
	        
	        x = (R+15) * Math.cos(t) + 550; 
	        y = (R+15) * Math.sin(t) + 500; 
	        z = 100; 
	        vW2[j] = new Vertex3D(x,  y,  z);
	        j++;
	    }
	    vW[vW.length-1]=new Vertex3D(vW[0]);
	    vW2[vW.length-1]=new Vertex3D(vW2[0]);
	}
	
	public void drawRocket(Graphics g, BufferedImage b) {
		this.setGraphics(g);
		this.setPlane(0, 8, 5);
		setLight(new Vertex3D(250,250,-5000));
		setKa(0.8);
		setKd(1);
		setKs(1);
		for(int i = 0; i<vertex.length; i++) aux1[i]=new Vertex3D(vertex[i]);

		for(int j = 0; j<35; j++) {
			for(int m = 0; m<vertex.length; m++)aux2[m] = new Vertex3D(aux1[m].getX()+9, aux1[m].getY()-6, aux1[m].getZ()+4);
			for(int l = 0; l<vertex.length-1; l++) {
				paintParallelPolygon(b, aux1[l], aux1[l+1], aux2[l], aux2[l+1], c);
			}
			for(int l = 0; l<vertex.length; l++) {
				aux1[l] = new Vertex3D(aux2[l].getX(), aux2[l].getY(), aux2[l].getZ());
			}

		}
		
		makeWindow();
		drawWindow(g,b);
		
	}
	
	public void drawWindow(Graphics g, BufferedImage b) {
		for(int i = 0; i<vW.length-1; i ++)	{
			//paintParallelPolygon(bf, vW[i], vW[i+1], vW2[i], vW2[i+1], UNUSED);
			draw3DParallelLine(vW[i], vW[i+1], Color.blue);
		}
		int x2, y2;
		x2 = 550 - (plane[0] * 100) / plane[2];
		y2 = 500 - (plane[1] * 100) / plane[2];
		paint2DFlood(b, x2, y2, Color.blue, Color.white);
		
		for(int i = 0; i<vW.length-1; i ++)	{
			paintParallelPolygon(b, vW[i], vW[i+1], vW2[i], vW2[i+1], Color.blue);
		}

	}
	

}




