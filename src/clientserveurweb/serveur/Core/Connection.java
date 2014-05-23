package clientserveurweb.serveur.Core;

import clientserveurweb.HTTPProtocol.Get;
import clientserveurweb.HTTPProtocol.Response;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
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

    public void analyzeRequest(String message) {
        String[] t = message.split(" ");
        File filerequested;
        Response rep;
        if (t.length >= 3) {
            try {
                URL url = new URL(t[1]);
                //Si la requête ne contient pas GET
                if (!t[0].contains("GET")) {
                    rep = new Response(400, "Bad request.");
                    _out.println(rep.getTotalText());
                } else {
                    //Si la requête a une mauvaise version de HTTP
                    if (t.length >= 3 && !t[2].contains(Get.HTTP_VERSION)) {
                        //Rajouter close de connexion dans le paquet de réponse
                        rep = new Response(505, "HTTP Version not supported");
                        _out.println(rep.getTotalText());
                    } else {
                        filerequested = new File(Serveur.SERVER_DIRECTORY + url.getFile());

                        //Si le fichier n'existe pas ou est inaccessible
                        if (!filerequested.exists() || !filerequested.isFile()) {
                            rep = new Response(404, "File not found");

                            _out.println(rep.getTotalText());
                        } else {
                            //Si le fichier n'est pas accessible en lecture
                            if (!filerequested.canRead()) {
                                rep = new Response(403, "Forbidden");
                                _out.println(rep.getTotalText());
                            } else {
                                // ... Sinon la requête est valide
                                rep = new Response(200, "OK", filerequested);
                                //_out.println(rep.getTotalText());
                                try {
                                    BufferedWriter buff_out = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
                                    buff_out.write("Test buff_out");
                                } catch (IOException ex) {
                                    Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                                

                            }
                        }
                    }
                }
            } catch (MalformedURLException ex) {
                rep = new Response(400, "Bad request.");
                _out.println(rep.getTotalText());
            } catch (NullPointerException e) {
                rep = new Response(400, "Bad request.");
                _out.println(rep.getTotalText());
            }
        } else {
            rep = new Response(400, "Bad request.");
            _out.println(rep.getTotalText());
        }
    }

}
