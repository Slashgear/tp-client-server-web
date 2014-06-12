package clientserveurweb.HTTPProtocol;

/**
 * HTTP request class
 *
 * @author Adrien
 */
public abstract class Request {

    /**
     * Default version of the HTTP protocol on the server
     */
    public static final String HTTP_VERSION = "HTTP/1.1";

    /**
     *
     */
    protected byte[] _content;

    public byte[] getContent() {
        return _content;
    }

}
