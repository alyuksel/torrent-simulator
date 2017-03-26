package Interface;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import communication.Pair;

public class PairView extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;
	private Pair pair;
	private DefaultTableModel model = new DefaultTableModel();
	
	public PairView(Pair pair) {
		this.pair = pair;
		this.pair.clients.addObserver(this);
		this.setSize(400,200);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		model.addColumn("adresse IP");model.addColumn("Port");model.addColumn("connected");
		this.add(new JLabel("Liste des Pairs"));
		this.add(new JScrollPane(new JTable(model)));
		this.setVisible(true);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		model.setRowCount(0);
		Map<String,SocketChannel> sockets = pair.clients.getClientsSock();
		Map<String, Integer> cl = pair.clients.getClients();
		cl.forEach((k,v)->{
			Boolean state = false;
			if(sockets.containsKey(k))state = true;
			model.addRow(new Object[]{k,v,state});
		});
	}
}
