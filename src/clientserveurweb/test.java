
package clientserveurweb;

import clientserveurweb.client.Core.Client;

/**
 *
 * @author Antoine
 */
public class test {
    
    public static void main(String[] args) {
        Client cli=new Client("127.0.0.1", 80, "Bonjour je suis le client");
        cli.traitements();
    }
}
