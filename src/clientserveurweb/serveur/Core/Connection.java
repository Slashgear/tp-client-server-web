package clientserveurweb.serveur.Core;

import clientserveurweb.HTTPProtocol.Get;
import clientserveurweb.HTTPProtocol.Response;
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
public class Connection extends Thread {

    /**
     * Socket renvoyé par le accept() du Serveur
     */
    private Socket _socket;

    /**
     * Flux sortant vers le client
     */
    private PrintStream _out;

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
            _out = new PrintStream(_socket.getOutputStream());
            message = in.readLine();
            analyzeRequest(message);
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Analyse de la requête HTTP envoyée par le client. Renvoie les codes
     * d'erreurs et la page d'erreur associée en cas d'erreur. Sinon renvoie la
     * page demandée après vérifications de son existence et de son accès
     * possible en lecture
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
                } else {
                    //Si la requête a une mauvaise version de HTTP
                    if (t.length >= 3 && !t[2].contains(Get.HTTP_VERSION)) {
                        filerequested = new File(Serveur.SERVER_DIRECTORY + "ERROR/505_HTTPVersionNotSupported.html");
                        rep = new Response(505, "HTTP Version not supported", filerequested);
                        _out.println(rep.getContent());
                        _out.close();
                    } else {
                        filerequested = new File(Serveur.SERVER_DIRECTORY + url.getFile());

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
            } catch (NullPointerException e) {
                filerequested = new File(Serveur.SERVER_DIRECTORY + "ERROR/400_BadRequest.html");
                rep = new Response(400, "Bad request.", filerequested);
                _out.println(rep.getContent());
            }
        } else {
            filerequested = new File(Serveur.SERVER_DIRECTORY + "ERROR/400_BadRequest.html");
            rep = new Response(400, "Bad request.", filerequested);
            _out.println(rep.getContent());
        }
    }

}
