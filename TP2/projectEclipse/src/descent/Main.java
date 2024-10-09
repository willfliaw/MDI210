package descent;

import javax.swing.JFrame;

import descent.view.View;

public class Main {
	public static void main(String[] arg) {
		JFrame window = new JFrame();
		window.setContentPane(new View());
		window.setLocation(300, 100);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}
}
