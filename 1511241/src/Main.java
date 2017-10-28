import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import cards.*;

public class Main {
   private JFrame mainFrame;
   private JFrame bankFrame;
   private JFrame playerFrame;
   private JLabel headerLabel;
   private JLabel subheaderLabel;
   private JLabel statusLabel;
   private JPanel mainCP;
   private JPanel playerCP;
   private JButton [] options;
   private String [] actions;
   private int maxPlayers = 4;
   BufferedImage bankBackground = null;
   BufferedImage card = null;
   
   static public String img_path = System.getProperty("user.dir") + "/src/images/";	// Project path

   public Main(){
	  // Prepare Guided User Interface
      prepareMainFrame();
      // Get images
      try 
      {
          bankBackground = ImageIO.read(new File(img_path + "blackjack.png")); // eventually C:\\ImageTest\\pic2.jpg
          card = ImageIO.read(new File(img_path + "2c.gif"));
      } 
      catch (IOException e) 
      {
          e.printStackTrace();
      }
   }
   public static void main(String[] args){
	  // Main function
      Main Main = new Main();
      Main.showEvent();
   }
   private void prepareMainFrame(){
	  // Create Frame
      mainFrame = new JFrame("Blackjack");
      mainFrame.setSize(400,400);
      mainFrame.setLayout(new GridLayout(4, 1)); // Organize components

      // Create Labels
      headerLabel = new JLabel("",JLabel.CENTER );
      subheaderLabel = new JLabel("", JLabel.CENTER);
      statusLabel = new JLabel("",JLabel.CENTER);        
      statusLabel.setSize(350,100);
      
      // Add Listener
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });
      
      // Create Buttons Panel
      mainCP = new JPanel();
      mainCP.setLayout(new FlowLayout());

      // Add components to frame
      mainFrame.add(headerLabel);
      mainFrame.add(subheaderLabel);
      mainFrame.add(mainCP);
      mainFrame.add(statusLabel);
      
      // Allow us to see the frame
      mainFrame.setVisible(true);
      
      // Makes the frame pop up centered
      mainFrame.setLocationRelativeTo(null);
   }
   private void preparePlayerFrame(String playerNumber){
	  int centerX, centerY, gap; 
	  
	  centerX = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().x;
	  centerY = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().y;
	  gap = 4;
	  
	  // Create Frame
      playerFrame = new JFrame("Player" + " " + playerNumber);
      playerFrame.setSize(220, 300);
      playerFrame.setLayout(new GridLayout(2, 1)); // Organize components
      
      // Create Panel
      playerCP = new JPanel();
      playerCP.setLayout(new FlowLayout());
      JPanel cardsCP = new JPanel();
      
      // Create button
      JButton newCardButton = new JButton("New Card");
      
      // Add components to control panel
      playerCP.add(newCardButton);
      cardsCP.add(new GameImagePanel(card));
      cardsCP.add(new GameImagePanel(card));
      
      // Set command action to the button
      newCardButton.setActionCommand("New Card Request");

      // Add listeners to button
      newCardButton.addActionListener(new ButtonClickListener());
      
      // Add components to frame
      playerFrame.add(playerCP);
      playerFrame.add(cardsCP);
      
      // Allow us to see the frame
      playerFrame.setVisible(true);
      
      // Set players frame location
      switch(Integer.parseInt(playerNumber)){
      	case 1:
    	  playerFrame.setLocation(centerX - bankFrame.getWidth()/2 - gap - playerFrame.getWidth(), centerY - bankFrame.getHeight()/2); 
    	  break;
      	case 2:
      	  playerFrame.setLocation(centerX - bankFrame.getWidth()/2 - gap - playerFrame.getWidth(), centerY + bankFrame.getHeight()/2 - playerFrame.getHeight()); 
      	  break;
      	case 3:
    	  playerFrame.setLocation(centerX + bankFrame.getWidth()/2 + gap, centerY - bankFrame.getHeight()/2); 
    	  break;
      	case 4:
    	  playerFrame.setLocation(centerX + bankFrame.getWidth()/2 + gap, centerY + bankFrame.getHeight()/2 - playerFrame.getHeight());
    	  break;
      	default:
      		playerFrame.setLocationRelativeTo(null);	  
      }
   }
   private void showEvent(){
	  int player;
	   
	  // Set header label
      headerLabel.setText("Welcome to Blackjack"); 
      subheaderLabel.setText("Select the number of players:");
      
      // Create Actions
      actions = new String[maxPlayers];
      for(player = 0; player < maxPlayers; player++)
      {
    	actions[player] = String.valueOf(player + 1);  // Set actions[0] = 1 and so on
      }
      
      // Create buttons
      options = new JButton[maxPlayers];
      for(player = 0; player < maxPlayers; player++)
      {
    	options[player] = new JButton(String.valueOf(player + 1));
      }

      // Set command actions to the buttons
      for(player = 0; player < maxPlayers; player++)
      {
    	options[player].setActionCommand(actions[player]);
      }

      // Add listeners to buttons 
      for(JButton b: options)
      {
    	  b.addActionListener(new ButtonClickListener());
      }
      
      // Add buttons to buttons panel
      for(JButton b: options)
      {
    	  mainCP.add(b);
      }
      
      // Allow us to see the frame
      mainFrame.setVisible(true);  
   }
   private class ButtonClickListener implements ActionListener{
	   // Listener function
	   public void actionPerformed(ActionEvent e) {
		 int player, numberOfPlayers = 0;
         String command = e.getActionCommand();
         
         // Select number of players on Main Frame
         if(Arrays.asList(actions).contains(command)) // Search for command in actions array
         {
             // Close Main Frame
             mainFrame.dispose();
             
             // Create Bank Frame
             bankFrame = new JFrame("Bank");
             bankFrame.setSize(bankBackground.getWidth(), bankBackground.getHeight());
             bankFrame.setLayout(new GridLayout(4, 1)); // Organize components
             bankFrame.setContentPane(new GameImage(bankBackground, "original"));
             
             // Add Listener
             bankFrame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent windowEvent){
                   System.exit(0);
                }        
             });
             
             
             // Allow us to see the frame
             bankFrame.setVisible(true);
             
             // Makes the frame pop up centered
             bankFrame.setLocationRelativeTo(null);
        	 
        	 numberOfPlayers = Integer.parseInt(command);

        	 for(player = 0; player < numberOfPlayers; player++)
        	 {
        		// Create Player Frame
        		preparePlayerFrame(String.valueOf(player + 1));
        	 }
         }
         // Card Actions in Players Frame
         else if (command.equals("New Card Request"))
         {
        	 // Verify which player requested a new card
        	 Frame[] f = Frame.getFrames();
        	 Frame active = null;
        	 Component [] comps;
        	 for (int i = 0; i < f.length; i++) {
        	   if (f[i].isActive()) {
        	     active = f[i]; 
        	     break;
        	   }
        	 }
        	 comps = active.getComponents();
        	 for (Component comp : comps) {
        		 System.out.println(comp);
    	        	if (comp instanceof Container) {
    	        		JPanel g = (JPanel) comp;
    	        		g.add(new GameImagePanel(card));
    	        	 }
        	 }
//        	 JFrame frame = (JFrame) active;
//        	 frame.getContentPane().add(new GameImage(card, "card"));
        	 
//        	 active.add(new GameImage(card, "card"));
        	 SwingUtilities.updateComponentTreeUI(active);
         }
         else
         {
        	statusLabel.setText("Invalid Option"); 
         }
      }		
   }
}