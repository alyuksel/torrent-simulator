package communication;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class Clients extends Observable {
	private Map<String, Integer> cl;
	private Map<String, SocketChannel> clientsSock;

	public Clients() {
		cl = new HashMap<String, Integer>();
		clientsSock = new HashMap<String, SocketChannel>();
	}


	public Map<String, Integer> getClients(){
		return cl;
	}

	public Map<String, SocketChannel> getClientsSock(){
		return clientsSock;
	}


	public void put(SocketChannel sc, int port, String hostName) {
		if(!cl.containsKey(hostName))cl.put(hostName, port);
		if(sc!=null)clientsSock.put(hostName, sc);
		update();
	}

	public void update(){
		setChanged();
		notifyObservers();
	}

}
