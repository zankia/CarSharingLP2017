package Pack_Genetique;
public class Main {
	
	static Passager[] lesPassagers;
    static int nbIterations = 1000;
    static int nbPassager = 20;
    static int taillePopulation = 10;
    public static void main(String[] args) {
    	
        long debut = System.currentTimeMillis();
    	//Generate 20 random passagers
		lesPassagers = new Passager[nbPassager];
		Passager unPassager;
		for(int i = 0; i < 20; i ++){
			unPassager = new Passager();
			lesPassagers[i] = unPassager;
		}  
		
		

        // Create the initial population
        Population myPop = new Population(taillePopulation, true);
        

        /* Evolve our population until we reach an optimum solution*/
        Membre meilleurMembre = null;
        int generationCount = 0;
        int nbIterationMeilleureSolution = 0;
        int meilleureSolution =  myPop.getMoreCompetent().getCompetence();

        while (nbIterationMeilleureSolution < nbIterations ) {
            generationCount++;
            System.out.println("Generation: " + generationCount + " distance parcourue: " + myPop.getMoreCompetent().getCompetence()+"m");
            myPop = Algo_Genetique.evolvePopulation(myPop);
            if (myPop.getMoreCompetent().getCompetence() < meilleureSolution){
            	meilleureSolution = myPop.getMoreCompetent().getCompetence();
            	nbIterationMeilleureSolution = 0;
            	meilleurMembre = myPop.getMoreCompetent();
            } 
            else 
            	nbIterationMeilleureSolution ++;  
        }
        
        for(int i = 0; i < 20; i++){
        	 System.out.println(lesPassagers[i].toString());
        }
        
        System.out.println("---------------------");
        System.out.println("Solution found ! Number of generations created : "+ generationCount);
        System.out.println("Distance: " + (float)meilleureSolution/100+" Km");
        System.out.println("---------------------");
        System.out.println("Répartition des passagers :");
        meilleurMembre.afficherMembres();
    
        System.out.println("---------------------");
        System.out.println("Matrice des points à parcourir : ");

        meilleurMembre.afficherPoints();
        
        long fin = System.currentTimeMillis();
        System.out.println("Méthode exécutée en " + Long.toString(fin - debut) + " millisecondes");

    }
}