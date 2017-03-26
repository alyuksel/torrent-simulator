
package communication;

import java.io.IOException;

import javax.swing.JPanel;

import Interface.CommandeView;
import Interface.Fenetre;
import Interface.MessageView;
import Interface.PairView;

public class App {

	private static Pair p = null;
	
	public static void main(String[] args) throws IOException {
		p = new Pair("alpi", "localhost", 2023);
		//p = new Pair("alpi", "localhost", 2024);

		Fenetre fenetre = new Fenetre();
		fenetre.addPane(new PairView(p));
		fenetre.addPane(new CommandeView(p));
		fenetre.addPane(new MessageView(p));
		fenetre.addPane(new JPanel());
		fenetre.addPane(new JPanel());
		fenetre.addPane(new JPanel());
		fenetre.setVisible(true);



		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					p.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}
}



