package clientserveurweb.client.Core;

import clientserveurweb.HTTPProtocol.Get;
import clientserveurweb.client.Core.Observers.Observable;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
 * Classz principale définissant un Client Basique
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
            _socket = new Socket(_ipServ, _port);

            // Flux entrant
            BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));

            // Flux sortant
            PrintStream out = new PrintStream(_socket.getOutputStream());

            //Autres variables
            StringBuilder builder = new StringBuilder();
            long contentLength = 0;
            String line = "", contentType = "", pageContent = "";

            System.out.println(_get.getContent());
            out.println(_get.getContent());
            out.flush();

            //récupération du header
            while ((line = in.readLine()) != null && !line.equals("")) {
                if (line.startsWith("Content-type")) {
                    int i = line.indexOf(line);
                    contentType = line.substring(i + "Content-type: ".length());
                }
                if (line.startsWith("Content-length")) {
                    contentLength = Long.parseLong(line.substring(line.indexOf(line) + "Content-length: ".length()));
                }
                builder.append(line);
                builder.append("\r\n");
            }
            builder.append("\r\n");

            if (contentType.startsWith("image")) {
                byte[] buf = new byte[(int) contentLength];
                DataInputStream datastream = new DataInputStream(_socket.getInputStream());

                // On récupère le nom du fichier
                String[] splits = _get.getUrl().getFile().split("/");
                //On crée un fichier pour l'image dans le répoertoire tmp
                File imgTmp = new File("tmp\\" + splits[splits.length - 1]);
                File dir = new File("tmp");
                if (!dir.isDirectory()) {
                    dir.mkdir();
                }
                imgTmp.createNewFile();
                FileOutputStream fos = new FileOutputStream(imgTmp);

                if (datastream.read(buf) == contentLength) {
                    fos.write(buf);
                }
                fos.close();
                datastream.close();
                //Envoie des données
                fireTextResponse(builder.toString());
                fireResponseContent(buildImagePageContent(imgTmp));

            } else if (contentType.startsWith("text")) {
                while ((line = in.readLine()) != null) {
                    builder.append(line);
                    builder.append("\r\n");
                }
                pageContent = builder.toString();
                int i = builder.toString().indexOf("\r\n\r\n");
                fireTextResponse(pageContent);
                fireResponseContent(buildPageContent(builder.toString().substring(i + 4), contentType));
            }

            in.close();
            out.close();

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String buildImagePageContent(File image) {
        //Affichage de l'image en HTML
        StringBuilder builder = new StringBuilder();
        builder.append("<!doctype HTML PUBLIC>\n<HTML>\n<BODY>");
        builder.append("<img src=\"");
        builder.append(image.getAbsolutePath());
        builder.append("\"/>");
        builder.append("<BODY>\n</HTML>");
        return builder.toString();

    }

    public String buildPageContent(String reponse, String mimeType) {
        File imgTmp;
        //Si le fichier est une image
        if (mimeType.contains("image")) {
            FileWriter wrt = null;
            try {
                // On récupère le nom du fichier
                String[] splits = _get.getUrl().getFile().split("/");
                //On crée un fichier pour l'image dans le répoertoire tmp
                imgTmp = new File("tmp\\" + splits[splits.length - 1]);
                File dir = new File("tmp");
                if (!dir.isDirectory()) {
                    dir.mkdir();
                }
                imgTmp.createNewFile();
                wrt = new FileWriter(imgTmp);
                wrt.write(reponse);
                wrt.close();

                //Affichage de l'image en HTML
                StringBuilder builder = new StringBuilder();
                builder.append("<!doctype HTML PUBLIC>\n<HTML>\n<BODY>");
                builder.append("<img src=\"");
                builder.append(imgTmp.getAbsolutePath());
                builder.append("\"/>");
                builder.append("<BODY>\n</HTML>");
                return builder.toString();

            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (wrt != null) {
                    try {
                        wrt.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        if (mimeType.startsWith("text")) {
            return reponse;
        }
        return null;
    }

    @Override
    public void run() {
        traitements();
    }
}
