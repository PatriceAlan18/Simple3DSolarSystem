package proyecto;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;

public class Sphere extends MyGraphics{

	protected Vertex3D[][] vertex;
	private Vertex3D[][] originals;
	protected Vertex3D center, originalC;
	protected Color c;
	private int R;
	private double[][] rotationz = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
	private double[][] rotationy = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
	private double[][] rotationx = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
	private double[][] rotationz2 = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
	private double[][] rotationy2 = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
	private double[][] rotationx2 = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
	private int[][] move = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
	private int rotationX = 0;
	private int rotationY = 0;
	private int rotationZ = 0;
	private int rotationX2 = 0;
	private int rotationY2 = 0;
	private int rotationZ2 = 0;
	
	public Sphere(Vertex3D centro, int R, Color c) {
		this.center = new Vertex3D(centro);
		this.originalC = new Vertex3D(centro);
		this.c = c;
		this.R = R;
		makeSphere();
	}
	
	public void makeSphere() {
	    double tIncrease = 0.52;

	    double tMin = 0;
	    double tMax = 2 * Math.PI;
	    double pMin = 0;
	    double pMax = Math.PI;
	    double pIncrease = 0.52;
	    int tIterations = (int) ((tMax - tMin) / tIncrease) + 1;
	    int pIterations = (int) ((pMax - pMin) / pIncrease) + 1;
	    vertex = new Vertex3D[tIterations][pIterations];
	    originals = new Vertex3D[tIterations][pIterations];
	    double cX = center.getX();
	    double cY = center.getY();
	    double cZ = center.getZ();
	    for (int tIndex = 0; tIndex < tIterations; tIndex++) {
	        for (int pIndex = 0; pIndex < pIterations; pIndex++) {
	            double p = pMin + pIndex * pIncrease;
	            double t = tMin + tIndex * tIncrease;
	            int x = (int) (cX + R * Math.sin(p) * Math.cos(t));
	            int y = (int) (cY + R * Math.sin(p) * Math.sin(t));
	            int z = (int) (cZ + R * Math.cos(p));
	            vertex[tIndex][pIndex] = new Vertex3D(x, y, z);
	            originals[tIndex][pIndex] = new Vertex3D(x, y, z);
	        }
	    }
	    
	    for(int i =0; i<pIterations; i++) {
	    	vertex[vertex.length-1][i]=new Vertex3D(vertex[0][i]);
	    	originals[originals.length-1][i]=new Vertex3D(vertex[0][i]);

	    }
	    
	}

	public void drawParallelSphere(BufferedImage bf ,Graphics g) {
		setGraphics(g);
		setPlane(0, 0, 100);	
	    for(int i = 0; i<vertex.length-1; i++) {
	    	for(int j =0; j<vertex[i].length-1; j++) {
	    		paintParallelPolygon(bf,vertex[i][j], vertex[i][j+1], vertex[i+1][j], vertex[i+1][j+1], c);
	    	}
	    }	 	    
	}

	public void drawPerspectiveSphere(Graphics g) {
		setGraphics(g);
		setPoint(180,180,-500);	
	    for(int i = 0; i<vertex.length; i++) for(int j =0; j<vertex[i].length-1; j++) draw3DPerspectiveLine(vertex[i][j], vertex[i][j+1], c);
	    for(int i = 0; i<vertex.length-1; i++) for(int j =0; j<vertex[i].length; j++) draw3DPerspectiveLine(vertex[i+1][j], vertex[i][j], c);
	    for (int j = 0; j < vertex[vertex.length - 1].length; j++) draw3DPerspectiveLine(vertex[0][j], vertex[vertex.length - 1][j], c);

	}
	
	public void getNewCenter() {
		int x = 0,y = 0,z = 0;
		int l = vertex.length;
		for(int i =0; i<l; i++) {
			for(int j = 0; j<vertex[i].length;j++) {
				x += vertex[i][j].getX();
				y += vertex[i][j].getY();
				z += vertex[i][j].getZ();
			}
		}
		l = l*vertex[0].length;
		center = new Vertex3D(x/l, y/l, z/l);
	}
	
	public void takeOriginalCenter() {
		this.center.setXYZ(originalC);
	}
	
	public void setOriginals() {
		for(int i = 0; i<vertex.length; i++) {
			for(int j = 0; j<vertex[i].length; j++)	originals[i][j].setXYZ(vertex[i][j]);
		}
	}
	
	public void takeOriginals() {
		for(int i = 0; i<vertex.length; i++) {
			for(int j = 0; j<vertex[i].length; j++)	vertex[i][j].setXYZ(originals[i][j]);
		}
	}
	
	public void moveToZero() {
		for(int i = 0; i<vertex.length; i++) {
			for(int j = 0; j<vertex[i].length; j++) {
				vertex[i][j].setX(vertex[i][j].getX()-center.getX());
				vertex[i][j].setY(vertex[i][j].getY()-center.getY());
				vertex[i][j].setZ(vertex[i][j].getZ()-center.getZ());
			}
		}
	}
	
