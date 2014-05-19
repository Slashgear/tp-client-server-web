package clientserveurweb.HTTPProtocol;

/**
 *
 * @author Adrien
 */
public class Response extends Request {

    private int _code;
    private String _message;
    private String _pageContent;

    public Response(int _code, String _message) {
        this._code = _code;
        this._message = _message;
    }

    public Response(int _code, String _message, String _pageContent) {
        this._code = _code;
        this._message = _message;
        this._pageContent = _pageContent;
    }
    
}
