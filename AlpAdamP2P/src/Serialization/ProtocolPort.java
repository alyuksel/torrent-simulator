package Serialization;

import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import communication.ClientHandler;
import communication.Pair;

public class ProtocolPort extends Protocole {
	private int port = 2081;
	private final byte id = 1;
	public ProtocolPort(int port) {
		this.port = port;
	}
	public ProtocolPort() {
		
	}
	@Override
	public void read(ProtocolBuffer p) {
		port = p.readInt();
	}

	@Override
	public void write(ProtocolBuffer p) {
		p.put(id);
		p.putInt(port);
		p.flip();
		toString();
	}
	@Override
	public void toDo(ClientHandler ch,Pair pair) {	
		SocketChannel sc = ch.channel();
		pair.clients.put(sc, port, sc.socket().getInetAddress().getHostAddress());
	}
	@Override
	public String toString() {
		return this.port+"";
	}
	public void setPort(int port) {
		this.port = port;
	}

}
