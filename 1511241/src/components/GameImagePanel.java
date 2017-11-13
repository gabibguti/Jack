package components;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.lang.reflect.Array;
import java.security.KeyStore.Entry;
import java.util.Map;

import javax.swing.JPanel;

public class GameImagePanel extends JPanel{
	Map<Image, Point> imgs;
	public GameImagePanel(Map<Image, Point> images) {
		imgs = images;
	}                                                                                                          	
	@Override                                                                                                  
    protected void paintComponent(Graphics g) {                                                                
        super.paintComponent(g);
        for (java.util.Map.Entry<Image, Point> e : imgs.entrySet()) {
        	Image img = e.getKey();
        	Point imgPoint = e.getValue();
        	g.drawImage(img, imgPoint.x, imgPoint.y, this);
        }
    }
}
