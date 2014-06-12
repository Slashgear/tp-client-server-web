package clientserveurweb.HTTPProtocol;

import java.net.URL;

/**
 *
 * @author Adrien
 */
public class Get extends Request {
    
    private URL _url;

    public URL getUrl() {
        return _url;
    }

    public Get(URL url) {
        this._url = url;
        StringBuilder requete = new StringBuilder();
        requete.append("GET ");
        requete.append(url.toString());
        requete.append(" ");
        requete.append(HTTP_VERSION);
        requete.append("\r\n");
        _content = requete.toString().getBytes();
    }

}
