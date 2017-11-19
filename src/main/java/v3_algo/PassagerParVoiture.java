package v3_algo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

import v3_window.Cell;
import v3_window.App;
import v3_window.States;

/**
 *
 * @author Romain Duret
 */
public class PassagerParVoiture {

	/**
	 * Matrice de passagers
	 */
	Passager[][] passagersOrdonnes;
	/**
	 * Matrice de point de passage <br>
	 * M(i,2*j)
	 */
	Cell[][] pointsDePassage;
	/**
	 * tableau de nombre de passager réel/voiture
	 */
	int[] nbPassagerParVoiture;
	/**
	 * Recursivité : poids du chemin optimal
	 */
	private int U;
	/**
	 * Récursivité : chemin le plus optimal
	 */
	private Cell[][] passageOptimal;
	/**
	 * Poids total de la solution
	 */
	int cost;
	/**
	 * Coût d'un déplacement
	 */
    public static final int COST = 1;

    boolean[][] closed;
    PriorityQueue<Cell> open;

	/**
	 * On créer des tableau de Passager de de Possitionpassager
	 */
	public PassagerParVoiture() {
		this.passagersOrdonnes = new Passager[Execut_Algo_Genetique.nbVoiture][Execut_Algo_Genetique.nbPlaceVoiture];
		this.pointsDePassage = new Cell[Execut_Algo_Genetique.nbVoiture][];
		this.passageOptimal = new Cell[Execut_Algo_Genetique.nbVoiture][];
		this.nbPassagerParVoiture = new int[Execut_Algo_Genetique.nbVoiture];
		for(int i = 0 ; i < Execut_Algo_Genetique.nbVoiture ; i++) {
			for(int j = 0 ; j < Execut_Algo_Genetique.nbPlaceVoiture ; j++){
				this.passagersOrdonnes[i][j] = new Passager(false);
			}
		}
		this.generatePassagerOnVoiture();
	}


	public PassagerParVoiture(int[] tabNbPassagerParVoiture) {
		this.passagersOrdonnes = new Passager[Execut_Algo_Genetique.nbVoiture][Execut_Algo_Genetique.nbPlaceVoiture];
		this.pointsDePassage = new Cell[Execut_Algo_Genetique.nbVoiture][];
		this.passageOptimal = new Cell[Execut_Algo_Genetique.nbVoiture][];
		this.nbPassagerParVoiture = tabNbPassagerParVoiture;

		for(int i = 0 ; i < Execut_Algo_Genetique.nbVoiture ; i++) {
			for(int j = 0 ; j < Execut_Algo_Genetique.nbPlaceVoiture ; j++){
				this.passagersOrdonnes[i][j] = new Passager(false);
			}
		}
	}


  /**
   * on repartit aléatoirement nbPassager (dans App) passagers dans (nbVoiture) voitures
   */

    public void generatePassagerOnVoiture() {
    	int capaciteMax = Execut_Algo_Genetique.nbVoiture*Execut_Algo_Genetique.nbPlaceVoiture;
    	Passager[] listeDesPassagers = returnListePassager(capaciteMax);
		int compteur = 0;
		for(int i = 0 ; i < Execut_Algo_Genetique.nbVoiture ; i++) {
			int compteur_PlaceVoiture = 0;
			for(int j = 0 ; j < Execut_Algo_Genetique.nbPlaceVoiture ; j++){
				if(compteur<capaciteMax && listeDesPassagers[compteur].getExist()) {
					this.setPassager(i, compteur_PlaceVoiture, listeDesPassagers[compteur]);
					compteur_PlaceVoiture++;
				}
				compteur++;
			}
			this.nbPassagerParVoiture[i] = compteur_PlaceVoiture;
		}
		Passager[][] tempon = this.passagersOrdonnes;
		this.passagersOrdonnes = new Passager[Execut_Algo_Genetique.nbVoiture][];
		for(int i = 0 ; i < Execut_Algo_Genetique.nbVoiture ; i++) {
			int nb = 0;
			this.passagersOrdonnes[i] = new Passager[this.nbPassagerParVoiture[i]];
			for(int j = 0 ; j < Execut_Algo_Genetique.nbPlaceVoiture ; j++){
				if(tempon[i][j].getExist()) {
					this.passagersOrdonnes[i][nb] = tempon[i][j];
					nb++;
				}
			}
		}
		this.attribuerPointsDePassage();
		}


