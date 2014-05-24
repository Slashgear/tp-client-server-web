package clientserveurweb.HTTPProtocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for a response to an HTTP Request
 *
 * @author Adrien
 */
public class Response extends Request {

    private int _code;
    private String _message;
    private File _pageContent;
    private long _contentLength;
    private String _contentType;
    private String _httpVersion;

    public Response(int _code, String _message) {
        this._code = _code;
        this._message = _message;
    }

    public Response(int _code, String _message, File _pageContent) {
        this._code = _code;
        this._message = _message;
        this._pageContent = _pageContent;
        this._contentLength = _pageContent.length();
        this._contentType = "txt/html";
        this._httpVersion = Request.HTTP_VERSION;
        this.buildContent();
    }

    @Override
    public String toString() {
        return "Erreur " + _code + "\n" + _message;
    }

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
