package connexion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientSide {
	List<Leecher> leechers ;
	public ClientSide() {
		leechers = new ArrayList<Leecher>();
	}
	
	public void connectTo(String name, String hostName, int port){
		Leecher l = null;
		try {
			l = new Leecher(name, hostName, port);
			l.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		leechers.add(l);
	}
	
}
