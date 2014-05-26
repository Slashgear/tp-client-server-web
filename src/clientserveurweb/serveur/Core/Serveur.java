package clientserveurweb.serveur.Core;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Antoine
 */
public class Serveur {

    /**
     * Socket du Serveur Web
     */
    private ServerSocket _serveur;

    public ServerSocket getServeur() {
        return _serveur;
    }

    public static final String SERVER_DIRECTORY = "DataServeur/";

    /**
     * Constructeur d'un Serveur Web HTTP 1.1
     *
     * @param port
     * @param connection_nb
     */
    public Serveur(int port, int connection_nb) {
        try {
            _serveur = new ServerSocket(port, connection_nb);
            System.out.println("Lancement du Serveur Web sur le port :" + port);
        } catch (IOException ex) {
            System.out.println("Lancement du Serveur impossible, port probablement occupé");
        }

    }

}
