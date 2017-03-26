package connexion;

import java.io.IOException;

public class App {
	Seeder server;
	ClientSide client;
	
	public App(String name, String hostName, int port) {
		try {
			server = new Seeder(name, hostName, port);
			client = new ClientSide();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void start(){
		Thread serv = new Thread(()->{
			try {
				server.start();
				
			} catch (Exception e) {e.printStackTrace();
			}
		});
		serv.start();
		client.connectTo("Adam","prog-reseau-m1.zzzz.io", 443);
		//client.connectTo("Adam","localhost", 2021);
	}
	
	public static void main(String[] args) {
		App app = new App("Adam","localhost",2081);
		app.start();
	}
}
