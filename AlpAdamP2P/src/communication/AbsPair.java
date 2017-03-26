package communication;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class AbsPair {
	protected String name;
	public int port;
	protected String hostName;
	protected ByteBuffer buffer = ByteBuffer.allocate(512);
	protected Charset cs = Charset.forName("UTF-8");
	
	public AbsPair(String name, String hostName, int p){
		this.name = name;
		this.hostName = hostName;
		port = p;
	}
}
