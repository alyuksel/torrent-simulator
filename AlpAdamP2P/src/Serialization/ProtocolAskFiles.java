package Serialization;

import communication.ClientHandler;
import communication.Pair;

public class ProtocolAskFiles extends Protocole {
	private final byte id = 4;
	public ProtocolAskFiles() {
	}
	@Override
	public void read(ProtocolBuffer p) {
	}

	@Override
	public void write(ProtocolBuffer p) {
		p.put(id);
		p.flip();
	}
	@Override
	public String toString() {
		return "AskFiles";
	}
	@Override
	public void toDo(ClientHandler ch,Pair pair) {
		ch.clearBuff();
		try {
			ProtocolSendFiles p = (ProtocolSendFiles) createProtocole(5);
			p.setMap(pair.files);
			p.write(ch.getProtoBuffer());
			ch.isWriting=true;
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
	}
}
