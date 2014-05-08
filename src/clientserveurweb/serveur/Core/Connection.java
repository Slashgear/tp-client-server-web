package clientserveurweb.serveur.Core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Connection d'un client sur le Serveur
 *
 * @author Antoine
 */
public class Connection extends Thread {
    /**
     * Socket renvoyé par le accept() du Serveur
     */
    private Socket _socket;

    /**
     * Constructeur prenant en paramètre un Socket
     * @param _socket 
     */
    public Connection(Socket _socket) {
        this._socket = _socket;
    }

    @Override
    public void run() {
        traitements();
    }
    
    /**
     * Traitement Basique d'un Serveur
     */
    public void traitements() {

        BufferedReader in = null;
        try {
            String message = "";
            System.out.println("Connexion avec le Client: " + _socket.getInetAddress() + " Sur le port :" + _socket.getPort());
            in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            PrintStream out = new PrintStream(_socket.getOutputStream());
            message = in.readLine();
            out.println("Bonjour " + message);
            
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
