package Serialization;

import communication.ClientHandler;
import communication.Pair;
import gestion.FileHandler;

public class ProtocolAskFrag extends Protocole {
	private final byte id = 6;
	private String file;
	private long fileSize;
	private long position;
	private int fragSize;
	
	public ProtocolAskFrag(String file,long fileSize,long position, int fragSize) {
		this.file = file;
		this.fileSize = fileSize;
		this.position = position;
		this.fragSize = fragSize;
	}
	public ProtocolAskFrag() {
	}
	@Override
	public void read(ProtocolBuffer p) {
		file = p.readString();
		fileSize = p.readLong();
		position = p.readLong();
		fragSize = p.readInt();
	}

	@Override
	public void write(ProtocolBuffer p) {
		p.put(id);
		p.putString(file);
		p.putLong(fileSize);
		p.putLong(position);
		p.putInt(fragSize);
		p.flip();
	}
	@Override
	public String toString() {
		return "["+file+","+fileSize +","+ position+","+fragSize+"]";
	}
	
	public String getName(){
		return this.file;
	}
	public long getFileSize(){
		return this.fileSize;
	}
	public long getOffset(){
		return this.position;
	}
	public int getFragSize(){
		return this.fragSize;
	}
	
	@Override
	public void toDo(ClientHandler ch,Pair pair) {
		ch.clearBuff();
		FileHandler fh = new FileHandler(ch.getProtoBuffer());
		fh.uploadAFile(getName(), getOffset(), getFragSize());
		ch.isWriting = true;
	}
	

}
