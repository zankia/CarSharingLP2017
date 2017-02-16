package Pack_Simu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.awt.Point;

public class Simulation{

	private int time;
	private boolean needAlgorithme;
	private ArrayList<Car> ListeVoitures; 
	private ArrayList<Client> ListeClients;
	private int nbVoituresSimulation;
	private int nbClientsSimulation;
	private Client notTargettedYet;
	private Car carSimulation;
	private Client selectedClient;
	private double carSpeedMean;
	double clientSpeedSum;
	int arrivedClient;
	private double clientSpeedMean; //Simulation
	private double clientRealSpeedMean; //Simulation
	private double arrivedRate;
	private int distSum;
	private int inconnu;

	//Initialisation des variables
	public Simulation(int cLength, int sLength, int width, int height) {
		
		Model model = new Model();
		City city = new City();
		this.time = 0;
		this.needAlgorithme = false;
		this.ListeVoitures = new ArrayList<Car>();
		this.ListeClients = new ArrayList<Client>();
		this.nbVoituresSimulation = 0;
		this.nbClientsSimulation = 0;
		this.notTargettedYet = null;
		this.carSimulation = null;
		this.selectedClient = null;
		model.setCarLength(cLength);
		model.setStreetLength(sLength);
		city.setCityWidth(width/model.getStreetLength()+1);
		city.setCityHeight(height/model.getStreetLength()+1);
		city.setStreetArray(new int [city.getCityWidth()][city.getCityHeight()][2]);
		this.carSpeedMean = 0;
		this.clientSpeedSum = 0;
		this.arrivedClient = 0;
		this.clientSpeedMean = 0;
		this.clientRealSpeedMean = 0;
		this.arrivedRate = 0;
		this.distSum = 0;
		this.inconnu = 0;
	}

	public void ajouterVoitureSimulation (int CoordX, int CoordY) {
		this.ListeVoitures.add(new Car(CoordX,CoordY));
		nbVoituresSimulation++;
		this.carSimulation = (this.ListeVoitures.get(nbVoituresSimulation-1));
		this.needAlgorithme = true;
	}

	public void ajouterClientSimulation(int x,int y) {
		//S'il y a un client dont la destination n'est pas encore définie
		if(this.notTargettedYet != null){
			this.notTargettedYet.getPosClient()[1] = new Point(x, y);
			this.ListeClients.add(this.notTargettedYet);
			this.nbClientsSimulation++;
			this.selectedClient = this.notTargettedYet;
			this.notTargettedYet = null;
			if(dist(this.selectedClient.getPosClient()[0],this.selectedClient.getPosClient()[1])==0) {
				deleteClient();
			} else {
				//Sinon on crée un nouveau client
				this.needAlgorithme = true;
				this.notTargettedYet = new Client(this.time,x,y);
			} 
		}
	}

	public void deleteCar(){
		this.ListeVoitures.remove(this.carSimulation);
		this.nbVoituresSimulation--;
		this.carSimulation = null;
		this.needAlgorithme = true;
	}

	public void deleteClient(){
		if(this.selectedClient == this.notTargettedYet){
			this.notTargettedYet = null;
		} else {
			if(this.selectedClient.getStateClient() == 1){
				this.selectedClient.getCarClient().getOccupantListCar().remove(this.selectedClient);
			}
			this.ListeClients.remove(this.selectedClient);
			this.nbClientsSimulation--;
		}
		this.selectedClient = null;
		this.needAlgorithme = true;
	}

	//Renvoie un tableau contenant les coordonnées des voitures
	public int[] getCoordoneeDesVoitures()
	{
		int[] t = new int[nbVoituresSimulation*2];
		for(int k=0;k<nbVoituresSimulation;k++)
		{
			t[2*k]=(int) this.ListeVoitures.get(k).getPosCar().getX();
			t[2*k+1]=(int) this.ListeVoitures.get(k).getPosCar().getY();
		}
		return t;
	}

