package pong.client.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AbilityDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID 	= 1L;
	private static final Color GREY_COLOR 		= new Color(51, 51, 51);
	private JButton nextButton					= null;
	private int numOfSelection 					= 0;
	private JLabel leftLabel					= new JLabel("<html><CENTER>Left Arrow</CENTER></html>");
	private JLabel rightLabel					= new JLabel("<html><CENTER>Right Arrow</CENTER></html>");
	private String [] selections				= new String[2];
	
	public AbilityDialog () {
		super();
		setMaximumSize(new Dimension(605, 435));
		setPreferredSize(new Dimension(605, 435));
		try {
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            jbInit();
            pack();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
	}
	
	private void jbInit () throws Exception {
		this.setResizable(false);
		setTitle("Select abilities");
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(createCenterPanel(), BorderLayout.CENTER);
		contentPane.add(createSouthPanel(), BorderLayout.SOUTH);
	}
	
	private JScrollPane createCenterPanel () {
		JPanel centerPanel = new JPanel(new GridLayout(3, 3));
		JScrollPane scrollPane = new JScrollPane(centerPanel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		centerPanel.add(createAbilityButton("pistol", true, "Reload time: 10s"));
		centerPanel.add(createAbilityButton("bomb", true, "Reload time: 45s"));
		centerPanel.add(createAbilityButton("decoy", true, "Reload time: 60s"));
		centerPanel.add(createAbilityButton("antibomb", true, "Reload time: 40s"));
		centerPanel.add(createAbilityButton("vision", true, "Reload time: 20s"));
		centerPanel.add(createAbilityButton("target", true, "Reload time: 60s"));
		centerPanel.add(createAbilityButton("magnet", true, "Reload time: 20s"));
		centerPanel.add(createAbilityButton("fat", true, "Reload time: 20s"));
		centerPanel.add(createAbilityButton("triple", true, "Reload time: 20s"));
		return scrollPane;
	}
	
	private JButton createAbilityButton (String abilityName, boolean enabled, String toolTip) {
		ImageIcon image = new ImageIcon(this.getClass().getResource("/pong/client/gui/images/" + abilityName + "L.png"));
		JButton button = new JButton(image);
		button.addActionListener(this);
		button.setActionCommand(abilityName);
		button.setEnabled(enabled);
		button.setToolTipText(toolTip);
		return button;
	}
	
	private JPanel createSouthPanel () {
		JPanel southPanel = new JPanel(new BorderLayout(10, 10));
		southPanel.setBackground(GREY_COLOR);
		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.setBackground(GREY_COLOR);
		nextButton = new JButton("Next");
		nextButton.setEnabled(false);
		nextButton.addActionListener(this);
		buttonPanel.add(nextButton);
		
		leftLabel.setBackground(Color.BLACK);
		rightLabel.setBackground(Color.BLACK);
		leftLabel.setMaximumSize(new Dimension(64, 64));
		rightLabel.setMaximumSize(new Dimension(64, 64));
		leftLabel.setPreferredSize(new Dimension(64, 64));
		rightLabel.setPreferredSize(new Dimension(64, 64));
		leftLabel.setForeground(Color.WHITE);
		rightLabel.setForeground(Color.WHITE);
		leftLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		rightLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		JPanel labelsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		labelsPanel.add(leftLabel);
		labelsPanel.add(rightLabel);
		labelsPanel.setBackground(GREY_COLOR);
		
		southPanel.add(labelsPanel, BorderLayout.CENTER);
		southPanel.add(buttonPanel, BorderLayout.EAST);
		return southPanel;
	}
	
	public void actionPerformed (ActionEvent e) {
		
		if (e.getSource() instanceof JButton) {
			JButton button = (JButton)e.getSource();
			if (button == nextButton) {
				dispose();
				return;
			}
			if (button.isSelected()) {
				button.setSelected(false);
				numOfSelection--;
				if (selections[0].equals(e.getActionCommand())) {
					selections[0] = selections[1];
					selections[1] = null;
					leftLabel.setIcon(rightLabel.getIcon());
					rightLabel.setIcon(null);
				}
				else {
					selections[1] = null;
					rightLabel.setIcon(null);
				}
				nextButton.setEnabled(false);
			} else if (numOfSelection < 2) {
				button.setSelected(true);
				selections[numOfSelection] = e.getActionCommand();
				ImageIcon icon = new ImageIcon(this.getClass().getResource("/pong/client/gui/images/" + e.getActionCommand() + "S.png"));
				if (numOfSelection == 0) leftLabel.setIcon(icon);
				else rightLabel.setIcon(icon);
				numOfSelection++;
			}
			if (numOfSelection == 2) nextButton.setEnabled(true);
		}
	}
	
	public String[] getAbilities () {
		return selections;
	}
}
