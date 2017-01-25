package Pack_Simu;

import java.util.ArrayList;

//La classe des voitures
public class Car
{
	private int idCar;
	private Point posCar;
	//parcoursList contient les étapes du parcours
	private ArrayList<ParcoursStep> parcoursList;
	//occupantList contient les clients dans la voiture
	private ArrayList<Client> occupantList;
	//indique le numéro de la rue dans laquelle la voiture se trouve
	int streetId;
	private boolean isDoingCarSharing;
	private static int cptIdCar = 0;

	Car(int coordX, int coordY){
		this.idCar = cptIdCar;
		this.posCar = new Point(coordX,coordY);
		this.parcoursList = new ArrayList<ParcoursStep>();
		this.occupantList = new ArrayList<Client>();
		this.streetId = -1;
		this.isDoingCarSharing = true;
		cptIdCar++;
	}

	public boolean getIsDoingCarSharing() {
		return this.isDoingCarSharing;
	}
	
	public void setIsDoingCarSharing(boolean newIsDoingCarSharing) {
		this.isDoingCarSharing = newIsDoingCarSharing;
	}

	public Point getPosCar() {
		return this.posCar;
	}

	public int getIdCar() {
		return this.idCar;
	}

	public ArrayList<Client> getOccupantListCar() {
		return this.occupantList;
	}
	
	public ArrayList<ParcoursStep> getParcoursListCar() {
		return this.parcoursList;
	}
	
	public void setParcoursList(ArrayList<ParcoursStep> newParcoursList) {
		this.parcoursList = newParcoursList;
	}
}
