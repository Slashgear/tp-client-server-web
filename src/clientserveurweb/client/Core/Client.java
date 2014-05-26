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

    public Socket getSocket() {
        return _socket;
    }

    public void setGet(URL url) {
        this._get = new Get(url);
    }

    public void traitements() {
        try {
            //if (!_socket.isConnected()) {
                _socket = new Socket(_ipServ, _port);
           //}
            
            BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            PrintStream out = new PrintStream(_socket.getOutputStream());
            StringBuilder builder = new StringBuilder();
            System.out.println(_get.getContent());
            out.println(_get.getContent());
            
            String line = "", contentType = "";
            while ((line = in.readLine()) != null) {
                if (line.startsWith("Content-type")) {
                    int i = line.indexOf(line);
                    contentType = line.substring(i + "Content-type : ".length() - 1);
                }
                builder.append(line);
                builder.append("\n");
            }
            fireTextResponse(builder.toString());
            int i = builder.toString().indexOf("\n\n");
            fireResponseContent(builder.toString().substring(i), contentType);

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        traitements();
    }
}
