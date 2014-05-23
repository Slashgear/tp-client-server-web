package clientserveurweb.HTTPProtocol;

import java.io.File;

/**
 *
 * @author Adrien
 */
public class Response extends Request {

    private int _code;
    private String _message;
    private File _pageContent;
    private long _contentLength;
    private String _contentType;
    

    public Response(int _code, String _message) {
        this._code = _code;
        this._message = _message;
    }

    public Response(int _code, String _message, File _pageContent) {
        this._code = _code;
        this._message = _message;
        this._pageContent = _pageContent;
        this._contentLength = _pageContent.length();
        //this._contentType = Files.getFileExtension(_pageContent);
    }
    
    public String getTotalText() {
        return "Erreur " + _code + "\n" + _message;
    }
    
}
