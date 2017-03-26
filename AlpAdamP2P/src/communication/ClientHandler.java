package communication;

import gestion.Fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Serialization.ProtocolAskFrag;
import Serialization.ProtocolBuffer;

public class ClientHandler {
	//Map<Integer,Fragment> fragments = new HashMap<Integer, Fragment>();
	ArrayList<Fragment> frags = new ArrayList<Fragment>();
	public Fragment curFrag;
	private ProtocolBuffer protoBuffer;
	public boolean isWriting = false;
	public boolean isDownloading = false;
	private int port;
	private String hostName;
	private SocketChannel socket;
	public boolean isAskingFile = false;

	public ClientHandler(int port, String hostName) {
		protoBuffer = new ProtocolBuffer(ByteBuffer.allocate(65536 + 1024));
		this.port = port;
		this.hostName = hostName;
	}

	public SocketChannel channel(){
		return socket;
	}

	public ProtocolBuffer getProtoBuffer() {
		return protoBuffer;
	}


	public int getPort() {
		return port;
	}

	public String getHostName() {
		return hostName;
	}

	public void setSocket(SocketChannel socket) {
		this.socket = socket;
	}

	public void write(){
		try {
			//System.out.println(protoBuffer.getBuffer().array()[0]+protoBuffer.getBuffer().array()[1]);
			socket.write(protoBuffer.getBuffer());
			isWriting=true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int read(){
		int val =-1;
		try {
			val = socket.read(protoBuffer.getBuffer());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return val;
	}

	public void clearBuff() {
		protoBuffer.getBuffer().clear();
	}
	public void nextFrag(){
		long current = (curFrag.position+curFrag.fragSize); 
		if(current<curFrag.fileSize){
			ProtocolAskFrag askf =null;
			if((current+65536)>curFrag.fileSize)
				askf = new ProtocolAskFrag(curFrag.name, curFrag.fileSize,current,(int) (curFrag.fileSize-current));
			else
				askf = new ProtocolAskFrag(curFrag.name, curFrag.fileSize,current,65536);
			protoBuffer.clear();
			askf.write(protoBuffer);
			isWriting = true;
		}
		else {
			try {
				FileOutputStream out = new FileOutputStream(curFrag.name);

				for(Fragment f : frags){
					out.write(f.blob);
				}
				out.close();
			} catch ( IOException e) {

				e.printStackTrace();
			}

		}
	}

	public void flip() {
		protoBuffer.flip();
	}


	public void download() {
		int lu = protoBuffer.getBuffer().position() - curFrag.headerSize;
		System.out.println(lu);
		if(lu >= curFrag.fragSize ){
			System.out.println("j'ai lu jusqu a : "+ protoBuffer.getBuffer().position() );
			protoBuffer.getBuffer().limit(protoBuffer.getBuffer().position());
			int rest = lu - curFrag.fragSize;
			Fragment frag = new Fragment(curFrag.name, curFrag.fileSize, curFrag.fragSize, curFrag.position, curFrag.headerSize);
			frag.fillBlob(protoBuffer.getBuffer());
			//fragments.put((int) curFrag.position, frag);
			frags.add(frag);
			if(rest >0 ){
				protoBuffer.getBuffer().compact();
				System.out.println("il restais : "+ protoBuffer.getBuffer().position());
			}
			isDownloading = false;
			nextFrag();
		}
	}

	public void initFrag(String file, long fileSize, long position,
			int fragSize, int headerSize) {
		curFrag = new Fragment(file, fileSize, fragSize, position, headerSize);
	}
}

