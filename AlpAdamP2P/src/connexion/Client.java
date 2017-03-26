package connexion;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public abstract class Client {
	protected String name;
	protected int port;
	protected String hostName;
	protected ByteBuffer buffer = ByteBuffer.allocate(512);
	protected Charset cs = Charset.forName("UTF-8");
	
	public Client(String name, String hostName, int port){
		this.name = name;
		this.hostName = hostName;
		this.port = port;
	}
	public abstract void start() throws IOException;
	

}
