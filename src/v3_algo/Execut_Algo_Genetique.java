package v3_algo;

import java.util.ArrayList;

import v3_window.Cell;

public class Execut_Algo_Genetique {


	/*
	   _____       _ _   _       _ _           _   _             
	  |_   _|     (_) | (_)     | (_)         | | (_)            
	    | |  _ __  _| |_ _  __ _| |_ ___  __ _| |_ _  ___  _ __  
	    | | | '_ \| | __| |/ _` | | / __|/ _` | __| |/ _ \| '_ \ 
	   _| |_| | | | | |_| | (_| | | \__ \ (_| | |_| | (_) | | | |
	  |_____|_| |_|_|\__|_|\__,_|_|_|___/\__,_|\__|_|\___/|_| |_|
	*/                                                             
	      
	/**
	 * Tableau des passagers 
	 */
	protected static Passager[] lesPassagers;
	/**
	 * Nombre d'itération à affectuer après la dernière diminution de taille
	 */
	protected static int nbIterations = 100;
	/**
	 * Nombre de passager en tout
	 */
    protected static int nbPassager;

    /**
     * Taille de la population utilisée par l'algo génétique
     */
    protected static int taillePopulation = 10;
    /**
     * Nombre de voiture
     */
    protected static  int nbVoiture;
    /** 
     * Nombre de place par voiture.
     */
    protected static final int nbPlaceVoiture = 4;
    /**
     * Taille de la grille
     */
    protected static int sizeGrille_X;
    protected static int sizeGrille_Y;
    /**
     * Premiere solution trouvée (distance)
     */
    int premiereSolution = 0;
    /**
     * Meilleure soltuion trouvée (distance)
     */
    int meilleureSolution;
    /**
     * Solution égoiste (1 voiture / passager)
     */
    int egoisteSolution;
    /**
     * Meilleure combinaison de Passager distribué dans des voitures
     */
    PassagerParVoiture meilleurPassagerParVoiture;
    /**
     * Conteur de génération
     */
    int generationCount;
    
    
    /*                                                   
    _____                _                   _                  
   / ____|              | |                 | |                 
  | |     ___  _ __  ___| |_ _ __ _   _  ___| |_ ___ _   _ _ __ 
  | |    / _ \| '_ \/ __| __| '__| | | |/ __| __/ _ \ | | | '__|
  | |___| (_) | | | \__ \ |_| |  | |_| | (__| ||  __/ |_| | |   
   \_____\___/|_| |_|___/\__|_|   \__,_|\___|\__\___|\__,_|_|  

	  */
   
    
    public Execut_Algo_Genetique(ArrayList<Cell> list_car, ArrayList<Cell> list_client, ArrayList<Cell> list_client_depot, int rows, int columns) {
    	
    	Execut_Algo_Genetique.nbPassager = list_client_depot.size();
    	Execut_Algo_Genetique.nbVoiture = list_car.size();
    	
    	Execut_Algo_Genetique.sizeGrille_X = rows;
    	Execut_Algo_Genetique.sizeGrille_Y = columns;
    	
    	long debut = System.currentTimeMillis(); //Debut du compteur
    	
 		Execut_Algo_Genetique.lesPassagers = Passager.buildPassager(list_client, list_client_depot); //passager créé aléatoirement
 		
 		Population myPop = this.execute(); //exuction de l'algo génétique 
 		
 		this.meilleureSolution = myPop.getMoreCompetent().getDistanceChemin(); //taille de la meilleure solution
 		this.affichage(debut);
    }
 
    /**
     * Execution des itérations
     * @return
     * @version Build III -  v0.2
	 * @since Build III -  v0.2
     */
    public Population execute() {
    	int nbIterationMeilleureSolution = 0;
    	 Population myPop = new Population(taillePopulation, true); //création de la population initialie
    	 this.generationCount = 0;
    	 this.meilleureSolution =  myPop.getMoreCompetent().getDistanceChemin();
    	 this.egoisteSolution = Passager.getPireDistance(lesPassagers);
    	 
    	 while (nbIterationMeilleureSolution < nbIterations ) {
             this.generationCount++;
             /*if(this.generationCount%10==0)*/ System.out.println("Generation: " + this.generationCount + " distance parcourue: " + myPop.getMoreCompetent().getDistanceChemin()+"m");
             myPop = Algo_Genetique.evolvePopulation(myPop);
             if (myPop.getMoreCompetent().getDistanceChemin() < this.meilleureSolution){
            	 this.meilleureSolution = myPop.getMoreCompetent().getDistanceChemin();
             	nbIterationMeilleureSolution = 0;
             	this.meilleurPassagerParVoiture = myPop.getMoreCompetent();
             } 
             if (this.generationCount == 1 ) {
            	 this.premiereSolution = this.meilleureSolution;
             }
             else 
             	nbIterationMeilleureSolution ++;  
         }
    	 return myPop;
    }
    
    /*

           __  __ _      _                            
    /\    / _|/ _(_)    | |                     
   /  \  | |_| |_ _  ___| |__   __ _  __ _  ___ 
  / /\ \ |  _|  _| |/ __| '_ \ / _` |/ _` |/ _ \
 / ____ \| | | | | | (__| | | | (_| | (_| |  __/
/_/    \_\_| |_| |_|\___|_| |_|\__,_|\__, |\___|
                                      __/ |     
                                     |___/       


*/      
    /**
     * Affichage des données dans la console
     * @version Build III -  v0.2
	 * @since Build III -  v0.2
     */
    public void affichage(long debut) {
    	for(int i = 0; i < Execut_Algo_Genetique.nbPassager; i++){
        	 System.out.println(Execut_Algo_Genetique.lesPassagers[i].toString());
        }
    	System.out.println("---------------------");
        System.out.println("Solution found ! Number of generations created : "+ this.generationCount);
        System.out.println("Distance: " + (float)this.meilleureSolution/100+" Km");
        System.out.println("Distance au début : " + (float)this.premiereSolution/100+" Km");
        System.out.println("Distance total 1 par 1 : " + (float)this.egoisteSolution/100 + "Km");

        System.out.println("---------------------");
        System.out.println("Répartition des passagers :");
        this.meilleurPassagerParVoiture.afficherPassagerOnVoitures();
    
        System.out.println("---------------------");
        System.out.println("Matrice des points à parcourir : ");

        this.meilleurPassagerParVoiture.afficherPoints();
        
        long fin = System.currentTimeMillis();
        System.out.println("Méthode exécutée en " + Long.toString(fin - debut) + " millisecondes");

    }
    
    
	
	
	
}
