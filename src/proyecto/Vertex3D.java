package proyecto;

public class Vertex3D {

	private double x;
	private double y;
	private double z;
	
	public Vertex3D(double x, double y, double z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public Vertex3D(Vertex3D vertex) {
		this.x=vertex.getX();
		this.y=vertex.getY();
		this.z=vertex.getZ();
	}
	
	public void setXYZ(Vertex3D vertex) {
		this.x=vertex.getX();
		this.y=vertex.getY();
		this.z=vertex.getZ();
	}
	
	public String show() {
		return x+" , "+y+" , "+z;
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	
	
	
}
