package Pack_Genetique;
import java.util.Arrays;
import java.util.Collections;

public class Algo_Genetique {
 
    /* Paramètres de l'algo */
    private static final double chanceDeCroisement = 0.8;
    private static final double mutationRate = 0.08;
    private static final int tournamentSize = 3;
 
    /* Méthodes publiques */
 
    // Evoluer la population
    public static Population evolvePopulation(Population pop) {
        Population newPopulation = new Population(pop.size(), false);
        
 
        // Garder le meilleur membre
        newPopulation.saveMembre(0, pop.getMoreCompetent());
        int elitismOffset = 1;
      
        // Loop over the population size and create new member with
        // Select new population
        for (int i = elitismOffset; i < pop.size(); i++) {
        	Membre newMembre = selection(pop, pop.getMembre(i));
            newPopulation.saveMembre(i, newMembre);
        }
        
        //Crossover
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
            croisement(newPopulation.getMembre(i));
        }
        
        // Mutate population
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
            mutation(newPopulation.getMembre(i));
        }
        
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
        	newPopulation.getMembre(i).attribuerPointsDePassage();
        }

        return newPopulation;
    }
    
    // Crossover of members
    private static void croisement(Membre membre) {
    	int nbSwap = (int)(Math.random() * 2 + 1); 

        for (int i = 0; i < membre.nbVoitures(); i++) 
        	if (Math.random() <= chanceDeCroisement)
        			swapPassagers(membre, i, nbSwap);

    } 
    
	private static void swapPassagers(Membre membre, int numVoiture, int nbSwap) {	
		if(nbSwap == 1){
			 Collections.swap(Arrays.asList(membre.passagersOrdonnes[numVoiture]), 0, 3);
		} 
		 else if(nbSwap == 2){
			Collections.swap(Arrays.asList(membre.passagersOrdonnes[numVoiture]), 0, 2);
			Collections.swap(Arrays.asList(membre.passagersOrdonnes[numVoiture]), 1, 3);
		}
		
	}

	// Selection members
    private static Membre selection(Population pop, Membre membre) {
        Membre newMember = new Membre();
        newMember = tournamentSelection(pop);
        return newMember;
    }
 
    // Mutate a member
    private static void mutation(Membre membre) {
        // Loop through passagers
        for (int i = 0; i < membre.nbVoitures(); i++) {
        	for (int j = 0; j < membre.nbPassagers(); j++){
        		if (Math.random() <= mutationRate) {
        			// Swap two random passagers
        			randomSwapPassagers(membre);
        		}
            }
        }
    }
    
    
    private static void randomSwapPassagers(Membre membre){
		int passager1 = (int)(Math.random() * 20 + 1);
		int passager2 = (int)(Math.random() * 20 + 1);
		Passager passagerTmp = null;
		int[] coord1 = new int[2];
		int[] coord2 = new int[2];

        for (int i = 0; i < membre.nbVoitures(); i++) {
        	for (int j = 0; j < membre.nbPassagers(); j++){
        		if(membre.passagersOrdonnes[i][j].getId() == passager1){
        			coord1[0] = i;
        			coord1[1] = j;
        		}
        		if(membre.passagersOrdonnes[i][j].getId() == passager2){
        			coord2[0] = i;
        			coord2[1] = j;        		
        		}		
        	}
        }
        passagerTmp = membre.passagersOrdonnes[coord1[0]][coord1[1]];
        membre.passagersOrdonnes[coord1[0]][coord1[1]] = membre.passagersOrdonnes[coord2[0]][coord2[1]];
        membre.passagersOrdonnes[coord2[0]][coord2[1]] = passagerTmp;
    }
    
    private static Membre tournamentSelection(Population pop) {
        // Création d'un battle royale entre plusieurs membres de la population
        Population tournament = new Population(tournamentSize, false);
        // Les participants sont tirés au sort
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournament.saveMembre(i, pop.getMembre(randomId));
        }
        // On choisi le membre gagnant !
        Membre winner = tournament.getMoreCompetent();
        return winner;
    }
}