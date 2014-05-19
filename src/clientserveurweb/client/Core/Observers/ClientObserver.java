package clientserveurweb.client.Core.Observers;

/**
 *
 * @author Adrien
 */
public interface ClientObserver {

    public void onResponse(String str);

    public void onError(int errorCode, String message);
}
