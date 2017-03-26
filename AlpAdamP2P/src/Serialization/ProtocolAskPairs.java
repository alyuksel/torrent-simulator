package Serialization;

import communication.ClientHandler;
import communication.Pair;

public class ProtocolAskPairs extends Protocole{
	private final byte id = 2;
	public ProtocolAskPairs() {}
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
		return "AskPairs";
	}

	@Override
	public void toDo(ClientHandler ch,Pair pair) {
		try {
			ProtocolSendPairs p = (ProtocolSendPairs) Protocole.createProtocole(3);
			ch.clearBuff();
			p.setMap(pair.clients.getClients());
			p.write(ch.getProtoBuffer());
			ch.isWriting = true;
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
	}
}
