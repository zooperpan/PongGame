package pong.client.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pong.client.conn.ConnectionHandler;
import pong.client.gui.FieldButton;
import pong.client.gui.FieldButtonGroup;
import pong.client.gui.GUIStatic;
import pong.client.gui.MyJButton;
import pong.com.FieldCharacteristics;
import pong.com.command.CreateFieldCommand;
import pong.com.command.JoinFieldCommand;

public class FieldsDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID 	= 1L;
	private static final Color GREY_COLOR 		= new Color(51, 51, 51);
	private FieldCharacteristics fields[];
	private ConnectionHandler connectionHandler;
	private FieldButtonGroup fieldButtonGroup;
	private MyJButton joinButton;
	private boolean connected 					= false;
	private boolean createORjoin 				= false; // true for create, false for join
	private String userName;
	private String opponentName;
	
	public FieldsDialog (ConnectionHandler connectionHandler, FieldCharacteristics fields[], String userName) {
		super();
		this.connectionHandler = connectionHandler;
		this.fields = fields;
		this.userName = userName;
		try {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            jbInit();
            setMaximumSize(new Dimension (434, 250));
			setPreferredSize(new Dimension (434, 250));
			pack();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
	}
	
	private void jbInit () throws Exception {
		this.setResizable(false);
		setTitle("Active Games");
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(new Color(51, 51, 51));
		contentPane.add(createCenterPanel(), BorderLayout.CENTER);
		contentPane.add(createEastPanel(), BorderLayout.EAST);
		
	}
	
	private JPanel createEastPanel () throws Exception {
		
		JPanel eastPanel = new JPanel(new GridLayout(2, 1));
		eastPanel.setBackground(GREY_COLOR);
		BufferedImage createImage = null;
		BufferedImage joinImage = null;;
		createImage = ImageIO.read(this.getClass().getResource("/pong/client/gui/images/create.png"));
		joinImage =  ImageIO.read(this.getClass().getResource("/pong/client/gui/images/join.png"));
		MyJButton createButton = new MyJButton(createImage);
		createButton.setActionCommand("create");
		createButton.addActionListener(this);
		createButton.setToolTipText("Create a new game");
		eastPanel.add(createButton);
		joinButton = new MyJButton(joinImage);
		joinButton.setActionCommand("join");
		joinButton.addActionListener(this);
		joinButton.setEnabled(false);
		joinButton.setToolTipText("Join an existing game");
		eastPanel.add(joinButton);
		
		return eastPanel;
	}
	
	private JScrollPane createCenterPanel () {
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(GREY_COLOR);
		JScrollPane scrollPane  = new JScrollPane(centerPanel);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		if (fields != null) {
			fieldButtonGroup = new FieldButtonGroup();
			for (int i = 0; i < fields.length; i++) {
				FieldButton fb = new FieldButton(fields[i], 300);
				fb.addActionListener(this);
				centerPanel.add(fb);
				fieldButtonGroup.addFieldButton(fb);
			}
			centerPanel.setMaximumSize(new Dimension (300, 70 * fields.length));
			centerPanel.setPreferredSize(new Dimension (300, 70 * fields.length));
			centerPanel.paintAll(centerPanel.getGraphics());
		} else {
			centerPanel.setMaximumSize(new Dimension (300, 200));
			centerPanel.setMinimumSize(new Dimension(300, 200));
			centerPanel.setPreferredSize(new Dimension(300, 200));
		}
		return scrollPane;
	}
	

	public void actionPerformed(ActionEvent e) {
		if ("create".equals(e.getActionCommand())) {
			CreateFieldDialog cfd = new CreateFieldDialog(fields);
			// Show the "Create Game" dialog
			GUIStatic.setDialogCenterLocation(cfd);
			cfd.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
			cfd.setVisible(true);
			if (!cfd.getFieldName().equals("")) {
				connectionHandler.sendUDPCommand(new CreateFieldCommand(cfd.getFieldName()));
				createORjoin = true;
				connected = true;
			}
			dispose();
		} else if ("join".equals(e.getActionCommand())) {
			// Find the game that the user wants to join
			for (int i = 0; i < fieldButtonGroup.getSize(); i++) {
				if (fieldButtonGroup.get(i).isSelected()) {
					connectionHandler.sendUDPCommand(new JoinFieldCommand(fields[i].getName(), userName));
					opponentName = fields[i].getUser1Name();
				}
			}
			createORjoin = false;
			connected = true;
			dispose();
		} else if (e.getSource() instanceof FieldButton) {
			if (((FieldButton) e.getSource()).isOpened()) {
				joinButton.setEnabled(true);
				fieldButtonGroup.buttonSelected((FieldButton) e.getSource());
			}
		}
	}

	public boolean isConnected () {
		return connected;
	}
	
	public boolean getCreateORjoin () {
		return createORjoin;
	}

	public String getOpponentName() {
		return opponentName;
	}
}
