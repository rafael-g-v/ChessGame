package observer;

import java.util.List;
import java.util.ArrayList;

public interface Observable {
	/* Implementação default para evitar código repetido */
    List<Observer> OBSERVADORES = new ArrayList<>();

    default void addObservador(Observer o)   { OBSERVADORES.add(o); }
    default void remObservador(Observer o)   { OBSERVADORES.remove(o); }
    default void notificarObservadores()         { 
        for (Observer ob : List.copyOf(OBSERVADORES))
            ob.notificar(this);
    }
}
