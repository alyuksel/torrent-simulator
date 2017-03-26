package connexion;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

import Serialization.ProtocolBuffer;
import Serialization.ProtocolException;
import Serialization.Protocole;

public class Seeder extends Client {
	
	private ServerSocketChannel s;
	private Selector selector;
	private Protocole p;
	private ProtocolBuffer pb;

	
	public Seeder(String name, String hostName, int port) throws IOException {
		super(name, hostName, port);
		this.s = ServerSocketChannel.open();
		InetSocketAddress addr = new InetSocketAddress(hostName,port);
		this.s.configureBlocking(false);
		this.s.bind(addr);
		this.selector = Selector.open();
		this.s.register(selector, SelectionKey.OP_ACCEPT);;
		this.pb = new ProtocolBuffer(buffer);
		
	}
	
	public void accept(){
		try {
			SocketChannel sc = s.accept();
			sc.configureBlocking(false);
			sc.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			System.out.println("new Client : " + sc.getLocalAddress());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void repeat(SelectionKey sk, Set<SelectionKey> keys) {
		if(sk.isAcceptable())this.accept();
		if(sk.isWritable()){
			SocketChannel sc = (SocketChannel) sk.channel();
			request();
			try {
				
				sc.write(buffer);
				buffer.clear();
				sk.interestOps(SelectionKey.OP_READ);
				sc.read(buffer);
				try {
					int i = buffer.get();
					System.out.println(i);
					p = Protocole.createProtocole(i);
					p.read(pb);
					System.out.println(p);
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				try {
					System.out.println(sc.getLocalAddress());
					sc.close();
					sk.cancel();
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}

			}
			
		}
		}
		
		
		
		
	
	public void request(){
		try {
			p = Protocole.createProtocole(0);
			p.write(pb);
			
			
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void start() throws IOException {
		while(true){
			selector.select();
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			Set<SelectionKey> keys = selector.keys();
			for(SelectionKey k : selectionKeys){
				this.repeat(k,keys);
			}
			selectionKeys.clear();
		}
		
	}
	public static void main(String[] args) {
		try {
			Seeder s = new Seeder("Adam","localhost",2081);
			s.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
