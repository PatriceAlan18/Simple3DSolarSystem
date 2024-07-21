package proyecto;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class MyGraphics {
	
	private BufferedImage buffer;
	private Graphics g;
	protected int[] plane;
	private int point[];
	private final int PARALLEL = 1;
	private final int PERSPECTIVE = 0;
	protected final Color UNUSED = new Color(0,187,45);
	private Color ambientLightColor;
	private Vertex3D light;
	private double Ka, Ks, Kd;

	public MyGraphics() {
		buffer = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
		plane = new int[3];
		point = new int[3];

	}
	
	protected void rectangle(int x0, int y0, int x1, int y1, Color c) {
		drawVerticalLine(x0,y0,y1,c);
		drawHorizontalLine(x0,x1,y1,c);
		drawVerticalLine(x1,y0,y1,c);
		drawHorizontalLine(x0,x1,y0,c);
	}
	
	public void paint2D(BufferedImage bf, int x, int y, Color c, Color[] back) {
		int x0 = x, x1 = y;
		int y0 = x0, y1 = x1;
		while(new Color(bf.getRGB(y0+1, x1)).getRGB()==c.getRGB()) y0++;
		while(new Color(bf.getRGB(x0-1, x1)).getRGB()==c.getRGB())x0--;
		drawHorizontalLine(x0,y0,y1,back);
		if(new Color(bf.getRGB(x, y+1)).getRGB()==c.getRGB())paint2D(bf,x, y+1, c, back);
		if(new Color(bf.getRGB(x, y-1)).getRGB()==c.getRGB())paint2D(bf,x, y-1, c, back);
	}
	
	public void paint2D(BufferedImage bf, int x, int y, Color c, Color back) {
		int x0 = x, x1 = y;
		int y0 = x0, y1 = x1;
		while(new Color(bf.getRGB(y0+1, x1)).getRGB()==c.getRGB()) y0++;
		while(new Color(bf.getRGB(x0-1, x1)).getRGB()==c.getRGB())x0--;
		drawHorizontalLine(x0,y0,y1,back);
		if(new Color(bf.getRGB(x, y+1)).getRGB()==c.getRGB())paint2D(bf,x, y+1, c, back);
		if(new Color(bf.getRGB(x, y-1)).getRGB()==c.getRGB())paint2D(bf,x, y-1, c, back);
	}
	
	public void paint2DFlood(BufferedImage bf, int x, int y, Color c, Color back) {
	    putPixel(x, y, back);
	    if (new Color(bf.getRGB(x+1,y)).getRGB() != c.getRGB() && new Color(bf.getRGB(x+1,y)).getRGB() != back.getRGB())paint2DFlood(bf,x+1,y, c, back);
	    if (new Color(bf.getRGB(x-1,y)).getRGB() != c.getRGB() && new Color(bf.getRGB(x-1,y)).getRGB() != back.getRGB())paint2DFlood(bf,x-1,y, c, back);
	    if (new Color(bf.getRGB(x,y+1)).getRGB() != c.getRGB() && new Color(bf.getRGB(x,y+1)).getRGB() != back.getRGB())paint2DFlood(bf,x,y+1, c, back);
	    if (new Color(bf.getRGB(x,y-1)).getRGB() != c.getRGB() && new Color(bf.getRGB(x,y-1)).getRGB() != back.getRGB())paint2DFlood(bf,x,y-1, c, back);
	}
	
	
	protected void paintParallelPolygon(BufferedImage bf, Vertex3D v1,Vertex3D v2,Vertex3D v3,Vertex3D v4, Color c){
		draw3DParallelLine(v1, v2, UNUSED);
		draw3DParallelLine(v1, v3, UNUSED);
		draw3DParallelLine(v3, v4, UNUSED);
		draw3DParallelLine(v2, v4, UNUSED);

		double x0 = v1.getX()+v2.getX()+v3.getX()+v4.getX();
		double y0 = v1.getY()+v2.getY()+v3.getY()+v4.getY();
		double z0 = v1.getZ()+v2.getZ()+v3.getZ()+v4.getZ();

		x0=x0/4;
		y0=y0/4;
		z0=z0/4;

		
		double x2, y2;
		x2 = x0 - (plane[0] * z0) / plane[2];
		y2 = y0 - (plane[1] * z0) / plane[2];

		Vertex3D[] v = {v1,v2,v3,v4};

		Color color  = getPhongColor(v, new Vertex3D(x0,y0,z0),c);
		//Color color = c;
		paint2DFlood(bf, (int)x2, (int)y2, UNUSED, color);
		draw3DParallelLine(v1, v2, color);
		draw3DParallelLine(v1, v3, color);
		draw3DParallelLine(v3, v4, color);
		draw3DParallelLine(v2, v4, color);
	}
	
	protected Color getPhongColor(Vertex3D[] vp, Vertex3D center, Color materialColor) {
		Vertex3D n = getNormal(vp);
		Vertex3D v = normalize(new Vertex3D(plane[0],plane[1],plane[2]));
		Vertex3D l = getLightVector(center);
		Vertex3D r = getReflectionVector(n,l);
		

		
		Color ambient = getAmbientColor(materialColor);
		Color diffuse = getDiffusedColor(n,l,ambient);
		Color specular = getSpecularColor(r,v,diffuse);
		

		return specular;
	}
	
	protected Color getSpecularColor(Vertex3D r, Vertex3D v, Color diffuse) {
		double dot = dotProduct(r, v);
		double angle = getAngle(r,v);
		double factor = Math.pow(Math.abs(dot), angle);
		double factorKs = factor*Ks;
		
		int red = (int) (diffuse.getRed() * factorKs);
	    int green = (int) (diffuse.getGreen() * factorKs);
	    int blue = (int) (diffuse.getBlue() * factorKs);
	    return new Color(red, green, blue);
	}
	
	protected double getAngle(Vertex3D r, Vertex3D v) {
		double rM = getMagnitude(r);
		double vM = getMagnitude(v);
		double dot = dotProduct(r, v);
		double cos = dot /(vM*rM);
		
		return Math.acos(cos);
	}
	
	protected double getMagnitude(Vertex3D v) {
		return Math.sqrt(v.getX()*v.getX()+v.getY()*v.getY()+v.getZ()*v.getZ());
	}
	
	protected Color getDiffusedColor(Vertex3D n, Vertex3D l, Color material) {
		double dot = dotProduct(n, l);
		double factor = Math.abs(Kd * dot);

		int red = (int) (factor * material.getRed());
	    int green = (int) (factor * material.getGreen());
	    int blue = (int) (factor * material.getBlue());
		Color c = new Color(red, green, blue);
		//System.out.println(c);

	    return c;
	}
	
	protected Color getAmbientColor(Color c) {
		 int red = (int) (c.getRed() * Ka);
		 int green = (int) (c.getGreen() * Ka);
		 int blue = (int) (c.getBlue() * Ka);
		 return new Color(red, green, blue);
	}
	
	protected Vertex3D getReflectionVector(Vertex3D n, Vertex3D l) {
		double dot = dotProduct(n, l);

		Vertex3D s = scale(n,2*dot);
		Vertex3D r = substract(s, l);
		return normalize(r);
	}
	
	protected Vertex3D getLightVector(Vertex3D cen) {
		if(this.light == null)throw new NullPointerException("Light Value Is Null");
		double dx = light.getX()-cen.getX();
		double dy = light.getY()-cen.getY();
		double dz = light.getZ()-cen.getZ();


		Vertex3D v = new Vertex3D(dx,dy,dz);
		
		return normalize(v);
	}
	
	protected Vertex3D getNormal(Vertex3D[] v) {

		Vertex3D AB = substract(v[1], v[0]);
		Vertex3D AC = substract(v[2], v[0]);

		Vertex3D normal = cross(AB, AC);
		

		return normalize(normal);
		
	}
	
	protected Vertex3D scale(Vertex3D v, double d) {
		double nx = v.getX()*d;
		double ny = v.getY()*d;
		double nz = v.getZ()*d;
		return new Vertex3D(nx, ny, nz);
	}
	
	protected double dotProduct(Vertex3D v1, Vertex3D v2) {
	    return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
	}
	
	protected Vertex3D add(Vertex3D v1, Vertex3D v2) {
		double newX = v1.getX()+v2.getX();
		double newY = v1.getY()+v2.getY();
		double newZ = v1.getZ()+v2.getZ();
		return new Vertex3D(newX, newY, newZ);
	}
	
	protected Vertex3D substract(Vertex3D v1, Vertex3D v2) {
		double newX = v1.getX()-v2.getX();
		double newY = v1.getY()-v2.getY();
		double newZ = v1.getZ()-v2.getZ();
		return new Vertex3D(newX, newY, newZ);
	}
	
	protected Vertex3D cross(Vertex3D v1, Vertex3D v2) {
		double newX = v1.getY()*v2.getZ() - v1.getZ()*v2.getY();
		double newY = v1.getZ()*v2.getX() - v1.getX()*v2.getZ();
		double newZ = v1.getX()*v2.getY() - v1.getY()*v2.getX();
		return new Vertex3D(newX, newY, newZ);
	}
	
	protected Vertex3D normalize(Vertex3D v) {
		double x = v.getX();
		double y = v.getY();
		double z = v.getZ();
		double magnitude = Math.sqrt(x * x + y * y + z * z);
		
		double newX =  (x/magnitude);
		double newY =  (y/magnitude);
		double newZ =  (z/magnitude);
		return new Vertex3D(newX, newY, newZ);
	}
	
	protected void draw3DParallelLine(Vertex3D v1, Vertex3D v2, Color c) {
		int x0 = (int) v1.getX();
		int y0 = (int) v1.getY();
		int z0 = (int) v1.getZ();
		int x1 = (int) v2.getX();
		int y1 = (int) v2.getY();
		int z1 = (int) v2.getZ();
		int x2, x3, y2, y3;
		x2 = x0 - (plane[0] * z0) / plane[2];
		y2 = y0 - (plane[1] * z0) / plane[2];
		x3 = x1 - (plane[0] * z1) / plane[2];
		y3 = y1 - (plane[1] * z1) / plane[2];
		drawLine(x2, y2, x3, y3, c);
	}

	protected void draw3DPerspectiveLine(Vertex3D v1, Vertex3D v2, Color c) {
		int x0 = (int) v1.getX();
		int y0 = (int) v1.getY();
		int z0 = (int) v1.getZ();
		int x1 = (int) v2.getX();
		int y1 = (int) v2.getY();
		int z1 = (int) v2.getZ();
		int x2, x3, y2, y3;
		x2 = point[0]-((point[2]*(x0-point[0]))/(z0-point[2]));
		y2 = point[1] - ((point[2]*(y0-point[1]))/(z0-point[2]));
		x3 = point[0]-((point[2]*(x1-point[0]))/(z1-point[2]));
		y3 = point[1] - ((point[2]*(y1-point[1]))/(z1-point[2]));
		drawLine(x2, y2, x3, y3, c);
	}

	protected void drawLine(int x1, int y1, int x2, int y2, Color c) {
		int dx = Math.abs(x2 - x1);
	    int dy = Math.abs(y2 - y1);
		if(dx==0) {
			drawVerticalLine(x1,y1,y2,c);
			return;
		}
		
		if(dy==0) {
			drawHorizontalLine(x1,x2,y1,c);
			return;
		}
		

	    int sx = (x1 < x2) ? 1 : -1;
	    int sy = (y1 < y2) ? 1 : -1;

	    int error = dx - dy;

	    while (x1 != x2 || y1 != y2) {
	        putPixel(x1, y1,c);
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

	    putPixel(x2, y2,c);

	}

	protected void drawVerticalLine(int x, int y, int yF, Color c) {
		if (yF < y) {
			int aux = y;
			y = yF;
			yF = aux;
		}
		for (int i = 0; i <= Math.abs(yF - y); i++)
			putPixel(x, y + i, c);
	}

	protected void drawHorizontalLine(int x, int xF, int y, Color c) {
		if (xF < x) {
			int aux = x;
			x = xF;
			xF = aux;
		}
		for (int i = 0; i <= Math.abs(xF - x); i++)
			putPixel(x + i, y, c);
	}
	
	private void drawHorizontalLine(int x, int xF, int y, Color c[]) {
		if(xF<x) {
			int aux = x;
			x=xF;
			xF=aux;
		}
		for(int i = 0; i <= Math.abs(xF-x); i++)  putPixel(x+i, y, c[getRandom(0, c.length-1)]);			
	}
	
	public int[] matrixMultiplication(int[] matrix1, int[][] matrix2) {
		int[] newM = new int[matrix1.length];
		for(int i = 0; i<matrix1.length; i++) {
			for(int j = 0; j<matrix2[i].length; j++) {
				newM[i]=newM[i]+matrix1[j]*matrix2[i][j];
			}
		}
		return newM;
	}

	public int[] matrixMultiplication(int[] matrix1, double[][] matrix2) {
		int[] newM = new int[matrix1.length];
		for(int i = 0; i<matrix1.length; i++) {
			for(int j = 0; j<matrix2[i].length; j++) {
				newM[i]=(int) (newM[i]+matrix1[j]*matrix2[i][j]);
			}
		}
		return newM;
	}

	public double[] matrixMultiplication(double[] matrix1,double[][] matrix2) {
		double[] newM = new double[matrix1.length];
		for(int i = 0; i<matrix1.length; i++) {
			for(int j = 0; j<matrix2[i].length; j++) {
				newM[i]= (newM[i]+matrix1[j]*matrix2[i][j]);
			}
		}
		return newM;
	}
	
	protected void putPixel(int x, int y, Color c) {
		buffer.setRGB(0, 0, c.getRGB());
		g.drawImage(buffer, x, y,null);

	}
	
	protected void setGraphics(Graphics g) {
		this.g=g;
	}
	
	protected void setPlane(int x, int y, int z) {
		plane[0] = x;
		plane[1] = y;
		plane[2] = z;
	}
	
	protected void setPoint(int x, int y, int z) {
		point[0] = x;
		point[1] = y;
		point[2] = z;
	}
	
	public int getRandom(int min, int max) {
	    return (int) (Math.random() * (max - min + 1) + min);
	}
	
	public void setLight(Vertex3D v) {
		this.light = new Vertex3D(v);
	}

	public void setKa(double K) {
		this.Ka = K;
	}
	
	public void setKs(double K) {
		this.Ks = K;
	}
	
	public void setKd(double K) {
		this.Kd = K;
	}
	
	public void setAmbientLightColor(Color a) {
		this.ambientLightColor = a;
	}
	
}
