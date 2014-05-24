package clientserveurweb.client.Core;

import clientserveurweb.HTTPProtocol.Get;
import clientserveurweb.client.Core.Observers.Observable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.CharBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class principale définissant un Client Basique
 *
 * @author Antoine
 */
public class Client extends Observable implements Runnable {

    private InetAddress _ipServ;
    private int _port;
    private Get _get;
    private Socket _socket;

    public Client(int _port, URL url) {
        try {
            this._ipServ = InetAddress.getByName(url.getHost());
            this._port = _port;
            this._get = new Get(url);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void traitements() {
        try {
            _socket = new Socket(_ipServ, _port);
            BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            PrintStream out = new PrintStream(_socket.getOutputStream());
            System.out.println(_get.getContent());
            out.println(_get.getContent());
            //Lire tout ce qu'on a reçu ?
            String line = "", pageContent = "";
            while((line = in.readLine()) != null) {
                pageContent += line;
                pageContent += "\n";
            }
            fireResponse(pageContent);

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        traitements();
    }
}
