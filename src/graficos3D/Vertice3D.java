package graficos3D;

public class Vertice3D {

	private int x;
	private int y;
	private int z;
	private int[][] translacion = {{1,0,0,9},{0,1,0,9},{0,0,1,9},{0,0,0,1}};
	private double[][] escalacion = {{9,0,0,0},{0,9,0,0},{0,0,9,0},{0,0,0,1}};
	
	public Vertice3D(int x, int y, int z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public Vertice3D(Vertice3D vertice) {
		this.x=vertice.getX();
		this.y=vertice.getY();
		this.z=vertice.getZ();
	}
	
	public void transladar(int dx, int dy, int dz) {
		
	}
	
	public void escalar(int sx, int sy, int sz) {
		
	}
	
	public void rotar(Vertice3D centro, int angulo, char eje) {
		
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	
	
	
}
