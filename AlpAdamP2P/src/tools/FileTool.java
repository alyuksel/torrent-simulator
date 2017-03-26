package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import communication.Pair;

public class FileTool {
	public static byte[] fileToByte(String s,Map<String,Long> files){
		byte[] tab = null;
		try {
			FileInputStream input = new FileInputStream(new File("send/"+s));
			tab = new byte[input.available()];
			input.read(tab);
			files.put(s, (long) tab.length);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tab;
	}
}
