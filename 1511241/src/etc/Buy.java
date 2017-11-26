package etc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import frames.PlayerFrame;
import main.Provider;

public class Buy {
	private static int maxCredit = Provider.initialAmount / 2;
	private BufferedImage image = null;
	final public String img_path = System.getProperty("user.dir") + "/src/images/";	// Images path
	
	public Buy() {
        try 
        {
            image = ImageIO.read(new File(img_path + "b" + ".gif"));
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

	public static void getCredit(int wantedCredit) {
		int player = Turn.currentPlayerTurn();
		PlayerFrame p = (PlayerFrame) Provider.framesList.get(player);
		p.setMoney(p.getMoney() + wantedCredit);
	}

	public BufferedImage getImage() {
		return image;
	}

	/**
	 * @return the maxCredit
	 */
	public static int getMaxCredit() {
		return maxCredit;
	}

	/**
	 * @param maxCredit the maxCredit to set
	 */
	public static void setMaxCredit(int maxCredit) {
		Buy.maxCredit = maxCredit;
	}
	
}