	//Renvoie un tableau contenant les coordonnées des clients
	public int[] getCoordoneeDesClients() {
		int[] coord = new int[this.nbClientsSimulation*4 + 2*((this.notTargettedYet!=null)?1:0)];
		for(int l=0;l<this.nbClientsSimulation;l++) {
			coord[4*l]=(int) this.ListeClients.get(l).getPosClient()[0].getX();
			coord[4*l+1]=(int) this.ListeClients.get(l).getPosClient()[0].getY();
			coord[4*l+2]=(int) this.ListeClients.get(l).getPosClient()[1].getX();
			coord[4*l+3]=(int) this.ListeClients.get(l).getPosClient()[1].getY();
		}
		if(this.notTargettedYet!=null) {
			coord[4*nbClientsSimulation]=(int) this.notTargettedYet.getPosClient()[0].getX();
			coord[4*nbClientsSimulation+1]=(int) this.notTargettedYet.getPosClient()[0].getY();	
		}
		return coord;
	}


	/** ALGORITHME : DETERMINATION DU PARCOURS **/
	private //Numero de l'algorithme sélectionné
	int algoId;
	//Numero de la fonction de coût sélectionnée
	private int costRate;
	//booléen indiquant si le crible diviser pour mieux régner doit être effectué
	private boolean divide;
	//Nombre d'étapes maximum sélectionné
	private int stepMax;
	//Nombre de passagers maximum
	private int occupantCapacity;

	//Fonction algorithme déterminant le parcours le moins couteux
	public void algorithmeParcoursMoinsCouteux(){
		//1. DEFINITION DU CADRE D'ETUDE
		//On ne sélectionne que les voitures qui participent au covoiturage
		ArrayList<Car> carAlgoList = new ArrayList<Car>();
		for(Car car: getCarList()){
			if(car.getIsDoingCarSharing()) carAlgoList.add(car);
		}
		//On cherche le nombre de clients sur le trottoir
		int clientWaitingNumber = 0;
		//On crée la liste des clients à prendre en compte
		ArrayList<Client> clientAlgoList = new ArrayList<Client>();
		for(Client cli: getClientList()){
			if(cli.getStateClient() == 0 && cli.getIsUsingCarSharing()){
				clientWaitingNumber++;
				//Les clients sur le trottoir sont rajoutés au début
				clientAlgoList.add(0,cli);
			}
			else if(cli.getStateClient() == 1 && cli.getIsUsingCarSharing())
				//Les clients déjà embarqués sont rajoutés à la fin
				clientAlgoList.add(cli);
		}

		//2. RECHERCHE DE LA MATRICE DE PASSAGE MINIMISANT LE COUT
		int[][] matriceDePassage = searchMatriceDePassage(carAlgoList,clientAlgoList,clientWaitingNumber);

		//3. CONFIGURATION DES PARCOURS DES VOITURES
		setParcours(matriceDePassage,carAlgoList,clientAlgoList);
		
		setNeedAlgorithme(false);
	}

