package clientserveurweb.serveur.Core;

import clientserveurweb.HTTPProtocol.Get;
import clientserveurweb.HTTPProtocol.Response;
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
    
    private PrintStream out;

    /**
     * Constructeur prenant en paramètre un Socket
     *
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
            out = new PrintStream(_socket.getOutputStream());
            message = in.readLine();
            out.println("Bonjour " + message);
            
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void analyzeRequest(String message) {
        if(!message.contains(Get.HTTP_VERSION)) {
            Response rep = new Response(500, "Bad protocol");
            
        }
        if(message.length()== 0 || !message.contains("GET") || !message.contains(Get.HTTP_VERSION)) {
            //Créer paquet de réponse erreur 400 bad request
          
        }
        //test erreur 404 not found
        
        //puis test erreur 403 forbidden
    }

}
