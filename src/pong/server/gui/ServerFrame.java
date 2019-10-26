package pong.server.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pong.server.PongServer;

public class ServerFrame extends JFrame {

	private static final long serialVersionUID 	= 1L;
	private JTextArea serverTextArea 			= null;
	private PongServer server					= null;

	public ServerFrame () {
		super("Pong Server");
		try {
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing (WindowEvent evt) {
					exitApplication();
				}
			});
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit () throws Exception {
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setOpaque(true);
		this.setPreferredSize(new Dimension(800, 600));
		contentPane.setLayout(new BorderLayout());
		serverTextArea = new JTextArea(5, 40);
		serverTextArea.setEditable(false);
		JScrollPane scrollPane1 = new JScrollPane(serverTextArea);
		contentPane.add(scrollPane1, BorderLayout.CENTER);

		// Create and start the server
		server = new PongServer(serverTextArea);
		if (server.isListening()) server.start();
	}

	private void exitApplication () {
		// Stop the server (if started)
		if (server.isListening()) server.stopListening();
		try {
			server.join();
		} catch (InterruptedException e) {}
		System.exit(0);
	}
}
