package frames.auxiliarframes;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import frames.player.Player;

public class InsuranceFrame {
	private JButton yes;
	private JButton no;
	private JFrame insuranceFrame;
	private JLabel text;
	private JPanel bottomPanel;
	private Player fatherFrame;
	
    public InsuranceFrame(JFrame frame) {
    	fatherFrame = (Player) frame;
		insuranceFrame = new JFrame("Insurance");
		insuranceFrame.setSize(400, 200);
		insuranceFrame.setLocationRelativeTo(frame);
		insuranceFrame.setLayout(new GridLayout(3, 1)); // Organize components
		
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		
		text = new JLabel("Do you want to buy an insurance? It costs half of your bet.", JLabel.CENTER);

		yes = new JButton("Yes");
		no = new JButton("No");
		
		yes.addActionListener(new ActionListener() { // Add ok listener event
			public void actionPerformed(ActionEvent actionEvent) {
				fatherFrame.setMoney(fatherFrame.getMoney() - fatherFrame.getBet()/2);
				fatherFrame.setInsured(true);
				insuranceFrame.dispose();
			}
		});
		
		no.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				insuranceFrame.dispose();
			}
		});
		
		bottomPanel.add(yes);
		bottomPanel.add(no);

		insuranceFrame.add(text);
		insuranceFrame.add(bottomPanel);
		
		insuranceFrame.setVisible(true);
    }
}