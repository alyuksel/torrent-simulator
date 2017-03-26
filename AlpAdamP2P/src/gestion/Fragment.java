package gestion;

import java.nio.ByteBuffer;

public class Fragment {
	public String name;
	public long fileSize;
	public int fragSize;
	public long position;
	public int headerSize;
	public byte[] blob;
	
	public Fragment(String name, long fileSize, int fragSize, long position, int headerSize) {
		this.name = name;
		this.fileSize = fileSize;
		this.fragSize = fragSize;
		this.position = position;
		this.headerSize = headerSize;
		this.blob = new byte[fragSize];
	}
	
	public void fillBlob(ByteBuffer b){
		System.out.println("header = "+ headerSize + " length = "+ blob.length);
		b.position(headerSize);
		b.get(blob, 0, blob.length);
		
	}
}
