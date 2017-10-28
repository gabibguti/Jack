package components;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

// Not being used

public class GameImagePanel extends JPanel{
	private Image img;
    public GameImagePanel(Image image) {                                                                       
        this.img = image; 
    }                                                                                                          
    @Override                                                                                                  
    protected void paintComponent(Graphics g) {                                                                
        super.paintComponent(g);
    	g.drawImage(img, 0, 0, this);
    }
}
