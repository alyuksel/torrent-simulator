package gestion;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.StandardOpenOption;

import Serialization.ProtocolBuffer;
import Serialization.ProtocolSendFrag;

public class FileHandler {
	private ProtocolSendFrag psf ;
	private ProtocolBuffer pb ;
	private int numberOfFrag;
	private long fileSize;
	private ByteBuffer data;
	private FileChannel fc ;
	
	
	public FileHandler(ProtocolBuffer pb) {
		this.pb = pb;
	}
	
	public void uploadAFile(String name,long offset,int size){
		
		try {
			this.fc = FileChannel.open(FileSystems.getDefault().getPath("send/"+name),StandardOpenOption.READ);
			fileSize = fc.size();
			data = ByteBuffer.allocate(size);
			fc.read(data, offset);
			fc.close();
			psf = new ProtocolSendFrag(name, fileSize, offset,size,data.array());
			psf.write(pb);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	
	
	public void bigFailHandle(String name, long position,long size, ByteBuffer blob){
		try {
			this.fc = FileChannel.open(FileSystems.getDefault().getPath("receive/"+name),StandardOpenOption.WRITE,StandardOpenOption.CREATE);
			this.fc.truncate(size);
			fc.position(position);
			fc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	public ByteBuffer getData(){
		return data;
	}
	public int getFileSize(){
		return numberOfFrag;
	}
	
}