	//fonction déterminant la matrice de passage la moins couteuse
	//Une matrice de passage est définie de la manière suivante
	//taille : carNumber lignes, 2*clientAlgoNumber colonnes
	//dans la case (k,2*l) : l'ordre de passage de la voiture k quand elle prend le client l
	//dans la case (k,2*l+1) : l'ordre de passage de la voiture k quand elle depose le client l
	//si la voiture k ne prend ni ne depose le client l, -1 dans les cases (k,2*l) et (k,2*l+1)
	//Exemple :
	//voiture 0 : prend 1, prend 0, depose 0, depose 1
	//voiture 1 : prend 2, depose 2
	//matriceDePassage :
	//| 1| 2| 0| 3|-1|-1|
	//|-1|-1|-1|-1| 0| 1|
	int[][] searchMatriceDePassage(ArrayList<Car> carAlgoList,
			ArrayList<Client> clientAlgoList, int clientWaitingNumber)
	{
		int carAlgoNumber = carAlgoList.size();
		//Le nombre de clients de l'alogrithme
		int clientAlgoNumber = clientAlgoList.size();
		//S'il n'y a pas de voiture ou pas de client dans le cadre d'Ã©tude, on ne lance pas l'algorithme
		if(carAlgoNumber == 0 || clientAlgoNumber == 0) return new int[carAlgoNumber][0];

		/** CREATION DE LA MATRICE DE PASSAGE INITIALE **/
		int[][] matriceDePassage = new int[carAlgoNumber][2*clientAlgoNumber];
		//On crée dans le même temps le tableau des occupants de la voiture renumérotés
		int[][] carOccupantArray = new int[carAlgoNumber][];
		//et on cherche le plus grand nombre d'occupants
		int occupantMax = 0;
		for(int k = 0; k<carAlgoNumber;k++){
			ArrayList<Client> occupantList = carAlgoList.get(k).getOccupantListCar();
			int occupantNumber = occupantList.size();
			for(int q=0;q<2*clientAlgoNumber;q++)
				//Les clients sur le trottoir sont rajoutés au début du parcours de la voiture 0
				//On remplit le reste de la matrice avec des -1
				matriceDePassage[k][q]=(k==0 && q<2*clientWaitingNumber)?q+occupantNumber:-1;
			occupantMax = Math.max(occupantMax,occupantNumber);
			carOccupantArray[k] = new int[occupantNumber];
			for(int z = 0; z<occupantNumber; z++){
				//On récupére le numero du client dans le reférentiel de l'algorithme
				int lAlgo = clientAlgoList.indexOf((Client)occupantList.get(z));
				//On l'ajoute dans la liste des occupants
				carOccupantArray[k][z] = lAlgo;
				//On l'ajoute dans le parcours de la voiture, à la suite des clients à prendre (car n°0)
				matriceDePassage[k][2*lAlgo+1] = z;
			}
		}
		//On cherchera le coût minimum des matrices de passages créées
		int costMin = cost(matriceDePassage,carAlgoList,clientAlgoList);

		/*
        Exemple : clientWaitingNumber = 3, deux occupants dans les voitures 0 et 1
        [ 0, 1, 2, 3, 4, 5,-1,-1,-1, 6]
		[-1,-1,-1,-1,-1,-1,-1, 0,-1,-1]
		Les six premiers indices correspondent aux trois clients sur le trottoir
		Les quatre derniers aux deux clients respectivement dans les voitures 1 et 0
		 */

		//On effectue une disjonction de cas selon l'algorithme sélectionné
		switch(getAlgoId()) {

		}

		//On affiche la matrice de passage dans la console ainsi que son coût
		System.out.println("Matrice de passage :");
		printMatrix(matriceDePassage);
		System.out.println("cost : "+costMin);

		return(matriceDePassage);
	}



	/* la fonction suivante détermine, pour un indice d'un point,
	 * le nombre de clients dans la voiture juste avant d'atteindre ce point
	 */
	public int passagers ( int[] t, int indice) {
		int pass =0 ;
		int clientNumber = t.length/2;
		/*for (int l=0 ; l< clientNumber ; l++){
			if ((t[2*l]==-1)&&(t[2*l+1]>-1)){
				pass+=1;
			}
		}*/
		for (int x=0 ; x<indice ; x++ ){
			for (int q=0; q<2*clientNumber ; q++){
				if ((t[q]==x)&&(q%2==0)){
					pass+=1;
				}
				else {
					if (t[q]==x){
						pass-=1;
					}
				}
			}
		}
		return pass;
	}
	
	/* nombreDeMoinsUn détermine le nombre de cases
	 * qui sont des -1 dans un tableau d'entiers
	 */
	public int nombreDeMoinsUn(int[] t){
		int compteur = 0;
		for(int z = 0 ; z < t.length ; z++)
			if(t[z]==-1) compteur++;
		return compteur;
	}

	// copyMatrix copie une matrice dans une nouvelle matrice
	public int[][] copyMatrix(int[][] m){
		int n = m.length;
		int[][] mat= new int[n][m[0].length];
		for(int x=0;x<n;x++)
			mat[x]=m[x].clone();
		return mat;
	}

