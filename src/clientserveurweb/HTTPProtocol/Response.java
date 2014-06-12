package clientserveurweb.HTTPProtocol;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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
     * MIME Type of the file Ex : text/html, image/jpeg etc.
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
        try {
            this._code = _code;
            this._message = _message;
            this._pageContent = _pageContent;
            this._contentLength = _pageContent.length();
            this._contentType = Files.probeContentType(_pageContent.toPath());
            this._httpVersion = Request.HTTP_VERSION;
            this.buildContent();
        } catch (IOException ex) {
            Logger.getLogger(Response.class.getName()).log(Level.SEVERE, null, ex);
        }
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
     * Build the _content of the Request from this format {HTTP Version}
     * {HTTPcode} {HTTP Message} Date : Content-type : Content-length :
     *
     * {File content}
     *
     * Page content is a "" string if any exception caught
     */
    public void buildContent() {

        String line;
        StringBuilder pageContent = new StringBuilder(), rep = new StringBuilder();
        try {

            //Add http version, http code and message to explain
            rep.append(_httpVersion);
            rep.append(" ");
            rep.append(_code);
            rep.append(" ");
            rep.append(_message);
            rep.append("\r\n");
            //Add current date, time
            rep.append("Date: ");
            rep.append(new Date());
            rep.append("\r\n");
            //Add Content-typ header
            rep.append("Content-type: ");
            rep.append(_contentType);
            rep.append("\r\n");
            //Add Content length header
            rep.append("Content-length: ");
            rep.append(_contentLength);
            rep.append("\r\n");
            //Void line
            rep.append("\r\n");

            //Page content
            if (_contentType.startsWith("text")) {
                //Reading content of the file
                BufferedReader buffrdr = new BufferedReader(new FileReader(_pageContent));
                while ((line = buffrdr.readLine()) != null) {
                    pageContent.append(line);
                    pageContent.append("\n");
                }
                buffrdr.close();
                rep.append(pageContent.toString());
                this._content = rep.toString().getBytes("UTF-8");
            } else {
                byte[] repBuf = rep.toString().getBytes("UTF-8");
                byte[] buf = new byte[(int) _contentLength];
                this._content = new byte[rep.toString().length() + (int) _contentLength];

                DataInputStream buffis = new DataInputStream(new FileInputStream(_pageContent));
                buffis.readFully(buf);
                
                System.arraycopy(repBuf, 0, this._content, 0, repBuf.length);
                System.arraycopy(buf, 0, this._content, repBuf.length, buf.length);

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Response.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Response.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
