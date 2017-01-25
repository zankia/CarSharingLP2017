package Pack_Simu;


//La classe client représente un voyageur
public class Client
{
	private int idClient;
	int appearanceMoment;
	private Point[] posClient;
	//Index de pos :
	//0 position du client
	//1 destination du client
	int stateClient;
	//state :
	//0 sur le trottoir
	//1 dans la voiture
	//2 arrivé
	private Car carClient;
	private boolean isUsingCarSharing;
	private static int idcptClient=0; 

	Client(int time, int coordX, int coordY){
		this.idClient = idcptClient;
		this.appearanceMoment = time;
		this.posClient = new Point[2];
		this.posClient[0] = new Point(coordX,coordY);
		this.stateClient = 0;
		this.isUsingCarSharing = true;
		idcptClient++;
	}
	
	public int getStateClient () {
		return this.stateClient;
	}
	
	public void setStateClient(int newStateClient) {
		this.stateClient = newStateClient;
	}

	public void setIsUsingCarSharing(boolean newIsUsingCarSharing) {
		this.isUsingCarSharing = newIsUsingCarSharing;
	}
	
	public boolean getIsUsingCarSharing () {
		return this.isUsingCarSharing;
	}
	
	public Point[] getPosClient () {
		return this.posClient;
	}
	
	public Car getCarClient () {
		return this.carClient;
	}
	
	public void setCarClient (Car newCar) {
		this.carClient = newCar;
	}

	public int getIdClient() {
		return this.idClient;
	}
}
