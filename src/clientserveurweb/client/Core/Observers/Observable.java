package clientserveurweb.client.Core.Observers;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Adrien
 */
public class Observable {

    private List<ClientObserver> _observers;

    public Observable() {
        _observers = new LinkedList<ClientObserver>();
    }

    public void addClientObserver(ClientObserver obs) {
        _observers.add(obs);
    }

    public void removeClientObserver(ClientObserver obs) {
        _observers.remove(obs);
    }

    public void removeClientObservers() {
        _observers.clear();
    }

    public void fireTextResponse(String str) {
        for (ClientObserver o : _observers) {
            o.onTextResponse(str);
        }
    }

    public void fireResponseContent(String str, String contentType) {
        for (ClientObserver o : _observers) {
            o.onResponseContent(str, contentType);
        }
    }

}
