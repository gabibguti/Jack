
                                                                                                               
import java.awt.*;                                                                                             
                                                                                                               
import javax.swing.*;                                                                                          
                                                                                                               
public class GameImage extends JComponent {                                                                    
	private Image img;
	private String imgType;
    public GameImage(Image image, String type) {                                                                       
        this.img = image; 
        imgType = type;
    }                                                                                                          
    @Override                                                                                                  
    protected void paintComponent(Graphics g) {                                                                
        super.paintComponent(g);
    	g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
//        switch(imgType)
//        {
//        	case "original":
//        		g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
//        		break;
//        	case "card":
//        		g.drawImage(img, 0, 0, 60, 80, this);
//        		break;
//        	default:
//        		g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
//        		break;        		
//        }                        
    }                                                                                                          
}                                                                                                              
                                                                                                               