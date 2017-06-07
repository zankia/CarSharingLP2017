package v3_algo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import v3_window.Main;
import v3_window.Cell;

/**
 * L'Algorithme Génétique. <br>
 * 
 * 
 * @author Romain Duret
 * @version Build III -  v0.6
 * @since Build III -  v0.0
 */
public class Algo_Genetique {
 
	/*
	   _____       _ _   _       _ _           _   _             
	  |_   _|     (_) | (_)     | (_)         | | (_)            
	    | |  _ __  _| |_ _  __ _| |_ ___  __ _| |_ _  ___  _ __  
	    | | | '_ \| | __| |/ _` | | / __|/ _` | __| |/ _ \| '_ \ 
	   _| |_| | | | | |_| | (_| | | \__ \ (_| | |_| | (_) | | | |
	  |_____|_| |_|_|\__|_|\__,_|_|_|___/\__,_|\__|_|\___/|_| |_|
	*/                                                             
	    
	
    
    private static final double chanceDeCroisement = 0.8;
    private static final double mutationRate = 0.05;
    private static final int tournamentSize = 3;

 
    /*
	  __  __      _   _               _           
	 |  \/  |    | | | |             | |          
	 | \  / | ___| |_| |__   ___   __| | ___  ___ 
	 | |\/| |/ _ \ __| '_ \ / _ \ / _` |/ _ \/ __|
	 | |  | |  __/ |_| | | | (_) | (_| |  __/\__ \
	 |_|  |_|\___|\__|_| |_|\___/ \__,_|\___||___/
	                                              
	 */	
    
    /**
     * Evoluer la population : <br>
     * On garde le meilleur Groupe, on créer un nouveau groupe de passager <br>
     * On croise
     * et on mute 
     * @param pop Population (voitures)
     * @return
     * @version Build III -  v0.6
     * @since Build III -  v0.0
     */
    public static Population evolvePopulation(Population pop, ArrayList<Cell> l_b) {
        Population newPopulation = new Population(pop.getSize(), false, l_b);
 
        // Garder le meilleur PassagerParVoiture
        newPopulation.savePassagerOnVoiture(0, pop.getMoreCompetent());
        int elitismOffset = 1;
      
        // Loop over the population size and create new member with
        // Select new population
        for (int i = elitismOffset; i < pop.getSize(); i++) {
        	PassagerParVoiture newPassagerOnVoiture = tournamentSelection(pop, l_b);
        	//newPassagerOnVoiture.rebuild();
            newPopulation.savePassagerOnVoiture(i, newPassagerOnVoiture);
        }
        
        // Mutate population
        for (int i = elitismOffset; i < newPopulation.getSize(); i++) {
            mutation(newPopulation.getPassagerOnVoiture(i));
        }
       
        for (int i = elitismOffset; i < newPopulation.getSize(); i++) {
        	//newPopulation.getPassagerOnVoiture(i).afficherPoints();
        	newPopulation.getPassagerOnVoiture(i).attribuerPointsDePassage();
        }

        return newPopulation;
    }
    
 
    /**
     * Déclanche une mutation si le facteur random est déclanché. <br>
     * On teste s'il y a mutation autant de fois qu'il y a de place en tout dans le groupe de voiture/passager 
     * @version Build III -  v0.6
     * @since Build III -  v0.0
     */
    private static void mutation(PassagerParVoiture PassagerParVoiture) {
        for (int i = 0; i < PassagerParVoiture.getNbVoitures(); i++) {
        	for (int j = 0; j < PassagerParVoiture.getNbPassagers(); j++){
        		if (Math.random() <= mutationRate) {
        			randomSwapPassagers(PassagerParVoiture, i);
        		}
            }
        }
    }
    
    /**
     * Evoluer la population : <br>
     * On garde le meilleur Groupe, on créer un nouveau groupe de passager <br>
     * On croise et on mute 
     * @param pop Population (voitures)
     * @return
     * @version Build III -  v0.6
     * @since Build III -  v0.0
     */
    private static void randomSwapPassagers(PassagerParVoiture PassagerParVoiture, int nVoiture){
    	//TODO switch même si "vide"
		int passager1 = (int)(Math.random() * Execut_Algo_Genetique.nbPassager + 1);
		int passager2 = (int)(Math.random() * Execut_Algo_Genetique.nbPassager + 1);

		Passager passagerTmp = null;
		int[] coord1 = new int[2];
		int[] coord2 = new int[2];

        	for (int j = 0; j < PassagerParVoiture.nbPassagerParVoiture[nVoiture]; j++){
        		if(PassagerParVoiture.passagersOrdonnes[nVoiture][j].getId() == passager1){
        			coord1[0] = nVoiture;
        			coord1[1] = j;
        		}
        		if(PassagerParVoiture.passagersOrdonnes[nVoiture][j].getId() == passager2){
        			coord2[0] = nVoiture;
        			coord2[1] = j;        		
        		}		
        	}
        passagerTmp = PassagerParVoiture.passagersOrdonnes[coord1[0]][coord1[1]];
        PassagerParVoiture.passagersOrdonnes[coord1[0]][coord1[1]] = PassagerParVoiture.passagersOrdonnes[coord2[0]][coord2[1]];
        PassagerParVoiture.passagersOrdonnes[coord2[0]][coord2[1]] = passagerTmp;
    }
    
    /**
     * 
     * @param pop Population (voitures)
     * @return
     * @version Build III -  v0.6
     * @since Build III -  v0.0
     */
    private static PassagerParVoiture tournamentSelection(Population pop, ArrayList<Cell> l_b) {
    	//TODO sélection en roue de la fortune
        Population tournament = new Population(tournamentSize, false, l_b);
        // Les participants sont tirés au sort
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.getSize());
            tournament.savePassagerOnVoiture(i, pop.getPassagerOnVoiture(randomId));
        }
        return tournament.getMoreCompetent();
    }
}