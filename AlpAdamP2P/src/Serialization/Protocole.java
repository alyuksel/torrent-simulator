package Serialization;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import communication.ClientHandler;
import communication.Pair;

public abstract class Protocole{
	private interface ProtocoleFactory{
		public Protocole generate(); 
		
	}
	
	public abstract void read(ProtocolBuffer p);
	public abstract void write(ProtocolBuffer p);
	public Object option(){
		return this;
	}
	public void toDo(ClientHandler ch, Pair pair) {}
	private static final Map<Integer,ProtocoleFactory> factoryMap = new HashMap<Integer,ProtocoleFactory>(){
		private static final long serialVersionUID = -2760327665218036980L;		
		{
			put(0 , ()->new ProtocolMessage());
			put(1 , ()-> new ProtocolPort());
			put(2 , ()-> new ProtocolAskPairs());
			put(3 , ()-> new ProtocolSendPairs());
			put(4 , ()-> new ProtocolAskFiles());
			put(5 , ()-> new ProtocolSendFiles());
			put(6 , ()-> new ProtocolAskFrag());
			put(7 , ()-> new ProtocolSendFrag());
		}
	};
	public static Protocole createProtocole(int i) throws ProtocolException {
	    ProtocoleFactory protocol = factoryMap.get(i);
	    if (protocol == null) {
	        throw new ProtocolException();
	    }
	    return protocol.generate();
	}
	
	
	
}
