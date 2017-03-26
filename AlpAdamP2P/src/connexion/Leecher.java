package connexion;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Leecher extends Client {
	private SocketChannel s;
	private boolean connected;
	public Leecher(String name, String hostName, int port) throws IOException {
		super(name, hostName, port);
		this.setConnected(true);
		this.s = SocketChannel.open();
		
		this.s.configureBlocking(true);
		InetSocketAddress addr = new InetSocketAddress(hostName,port);
		this.s.connect(addr);
	}
	
	@Override
	public void start() throws IOException{
		Thread download = new Thread(new Download(s, this));
		Thread request = new Thread(new Requests(s, this));
		download.start();
		request.start();
		try {
			download.join();
			request.join();
		} catch (InterruptedException e) {}
		this.setConnected(false);
		
		
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	public static void main(String[] args) {
		try {
			Leecher leecher = new Leecher("Adam","prog-reseau-m1.zzzz.io", 443);
			//Leecher leecher = new Leecher("Adam","localhost", 2023);
			leecher.start();
		} catch (IOException e) {
			
		}
	}

}
