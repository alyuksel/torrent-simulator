package Serialization;

import java.util.HashMap;
import java.util.Map;

import communication.ClientHandler;
import communication.Pair;

public class ProtocolSendFiles extends Protocole {
	private Map<String,Long> mapFiles;
	private final byte id = 5;
	public ProtocolSendFiles(Map<String, Long> mapFiles) {
		this.mapFiles = mapFiles;
	}
	public ProtocolSendFiles() {
		mapFiles = new HashMap<>();
		//mapFiles.put("image.jpg", new File("send/image.jpg").length());
	}
	@Override
	public void read(ProtocolBuffer p) {
		int size = p.readInt();
		for(int i=0;i<size;i++){
			String name = p.readString();
			long fileSize = p.readLong();
			mapFiles.put(name,Long.valueOf(fileSize));
		}
	}

	@Override
	public void write(ProtocolBuffer p) {
			p.put(id);
			p.putInt(mapFiles.size());
			for (String k : mapFiles.keySet()) {
				p.putString(k);
				p.putLong(mapFiles.get(k));
			}
			p.flip();
	}
	@Override
	public String toString() {
		return mapFiles.toString();
	}
	@Override
	public void toDo(ClientHandler ch,Pair pair) {
		StringBuilder str = new StringBuilder("<html>");
		mapFiles.forEach((s,l)->{
			str.append(s+" = "+l+"<br>");
		});
		str.append("</html>");
		pair.message.setText(str.toString());
		pair.message.uptdate();
	}
	
	public void setMap(Map<String,Long> map){
		this.mapFiles = map;
	}
	
	public Map<String,Long> getMap(){
		return this.mapFiles;
	}
}
