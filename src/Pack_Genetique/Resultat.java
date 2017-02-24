package Pack_Genetique;
public class Resultat {
 
 
    // Trouver le resultat d'un membre, la somme des distances entre le 1er et le 2nd, puis le 2nd et le 3ème.... 
    static int getResultat(Membre membre) {
        int resultat = 0;
        int distanceEntreDeuxPoints = 0;
        
        /* si la voiture part de (0,0)
        distanceEntreDeuxPoints += Math.abs(membre.pointsDePassage[0][0].getPos_y());
    	distanceEntreDeuxPoints += Math.abs(membre.pointsDePassage[0][0].getPos_x());
    	resultat += distanceEntreDeuxPoints;
         */
        for (int i = 0; i < membre.pointsDePassage.length - 1 ; i++){
            for (int j = 0; j < membre.pointsDePassage[i].length - 1 ; j++){
        	distanceEntreDeuxPoints = 0;
        	distanceEntreDeuxPoints += Math.abs(membre.pointsDePassage[i][j].getPos_y() - membre.pointsDePassage[i][j+1].getPos_y());
        	distanceEntreDeuxPoints += Math.abs(membre.pointsDePassage[i][j].getPos_x() - membre.pointsDePassage[i][j+1].getPos_x());
        	resultat += distanceEntreDeuxPoints;
            }
        }
    	
        return resultat;
    }
 
}