package resources;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Images {

	public static BufferedImage [] blocks;
	public static BufferedImage [] diken;
	public static BufferedImage [] pota;
	
	public Images() {
		blocks = new BufferedImage[1];
		try {
		blocks[0] = ImageIO.read(getClass().getResourceAsStream("/Blocks/zigblockbir.jpeg"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		diken = new BufferedImage[9];
		try {
		diken[8] = ImageIO.read(getClass().getResourceAsStream("/Blocks/zigtestere.png"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		pota = new BufferedImage[7];
		try {
		pota[6] = ImageIO.read(getClass().getResourceAsStream("/Blocks/zigpota.png"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
}