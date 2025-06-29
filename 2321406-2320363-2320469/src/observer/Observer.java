package observer;

//Ao receber notificação de atualização, notifica o observable que disparou o evento
public interface Observer {
	void notify(Observable o);
}
