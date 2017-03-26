package Interface;


import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Serialization.ProtocolAskFiles;
import Serialization.ProtocolAskFrag;
import Serialization.ProtocolAskPairs;
import Serialization.ProtocolMessage;
import Serialization.ProtocolPort;
import Serialization.Protocole;
import communication.ClientHandler;
import communication.Pair;

public class CommandeView extends JPanel {
	private static final long serialVersionUID = 1L;
	Pair pair;
	JTextField champ = new JTextField(10);
	JTextField champPort = new JTextField(10);
	JTextField champMess = new JTextField(15);
	JTextField champFragSize = new JTextField(5);
	JTextField champFragName = new JTextField(10);

	public CommandeView(Pair pair) {
		this.pair = pair;
		this.setSize(400,200);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(new JLabel("To : "));
		this.add(champ);
		JButton askpair = new JButton("AskPairs");
		askpair.addActionListener(e->{
			ClientHandler ch = (ClientHandler) this.pair.clients.getClientsSock().get(champ.getText()).keyFor(pair.selector).attachment();
			try {
				ProtocolAskPairs p = (ProtocolAskPairs) Protocole.createProtocole(2);
				synchronized (ch) {
					ch.clearBuff();
					p.write(ch.getProtoBuffer());
					ch.write();
					ch.clearBuff();
				}
			} catch (Exception e2) {}
		});
		JButton port = new JButton("SendPort");
		port.addActionListener(e->{
			ClientHandler ch = (ClientHandler) pair.clients.getClientsSock().get(champ.getText()).keyFor(pair.selector).attachment();
			try {
				ProtocolPort p = (ProtocolPort) Protocole.createProtocole(1);
				synchronized (ch) {
					ch.clearBuff();
					p.setPort(pair.port);
					p.write(ch.getProtoBuffer());
					ch.write();
					ch.clearBuff();
				}
			} catch (Exception e2) {}
		});
		this.add(askpair);
		this.add(port);
		this.add(new JLabel("Port : "));
		this.add(champPort);
		JButton connect = new JButton("Connect");
		connect.addActionListener(e->{
			new Thread(()->{
				try {
					this.pair.connectTo(champ.getText(), Integer.valueOf(champPort.getText()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}).start();

		});

		JButton askFiles = new JButton("AskFiles");
		askFiles.addActionListener(e->{
			ClientHandler ch = (ClientHandler) pair.clients.getClientsSock().get(champ.getText()).keyFor(pair.selector).attachment();
			try {
				ProtocolAskFiles p = (ProtocolAskFiles) Protocole.createProtocole(4);
				synchronized (ch) {
					ch.clearBuff();
					p.write(ch.getProtoBuffer());
					ch.write();
					ch.clearBuff();
				}
			} catch (Exception e2) {}
		});

		JButton sendMess = new JButton("Send");
		sendMess.addActionListener(e->{
			ClientHandler ch = (ClientHandler) pair.clients.getClientsSock().get(champ.getText()).keyFor(pair.selector).attachment();
			try {
				ProtocolMessage p = (ProtocolMessage) Protocole.createProtocole(0);
				synchronized (ch) {
					ch.clearBuff();
					p.setText(champMess.getText());
					p.write(ch.getProtoBuffer());
					ch.write();
					ch.clearBuff();
				}
			} catch (Exception e2) {}
		});

		JButton askFragButton = new JButton("AskFrag");
		askFragButton.addActionListener(e->{
			ClientHandler ch = (ClientHandler) pair.clients.getClientsSock().get(champ.getText()).keyFor(pair.selector).attachment();
			try {
				long size = Long.valueOf(champFragSize.getText());
				int fragsize=65536;
				if(size<65536)fragsize = (int) size;
				ProtocolAskFrag p = new ProtocolAskFrag(champFragName.getText(),size,Long.valueOf(0),fragsize);
				synchronized (ch) {
					ch.clearBuff();
					p.write(ch.getProtoBuffer());
					ch.write();
					ch.clearBuff();
				}
			} catch (Exception e2) {}

		});

		this.add(connect);
		this.add(askFiles);
		this.add(new JLabel("Send Message : ")); this.add(champMess);this.add(sendMess);
		this.add(new JLabel("File :")); this.add(champFragName); this.add(new JLabel("Size : "));this.add(champFragSize);
		this.add(askFragButton);
		this.setVisible(true);
	}



}
