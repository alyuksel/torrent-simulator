package Serialization;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class ProtocolBuffer {
	private ByteBuffer buffer;
	private Charset cs = Charset.forName("UTF-8");
	
	public ProtocolBuffer(ByteBuffer buffer){
		this.buffer = buffer;
	}
	public void putString(String s){
		ByteBuffer bb = cs.encode(s);
		buffer.putInt(bb.remaining());
		buffer.put(bb);
	}
	public void put(byte b){
		buffer.put(b);
	}
	public byte get(){
		return buffer.get();
	}
	public void putInt(int i){
		buffer.putInt(i);
	}
	
	public void putLong(long l){
		buffer.putLong(l);
	}
	
	public int readInt(){
		return buffer.getInt();
	}
	
	public long readLong(){
		return buffer.getLong();
	}
	
	public void readBolb(byte [] dst){
		buffer.get(dst);
	}
	
	public void putBlob(byte[] src){
		buffer.put(src);
	}
	
	public String readString(){
		int i = buffer.getInt();
		int limit = buffer.limit();
		System.out.println();
		buffer.limit(buffer.position()+i);
		CharBuffer cb = cs.decode(buffer);
		buffer.limit(limit);
		return cb.toString();
	}
	
	
	public void flip(){
		buffer.flip();
	}
	public void clear(){
		buffer.clear();
	}
	public ByteBuffer getBuffer(){
		return this.buffer;
	}
	
	public static void main(String[] args) {
		ProtocolBuffer p = new ProtocolBuffer(ByteBuffer.allocate(512));
		p.putInt(0);
		p.putString("Salut");
		p.putInt(5);
		p.flip();
		System.out.println(p.readInt());
		System.out.println(p.readString());
		System.out.println(p.readInt());
		
		
	}
	
}
