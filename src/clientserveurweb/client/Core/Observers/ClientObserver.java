package clientserveurweb.client.Core.Observers;

/**
 *
 * @author Adrien
 */
public interface ClientObserver {

    public void onTextResponse(String str);

    public void onResponseContent(String str);
    
}
