package clientserveurweb.serveur.Core.Observers;

/**
 *
 * @author Adrien
 */
public interface ServerObserver {

    public void onRequest(String str);
    
    public void onNewClient();
}
