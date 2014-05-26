package clientserveurweb;

import clientserveurweb.serveur.UI.MainFrame;

/**
 *
 * @author Antoine
 */
public class ServeurWeb {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainFrame main = new MainFrame();
        main.setVisible(true);
        main.lancerTraitementServeur();
    }

}
