package Pack_Simu;
// Java.awt.Point
public class Point {
	
	private int coordX;
	private int coordY;

	Point(int coordX,int coordY) {
		this.coordX = coordX;
		this.coordY = coordY;
	}
	
	public void updatePoint(int newCoordX, int newCoordY)
	{
		this.coordX = newCoordX;
		this.coordY = newCoordY;
	}
	
	public int getCoordX() {
		return this.coordX;
	}
	
	public int getCoordY() {
		return this.coordY;
	}
}
