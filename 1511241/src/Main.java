import javax.swing.*;

public class Main {
	public static void main (String [] args)
	{
		System.out.println("Blackjack");
		JFrame parent = new JFrame();
		JButton button = new JButton();
		JLabel label = new JLabel();
//		JLabel [] options = { new JLabel(), new JLabel(), new JLabel() };
//		options[0].setText("1");
//		options[1].setText("2");
//		options[2].setText("3");
		button.setText("First Button");
		label.setText("Welcome to BlackJack");
		parent.add(button);
		parent.add(label);
//		parent.add(options[0]);
//		parent.add(options[1]);
//		parent.add(options[3]);
		parent.setVisible(true);
		parent.setSize(500, 300);
		parent.setLocationRelativeTo(null);
	}
}
