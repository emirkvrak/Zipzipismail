
package gamestate;

import gamestate.GameStateManager;
import main.GamePanel;
import gamestate.GameState;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import mapping.Map;
import entities.Player;
import objects.Block;


public class Level1State extends GameState
{

    private Player player;
    private Map map;

    public Level1State(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        player = new Player(30, 30);
        map = new Map("/Maps/map1.map");
        
        xOffset = -200;
        yOffset = -300;
    }

    public void tick() {
        player.tick(map.getBlocks(), map.getMovingBlocks());
        map.tick();
        //System.out.println(xOffset + "," + yOffset);
    }

    public void draw(Graphics g) {
    	
    	// arka plan resim çizimi
        int baslangicX = 0;
        int baslangicY = 0;
    
        int bitisX = GamePanel.WIDTH;
        int bitisY = GamePanel.HEIGHT;
        
        Image image = new ImageIcon("res/Resim/colbir.jpeg").getImage();
        
        g.drawImage(image, baslangicX, baslangicY, bitisX, bitisY, null);
        
        player.draw(g);
        map.draw(g);
    }

    public void keyPressed(int k) {
        player.keyPressed(k);
    }

    public void keyReleased(int k) {
        player.keyReleased(k);
    }
    
}