	public void moveToCenter() {
		for(int i = 0; i<vertex.length; i++) {
			for(int j = 0; j<vertex[i].length; j++) {
				vertex[i][j].setX(vertex[i][j].getX()+center.getX());
				vertex[i][j].setY(vertex[i][j].getY()+center.getY());
				vertex[i][j].setZ(vertex[i][j].getZ()+center.getZ());
			}
		}
	}
	
	public void move(int dx, int dy, int dz) {
		move[0][3] = dx;
		move[1][3] = dy;
		move[2][3] = dz;
		for(int i =0; i<vertex.length; i++) {
			for(int j = 0; j<vertex[i].length; j++) {
				int[] puntos = {(int) vertex[i][j].getX(), (int) vertex[i][j].getY(), (int) vertex[i][j].getZ(),1};
				puntos = matrixMultiplication(puntos,move);
				vertex[i][j] = new Vertex3D(puntos[0], puntos[1], puntos[2]);
			}

		}	
	}

	public void rotate(int angle, char axis) {
			takeOriginals();
			moveToZero();
			double radians;
			double sin;
			double cos;

			switch(axis) {
			case 'x':
				if(rotationX>360)rotationX-=360;
				rotationX+=angle;
				radians = Math.toRadians(rotationX);
				sin = Math.sin(radians);
				cos = Math.cos(radians);
				rotationx[1][1] = cos;
				rotationx[1][2] = sin*(-1);
				rotationx[2][1] = sin;
				rotationx[2][2] = cos;
				break;
			case'y':

				if(rotationY>360)rotationY-=360;
				rotationY+=angle;
				radians = Math.toRadians(rotationY);
				sin = Math.sin(radians);
				cos = Math.cos(radians);
				rotationy[0][0] = cos;
				rotationy[0][2] = sin*(-1);
				rotationy[2][0] = sin;
				rotationy[2][2] = cos;
				break;
			case 'z':
				if(rotationZ>720)rotationZ-=360;
				rotationZ+=angle;
				radians = Math.toRadians(rotationZ);
				sin = Math.sin(radians);
				cos = Math.cos(radians);
				rotationz[0][0] = cos;
				rotationz[0][1] = sin*(-1);
				rotationz[1][0] = sin;
				rotationz[1][1] = cos;
				break;
				default: 
					throw new IllegalArgumentException("The axis must be x, y or z");
			}	
			for(int i =0; i<vertex.length; i++) {
				for(int j = 0; j<vertex[i].length; j++) {
					int[] puntos = {(int) vertex[i][j].getX(), (int) vertex[i][j].getY(), (int) vertex[i][j].getZ(),1};
					puntos = matrixMultiplication(puntos,rotationy);
					puntos = matrixMultiplication(puntos,rotationz);
					puntos = matrixMultiplication(puntos,rotationx);
					vertex[i][j] = new Vertex3D(puntos[0], puntos[1], puntos[2]);
				}
			}
					
			moveToCenter();
	}
	
	public void rotate(Vertex3D ce, int angle, char axis) {
		takeOriginals();
		getNewCenter();
		moveToZero();
		int dx = (int) (center.getX()-ce.getX());
		int dy = (int) (center.getY()-ce.getY());
		int dz = (int) (center.getZ()-ce.getZ());
		
		
		center = new Vertex3D(dx,dy,dz);
	    double radians;
	    double sin;
	    double cos;

	    switch (axis) {
	        case 'x':
				if(rotationX2>360)rotationX2-=360;
				rotationX2 +=angle;
	    	    radians = Math.toRadians(rotationX2);
	    	    sin = Math.sin(radians);
	    	    cos = Math.cos(radians);
	    	    rotationx2[1][1] = cos;
	    	    rotationx2[1][2] = sin * (-1);
	    	    rotationx2[2][1] = sin;
	    	    rotationx2[2][2] = cos;
	            break;
	        case 'y':
				if(rotationY2>360)rotationY2-=360;
				rotationY2 +=angle;
	    		radians = Math.toRadians(rotationY2);
	    		sin = Math.sin(radians);
	    		cos = Math.cos(radians);
	    		rotationy2[0][0] = cos;
	    		rotationy2[0][2] = sin*(-1);
	    		rotationy2[2][0] = sin;
	    		rotationy2[2][2] = cos;
	            break;
	        case 'z':
				if(rotationZ2>360)rotationZ2-=360;
				rotationZ2 +=angle;
	    	    radians = Math.toRadians(rotationZ2);
	    	    sin = Math.sin(radians);
	    	    cos = Math.cos(radians);
	    	    rotationz2[0][0] = cos;
	    	    rotationz2[0][1] = sin * (-1);
	    	    rotationz2[1][0] = sin;
	    	    rotationz2[1][1] = cos;
	            break;
	        default:
	            throw new IllegalArgumentException("El valor del eje debe ser 'x', 'y' o 'z'");
	    }
	    int[] puntos = {(int) center.getX(), (int) center.getY(), (int) center.getZ(), 1};
	    puntos = matrixMultiplication(puntos, rotationx2);
	    puntos = matrixMultiplication(puntos, rotationy2);
	    puntos = matrixMultiplication(puntos, rotationz2);
	    
	    center = new Vertex3D(puntos[0]+ce.getX(),puntos[1]+ce.getY(),puntos[2]+ce.getZ());

	    move(puntos[0]+(int)ce.getX(),puntos[1]+(int)ce.getY(),puntos[2]+(int)ce.getZ());
	}
	
