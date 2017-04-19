package Pack_Genetique;

/**
 * Class Main qui permet de faire tourner l'algorithme.
 * Doit � terme disparaitre.
 * 
 * @author Romain Duret
 * @version Build III -  v0.2
 * @since Build III -  v0.0
 */
public class Main {
	
	protected static Passager[] lesPassagers;
	protected static int nbIterations = 100;
    protected static int nbPassager = 20;
    protected static int taillePopulation = 10;
    protected static final int nbVoiture = 5;
    protected static final int nbPlaceVoiture = 4;
    
    /**
     * Main.
     * @param args
     * @since Build III - v0.0
     * @version Build III - v0.2
     */
    public static void main(String[] args) {
    	
        long debut = System.currentTimeMillis();
    	//Generate 20 random passagers
		lesPassagers = new Passager[nbPassager];
		Passager unPassager;
		for(int i = 0; i < nbPassager; i ++){
			unPassager = new Passager();
			lesPassagers[i] = unPassager;
		}  
		
		

        // Create the initial population
        Population myPop = new Population(taillePopulation, true);
        

        /* Evolve our population until we reach an optimum solution*/
        PassagerOnVoiture meilleurPassagerOnVoiture = null;
        int generationCount = 0;
        int nbIterationMeilleureSolution = 0;
        int meilleureSolution =  myPop.getMoreCompetent().getDistanceChemin();
        int premiereSolution = 0;

        while (nbIterationMeilleureSolution < nbIterations ) {
            generationCount++;
            if(generationCount%10==0) System.out.println("Generation: " + generationCount + " distance parcourue: " + myPop.getMoreCompetent().getDistanceChemin()+"m");
            myPop = Algo_Genetique.evolvePopulation(myPop);
            if (myPop.getMoreCompetent().getDistanceChemin() < meilleureSolution){
            	meilleureSolution = myPop.getMoreCompetent().getDistanceChemin();
            	nbIterationMeilleureSolution = 0;
            	meilleurPassagerOnVoiture = myPop.getMoreCompetent();
            } 
            if (generationCount == 1 ) {
            	premiereSolution = meilleureSolution;
            }
            else 
            	nbIterationMeilleureSolution ++;  
        }
        
        for(int i = 0; i < nbPassager; i++){
        	 System.out.println(lesPassagers[i].toString());
        }
        
        System.out.println("---------------------");
        System.out.println("Solution found ! Number of generations created : "+ generationCount);
        System.out.println("Distance: " + (float)meilleureSolution/100+" Km");
        System.out.println("Distance au d�but : " + (float)premiereSolution/100+" Km");
        System.out.println("---------------------");
        System.out.println("R�partition des passagers :");
        meilleurPassagerOnVoiture.afficherPassagerOnVoitures();
    
        System.out.println("---------------------");
        System.out.println("Matrice des points � parcourir : ");

        meilleurPassagerOnVoiture.afficherPoints();
        
        long fin = System.currentTimeMillis();
        System.out.println("M�thode ex�cut�e en " + Long.toString(fin - debut) + " millisecondes");

    }
}