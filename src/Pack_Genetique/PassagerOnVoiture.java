package Pack_Genetique;
import java.util.Arrays;
import java.util.Collections;

/**
 * 
 * @author Romain Duret
 * @version Build III -  v0.1
 * @since Build III -  v0.0
 */
public class PassagerOnVoiture {
	
	/**
	 * Matrice de passagers
	 */
	Passager[][] passagersOrdonnes;
	/**
	 * Matrice de point de passage <br>
	 * M(i,2*j)
	 */
	Point[][] pointsDePassage;
	private int competence = 0;
	/**
	 * Recursivité : poids du chemin optimal
	 */
	private int U;
	/**
	 * Récursivité : chemin le plus optimal
	 */
	private Point[][] passageOptimal;
	
	public PassagerOnVoiture() {
		this.passagersOrdonnes = new Passager[Main.nbVoiture][Main.nbPlaceVoiture];
		this.pointsDePassage = new Point[Main.nbVoiture][Main.nbPlaceVoiture*2];
		this.passageOptimal = new Point[Main.nbVoiture][Main.nbPlaceVoiture*2];
	}
    /** 
     * Create a random passager group
     * @version Build III -  v0.2
	 * @since Build III -  v0.0
     */
    public void generatePassagerOnVoiture() {
    	//on repartit aléatoirement les 20 passagers dans les 5 voitures
    	this.pointsDePassage = new Point[Main.nbVoiture][Main.nbPlaceVoiture*2];
    	Passager[] listeDesPassagers= new Passager[Main.nbPassager];
    	for(int i = 0; i < Main.nbPassager; i++)
    		listeDesPassagers[i] = Main.lesPassagers[i];
		Collections.shuffle(Arrays.asList(listeDesPassagers));
		int index = 0;
		for(int i = 0 ; i < Main.nbVoiture ; i++)
			for(int j = 0 ; j < Main.nbPlaceVoiture ; j++){
				this.setPassager(i, j, listeDesPassagers[index]);
				index++;
			}
		
		this.attribuerPointsDePassage();
		}

	/**
	 * Pour chaque voiture on attribue l'ordre de passage à chaque départ et déstination de chaque passager
	 * <br>
	 * Effectué dans l'ordre "normale" (pas de dépot avant
	 * @version Build III -  v0.0
	 * @since Build III -  v0.0
	 */
    public void attribuerPointsDePassage() {
    	
    	for (int i = 0; i < Main.nbVoiture; i++){ // Pour chaque voiture ayant x Place
    		Algo_DeterministePourPoint(passagersOrdonnes[i], i);
    		for(int j = 0; j < this.passageOptimal[i].length ; j++) {
    			this.pointsDePassage[i][j] = this.passageOptimal[i][j];
    		}
    		this.passageOptimal = new Point[Main.nbVoiture][Main.nbPlaceVoiture*2];
    	}
    	
	}
    
    /**
     * Récursif qui teste toutes les combinaisons possibles <br>
     * En s'inspirant de l'algorithme sac à dos, on ne sélectionne que le parcours le plus petit et on le stock (this.passageOptimal) <br>
     * On utilise this.U pour connaitre la taille du chemin
     * @since Build III - v0.0
     * @version Build III - v0.2
     * @param passagers liste des passagers
     * @param listeDesPoints "conteneur de point"
     * @param possable est-ce que tel passager est possable
     * @param selected est-ce que tel passager est prenable
     * @param n index 
     */
    private void recursif(Passager[] passagers, Point[] listeDesPoints, boolean[] possable, boolean[] selected, int n, int numeroVoiture) {
    	if(n==((Main.nbPlaceVoiture*2)-1)) {
    		for(int i=0;i<Main.nbPlaceVoiture;i++) {
    			if(possable[i]==true) {
    				listeDesPoints[n] = passagers[i].getArrivee();
    				int poids = this.getDistanceCheminPourU(listeDesPoints);
        			if(this.U>poids || this.U==0){
        				this.U = poids;
        				for(int k=0; k<listeDesPoints.length;k++) {
        					this.passageOptimal[numeroVoiture][k] = listeDesPoints[k];
        				}
        				
        			}
    			}		
    		}
    	} else {		
    		for(int i=0;i<Main.nbPlaceVoiture;i++) {
    			
    			boolean[] tempon1 = new boolean[selected.length];
    			boolean[] tempon2 = new boolean[possable.length];
    			if(selected[i]==false) {
    				listeDesPoints[n] = passagers[i].getDepart();
    				for(int k=0; k<selected.length;k++) {
    					tempon1[k] = selected[k];
    				}
    				tempon1[i] = true;
    
    				this.recursif(passagers, listeDesPoints, possable, tempon1, n+1, numeroVoiture);
    			} else if(possable[i]==true) {
    				
    				listeDesPoints[n] = passagers[i].getArrivee();
    				for(int k=0; k<possable.length;k++) {
    					tempon2[k] = possable[k];
    				}
    				tempon2[i] = false;
    	    			
    				this.recursif(passagers, listeDesPoints, tempon2, selected, n+1, numeroVoiture);
    			} else {
    	    	
    			}
    		}	
    	}
		
    }
    /**
     * Pré-récursif
     * @since Build III - v0.2
     * @version Build III - v0.2
     * @param passagers liste des passagers
     */
    private void Algo_DeterministePourPoint(Passager[] passagers, int numeroVoiture) {
    	boolean[] selected = new boolean[passagers.length]; //déclare si déjà sélectionné.
    	for(int k=0; k < selected.length; k++) { selected[k] = false; }
    	boolean[] possable = new boolean[passagers.length]; //déclare si déjà sélectionné.
    	for(int k=0; k < selected.length; k++) { possable[k] = true; }
    	Point[] listeDesPoints = new Point[Main.nbPlaceVoiture*2];
    	this.U = 0;
    	this.recursif(passagers, listeDesPoints, possable, selected, 0, numeroVoiture);
	}
    /* Getters and setters */
    
    
	public Passager getPassager(int voiture,int index) {
        return passagersOrdonnes[voiture][index];
    }
 
