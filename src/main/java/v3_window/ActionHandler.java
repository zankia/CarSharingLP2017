package v3_window;

import java.awt.event.*;

import v3_algo.Execut_Algo_Genetique;

/**
 * Fonctions correspond aux actions d'un bouton
 * @author AirDur
 */
public class ActionHandler implements ActionListener {
    protected Window win;
    protected String name;


    protected ActionHandler(Window win, String name) {
        this.win = win;
        this.name = name;
    }


    @Override
    /**
     * Classe qui gère les actions
     * @author AirDur
     */
    public void actionPerformed(ActionEvent evt) {
        switch (this.name) {
            case "New Grid":
                //Affiche une nouvelle grille selon les paramètres
                win.realTime = false;
                win.animationButton.setEnabled(true);
                win.slider.setEnabled(true);
                win.initializeGrid();
                break;

            case "Clear":
                //Efface le parcours (Animation)
                win.deleteCarAndPassagerGrid();
                break;
            case "Clear2":
                //Enlève tout les murs de la grille.
                win.deleteWallGrid();
                break;
            case "Animation":
                //Affiche pour chaque étape de l'algo génétique le parcours des voitures.
                if (!win.endOfSearch) { //Ne peut pas se déclancher si le truc marche pas.
                    win.realTime = false;
                    win.searching = true;
                    win.message.setText((String) win.JSON_Window.get("msgSelectStepByStepEtc"));
                    win.promptSelected = true;
                    win.algo = new Execut_Algo_Genetique(win);
                    win.timer.setDelay(win.delay);
                    win.timer.start();
                }
                break;
            case "ChangeState":
                win.editButton("ChangeState_2", win.changeStateButton);
                win.typeColoriage = true;
                break;
            case "ChangeState_2":
                win.editButton("ChangeState", win.changeStateButton);
                win.typeColoriage = false;
                break;
            case "About":
                About about = new About(App.mazeFrame, true);
                about.setVisible(true);
                break;

        }

    }
} // end nested class ActionHandler
