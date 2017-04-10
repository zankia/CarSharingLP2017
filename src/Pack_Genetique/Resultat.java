package Pack_Genetique;

/**
 * Classe à une méthode. Ne sert à rien.
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 */
public class Resultat {
 
 
    /**
     *  Trouver le resultat d'un GroupePassager, la somme des distances entre le 1er et le 2nd, puis le 2nd et le 3ème.... 
     * @param GroupePassager
     * @return
     */
    static int getResultat(PassagerOnVoiture GroupePassager) {
        int resultat = 0;
        int distanceEntreDeuxPoints = 0;
        
        /* si la voiture part de (0,0)
        distanceEntreDeuxPoints += Math.abs(GroupePassager.pointsDePassage[0][0].getPos_y());
    	distanceEntreDeuxPoints += Math.abs(GroupePassager.pointsDePassage[0][0].getPos_x());
    	resultat += distanceEntreDeuxPoints;
         */
        for (int i = 0; i < GroupePassager.pointsDePassage.length - 1 ; i++){
            for (int j = 0; j < GroupePassager.pointsDePassage[i].length - 1 ; j++){
        	distanceEntreDeuxPoints = 0;
        	distanceEntreDeuxPoints += Math.abs(GroupePassager.pointsDePassage[i][j].getPos_y() - GroupePassager.pointsDePassage[i][j+1].getPos_y());
        	distanceEntreDeuxPoints += Math.abs(GroupePassager.pointsDePassage[i][j].getPos_x() - GroupePassager.pointsDePassage[i][j+1].getPos_x());
        	resultat += distanceEntreDeuxPoints;
            }
        }
    	
        return resultat;
    }
 
}