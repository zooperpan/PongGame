package pong.client.gui;

import java.util.ArrayList;

public class FieldButtonGroup {

	private ArrayList<FieldButton> buttons = null;
	
	public FieldButtonGroup () {
		buttons = new ArrayList<FieldButton>();
	}
	
	public void addFieldButton (FieldButton b) {
		buttons.add(b);
	}
	
	public void buttonSelected (FieldButton b) {
		for (int i = 0; i < buttons.size(); i++) {
			if (buttons.get(i).isSelected()) {
				buttons.get(i).setSelected(false);
				buttons.get(i).repaint();
				break;
			}
		}
		b.setSelected(true);
		b.repaint();
	}
	
	public int getSize () {
		return buttons.size();
	}
	
	public FieldButton get (int index) {
		return buttons.get(index);
	}
}
