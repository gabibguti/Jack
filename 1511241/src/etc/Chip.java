package etc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Chip {
	private final int value;
	private BufferedImage image = null;
	final public String img_path = System.getProperty("user.dir") + "/src/images/ficha ";	// Images path
	
	public Chip(int value) {
        this.value = value;
        try 
        {
            image = ImageIO.read(new File(img_path + String.valueOf(this.value) + "$.png"));
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

	public int getValue() {
		return value;
	}

	public BufferedImage getImage() {
		return image;
	}
	
}
