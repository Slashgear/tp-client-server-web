package clientserveurweb.HTTPProtocol;

import java.net.URL;

/**
 *
 * @author Adrien
 */
public class Get extends Request {

    public Get(URL url) {
        StringBuilder requete = new StringBuilder();
        requete.append("GET ");
        requete.append(url.toString());
        requete.append(" ");
        requete.append(HTTP_VERSION);
        _content = requete.toString();
    }

}