    /**
     * Retourne une liste de passager généré aléatoirement, en fonction des passagers et du nombre de place. <br>
     * En gros, le tableau contient tout les passagers possibles. <br>
     * S'il ya plus de place que de passager, on peut se retrouver avec : vide vide P1 P3 vide P5 vide P2 P4
     * S'il y a autant ou moins de place que de passager, on se retrouve avec une liste sans élément vide : P3 P4 p7 P10 P8 P2 P1 P9 P5 P6
	 * @param capaciteMax maximum capacity
     * @return the list of passengers
     */
    public Passager[] returnListePassager(int capaciteMax) {

    	if(capaciteMax < Execut_Algo_Genetique.nbPassager)
    		capaciteMax = Execut_Algo_Genetique.nbPassager;
    	Passager[] listeDesPassagers= new Passager[capaciteMax];
    	for(int i = 0; i < Execut_Algo_Genetique.nbPassager; i++)
    		listeDesPassagers[i] = Execut_Algo_Genetique.lesPassagers[i];
    	for(int i = Execut_Algo_Genetique.nbPassager; i < capaciteMax; i++) {
    		listeDesPassagers[i] = new Passager(false);
    	}
    	Collections.shuffle(Arrays.asList(listeDesPassagers));
    	return listeDesPassagers;
    }
	/**
	 * Pour chaque voiture on attribue l'ordre de passage à chaque départ et déstination de chaque passager
	 * <br>
	 * Effectué dans l'ordre "normale" (pas de dépot avant
	 */
    public void attribuerPointsDePassage() {
    	this.cost = 0;
    	for (int i = 0; i < Execut_Algo_Genetique.nbVoiture; i++){ // Pour chaque voiture ayant x Place
    		this.passageOptimal[i] = new Cell[this.nbPassagerParVoiture[i]*2];
    		this.pointsDePassage[i] = new Cell[this.nbPassagerParVoiture[i]*2];
    		Pre_Algo_DeterministePourPoint(this.passagersOrdonnes[i], i);
    		for(int j = 0; j < (this.nbPassagerParVoiture[i]*2); j++) {
    			this.pointsDePassage[i][j] = this.passageOptimal[i][j];
    		}
    		this.cost = this.cost + this.U;
    	}
	}

    /**
    * Méthode ré-récursive qui initialise les variables
    * @param passagers liste des passagers
	* @param numeroVoiture number of the car
    */
    private void Pre_Algo_DeterministePourPoint(Passager[] passagers, int numeroVoiture) {
	   	boolean[] selected = new boolean[passagers.length]; //déclare si déjà sélectionné.
	   	for(int k=0; k < selected.length; k++) { selected[k] = false; }

	   	boolean[] possable = new boolean[passagers.length]; //déclare si déjà sélectionné.
	   	for(int k=0; k < selected.length; k++) { possable[k] = true; }

	   	Cell[] listeDesPoints = new Cell[this.nbPassagerParVoiture[numeroVoiture]*2];

	   	this.U = 0;

	   	this.Algo_DeterministeParRecursivite(passagers, listeDesPoints, possable, selected, 0, numeroVoiture);
	}

