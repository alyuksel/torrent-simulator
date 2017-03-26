package connexion;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import Serialization.ProtocolAskFrag;
import Serialization.ProtocolBuffer;
import Serialization.ProtocolException;
import Serialization.ProtocolSendFiles;
import Serialization.ProtocolSendFrag;
import Serialization.Protocole;
import gestion.FileHandler;

public class Download implements Runnable {
	private SocketChannel sc;
	private Leecher leecher;
	private ByteBuffer buff;
	private FileHandler fh;
	private String file;
	private long fileSize;
	ByteBuffer blob = null;
	
	public Download(SocketChannel sc, Leecher leecher) {
		this.sc = sc;
		this.leecher = leecher;
		this.buff = ByteBuffer.allocateDirect(65536+1024);
	}

	@Override
	public void run() {
		while (leecher.isConnected()) {
			buff.clear();
			if(blob != null){
				buff.put(blob);
			}
			try {
				int val = sc.read(buff);
				System.out.println("octet lu : " + val);
				if (val == -1) {
					leecher.setConnected(false);
					throw new IOException();
				}
				buff.flip();
				ProtocolBuffer p = new ProtocolBuffer(buff);

				byte id = p.get();
				System.out.println("id : " + id);
				Protocole protocole = Protocole.createProtocole(id);
				System.out.println(p.getBuffer());
				int remain = p.getBuffer().remaining();
				System.out.println("pos : " + remain);
				protocole.read(p);
				if (protocole.getClass().equals(ProtocolSendFrag.class)){
					System.out.println("entree frag");
					buff.clear();
					int lecture = 0;
					int read= 0;
					ArrayList<Object> arr = (ArrayList<Object>) protocole.option();
					long lastPos = (long) arr.get(3);
					int fragSize = (int) arr.get(4);
					
					while (lecture < fragSize - remain) {
						System.out.println(lecture + " < " + (fragSize - remain));
						System.out.println("octects lu : " + lecture);
						read = sc.read(buff);
						if(read == -1)break;
						lecture = lecture + read;
						System.out.println(buff);
						System.out.println("laste pooooooos" + lastPos);
					}
					if(lecture > fragSize-remain){
						System.out.println("kekekekek");
						System.out.println(lecture);
						System.out.println((fragSize-remain)%read);
						buff.position(lecture-((fragSize-remain)%read));
						System.out.println(buff);
						blob = buff.compact().duplicate();
						System.out.println(buff+"   "+blob);
						p.clear();
						
					}else{
						fh = new FileHandler(p);
						p.flip();
						fh.bigFailHandle(file, lastPos, fileSize, buff);
						p.clear();
					}
					
					
					
				}
				System.out.println(id + "" + protocole.toString());
				if (protocole.getClass().equals(ProtocolAskFrag.class)) {
					ProtocolAskFrag pro2 = (ProtocolAskFrag) protocole;
					p.clear();
					FileHandler fh = new FileHandler(p);
					fh.uploadAFile(pro2.getName(), pro2.getOffset(), pro2.getFragSize());
					sc.write(buff);
					p.clear();
				}
				if (protocole.getClass().equals(ProtocolSendFiles.class)) {
					p.clear();
					file = "mediumfile.pdf";
					fileSize = 241455;
					if (fileSize > 65536) {
						int numberOfFrag = (int) (fileSize / 65536);
						int rest = (int) (fileSize%65536);
						System.out.println("nombre de Fragments : " + numberOfFrag);
						System.out.println("reste :" + rest);
						for(int i =0; i<numberOfFrag;i++){
							ProtocolAskFrag askf = new ProtocolAskFrag(file, fileSize,i*65536,65536);
							askf.write(p);
							sc.write(buff);
							p.clear();
						}
						ProtocolAskFrag askf = new ProtocolAskFrag(file, fileSize,numberOfFrag+1*65536,rest);
						askf.write(p);
						sc.write(buff);
						p.clear();
						
					}else{
						ProtocolAskFrag askf = new ProtocolAskFrag(file, fileSize,0,(int)fileSize);
						askf.write(p);
						sc.write(buff);
						p.clear();
					}
					
				}

			} catch (IOException | ProtocolException   e) {
				e.printStackTrace();

			}

		}

	}

}
