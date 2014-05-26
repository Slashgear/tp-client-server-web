package clientserveurweb.HTTPProtocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;

/**
 * Class for a response to an HTTP Request
 *
 * @author Adrien
 */
public class Response extends Request {

    /**
     * HTTP code
     */
    private int _code;
    /**
     * Message associated with this code
     */
    private String _message;
    /**
     * File requested by the client or file to send
     */
    private File _pageContent;
    /**
     * Length of the file (in bytes)
     */
    private long _contentLength;
    /**
     * MIME Type of the file Ex : txt/html, image/jpeg etc.
     */
    private String _contentType;
    /**
     * HTTP Protocol version
     */
    private String _httpVersion;

    /**
     * Constructor used for debugging.
     *
     * @param _code HTTP code
     * @param _message Message associated to the code
     */
    public Response(int _code, String _message) {
        this._code = _code;
        this._message = _message;
    }

    /**
     * Constructor that build a full response request. Initializes all
     * attributes Sending headers, page content.
     *
     * @param _code
     * @param _message
     * @param _pageContent
     */
    public Response(int _code, String _message, File _pageContent) {
        this._code = _code;
        this._message = _message;
        this._pageContent = _pageContent;
        this._contentLength = _pageContent.length();
        this._contentType = new MimetypesFileTypeMap().getContentType(_pageContent);
        this._httpVersion = Request.HTTP_VERSION;
        this.buildContent();
    }

    /**
     * Build a string like "Error {code} {message}"
     *
     * @return String a basic representation of a response
     */
    @Override
    public String toString() {
        return "Error " + _code + "\n" + _message;
    }

    /**
     * Build the _content of the Request from this format
     * {HTTP Version} {HTTPcode} {HTTP Message}
     * Date : 
     * Content-type :
     * Content-length :
     * 
     * {File content}
     * 
     * Return a "" string if any exception caught
     */
    public void buildContent() {
        FileReader reader = null;
        BufferedReader buffrdr;
        String rep = "", line, pageContent = "";
        try {
            //Reading content of the file
            buffrdr = new BufferedReader(new FileReader(_pageContent));
            while ((line = buffrdr.readLine()) != null) {
                pageContent += line;
                pageContent += "\n";
            }
            buffrdr.close();

            //Add http version, http code and message to explain
            rep = _httpVersion + " " + _code + " " + _message;
            //Add current date, time
            rep = rep + "\n" + "Date : " + new Date();
            //Add Content-typ header
            rep = rep + "\n" + "Content-type : " + _contentType;
            //Add Content length header
            rep = rep + "\n" + "Content-length : " + _contentLength;
            //Void line
            rep = rep + "\n\n";
            //Page content
            rep = rep + pageContent;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Response.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Response.class.getName()).log(Level.SEVERE, null, ex);
        }
        this._content = rep;
    }

}