	public void bothRotates(Vertex3D ce, int angle1, int angle2, char axis1, char axis2) {
		takeOriginals();
		getNewCenter();
		moveToZero();
		int dx = (int) (center.getX()-ce.getX());
		int dy = (int) (center.getY()-ce.getY());
		int dz = (int) (center.getZ()-ce.getZ());
		
		
		center = new Vertex3D(dx,dy,dz);
	    double radians;
	    double sin;
	    double cos;

	    switch (axis1) {
	        case 'x':
				if(rotationX2>360)rotationX2-=360;
				rotationX2 +=angle1;
	    	    radians = Math.toRadians(rotationX2);
	    	    sin = Math.sin(radians);
	    	    cos = Math.cos(radians);
	    	    rotationx2[1][1] = cos;
	    	    rotationx2[1][2] = sin * (-1);
	    	    rotationx2[2][1] = sin;
	    	    rotationx2[2][2] = cos;
	            break;
	        case 'y':
				if(rotationY2>360)rotationY2-=360;
				rotationY2 +=angle1;
	    		radians = Math.toRadians(rotationY2);
	    		sin = Math.sin(radians);
	    		cos = Math.cos(radians);
	    		rotationy2[0][0] = cos;
	    		rotationy2[0][2] = sin*(-1);
	    		rotationy2[2][0] = sin;
	    		rotationy2[2][2] = cos;
	            break;
	        case 'z':
				if(rotationZ2>360)rotationZ2-=360;
				rotationZ2 +=angle1;
	    	    radians = Math.toRadians(rotationZ2);
	    	    sin = Math.sin(radians);
	    	    cos = Math.cos(radians);
	    	    rotationz2[0][0] = cos;
	    	    rotationz2[0][1] = sin * (-1);
	    	    rotationz2[1][0] = sin;
	    	    rotationz2[1][1] = cos;
	            break;
	        default:
	            throw new IllegalArgumentException("El valor del eje debe ser 'x', 'y' o 'z'");
	    }
	    int[] puntos = {(int) center.getX(), (int) center.getY(), (int) center.getZ(), 1};
	    puntos = matrixMultiplication(puntos, rotationx2);
	    puntos = matrixMultiplication(puntos, rotationy2);
	    puntos = matrixMultiplication(puntos, rotationz2);
	    
	    center = new Vertex3D(puntos[0]+ce.getX(),puntos[1]+ce.getY(),puntos[2]+ce.getZ());

	    move((int)(puntos[0]+ce.getX()),(int)(puntos[1]+ce.getY()),(int)(puntos[2]+ce.getZ()));
	    
	    moveToZero();
		double radians2;
		double sin2;
		double cos2;

		switch(axis2) {
		case 'x':
			if(rotationX>360)rotationX-=360;
			rotationX+=angle2;
			radians2 = Math.toRadians(rotationX);
			sin2 = Math.sin(radians2);
			cos2 = Math.cos(radians2);
			rotationx[1][1] = cos2;
			rotationx[1][2] = sin2*(-1);
			rotationx[2][1] = sin2;
			rotationx[2][2] = cos2;
			break;
		case'y':

			if(rotationY>360)rotationY-=360;
			rotationY+=angle2;
			radians2 = Math.toRadians(rotationY);
			sin2 = Math.sin(radians2);
			cos2 = Math.cos(radians2);
			rotationy[0][0] = cos2;
			rotationy[0][2] = sin2*(-1);
			rotationy[2][0] = sin2;
			rotationy[2][2] = cos2;
			break;
		case 'z':
			if(rotationZ>720)rotationZ-=360;
			rotationZ+=angle2;
			radians2 = Math.toRadians(rotationZ);
			sin2 = Math.sin(radians2);
			cos2 = Math.cos(radians2);
			rotationz[0][0] = cos2;
			rotationz[0][1] = sin2*(-1);
			rotationz[1][0] = sin2;
			rotationz[1][1] = cos2;
			break;
			default: 
				throw new IllegalArgumentException("The axis must be x, y or z");
		}	
		for(int i =0; i<vertex.length; i++) {
			for(int j = 0; j<vertex[i].length; j++) {
				int[] puntos2 = {(int) vertex[i][j].getX(), (int) vertex[i][j].getY(), (int) vertex[i][j].getZ(),1};
				puntos2 = matrixMultiplication(puntos2,rotationy);
				puntos2 = matrixMultiplication(puntos2,rotationz);
				puntos2 = matrixMultiplication(puntos2,rotationx);
				vertex[i][j] = new Vertex3D(puntos2[0], puntos2[1], puntos2[2]);
			}
		}
				
		moveToCenter();
	    
	}
	
	protected void paintParallelPolygon(BufferedImage b, Vertex3D v1,Vertex3D v2,Vertex3D v3,Vertex3D v4, Color c){
		
		
	
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
