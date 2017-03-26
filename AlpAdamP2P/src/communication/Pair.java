package communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.Request;

import connexion.Leecher;
import connexion.Requests;
import Serialization.ProtocolException;
import Serialization.Protocole;
import tools.FileTool;

public class Pair extends AbsPair {
	public Clients clients= new Clients();
	public Map<String,Long> files;
	private ServerSocketChannel ssc;
	public  Selector selector;
	private Protocole p;
	public Message message = new Message();

	public Pair(String name, String hostName, int port) throws IOException {
		super(name, hostName, port);
		clients = new Clients();
		files = new HashMap<String,Long>();
		FileTool.fileToByte("l1.jpg",files);
		this.ssc = ServerSocketChannel.open();
		InetSocketAddress addr = new InetSocketAddress(hostName,port);
		this.ssc.configureBlocking(false);
		this.ssc.bind(addr);
		selector = Selector.open();
		this.ssc.register(selector, SelectionKey.OP_ACCEPT);
	}

	private void repeat (SelectionKey k) throws IOException{
		ClientHandler ch = (ClientHandler) k.attachment();
		if(k.isReadable()){
			int id=-1;
			synchronized (ch) {
				int val = ch.read();
				
				if (val == -1) {
					System.out.println("deconnection");
					throw new IOException();
				}
				if(ch.isDownloading){
					ch.download();
				}
				else{
					ch.flip();
					id = ch.getProtoBuffer().get();
					try {
						p = Protocole.createProtocole(id);
					} catch (ProtocolException e) {return ;}
					p.read(ch.getProtoBuffer());
					if(id==7)ch.getProtoBuffer().getBuffer().limit(ch.getProtoBuffer().getBuffer().capacity());
					System.out.println(id + " " +p.toString());
					p.toDo(ch,this);
				}
				if(ch.isWriting)
					k.interestOps(SelectionKey.OP_WRITE);
				else if(id!=7 && id!=-1){
					System.out.println("j'efface");
					ch.clearBuff();
				}
			}
		}

		else if(k.isWritable()){
			synchronized (ch) {
				if(ch.isWriting){
					ch.write();
					ch.clearBuff();
					ch.isWriting = false;
				}
				k.interestOps(SelectionKey.OP_READ);
			}
		}
	}

	private void accept() {
		try {
			SocketChannel socket = ssc.accept();
			socket.configureBlocking(false);
			ClientHandler ch = new ClientHandler(socket.socket().getPort(),socket.getLocalAddress().toString());
			ch.setSocket(socket);
			selector.wakeup();
			socket.register(selector, SelectionKey.OP_WRITE| SelectionKey.OP_READ,ch);
			System.out.println("new Client : " + socket.getLocalAddress());
			p = Protocole.createProtocole(0);
			ch.clearBuff();
			p.write(ch.getProtoBuffer());
			socket.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
			ch.isWriting = true;
		} catch (IOException | ProtocolException e) {
			e.printStackTrace();
		}
	}
	public void connectTo(String hostName, int p) throws IOException{
		InetSocketAddress addr = new InetSocketAddress(hostName,p);
		SocketChannel sc = SocketChannel.open(addr);
		sc.configureBlocking(false);
		clients.put(sc,p,hostName);
		ClientHandler ch = new ClientHandler(p,hostName);
		ch.setSocket(sc);
		selector.wakeup();
		sc.register(selector, SelectionKey.OP_WRITE| SelectionKey.OP_READ,ch);
	//	Thread t = new Thread(new Requests(sc, new Leecher(name, hostName, port)));
		//t.start();
	}

	public void start() throws IOException{
		//connectTo("prog-reseau-m1.zzzz.io", 443);
		//connectTo("Adam","localhost", 2023);

		while(true){
			selector.select();
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			for(SelectionKey k : selectionKeys){
				if(k.isAcceptable())
					this.accept();
				else
					repeat(k);
			}
			selectionKeys.clear();
		}
	}

}
