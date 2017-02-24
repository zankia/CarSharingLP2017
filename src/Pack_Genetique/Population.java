package Pack_Genetique;
public class Population {
 
    Membre[] membres;
    /*
     * Constructor
     */
    // Create a population
    public Population(int populationSize, boolean initialise) {
        membres = new Membre[populationSize];
        // Initialise population
        if (initialise) {
            // Loop and create members
            for (int i = 0; i < populationSize; i++) {
            	Membre newMembre = new Membre();
                newMembre.generateMembre();
                saveMembre(i, newMembre);
            }
        }
    }
 
    /* Getters */
    public Membre getMembre(int index) {
        return membres[index];
    }
 
    public Membre getMoreCompetent() {
    	Membre moreCompetent = membres[0];
        // Loop through passagers to find more competent
        for (int i = 0; i < size(); i++) {
            if (moreCompetent.getCompetence() >= getMembre(i).getCompetence()) {
                moreCompetent = getMembre(i);
            }
        }
        return moreCompetent;
    }
 
    /* Public methods */
    // Get population size
    public int size() {
        return membres.length;
    }
 
    // Save passager
    public void saveMembre(int index, Membre membre) {
    	membres[index] = new Membre();
    	for( int i = 0 ; i < membre.nbVoitures() ; i++){
        	for( int j = 0 ; j < membre.nbPassagers() ; j++){
    		membres[index].passagersOrdonnes[i][j] =  membre.passagersOrdonnes[i][j];
        	}
    	}
    	membres[index].attribuerPointsDePassage();
    }
}