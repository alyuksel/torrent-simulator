package Serialization;

import communication.ClientHandler;
import communication.Message;
import communication.Pair;

public class ProtocolMessage extends Protocole {
	private String s;
	private final byte id = 0;
	
	public ProtocolMessage(String s) {
		this.s = s;
	}
	public ProtocolMessage() {
		s= "Reference Implementation";
	}
	
	@Override
	public void read(ProtocolBuffer p) {
		this.s = p.readString();
		

	}
	@Override
	public void write(ProtocolBuffer p) {
		p.put(id);
		p.putString(s);
		p.flip();
	}
	
	@Override
	public String toString() {
		return this.s;
	}
	
	@Override
	public void toDo(ClientHandler ch, Pair pair) {
		pair.message.setText(s);
		pair.message.uptdate();
	}
	public void setText(String text) {
		this.s = text;
	}

}
