package Serialization;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import communication.ClientHandler;
import communication.Pair;
import connexion.Download;
import gestion.FileHandler;
import gestion.Fragment;

public class ProtocolSendFrag extends Protocole {
	private final byte id = 7;
	private String file;
	private int headerSize;
	private long fileSize;
	private long position;
	private int fragSize;
	private byte[] blob ;
	private long lastPos;

	public ProtocolSendFrag(String file, long fileSize, long position, int fragSize, byte[] blob) {
		this.file = file;
		this.fileSize = fileSize;
		this.position = position;
		this.fragSize = fragSize;
		this.blob = blob;
	}
	public ProtocolSendFrag() {
		
	}
	

	@Override
	public void read(ProtocolBuffer p) {
		file = p.readString();
		//System.out.println(file);
		fileSize = p.readLong();
		//System.out.println(fileSize);
		position = p.readLong();
		//System.out.println(position);
		fragSize = p.readInt();
		headerSize = p.getBuffer().position();
		//System.out.println(fragSize);
		//System.out.println(p.getBuffer());
		blob = new byte[p.getBuffer().remaining()];
		//System.out.println("remaining : "+p.getBuffer().remaining());
		p.readBolb(blob);
		//lastPos = position + blob.length;
		//fh = new FileHandler(p);
		//ByteBuffer bb = ByteBuffer.allocate(blob.length);
		//bb.put(blob);
		//bb.flip();
		//bb.compact();
		//fh.bigFailHandle(file, position,fragSize, bb);
		//p.clear();
	}

	@Override
	public void write(ProtocolBuffer p) {
		p.put(id);
		p.putString(file);
		p.putLong(fileSize);
		p.putLong(position);
		p.putInt(fragSize);
		p.putBlob(blob);
		p.flip();
	}
	@Override
	public String toString() {
		String blb="";
		for(byte b : blob){
			blb = blb+b;
		}
		return "["+file+","+fileSize+","+position+","+fragSize+","+blb+"]";
	}
	
	public byte[] getBlob(){
		return blob;
	}
	@Override
	public Object option() {
		List<Object> frag = new ArrayList<>();
		frag.add(this.file);
		frag.add(this.position);
		frag.add(this.blob);
		frag.add(this.lastPos);
		frag.add(this.fragSize);
		return frag;
	}
	
	@Override
	public void toDo(ClientHandler ch,Pair pair) {
		ch.initFrag(file,fileSize,position,fragSize,headerSize);
		ch.isDownloading=true;
		ch.download();
	}
}