	// printMatrix affiche une matrice dans la console
	public void printMatrix(int[][] m){
		System.out.print("[");
		for(int x=0;x<m.length;x++)
			System.out.println(Arrays.toString(m[x]));
	}

	
	/** FONCTIONS RELATIVES AU CALCUL DU COUT **/
	//il s'agit de la distance en norme 1, nous sommes à Manhattan
	public int dist( Point p1, Point p2)
	{
		return Math.abs(p1.getCoordX()-p2.getCoordX()) + Math.abs(p1.getCoordY()-p2.getCoordY());
	}

	//recherche d'index d'une valeur dans un tableau
	public int searchInArray(int a, int[] t)
	{
		for(int z = 0; z<t.length; z++)
			if(t[z] == a) return z;
		return -1;
	}

	//Cette fonction calcule le cout d'un parcours défini par une matrice de passage
	public int cost(int[][] matriceDePassage, ArrayList<Car> carAlgoList, ArrayList<Client> clientAlgoList)
	{
		int carAlgoNumber = carAlgoList.size();
		int cost = 0;
		for(int k=0;k<carAlgoNumber;k++)
		{
			int clientCost = 0;
			int bestDistSum = 0;
			int carDist = 0;
			//rank est le numéro d'ordre de l'étape de la voiture
			int rank = 0;
			Point p0 = null;
			int q1;
			//On cherche la rankiême étape
			while((q1 = searchInArray(rank,matriceDePassage[k]))!=-1)
			{
				//On récupêre le point correspondant à q1
				Point p1 = clientAlgoList.get(q1/2).getPosClient()[q1%2];
				//On ajoute la distance entre la voiture et p1 ou entre p0 et p1 si p0 non null
				carDist += dist((p0==null)?carAlgoList.get(k).getPosCar():p0, p1);
				if(q1%2==1){
					int bestDist = dist(clientAlgoList.get(q1/2).getPosClient()[0],clientAlgoList.get(q1/2).getPosClient()[1]);
					int realDist =
							((getTime()-clientAlgoList.get(q1/2).appearanceMoment)*carLength+carDist);
					clientCost += realDist * realDist /(bestDist*bestDist);
					bestDistSum += bestDist;
				}
				//On passe à l'étape suivante
				rank++;
				p0 = p1;
			}
			if(bestDistSum != 0) cost += getCostRate()*clientCost
					+ (100-getCostRate())*carDist*carDist/(bestDistSum*bestDistSum);
		}
		return cost;
	}

	//Cette fonction remplit les listes parcoursList des voitures à partir d'une matriceDePassage
	//Logiquement cette fonction est appelée lorsqu'on a trouvé la matriceDePassage qui minimise le cout
	public void setParcours(int[][] matriceDePassage,ArrayList<Car> carAlgoList, ArrayList<Client> clientAlgoList)
	{
		int carAlgoNumber = carAlgoList.size();
		for(int k=0; k<carAlgoNumber; k++)
		{
			//On réinitialise le parcours
			carAlgoList.get(k).setParcoursList(new ArrayList<ParcoursStep>());
			int rank = 0;
			int q;
			//On cherche la stepNumiême étape dans la matrice de passage
			while((q = searchInArray(rank,matriceDePassage[k]))!=-1)
			{
				//on ajoute l'étape au parcours de la voiture
				carAlgoList.get(k).getParcoursListCar().add(new ParcoursStep(clientAlgoList.get(q/2), q%2));
				rank++;
			}
		}
	}