   /**
    * Récursif qui teste toutes les combinaisons possibles <br>
    * En s'inspirant de l'algorithme sac à dos, on ne sélectionne que le parcours le plus petit et on le stock (this.passageOptimal) <br>
    * On utilise this.U pour connaitre la taille du chemin
    * @param passagers liste des passagers
    * @param listeDesPoints "conteneur de point"
    * @param possable est-ce que tel passager est possable
    * @param selected est-ce que tsager est prenable
    * @param n index
    */
   private void Algo_DeterministeParRecursivite(Passager[] passagers, Cell[] listeDesPoints, boolean[] possable, boolean[] selected, int n, int numeroVoiture) {
	   //Cas de base : la liste se termine
   	if(n==((this.nbPassagerParVoiture[numeroVoiture]*2)-1)) {
   		for(int i=0;i<passagers.length;i++) {
   			if(possable[i]==true) {
   				listeDesPoints[n] = passagers[i].getArrivee();
   				int poids = this.getDistanceCheminPourU(listeDesPoints, this.nbPassagerParVoiture[numeroVoiture]);
       			if((this.U>poids || this.U==0) && poids!= -1){
       				this.U = poids;
       				this.passageOptimal[numeroVoiture] = new Cell[this.nbPassagerParVoiture[numeroVoiture]*2];
       				for(int k=0; k<listeDesPoints.length;k++) {
       					this.passageOptimal[numeroVoiture][k] = listeDesPoints[k];
       				}
       			} else if(poids == -1) {

       			}
   			}
   		}
  	} else { //cas récursif
  		//Pour chaque passager, on lit s'il est déjà sélectionné. Si c'est non, on crée une branche, si oui, on saute.
  		// On lit aussi si le passager est possable. Si oui, on crée une branche, sinon, on saute.
  		for(int i=0;i<passagers.length;i++) {
			boolean[] tempon1 = new boolean[selected.length];
  			boolean[] tempon2 = new boolean[possable.length];
  			if(selected[i]==false && passagers[i].getExist()) {
  				Cell p = passagers[i].getDepart();
  				listeDesPoints[n] = p;
   				for(int k=0; k<selected.length;k++) {
   					tempon1[k] = selected[k];
   				}
   				tempon1[i] = true;
   				this.Algo_DeterministeParRecursivite(passagers, listeDesPoints, possable, tempon1, n+1, numeroVoiture);
   			} else if(possable[i]==true && passagers[i].getExist()) {

   				listeDesPoints[n] = passagers[i].getArrivee();
   				for(int k=0; k<possable.length;k++) {
   					tempon2[k] = possable[k];
   				}
   				tempon2[i] = false;

   				this.Algo_DeterministeParRecursivite(passagers, listeDesPoints, tempon2, selected, n+1, numeroVoiture);
   			}  //Il y a des cas où rien ne se passe et on saute

   		}
   	}
   }


   public void afficherPassagerOnVoitures() {
		for(int i = 0 ; i < Execut_Algo_Genetique.nbVoiture  ; i++){
			System.out.print(this.passagersOrdonnes[i].length + " : ");
			for(int j = 0 ; j < this.nbPassagerParVoiture[i] ; j++){
				System.out.print(this.passagersOrdonnes[i][j].getId() + " ");
			}
		System.out.println("");
		}
    }

   public void afficherPoints() {
	   System.out.println(this.pointsDePassage.length);
		for(int i = 0 ; i < Execut_Algo_Genetique.nbVoiture; i++){
			System.out.print(this.pointsDePassage[i].length + " : ");
			for(int j = 0 ; j < (this.nbPassagerParVoiture[i]*2) ; j++){
				if(this.pointsDePassage!=null) {
					System.out.print(this.pointsDePassage[i][j].toString() + " ");
				} else {
					System.out.print(" x ");
				}

			}
		System.out.println("");
		}
    }


   public int getNbPassagers() {
       return this.passagersOrdonnes[0].length;
   }
   public int getNbVoitures() {
       return this.passagersOrdonnes.length;
   }

   public Cell[][] getPointsDePassage() {
   	return this.pointsDePassage;
   }

   public Passager getPassager(int voiture,int index) {
       return this.passagersOrdonnes[voiture][index];
   }

   public void setPassager(int voiture, int index, Passager passager) {
       this.passagersOrdonnes[voiture][index] = passager;
   }

	public int getU() {
		return this.cost;
	}

