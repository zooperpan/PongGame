package pong.client.gui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UserNameDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID 	= 1L;
	private String userName 					= "";
	private JTextField textField 				= null;
	private JButton okButton 					= new JButton("OK");
	
	public UserNameDialog () {
		super();
		try {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            jbInit();
            pack();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
	}
	
	private void jbInit () throws Exception {
		this.setResizable(false);
		setTitle("Set User Name");
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new BorderLayout());
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createTitledBorder("User Name"));
		textField = new JTextField(20);
		textField.setText("");
		textField.addKeyListener(new EnterKeyAdapter());
		centerPanel.add(textField, BorderLayout.CENTER);
		contentPane.add(centerPanel, BorderLayout.CENTER);
		contentPane.add(createSouthPanel(), BorderLayout.SOUTH);
		
	}
	
	private JPanel createSouthPanel () {
		JPanel southPanel = new JPanel(new BorderLayout());
		okButton.addActionListener(this);
		southPanel.add(okButton, BorderLayout.EAST);
		return southPanel;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okButton) {
			if (!textField.getText().equals("")) {
				userName = textField.getText();
				dispose();
			}
        } 
	}
	
	public String getUserName () {
		return userName;
	}
	
	private class EnterKeyAdapter extends KeyAdapter {
		
		public void keyReleased (KeyEvent ke) {
			if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
				if (!textField.getText().equals("")) {
					userName = textField.getText();
					dispose();
				}
			}
		}
	}
}
