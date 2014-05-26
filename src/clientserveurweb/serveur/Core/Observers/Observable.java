
package clientserveurweb.serveur.Core.Observers;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Adrien
 */
public class Observable {
    private List<ServerObserver> _observers;
    
    public Observable() {
        this._observers = new LinkedList<ServerObserver>();
    }
    
    public void addServerObserver(ServerObserver obs) {
        _observers.add(obs);
    }
    
    public void removeServerObserver(ServerObserver obs) {
        _observers.remove(obs);
    }
    
    public void removerServerObservers() {
        _observers.clear();
    }
    
    public void fireRequest(String str) {
        for(ServerObserver obs : _observers) {
            obs.onRequest(str);
        }
    }
    
    public void fireNewClient() {
        for(ServerObserver obs : _observers) {
            obs.onNewClient();
        }
    }
}
