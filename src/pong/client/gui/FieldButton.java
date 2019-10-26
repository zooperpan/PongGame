package pong.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.AbstractButton;

import pong.com.FieldCharacteristics;

public class FieldButton extends AbstractButton {

	private static final long serialVersionUID 	= 1L;
	private static final Color OPENED_COLOR 	= new Color(10, 255, 10);
	private static final Color CLOSED_COLOR 	= new Color(255, 10, 10);
	private static final Color SELECTED_COLOR 	= new Color(10, 10, 255);
	private String user1Name;
	private String user2Name;
	private String fieldName;
	private boolean opened;
	private boolean selected;
	private int width;
	private ActionListener actionListener;
	
	public FieldButton (FieldCharacteristics field, int width) {
		super();
		this.addMouseListener(new FieldButtonMouseAdapter());
		this.width = width;
		user1Name = field.getUser1Name();
		user2Name = field.getUser2Name();
		fieldName = field.getName();
		opened = field.isOpened();
		setSelected(false);
		setSize(new Dimension(width, 70));
		setMaximumSize(new Dimension(width, 70));
		setMinimumSize(new Dimension(width, 70));
		setPreferredSize(new Dimension(width, 70));
	}
	
	public void paint (Graphics g) {
		super.paint(g);
		Graphics2D g2D = (Graphics2D)g.create();
		Rectangle2D e;
		if (isSelected()) {
			e = new Rectangle2D.Double(0, 0, width, 70);
			g2D.setColor(Color.BLACK);
			g2D.fill(e);
			g2D.setColor(SELECTED_COLOR);
			e = new Rectangle2D.Double(2, 2, width - 4, 66);
			g2D.fill(e);
			g2D.setColor(Color.WHITE);
			g2D.setFont(new Font("Times New Roman", Font.BOLD, 16));
			g2D.drawString(fieldName, 10, 20);
			g2D.setFont(new Font("Times New Roman", Font.PLAIN, 14));
			g2D.drawString("Player1: " + user1Name, 10, 40);
			if (user2Name != null) {
				g2D.drawString("Player2: " + user2Name, 10, 60);
			} else {
				g2D.drawString("Player2: -", 10, 60);
			}
		} else if (opened) {
			e = new Rectangle2D.Double(0, 0, width, 70);
			g2D.setColor(Color.BLACK);
			g2D.fill(e);
			g2D.setColor(OPENED_COLOR);
			e = new Rectangle2D.Double(2, 2, width - 4, 66);
			g2D.fill(e);
			g2D.setColor(Color.BLACK);
			g2D.setFont(new Font("Times New Roman", Font.BOLD, 16));
			g2D.drawString(fieldName, 10, 20);
			g2D.setFont(new Font("Times New Roman", Font.PLAIN, 14));
			g2D.drawString("Player1: " + user1Name, 10, 40);
			if (user2Name != null) {
				g2D.drawString("Player2: " + user2Name, 10, 60);
			} else {
				g2D.drawString("Player2: -", 10, 60);
			}
		} else {
			e = new Rectangle2D.Double(0, 0, width, 70);
			g2D.setColor(Color.BLACK);
			g2D.fill(e);
			g2D.setColor(CLOSED_COLOR);
			e = new Rectangle2D.Double(2, 2, width - 4, 66);
			g2D.fill(e);
			g2D.setColor(Color.BLACK);
			g2D.setFont(new Font("Times New Roman", Font.BOLD, 16));
			g2D.drawString(fieldName, 10, 20);
			g2D.setFont(new Font("Times New Roman", Font.PLAIN, 14));
			g2D.drawString("Player1: " + user1Name, 10, 40);
			if (user2Name != null) {
				g2D.drawString("Player2: " + user2Name, 10, 60);
			} else {
				g2D.drawString("Player2: -", 10, 60);
			}
		}
		g2D.dispose();
	}
	
	public void setSelected(boolean selected) {
		this.selected  = selected;
	}
	
	public boolean isSelected () {
		return selected;
	}
	
	public void doClick () {
		ActionEvent e = new ActionEvent(this, 1001, "click");
		actionListener.actionPerformed(e);
	}
	
	public void addActionListener (ActionListener l) {
		actionListener = l;
	}
	
	public boolean isOpened () {
		return opened;
	}
	
	private class FieldButtonMouseAdapter extends MouseAdapter {
		
		public FieldButtonMouseAdapter () {
			super();
		}
		
		public void mouseReleased (MouseEvent me) {
			doClick();
		}
	}
}