    public void setPassager(int voiture, int index, Passager passager) {
        passagersOrdonnes[voiture][index] = passager;
    }
 
    /* Public methods */
    public int nbPassagers() {
        return passagersOrdonnes[0].length;
    }
    public int nbVoitures() {
        return passagersOrdonnes.length;
    }
    
    /**
     *  Trouver le resultat d'un GroupePassager, la somme des distances entre le 1er et le 2nd, puis le 2nd et le 3ème.... 
     * @param GroupePassager
     * @return
     * @version Build III -  v0.1
	 * @since Build III -  v0.0
     */
    public int getDistanceChemin() {
    	int resultat = 0;
        int distanceEntreDeuxPoints = 0;
        
        /* si la voiture part de (0,0)
        distanceEntreDeuxPoints += Math.abs(GroupePassager.pointsDePassage[0][0].getPos_y());
    	distanceEntreDeuxPoints += Math.abs(GroupePassager.pointsDePassage[0][0].getPos_x());
    	resultat += distanceEntreDeuxPoints;
         */
        for (int i = 0; i < this.pointsDePassage.length - 1 ; i++){
            for (int j = 0; j < this.pointsDePassage[i].length - 1 ; j++){
        	distanceEntreDeuxPoints = 0;
        	distanceEntreDeuxPoints += Math.abs(this.pointsDePassage[i][j].getPos_y() - this.pointsDePassage[i][j+1].getPos_y());
        	distanceEntreDeuxPoints += Math.abs(this.pointsDePassage[i][j].getPos_x() - this.pointsDePassage[i][j+1].getPos_x());
        	resultat += distanceEntreDeuxPoints;
            }
        }
    	
        return resultat;
    }
    
    public int getDistanceCheminPourU(Point[] listeDesPoints) {
    	int resultat = 0;
        int distanceEntreDeuxPoints = 0;
        
    	for (int j = 0; j < listeDesPoints.length - 1 ; j++){
    	distanceEntreDeuxPoints = 0;
    	distanceEntreDeuxPoints += Math.abs(listeDesPoints[j].getPos_y() - listeDesPoints[j+1].getPos_y());
    	distanceEntreDeuxPoints += Math.abs(listeDesPoints[j].getPos_x() - listeDesPoints[j+1].getPos_x());
    	resultat += distanceEntreDeuxPoints;
        }
    	
    	return resultat;
    }
 

    public void afficherPassagerOnVoitures() {
		for(int i = 0 ; i < 5 ; i++){
			for(int j = 0 ; j < 4 ; j++){
				System.out.print(passagersOrdonnes[i][j].getId() + " ");
			}
		System.out.println("");
		}
     }
    
    public void afficherPoints() {
		for(int i = 0 ; i < 5 ; i++){
			for(int j = 0 ; j < 8 ; j++){
				System.out.print(pointsDePassage[i][j].toString() + " ");
			}
		System.out.println("");
		}
     }
    
    public Point[][] getPointsDePassage() {
    	return this.pointsDePassage;
    }
}