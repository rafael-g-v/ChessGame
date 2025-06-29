package observer;

import java.util.List;
import java.util.ArrayList;

public interface Observable {
	/* Implementação default para evitar código repetido */
    List<Observer> OBSERVERS = new ArrayList<>();

    default void addObserver(Observer o)   { OBSERVERS.add(o); }
    default void remObserver(Observer o)   { OBSERVERS.remove(o); }
    default void notifyObservers()         { 
        for (Observer ob : List.copyOf(OBSERVERS))
            ob.notify(this);
    }
}
