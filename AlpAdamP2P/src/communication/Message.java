package communication;

import java.util.Observable;

public class Message  extends Observable {
	private String mess = "";
	
	public void setText(String s ){
		this.mess = s;
	}
	
	public String getText(){
		return mess;
	}
	
	public void uptdate(){
		setChanged();
		notifyObservers();
	}
}