	public void setU(int U) {
		this.U = U;
	}

	/**
	 *  Trouver le resultat d'un GroupePassager, la somme des distances entre le 1er et le 2nd, puis le 2nd et le 3ème....
	 * @param l_b .
	 * @return the total distance
	 */
   public int getDistanceChemin(ArrayList<Cell> l_b) {
   		int resultat = 0;
   		for (int i = 0; i < this.pointsDePassage.length; i++){
        	for (int j = 0; j < this.pointsDePassage[i].length - 1 ; j++){

        	   this.closed = new boolean[Execut_Algo_Genetique.sizeGrille_X][Execut_Algo_Genetique.sizeGrille_Y];
               this.open = new PriorityQueue<>((o1, o2) -> {
            	   Cell c1 = (Cell)o1;
            	   Cell c2 = (Cell)o2;

            	   return c1.finalCost<c2.finalCost?-1:
                       c1.finalCost>c2.finalCost?1:0;
            	   });
               for(int a=0;a<Execut_Algo_Genetique.sizeGrille_X;++a){
                   for(int b=0;b<Execut_Algo_Genetique.sizeGrille_Y;++b){
                	   //Execut_Algo_Genetique.grid[a][b].finalCost = 0;
                	   int k = Math.abs(a-this.pointsDePassage[i][j+1].getRow())+Math.abs(b-this.pointsDePassage[i][j+1].getColumn());
                       Execut_Algo_Genetique.grid[a][b].heuristicCost = k;
                       Execut_Algo_Genetique.grid[a][b].parent = null;
                   }
                }
               Execut_Algo_Genetique.grid[this.pointsDePassage[i][j].getRow()][this.pointsDePassage[i][j].getColumn()].finalCost = 0;

               //add the start location to open list.
               this.open.add(Execut_Algo_Genetique.grid[this.pointsDePassage[i][j].getRow()][this.pointsDePassage[i][j].getColumn()]);

               Cell current;

               while(true){
                   current = open.poll();
                   if(current.getStates()==States.WALL)break;
                   this.closed[current.row][current.col]=true;

                   if(current.equals(Execut_Algo_Genetique.grid[this.pointsDePassage[i][j+1].getRow()][this.pointsDePassage[i][j+1].getColumn()])){
                       break;
                   }
                   Cell t;
                   if(current.row-1>=0){
                       t = Execut_Algo_Genetique.grid[current.row-1][current.col];
                       this.checkAndUpdateCost(current, t, current.finalCost+COST);
                   }
                   if(current.col-1>=0){
                       t = Execut_Algo_Genetique.grid[current.row][current.col-1];
                       this.checkAndUpdateCost(current, t, current.finalCost+COST);
                   }
                   if(current.col+1<Execut_Algo_Genetique.grid[0].length){
                       t = Execut_Algo_Genetique.grid[current.row][current.col+1];
                       this.checkAndUpdateCost(current, t, current.finalCost+COST);
                   }
                   if(current.row+1<Execut_Algo_Genetique.grid.length){
                       t = Execut_Algo_Genetique.grid[current.row+1][current.col];
                       this.checkAndUpdateCost(current, t, current.finalCost+COST);
                   }
               }

               if(closed[this.pointsDePassage[i][j+1].getRow()][this.pointsDePassage[i][j+1].getColumn()]){
                   //Trace back the path
                    Cell current2 = Execut_Algo_Genetique.grid[this.pointsDePassage[i][j+1].getRow()][this.pointsDePassage[i][j+1].getColumn()];
                    while(current2.parent!=null){
                        current2 = current2.parent;
                        resultat ++;
                    }
               }else {
            	   System.out.println("No possible path");
            	   return resultat; //TODO
               }

           }
       }
        return resultat;
   }

   private void checkAndUpdateCost(Cell current, Cell t, int cost){
       if(t.getStates() == States.WALL || this.closed[t.row][t.col])return;
       int t_final_cost = t.heuristicCost+cost;

       boolean inOpen = this.open.contains(t);
       if(!inOpen || t_final_cost<t.finalCost){
           t.finalCost = t_final_cost;
           t.parent = current;
           if(!inOpen)this.open.add(t);
       }
   }

