package Interface;

import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import communication.Pair;

public class MessageView extends JPanel implements Observer{
	private static final long serialVersionUID = 1L;
	private Pair pair;
	private JLabel text = new JLabel();

	public MessageView(Pair pair) {
		this.pair = pair;
		this.pair.message.addObserver(this);
		this.setSize(400,200);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(new JLabel("reponse : "));
		this.add(new JScrollPane(text));
		
		this.setVisible(true);
	}

	@Override
	public void update(Observable o, Object arg) {
		text.setText(pair.message.getText());
	}
	
	
}
