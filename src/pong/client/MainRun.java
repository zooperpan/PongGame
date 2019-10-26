package pong.client;


import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import pong.client.gui.ClientFrame;

public class MainRun {

	public MainRun() {
		//MainFrame frame = new MainFrame();
		ClientFrame frame = new ClientFrame();
        frame.setPreferredSize(new Dimension(800, 650));
		frame.setSize(new Dimension(800, 650));
        // Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2,
                          (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                	UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                new MainRun();
            }
        });
	}
}
