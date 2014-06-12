package clientserveurweb.client.Core;

import clientserveurweb.HTTPProtocol.Get;
import clientserveurweb.client.Core.Observers.Observable;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
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
            InputStream is = _socket.getInputStream();
            //BufferedReader in = new BufferedReader(new InputStreamReader(is));

            BufferedInputStream bis = new BufferedInputStream(is);

            // Flux sortant
            DataOutputStream out = new DataOutputStream(_socket.getOutputStream());

            //Autres variables
            StringBuilder builder = new StringBuilder();
            long contentLength = 0;
            String line = "", contentType = "", pageContent = "";

            //Envoi de la requete HTTP
            System.out.println(new String(_get.getContent()));
            out.write(_get.getContent());
            out.flush();

            //Récupération de tout le contenu de la réponse en binaire
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int numberRead;
            byte[] buf = new byte[16384];

            while ((numberRead = is.read(buf, 0, buf.length)) != -1) {
                buffer.write(buf, 0, numberRead);
            }

            buf = buffer.toByteArray();
            int length = buf.length;

            byte[] delimiter = "\r\n\r\n".getBytes();
            int indexOfDelimiter = -1;

            for (int i = 0; i < length && i + 4 < length && indexOfDelimiter < 0; i++) {
                if (buf[i] == delimiter[0] && buf[i + 1] == delimiter[1] && buf[i + 2] == delimiter[2] && buf[i + 3] == delimiter[3]) {
                    indexOfDelimiter = i;
                }
            }

            //Récupération que des headers
            ByteBuffer bytebuff = ByteBuffer.allocate(indexOfDelimiter + 4);
            bytebuff.put(buf, 0, indexOfDelimiter);
            bytebuff.put(delimiter);
            byte[] headers = new byte[indexOfDelimiter + 4];
            bytebuff.rewind();
            bytebuff.get(headers);

            String header = new String(headers, "UTF-8");

            //Récupération du contenu du fiohier
            if (header.contains("Content-type")) {
                int startindex = header.indexOf("Content-type") + "Content-type: ".length();
                int endindex = header.indexOf("\r\n", startindex);
                contentType = header.substring(startindex, endindex);
            }

            //Récupération de la taille du fichier
            if (header.contains("Content-length")) {
                int startindex = header.indexOf("Content-length") + "Content-length: ".length();
                int endindex = header.indexOf("\r\n", startindex);
                contentLength = Long.parseLong(header.substring(startindex, endindex));
            }

            //Récupération du contenu du fichier envoyé
            bytebuff.rewind();
            bytebuff = ByteBuffer.allocate((int) contentLength);
            bytebuff.put(buf, indexOfDelimiter + 4, length - indexOfDelimiter - 4);
            bytebuff.rewind();
            byte[] contentBytes = new byte[length - indexOfDelimiter - 4];
            bytebuff.get(contentBytes);

            //Si le fichier est une image
            if (contentType.startsWith("image")) {
                // On récupère le nom du fichier
                String[] splits = _get.getUrl().getFile().split("/");
                //On crée un fichier pour l'image dans le répertoire tmp
                File imgTmp = new File("tmp\\" + splits[splits.length - 1]);
                File dir = new File("tmp");
                if (!dir.isDirectory()) {
                    dir.mkdir();
                }
                imgTmp.createNewFile();

                //Flux d'écriture dans le fichier
                FileOutputStream fos = new FileOutputStream(imgTmp);
                fos.write(contentBytes);
                fos.close();
                
                //On envoit les données à afficher sur le navigateur
                fireTextResponse(header);
                fireResponseContent(buildImagePageContent(imgTmp));

            } else if (contentType.startsWith("text")) {
                String content = new String(contentBytes, "UTF-8");
                //Préparation du contenu textuel
                builder.append(header);
                builder.append(content);
                
                //On envoit les données à afficher sur le navigateur
                fireTextResponse(builder.toString());
                fireResponseContent(content);
            }
            //Fermeture des flux
            is.close();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String buildImagePageContent(File image) {
        //Affichage de l'image en HTML
        StringBuilder builder = new StringBuilder();
        builder.append("<!doctype HTML PUBLIC>\n<HTML>\n<BODY>");
        builder.append("<img src='");
        builder.append(image.toURI().toString());
        builder.append("'/>");
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
