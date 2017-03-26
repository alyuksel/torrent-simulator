package Serialization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import communication.ClientHandler;
import communication.Pair;

public class ProtocolSendPairs extends Protocole {
	private Map<Integer, ArrayList<String>> pairsMap;
	private Map<String, Integer> pairsMapSend;
	private final byte id = 3;

	public ProtocolSendPairs(Map<Integer, ArrayList<String>> pairsMap) {
		this.pairsMap = pairsMap;
	}

	public ProtocolSendPairs() {
		pairsMap = new HashMap<Integer, ArrayList<String>>();
	}

	@Override
	public void read(ProtocolBuffer p) {
		int size = p.readInt();
		for (int i = 0; i < size; i++) {
			int port = p.readInt();
			String addr = p.readString();
			if(pairsMap.containsKey(port)){
				pairsMap.get(port).add(addr);
			}
			else{
				ArrayList<String> s = new ArrayList<>();
				s.add(addr);
				pairsMap.put(port, s);
			}
		}
		toString();
	}

	@Override
	public void write(ProtocolBuffer p) {
		int size = pairsMapSend.size();
		p.put(id);
		p.putInt(size);
		for(Entry<String, Integer> kv : pairsMapSend.entrySet()){
			p.putInt(kv.getValue());	
			p.putString(kv.getKey());
		}
		p.flip();
	}

	@Override
	public void toDo(ClientHandler ch,Pair pair) {
		pairsMap.forEach((k,v)->v.forEach(s->{
			pair.clients.put(null, k, s);
		}));
		
	}

	@Override
	public String toString() {
		return pairsMap.toString();
	}

	public void setMap(Map<String, Integer> map){
		this.pairsMapSend = map;
	}


}
