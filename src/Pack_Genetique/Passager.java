package Pack_Genetique;
public class Passager {
	
	//Chaque Passager est caractérisé par un ID unique et possède deux Points, un point de départ et un point d'arrivée
	private int id;
	private Point[] points = new Point[2] ;
	private static int nbPassagers = 0;

	public Passager(){
		Passager.nbPassagers ++;
		this.id = Passager.nbPassagers;
		points[0] = new Point();
		points[1] = new Point();

	}
 
    /* Getters and setters */
	public Point getArrivee() {
		return this.points[0];
	}

	public Point getDepart() {
		return this.points[1];
	}
	
	public int getId() {
		return this.id;
	}
	public void setId(int i) {
		this.id = i;
	}
 

    @Override
    public String toString() {
        String passagerString = "";
        passagerString += "Passager n°"+this.id+" position de départ: "+this.getDepart()+", destination : "+this.getArrivee(); 
        return passagerString;
    }




}