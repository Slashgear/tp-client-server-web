
package clientserveurweb.client.Core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class principale définissant un Client Basique
 * @author Antoine
 */
public class Client {
    private InetAddress _ipServ;
    private int _port;
    private String _message;
    private Socket _socket;

    public Client(String _ipServ, int _port, String _message) {
        try {
            this._ipServ =InetAddress.getByName(_ipServ);
            this._port = _port;
            this._message = _message;
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void traitements(){
        try {
            _socket= new Socket(_ipServ,_port);
            BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            PrintStream out = new PrintStream(_socket.getOutputStream());
            out.println(_message);
            System.out.println(in.readLine());
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
}