   /**
    * Trouve la samme des distances pour un seul véhicule.
    * @param listeDesPoints where to go
	* @param nbPassager number of passengers
    * @return the total distance
    */
   public int getDistanceCheminPourU(Cell[] listeDesPoints, int nbPassager) {
   		int resultat = 0;
        for (int j = 0; j < listeDesPoints.length - 1 ; j++){
     	   this.closed = new boolean[Execut_Algo_Genetique.sizeGrille_X][Execut_Algo_Genetique.sizeGrille_Y];
            this.open = new PriorityQueue<>((o1, o2) -> {
         	   Cell c1 = (Cell)o1;
         	   Cell c2 = (Cell)o2;

         	   return c1.finalCost<c2.finalCost?-1:
                    c1.finalCost>c2.finalCost?1:0;
         	   });
            for(int a=0;a<Execut_Algo_Genetique.sizeGrille_X;++a){
                for(int b=0;b<Execut_Algo_Genetique.sizeGrille_Y;++b){
                    Execut_Algo_Genetique.grid[a][b].heuristicCost = Math.abs(a-listeDesPoints[j+1].getRow())+Math.abs(b-listeDesPoints[j+1].getColumn());
                    Execut_Algo_Genetique.grid[a][b].parent = null;
                }
             }
            Execut_Algo_Genetique.grid[listeDesPoints[j].getRow()][listeDesPoints[j].getColumn()].finalCost = 0;

            //add the start location to open list.
            this.open.add(Execut_Algo_Genetique.grid[listeDesPoints[j].getRow()][listeDesPoints[j].getColumn()]);

            Cell current;

            while(true && open.size()>0){
                current = open.poll();
                if(current.getStates()==States.WALL) {
                	break;
                }


                this.closed[current.row][current.col]=true;

                if(current.equals(Execut_Algo_Genetique.grid[listeDesPoints[j+1].getRow()][listeDesPoints[j+1].getColumn()])){
                    break;
                }
                Cell t;
                if(current.row-1>=0){
                    t = Execut_Algo_Genetique.grid[current.row-1][current.col];
                    this.checkAndUpdateCost(current, t, current.finalCost+COST);
                }
                if(current.col-1>=0){
                    t = Execut_Algo_Genetique.grid[current.row][current.col-1];
                    this.checkAndUpdateCost(current, t, current.finalCost+COST);
                }
                if(current.col+1<Execut_Algo_Genetique.grid[0].length){
                    t = Execut_Algo_Genetique.grid[current.row][current.col+1];
                    this.checkAndUpdateCost(current, t, current.finalCost+COST);
                }
                if(current.row+1<Execut_Algo_Genetique.grid.length){
                    t = Execut_Algo_Genetique.grid[current.row+1][current.col];
                    this.checkAndUpdateCost(current, t, current.finalCost+COST);
                }
            }

            if(closed[listeDesPoints[j+1].getRow()][listeDesPoints[j+1].getColumn()]){
                //Trace back the path
                 Cell current2 = Execut_Algo_Genetique.grid[listeDesPoints[j+1].getRow()][listeDesPoints[j+1].getColumn()];
                 while(current2.parent!=null){
                     current2 = current2.parent;
                     resultat ++;
                 }
            }else {
         	   return -1;
            }
        }

   	return resultat;
   }


		/**
		* Affichage de la matrice.
		* @param passagersOrdonnes2 .
		*/
		protected void prompt(Passager[][] passagersOrdonnes2) {
		for(int i=0;i<passagersOrdonnes2.length;i++) {
			System.out.print("[");
			for(int j=0;j<passagersOrdonnes2[i].length;j++) {
				System.out.print("["+passagersOrdonnes2[i][j].getId()+"]");
			}
			System.out.println("]");
		}

	}
}
