package v3_algo;

import java.util.ArrayList;

import v3_window.Cell;

/**
 * Classe Population
 * 
 * @author Romain Duret
 * @version Build III -  v0.2
 * @since Build III -  v0.0
 */
public class Population {
 
	
	/*
	   _____       _ _   _       _ _           _   _             
	  |_   _|     (_) | (_)     | (_)         | | (_)            
	    | |  _ __  _| |_ _  __ _| |_ ___  __ _| |_ _  ___  _ __  
	    | | | '_ \| | __| |/ _` | | / __|/ _` | __| |/ _ \| '_ \ 
	   _| |_| | | | | |_| | (_| | | \__ \ (_| | |_| | (_) | | | |
	  |_____|_| |_|_|\__|_|\__,_|_|_|___/\__,_|\__|_|\___/|_| |_|
	*/
	
	/** 
	 * Liste de passager dans des voitures
	 */
    PassagerParVoiture[] PassagerParVoitures;

    ArrayList<Cell> lb;
	 /*                                                   
    _____                _                   _                  
   / ____|              | |                 | |                 
  | |     ___  _ __  ___| |_ _ __ _   _  ___| |_ ___ _   _ _ __ 
  | |    / _ \| '_ \/ __| __| '__| | | |/ __| __/ _ \ | | | '__|
  | |___| (_) | | | \__ \ |_| |  | |_| | (__| ||  __/ |_| | |   
   \_____\___/|_| |_|___/\__|_|   \__,_|\___|\__\___|\__,_|_|  
	  */
    
    
    
    /**
     *  Create a population and PassagerParVoiture inside
     * @param populationSize
     * @param initialise
     * @version Build III -  v0.0
     * @since Build III -  v0.0
     */
    public Population(int populationSize, boolean initialise, ArrayList<Cell> list_block) {
        this.PassagerParVoitures = new PassagerParVoiture[populationSize];
        this.lb = list_block;
        if (initialise) {
            for (int i = 0; i < populationSize; i++) {
            	PassagerParVoiture newPassagerOnVoiture = new PassagerParVoiture();
                savePassagerOnVoiture(i, newPassagerOnVoiture);
            }
        }
    }
 
    
	/*
	  __  __      _   _               _           
	 |  \/  |    | | | |             | |          
	 | \  / | ___| |_| |__   ___   __| | ___  ___ 
	 | |\/| |/ _ \ __| '_ \ / _ \ / _` |/ _ \/ __|
	 | |  | |  __/ |_| | | | (_) | (_| |  __/\__ \
	 |_|  |_|\___|\__|_| |_|\___/ \__,_|\___||___/
	                                              
	 */	
    
    /**
     * Sauvegarde un groupe de Passager réparti dans des voitures à un certain emplacement
     * @param index
     * @param PassagerParVoiture
     * @version Build III -  v0.5
     * @since Build III -  v0.0
     */
    public void savePassagerOnVoiture(int index, PassagerParVoiture PassagerParVoiture) {
    	this.PassagerParVoitures[index] = PassagerParVoiture;
    }
    
	/*
	   _____      _               _____      _   
	  / ____|    | |     ___     / ____|    | |  
	 | |  __  ___| |_   ( _ )   | (___   ___| |_ 
	 | | |_ |/ _ \ __|  / _ \/\  \___ \ / _ \ __|
	 | |__| |  __/ |_  | (_>  <  ____) |  __/ |_ 
	  \_____|\___|\__|  \___/\/ |_____/ \___|\__|
	                                             
	  */ 
    
    
    public PassagerParVoiture getPassagerOnVoiture(int index) {
        return this.PassagerParVoitures[index];
    }
 
    public PassagerParVoiture getMoreCompetent() {
    	PassagerParVoiture moreCompetent = this.PassagerParVoitures[0];
        // Loop through passagers to find more competent
        for (int i = 0; i < this.getSize(); i++) {
            if (moreCompetent.getU() > this.PassagerParVoitures[i].getU()) {
                moreCompetent = this.PassagerParVoitures[i];
            }
        }
        return moreCompetent;
    }
 
    /**
     *  Get population size
     * @return
     * @version Build III -  v0.2
     * @since Build III -  v0.0
     */
    public int getSize() {
        return this.PassagerParVoitures.length;
    }
 
    
}
