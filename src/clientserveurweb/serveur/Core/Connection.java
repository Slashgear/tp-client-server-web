package clientserveurweb.serveur.Core;

import clientserveurweb.HTTPProtocol.Get;
import clientserveurweb.HTTPProtocol.Response;
import clientserveurweb.serveur.Core.Observers.Observable;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Connexion avec un client, côté serveur
 *
 * @author Antoine
 */
public class Connection extends Observable implements Runnable {

    /**
     * Socket renvoyé par le accept() du Serveur
     */
    private Socket _socket;

    /**
     * Flux sortant vers le client
     */
    private PrintStream _out;

    /**
     * Flux entrant du client
     */
    private BufferedReader _in;
    
    /**
     * Booléen : true si connexion active, false si connexion terminée
     */
    private boolean _terminated;

    /**
     * Constructeur prenant en paramètre un Socket. Initialise les flux entrant
     * et sortant avec le client
     *
     * @param _socket
     */
    public Connection(Socket _socket) {
        try {
            this._socket = _socket;
            fireRequest("Connexion avec le Client: " + _socket.getInetAddress() + " Sur le port :" + _socket.getPort() + "\n");
            _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            _out = new PrintStream(_socket.getOutputStream());
            _terminated = false;
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        traitements();
    }

    /**
     * Traitement Basique d'un Serveur
     */
    public void traitements() {

        try {
            //while (_terminated == false) {
                String message = "";
                message = _in.readLine();
                fireRequest("Client: " + _socket.getInetAddress() + " \nRequête : " + message + "\n");
                analyzeRequest(message);
            //}
            _in.close();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Analyse de la requête HTTP envoyée par le client. Envoie les codes
     * d'erreurs et la page d'erreur associée en cas d'erreur au client via le
     * flux de sortie. Sinon envoie la page demandée après vérifications de son
     * existence et de son accès possible en lecture.
     *
     * @param message requête HTTP envoyée par le client
     */
    public void analyzeRequest(String message) {
        String[] t = message.split(" ");
        File filerequested;

        Response rep;
        if (t.length >= 3) {
            try {
                URL url = new URL(t[1]);
                //Si la requête ne contient pas GET
                if (!t[0].contains("GET")) {
                    filerequested = new File(Serveur.SERVER_DIRECTORY + "ERROR/400_BadRequest.html");
                    rep = new Response(400, "Bad request.", filerequested);
                    _out.println(rep.getContent());
                    _terminated = false;
                } else {
                    //Si la requête a une mauvaise version de HTTP
                    if (t.length >= 3 && !t[2].contains(Get.HTTP_VERSION)) {
                        filerequested = new File(Serveur.SERVER_DIRECTORY + "ERROR/505_HTTPVersionNotSupported.html");
                        rep = new Response(505, "HTTP Version not supported", filerequested);
                        _out.println(rep.getContent());
                        _out.close();
                        _terminated = true;
                    } else {
                        //Si un fichier a été spécifié dans l'URL
                        if (url.getFile() == null) {
                            // On va chercher la page d'index
                            filerequested = new File(Serveur.SERVER_DIRECTORY + "index.html");
                        } else {
                            // Sinon on tente d'aller chercher le fichier
                            filerequested = new File(Serveur.SERVER_DIRECTORY + url.getFile());
                        }
                        //Si le fichier n'existe pas ou est inaccessible
                        if (!filerequested.exists() || !filerequested.isFile()) {
                            filerequested = new File(Serveur.SERVER_DIRECTORY + "ERROR/404_NotFound.html");
                            rep = new Response(404, "File not found", filerequested);
                            _out.println(rep.getContent());
                        } else {
                            //Si le fichier n'est pas accessible en lecture
                            if (!filerequested.canRead()) {
                                filerequested = new File(Serveur.SERVER_DIRECTORY + "ERROR/403_Forbidden.html");
                                rep = new Response(403, "Forbidden", filerequested);
                                _out.println(rep.getContent());
                            } else {
                                // ... Sinon la requête est valide
                                rep = new Response(200, "OK", filerequested);
                                _out.println(rep.getContent());

                            }
                        }
                    }
                }
            } catch (MalformedURLException ex) {
                filerequested = new File(Serveur.SERVER_DIRECTORY + "ERROR/400_BadRequest.html");
                rep = new Response(400, "Bad request.", filerequested);
                _out.println(rep.getContent());
                _terminated = false;
            } catch (NullPointerException e) {
                filerequested = new File(Serveur.SERVER_DIRECTORY + "ERROR/400_BadRequest.html");
                rep = new Response(400, "Bad request.", filerequested);
                _out.println(rep.getContent());
                _terminated = false;
            }
        } else {
            filerequested = new File(Serveur.SERVER_DIRECTORY + "ERROR/400_BadRequest.html");
            rep = new Response(400, "Bad request.", filerequested);
            _out.println(rep.getContent());
            _terminated = false;
        }
    }

}