	/** MOUVEMENTS ELEMENTAIRES **/
	//avance les Car d'une case vers leur prochaine étape
	public void OneMove() {
		//On incrémente le temps de la simulation
		setTime(getTime() + 1);
		//Nombre de voitures en circulation
		int activeCar = 0;
		//Somme des vitesses des voitures
		int speedSum = 0;
		//Somme des vitesses instantanées des clients
		double clientRealSpeedSum = 0;
		for(Iterator<Car> it= getCarList().iterator();it.hasNext();)
		{
			Car car = it.next();
			//Si la voiture n'a pas terminé son parcours
			if(!car.getParcoursListCar().isEmpty()){
				activeCar++;
				int X = car.getPosCar().getCoordX();
				int Y = car.getPosCar().getCoordY();
				ParcoursStep step = car.getParcoursListCar().get(0);
				Point p = step.cli.getPosClient()[step.type];
				//Par défaut la voiture est de vitesse la moitié de sa longueur par unité de temps
				int v = carLength/2;
				//Si la voiture se trouve dans une rue
				if(car.streetId != -1)
					v = v-v*2*carLength*(-1+
							//On calcule et on décrémente le nombre de voitures dans la rue
					streetArray[(car.streetId/2)%cityWidth][(car.streetId/2)/cityWidth][car.streetId%2]--
					)/streetLength;
				//Si la vitesse est nulle, on donne une vitesse de 1 px à une probabilité de 20%
				if(v<=0) v=(Math.random()>0.8)?1:0;
				//On incrémente les données de simulations
				speedSum += v;
				setDistSum(getDistSum() + v);
				setInconnu(getInconnu() + ((v== 0 || v == 1)?carLength/2:v));
				if(X < p.getX()) car.getPosCar().updatePoint((X+v>p.getX())?p.getX():X+v,Y);
				else if(X > p.getX()) car.getPosCar().updatePoint((X-v<p.getX())?p.getX():X-v,Y);
				else if(Y < p.getY()) car.getPosCar().updatePoint(X,(Y+v>p.getY())?p.getY():Y+v);
				else if(Y > p.getY()) car.getPosCar().updatePoint(X,(Y-v<p.getX())?p.getY():Y-v);
				else {
					//Si la voiture est arrivée à une étape du parcours
					//On change l'état du client embarqué ou déposé
					step.cli.setStateClient(step.cli.getStateClient() + 1);
					//On l'ajoute à la liste des occupants s'il est embarqué
					if(step.cli.getStateClient() == 1){
						car.getOccupantListCar().add(step.cli);
						step.cli.setCarClient(car);
					}
					//Sinon on l'y enlève
					else{
						arrivedClient++;
						clientSpeedSum += (double)dist(step.cli.getPosClient()[0],step.cli.getPosClient()[1]) 
								/(double)(getTime()-step.cli.appearanceMoment);
						car.getOccupantListCar().remove(step.cli);
						getClientList().remove(step.cli);
						clientTotalNumber--;
					}
					//On supprime l'étape du parcours de la voiture
					car.getParcoursListCar().remove(0);
				}
				//Si la voiture est toujours en circulation, on calcule le numéro de sa rue
				if(!car.getParcoursListCar().isEmpty()){
					X = car.getPosCar().getCoordX()/streetLength;
					Y = car.getPosCar().getCoordY()/streetLength;
					if(car.getPosCar().getCoordX() % streetLength == 0 && car.getPosCar().getCoordY() % streetLength == 0)
						car.streetId = -1;
					else car.streetId = 2*Y*cityWidth+2*X+((car.getPosCar().getCoordX() % streetLength == 0)?0:1);
					//On incrémente le nombre de voitures dans la rue de la voiture
					if(car.streetId != -1) streetArray[X][Y][car.streetId%2]++;
				}
				//On supprime la voiture si elle ne fait pas de covoiturage
				else if(!car.getIsDoingCarSharing()){
					it.remove();
					carNumber--;
				}
			}
		}
		//CALCUL DES VITESSES MOYENNES
		setCarSpeedMean((activeCar!=0)?((double) speedSum / (double) activeCar):0);
		setClientSpeedMean((arrivedClient!=0)?(clientSpeedSum/(double)arrivedClient):0);
		for(Client cli: getClientList())
			clientRealSpeedSum += (cli.getCarClient() == null)?0:(double)dist(cli.getPosClient()[0],cli.getCarClient().getPosCar()) 
			/(double)(getTime()-cli.appearanceMoment);
		setClientRealSpeedMean((clientTotalNumber!=0)?(clientRealSpeedSum/(double)clientTotalNumber):0);
		setArrivedRate((double) arrivedClient/ (double)(arrivedClient+clientTotalNumber));
	}
}
