package frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.io.File;
import java.io.FileWriter;

import components.GameImage;
import components.GameImagePanel;
import etc.Chip;

public class BankFrame extends JFrame {
	private Chip[] chips = new Chip[6];
	
	{
		chips[0] = new Chip(1);
		chips[1] = new Chip(5);
		chips[2] = new Chip(10);
		chips[3] = new Chip(20);
		chips[4] = new Chip(50);
		chips[5] = new Chip(100);
	}
	
	public BankFrame(String name, BufferedImage bankBackground) {
		super(name);
		
		setLayout(new BorderLayout()); // Organize components
		
		JButton bEndGame = new JButton("End Game");
		JButton bNewRound = new JButton("New Round");
		JButton bSave = new JButton("Save");
		
		// EndGame button action listener
		bEndGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				System.exit(0);				
			}
		});
		
		// Save button action listener
		bSave.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent actionEvent) {
				String s = "testezão do sucesso";
				final JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
			    int retrival = fc.showSaveDialog(null);
			    if (retrival == JFileChooser.APPROVE_OPTION) {
			        try {
			            FileWriter fw = new FileWriter(fc.getSelectedFile() + ".txt");
			            fw.write(s);	// TODO: Change s for file containing game info
			            fw.close();
			        } catch (Exception ex) {
			            ex.printStackTrace();
			        }
			    }
			}
			
		});
		
		setSize(bankBackground.getWidth(), bankBackground.getHeight());
		setContentPane(new GameImage(bankBackground));
		
		// Add Listener
        addWindowListener(new WindowAdapter() {
           public void windowClosing(WindowEvent windowEvent){
              System.exit(0);
           }        
        });
        
        JPanel pButtons = new JPanel();
        // Add buttons
		pButtons.add(bEndGame);
		pButtons.add(bNewRound);
		pButtons.add(bSave);
		
		// Place buttons under image
		setLayout(new BorderLayout());
		

		// Draw chip images
		JPanel pChips = new JPanel();
		pChips.setLayout(new GridLayout(1, 6));
		pChips.setOpaque(false);
		for(int i = 0; i < chips.length; i++) {
			Icon icon = new ImageIcon(chips[i].getImage());
			JLabel lb = new JLabel(icon);
			pChips.add(lb);
		}
		
		add(pChips, BorderLayout.SOUTH);
		add(pButtons, BorderLayout.NORTH);
        // Allow us to see the frame
        setVisible(true);
        
        // Makes the frame pop up centered
        setLocationRelativeTo(null);
	}
}
