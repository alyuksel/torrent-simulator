package connexion;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import communication.ClientHandler;
import communication.Pair;
import Serialization.ProtocolAskFrag;
import Serialization.ProtocolBuffer;
import Serialization.ProtocolException;
import Serialization.Protocole;

public class Requests implements Runnable {
	private SocketChannel sc;
	private Leecher leecher;
	private ByteBuffer buff;
	private Scanner scan;
	private Selector selector;

	public Requests(SocketChannel sc, Leecher leecher) {
		this.sc = sc;
		this.leecher = leecher;
		buff = ByteBuffer.allocate(65536+1024);
		scan = new Scanner(System.in);
	}
	
	

	public Requests(SocketChannel sc, Leecher leecher, Selector selector) {
		this.sc = sc;
		this.leecher = leecher;
		this.selector = selector;
		buff = ByteBuffer.allocate(65536+1024);
		scan = new Scanner(System.in);
	}



	@Override
	public void run() {
		while (leecher.isConnected()) {
			buff.clear();
			try {
				int val = scan.nextInt();
				if (val == -1) {
					throw new IOException();
				}
				if(val == 7){
					SelectionKey key = sc.keyFor(selector);
					ClientHandler cl = (ClientHandler) key.attachment();
					String file = "meidumfile.pdf";
					long fileSize = 241455;
					ProtocolAskFrag askf = new ProtocolAskFrag(file, fileSize,0,65536);
					if(cl != null){
						cl.clearBuff();
						askf.write(cl.getProtoBuffer());
						cl.isWriting = true;
						key.interestOps(SelectionKey.OP_WRITE);
					}
				}
				
				else{
					ProtocolBuffer pb = new ProtocolBuffer(buff);
					System.out.println(val);
					buff.clear();
					Protocole protocole = Protocole.createProtocole(val);
					protocole.write(pb);
					System.out.println(protocole);
					sc.write(buff);
					buff.clear();
				}
			} catch (IOException | ProtocolException e) {
				leecher.setConnected(false);
			}
		}

	}

}
