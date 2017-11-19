package v3_algo;

import java.util.ArrayList;

import v3_window.Cell;

/**
 * Classe Population
 *
 * @author Romain Duret
 */
public class Population {
    /**
     * Liste de passager dans des voitures
     */
    PassagerParVoiture[] PassagerParVoitures;

    ArrayList<Cell> lb;


    /**
     *  Create a population and PassagerParVoiture inside
     * @param populationSize amount of cars
     * @param initialise wether or not creating PassagerParVoiture inside the
     *                   Population
     * @param list_block .
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


    /**
     * Sauvegarde un groupe de Passager réparti dans des voitures à un certain emplacement
     * @param index index of the element to update
     * @param PassagerParVoiture element to be stored at the specified position
     */
    public void savePassagerOnVoiture(int index, PassagerParVoiture PassagerParVoiture) {
        this.PassagerParVoitures[index] = PassagerParVoiture;
    }


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
     * @return the size of the Population
     */
    public int getSize() {
        return this.PassagerParVoitures.length;
    }
}
