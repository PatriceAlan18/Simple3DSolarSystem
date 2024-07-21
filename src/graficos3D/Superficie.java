package graficos3D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Superficie {

	private Vertice3D[] vertices;
	private Vertice3D[] verticesAux, aux1,aux2;
	private char sentido;
	private int distancia, profundidad;
	private BufferedImage buffer;
	private int[] punto = new int[3];
	private int[] plano = new int[3];
	private Color color;
	public static final int PARALELA = 1;
	public static final int PERSPECTIVA = 0;
	private Vertice3D centro = new Vertice3D(0,0,0);
	private int dx, dy,dz, sx, sy , sz;
	private int[][] translacion = {{1,0,0,dx},{0,1,0,dy},{0,0,1,dz},{0,0,0,1}};
	private double[][] escalacion = {{sx,0,0,0},{0,sy,0,0},{0,0,sz,0},{0,0,0,1}};
	double[][] rotacionz = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
	double[][] rotaciony = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
	double[][] rotacionx = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
	private int rotacionActualx = 0;
	private int rotacionActualy = 0;
	private int rotacionActualz = 0;
	private int rotacionActualx2 = 0;
	private int rotacionActualy2 = 0;
	private int rotacionActualz2 = 0;
	private int xA = 0, yA = 0, zA = 0;

	
	public Superficie(Vertice3D[] vertices, char sentido, int distancia, int profundidad, Color c) {
		buffer = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
		this.vertices = vertices;
		this.verticesAux = new Vertice3D[vertices.length];
		this.aux1 = new Vertice3D[vertices.length];
		this.aux2 = new Vertice3D[vertices.length];
		this.sentido = sentido;
		this.color=c;
		this.distancia = distancia;
		this.profundidad = profundidad;
		aumento();
		actualizarCentro();
		asegurarVertices();
	}
	
	public void aumento() {
		switch(sentido) {
		case 'x':
			xA = distancia;
			break;
		case 'y':
			yA = distancia;
			break;
		case 'z':
			zA = distancia;
			break;
			default:
				throw new IllegalArgumentException("El sentido debe ser x, y o z");
		}
	}
	
	public void dibujar(int proyeccion, Graphics g,  int[] puntos) {
		
		for(int i = 0; i<vertices.length; i++) aux1[i]=new Vertice3D(vertices[i]);
		
		if(proyeccion == PARALELA) {
			this.plano = puntos;
			for(int i = 0; i<vertices.length-1; i ++)	Linea3DParalela(aux1[i], aux1[i+1], color, g);

			for(int j = 0; j<profundidad; j++) {
				for(int m = 0; m<vertices.length; m++)aux2[m] = new Vertice3D(aux1[m].getX()+xA, aux1[m].getY()+yA, aux1[m].getZ()+zA);
				for(int i = 0; i<vertices.length-1; i++) {
					Linea3DParalela(aux2[i], aux2[i+1], color, g);
					Linea3DParalela(aux1[i], aux2[i+1], color, g);
				}
				for(int l = 0; l<vertices.length; l++) {
					Linea3DParalela(aux1[l], aux2[l], color, g);
					aux1[l] = new Vertice3D(aux2[l].getX(), aux2[l].getY(), aux2[l].getZ());
				}

			}
			
			return;
		}
		if(proyeccion == PERSPECTIVA) {
			this.punto = puntos;
			for(int i = 0; i<vertices.length-1; i ++)	Linea3DPerspectiva(aux1[i], aux1[i+1], color, g);

			for(int j = 0; j<profundidad; j++) {
				for(int m = 0; m<vertices.length; m++)aux2[m] = new Vertice3D(aux1[m].getX()+xA, aux1[m].getY()+yA, aux1[m].getZ()+zA);
				for(int i = 0; i<vertices.length-1; i++) {
					Linea3DPerspectiva(aux2[i], aux2[i+1], color, g);
					Linea3DPerspectiva(aux1[i], aux2[i+1], color, g);

				}
				for(int l = 0; l<vertices.length; l++) {
					Linea3DPerspectiva(aux1[l], aux2[l], color, g);
					aux1[l] = new Vertice3D(aux2[l].getX(), aux2[l].getY(), aux2[l].getZ());
				}

			}
			return;
		}
		throw new IllegalArgumentException("La proyeccion no es valida");
	}
	
	public void transladarVerticesOrigen() {
		for(int i =0; i<vertices.length; i++) {
			vertices[i].setX(vertices[i].getX()-centro.getX());
			vertices[i].setY(vertices[i].getY()-centro.getY());
			vertices[i].setZ(vertices[i].getZ()-centro.getZ());
		}
	}
	
	/**Mueve los vertices al sitio donde se encontraban, especialmente utl
	 * despues de mover a 0,0,0
	**/
	public void transladarVerticesCentro() {
		for(int i =0; i<vertices.length; i++) {
			vertices[i].setX(vertices[i].getX()+centro.getX());
			vertices[i].setY(vertices[i].getY()+centro.getY());
			vertices[i].setZ(vertices[i].getZ()+centro.getZ());
		}
	}
	
	/**Actualiza los valores del centro a los nuevos, es usado despues de transladar 
	**/
	public void actualizarCentro() {
		int x = 0, y= 0, z = 0;
		int l = vertices.length;
		for(int i = 0; i<l; i++) {
			x+=vertices[i].getX();
			y+=vertices[i].getY();
			z+=vertices[i].getZ();
		}
		centro.setX(x/l);
		centro.setY(y/l);
		centro.setZ(z/l);
	}
	
	public void vertices() {
		for(int i =0; i<vertices.length; i++)vertices[i] = new Vertice3D(verticesAux[i].getX(), verticesAux[i].getY(), verticesAux[i].getZ());

	}
	
	/**Despues de cambiar los vertices mediante una translacion o escalacion estos se aseguran en una matriz
	 * para futuras rotaciones
	**/
	public void asegurarVertices() {
		for(int i =0; i<vertices.length; i++)verticesAux[i] = new Vertice3D(vertices[i].getX(), vertices[i].getY(), vertices[i].getZ());
		
	}
	
	/**
	 * Mueve la curva la distancia indicada
	 * @param dx Distancia x a transladarse
	 * @param dy Distancia y a transladarse
	 * @param dz Distancia z a transladarse
	 */
	public void transladar(int dx, int dy, int dz) {
		translacion[0][3] = dx;
		translacion[1][3] = dy;
		translacion[2][3] = dz;
		for(int i =0; i<vertices.length; i++) {
			int[] puntos = {vertices[i].getX(), vertices[i].getY(), vertices[i].getZ(),1};
			puntos = multiplicarMatrices(puntos,translacion);
			vertices[i] = new Vertice3D(puntos[0], puntos[1], puntos[2]);
		}	
		
		actualizarCentro();
		asegurarVertices();

	}
	
	/**
	 * Mueve la curva la distancia indicada sin asegurar los vertices
	 * @param dx Distancia x a transladarse
	 * @param dy Distancia y a transladarse
	 * @param dz Distancia z a transladarse
	 * @param asegurar Cualquier valor booleano
	 */
	public void transladar(int dx, int dy, int dz, boolean asegurar) {
		translacion[0][3] = dx;
		translacion[1][3] = dy;
		translacion[2][3] = dz;
		for(int i =0; i<vertices.length; i++) {
			int[] puntos = {vertices[i].getX(), vertices[i].getY(), vertices[i].getZ(),1};
			puntos = multiplicarMatrices(puntos,translacion);
			vertices[i] = new Vertice3D(puntos[0], puntos[1], puntos[2]);
		}	

	}
	
	/**Escala los vertices, la curva se coloca en el centro con tamano diferente 
	 * @param sx El valor porcentual a escalar el eje x
	 * @param sy El valor porcentual a escalar el eje y 
	 * @param sz El valor porcentual a escalar el eje z
	**/
	public void escalar(int sx, int sy, int sz) {
		sx = sx/100;
		sy = sy/100;
		sz = sz/100;
		escalacion[0][0] = sx;
		escalacion[1][1] = sy;
		escalacion[2][2] = sz;	
		transladarVerticesOrigen();	
		for(int i =0; i<8; i++) {
			int[] puntos = {vertices[i].getX(), vertices[i].getY(), vertices[i].getZ(),1};
			puntos = multiplicarMatrices(puntos,escalacion);
			vertices[i] = new Vertice3D(puntos[0], puntos[1], puntos[2]);
		}
		transladarVerticesCentro();
		asegurarVertices();
	}
	
	/**
	 * Rota los vertices de la figura alrededor del punto proporcionado
	 * @param ce El centro sobre el que sera rotado
	 * @param angulo El angulo de rotacion
	 * @param eje El eje sobre el que se aplicara el angulo
	 * @throws IllegalArgumentException si el eje no es x, y o z
	 */
	public void rotar(Vertice3D ce, int angulo, char eje) {
		vertices();
		actualizarCentro();
		dx = centro.getX()-ce.getX();
		dy = centro.getY()-ce.getY();
		dz = centro.getZ()-ce.getZ();
		
		transladarVerticesOrigen();
		centro = new Vertice3D(dx,dy,dz);
	    double radianes;
	    double seno;
	    double coseno;

	    switch (eje) {
	        case 'x':
				if(rotacionActualx2>360)rotacionActualx2-=360;
				rotacionActualx2+=angulo;
	        	rotacionActualx2 +=angulo;
	    	    radianes = Math.toRadians(rotacionActualx2);
	    	    seno = Math.sin(radianes);
	    	    coseno = Math.cos(radianes);
	    	    rotacionx[1][1] = coseno;
	    	    rotacionx[1][2] = seno * (-1);
	    	    rotacionx[2][1] = seno;
	    	    rotacionx[2][2] = coseno;
	            break;
	        case 'y':
				if(rotacionActualy2>360)rotacionActualy2-=360;
				rotacionActualy2+=angulo;
	        	rotacionActualy2 +=angulo;
	    		radianes = Math.toRadians(rotacionActualy2);
	    		seno = Math.sin(radianes);
	    		coseno = Math.cos(radianes);
	    		rotaciony[0][0] = coseno;
	    		rotaciony[0][2] = seno*(-1);
	    		rotaciony[2][0] = seno;
	    		rotaciony[2][2] = coseno;
	            break;
	        case 'z':
				if(rotacionActualz2>360)rotacionActualz2-=360;
				rotacionActualz2+=angulo;
	        	rotacionActualz2 +=angulo;
	    	    radianes = Math.toRadians(rotacionActualz2);
	    	    seno = Math.sin(radianes);
	    	    coseno = Math.cos(radianes);
	    	    rotacionz[0][0] = coseno;
	    	    rotacionz[0][1] = seno * (-1);
	    	    rotacionz[1][0] = seno;
	    	    rotacionz[1][1] = coseno;
	            break;
	        default:
	            throw new IllegalArgumentException("El valor del eje debe ser 'x', 'y' o 'z'");
	    }
	    int[] puntos = {centro.getX(), centro.getY(), centro.getZ(), 1};
	    puntos = multiplicarMatrices(puntos, rotacionx);
	    puntos = multiplicarMatrices(puntos, rotaciony);
	    puntos = multiplicarMatrices(puntos, rotacionz);
	    
	    centro = new Vertice3D(puntos[0]+ce.getX(),puntos[1]+ce.getY(),puntos[2]+ce.getZ());

	    transladar(puntos[0]+ce.getX(),puntos[1]+ce.getY(),puntos[2]+ce.getZ(),false);
	}
	
	/**Rota los vertices sobre su centro
	 * @param angulo En angulo al que seran rotados
	 * @param eje El eje respecto al que sera efectuada esta rotacion, el cual puede ser x,y,z
	 *@throws IllegalArgumentException Cuando el eje no es x,y o z
	**/
	public void rotarSobreEje(int angulo, char eje) {
		vertices();
		transladarVerticesOrigen();
		double radianes;
		double seno;
		double coseno;

		switch(eje) {
		case 'x':
			if(rotacionActualx>360)rotacionActualx-=360;
			rotacionActualx+=angulo;
			radianes = Math.toRadians(rotacionActualx);
			seno = Math.sin(radianes);
			coseno = Math.cos(radianes);
			rotacionx[1][1] = coseno;
			rotacionx[1][2] = seno*(-1);
			rotacionx[2][1] = seno;
			rotacionx[2][2] = coseno;
			break;
		case'y':

			if(rotacionActualy>360)rotacionActualy-=360;
			rotacionActualy+=angulo;
			radianes = Math.toRadians(rotacionActualy);
			seno = Math.sin(radianes);
			coseno = Math.cos(radianes);
			rotaciony[0][0] = coseno;
			rotaciony[0][2] = seno*(-1);
			rotaciony[2][0] = seno;
			rotaciony[2][2] = coseno;
			break;
		case 'z':
			if(rotacionActualz>360)rotacionActualz-=360;
			rotacionActualz+=angulo;
			radianes = Math.toRadians(rotacionActualz);
			seno = Math.sin(radianes);
			coseno = Math.cos(radianes);
			rotacionz[0][0] = coseno;
			rotacionz[0][1] = seno*(-1);
			rotacionz[1][0] = seno;
			rotacionz[1][1] = coseno;
			break;
			default: 
				throw new IllegalArgumentException("El valor del eje debe ser 'x', 'y' o 'z'");
		}	
		for(int i =0; i<vertices.length; i++) {
			int[] puntos = {vertices[i].getX(), vertices[i].getY(), vertices[i].getZ(),1};
			puntos = multiplicarMatrices(puntos,rotaciony);
			puntos = multiplicarMatrices(puntos,rotacionz);
			puntos = multiplicarMatrices(puntos,rotacionx);
			vertices[i] = new Vertice3D(puntos[0], puntos[1], puntos[2]);
		}
				
		transladarVerticesCentro();

	}
	

	/**Multiplica 2 matrices
	 * @param matriz1 Una matriz de 1xN donde n es cualquier valor
	 * @param matriz2 Una matriz de NxM donde m es cualquier valor
	 * @return la matriz resultante de 1xn
	**/
	public int[] multiplicarMatrices(int[] matriz1,int[][] matriz2) {
		int[] nueva = new int[matriz1.length];
		for(int i = 0; i<matriz1.length; i++) {
			for(int j = 0; j<matriz2[i].length; j++) {
				nueva[i]=nueva[i]+matriz1[j]*matriz2[i][j];
			}
		}
		return nueva;
	}
	
	/**Multiplica 2 matrices
	 * @param matriz1 Una matriz de 1xN donde n es cualquier valor
	 * @param matriz2 Una matriz de NxM donde m es cualquier valor
	 * @return la matriz resultante de 1xn
	**/
	public int[] multiplicarMatrices(int[] matriz1,double[][] matriz2) {
		int[] nueva = new int[matriz1.length];
		for(int i = 0; i<matriz1.length; i++) {
			for(int j = 0; j<matriz2[i].length; j++) {
				nueva[i]=(int) (nueva[i]+matriz1[j]*matriz2[i][j]);
			}
		}
		return nueva;
	}
	
	/**Multiplica 2 matrices
	 * @param matriz1 Una matriz de 1xN donde n es cualquier valor
	 * @param matriz2 Una matriz de NxM donde m es cualquier valor
	 * @return la matriz resultante de 1xn
	**/
	public double[] multiplicarMatrices(double[] matriz1,double[][] matriz2) {
		double[] nueva = new double[matriz1.length];
		for(int i = 0; i<matriz1.length; i++) {
			for(int j = 0; j<matriz2[i].length; j++) {
				nueva[i]= (nueva[i]+matriz1[j]*matriz2[i][j]);
			}
		}
		return nueva;
	}
	
	/**Muestra en consola la matriz proporcionada
	 * @param matriz1 Una matriz de 1xN donde n es cualquier valor
	**/
	public void verMatriz(int[] matriz) {
		for(int i = 0; i<matriz.length; i++) {
			System.out.print(matriz[i]+"  ");

		}
		System.out.print("\n");
	}
	
	public Vertice3D[] getVertices() {
		return vertices;
	}
	
	private void Linea3DParalela(Vertice3D v1, Vertice3D v2, Color c, Graphics g) {
		int x0 = v1.getX();
		int y0 = v1.getY();
		int z0 = v1.getZ();
		int x1 = v2.getX();
		int y1 = v2.getY();
		int z1 = v2.getZ();
		int x2, x3, y2, y3;
		x2 = x0 - (plano[0] * z0) / plano[2];
		y2 = y0 - (plano[1] * z0) / plano[2];

		x3 = x1 - (plano[0] * z1) / plano[2];
		y3 = y1 - (plano[1] * z1) / plano[2];

		dibujarLinea(x2, y2, x3, y3, c,g);


	}

	private void Linea3DPerspectiva(Vertice3D v1, Vertice3D v2, Color c, Graphics g) {
		int x0 = v1.getX();
		int y0 = v1.getY();
		int z0 = v1.getZ();
		int x1 = v2.getX();
		int y1 = v2.getY();
		int z1 = v2.getZ();
		int x2, x3, y2, y3;
		x2 = punto[0]-((punto[2]*(x0-punto[0]))/(z0-punto[2]));
		y2 = punto[1] - ((punto[2]*(y0-punto[1]))/(z0-punto[2]));
		
		x3 = punto[0]-((punto[2]*(x1-punto[0]))/(z1-punto[2]));
		y3 = punto[1] - ((punto[2]*(y1-punto[1]))/(z1-punto[2]));
		dibujarLinea(x2, y2, x3, y3, c,g);
	}

	public void dibujarLinea(int x1, int y1, int x2, int y2, Color c, Graphics g) {
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
		if (yF < y) {
			int aux = y;
			y = yF;
			yF = aux;
		}
		for (int i = 0; i <= Math.abs(yF - y); i++)
			ponerPixel(x, y + i, c, g);
	}

	private void dibujarLineaHorizontal(int x, int xF, int y, Color c, Graphics g) {
		if (xF < x) {
			int aux = x;
			x = xF;
			xF = aux;
		}
		for (int i = 0; i <= Math.abs(xF - x); i++)
			ponerPixel(x + i, y, c, g);
	}
	
	private void ponerPixel(int x, int y, Color c, Graphics g) {
		buffer.setRGB(0, 0, c.getRGB());
		g.drawImage(buffer, x, y,null);

	}
	
}
