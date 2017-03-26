package Interface;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Fenetre extends JFrame {
	private static final long serialVersionUID = -7599647515391338300L;
	JPanel panel = new JPanel();
	
	public Fenetre() {
		this.setTitle("BitTorrent");
		this.setSize(800,800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setLayout(new GridLayout(3, 2));
		this.add(panel);
	}
	
	public void addPane(JPanel pane){
		panel.add(pane);
	}

}
