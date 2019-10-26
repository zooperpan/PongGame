package pong.client.gui.pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import pong.client.ability.AbilityFeature;

public class Bar extends MovingObject implements AbilityFeature {

	private static final long serialVersionUID	= 1L;
	public static final int STARTING_VELOCITY	= 10;
	private ArrayList<String> abilities			= null;
	
	public Bar (int width, int height, Color color, String name) {
		super(width, height, color, name);
		abilities = new ArrayList<String>();
	}

	public void paint(Graphics g) {
		
		Graphics2D g2 = (Graphics2D)g;
		if (hasAbility("antibomb")) {
			g2.setColor(Color.BLACK);
			Rectangle2D e2d = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
			g2.fill(e2d);
		} else {
			g2.setColor(this.getColor());
			Rectangle2D e2d = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
			g2.fill(e2d);
		}
		
	}
	
	public void reset () {
		
		setHeight(80);
		setWidth(10);
		super.reset();
		this.repaint();
	}

	@Override
	public void addActiveAbility(String ability) {
		abilities.add(ability);
	}

	@Override
	public ArrayList<String> getActiveAbilities() {
		return abilities;
	}
	
	@Override
	public boolean hasAbility (String ability) {
		for (int i = 0; i < abilities.size(); i++) {
			if (abilities.get(i).equals(ability)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void removeActiveAbility (String ability) {
		for (int i = 0; i < abilities.size(); i++) {
			if (abilities.get(i).equals(ability)) {
				abilities.remove(i);
				return;
			}
		}
	}
}
