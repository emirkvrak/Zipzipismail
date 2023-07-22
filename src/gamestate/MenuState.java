
package gamestate;

import gamestate.Level1State;
import gamestate.GameStateManager;
import gamestate.GameState;
import main.GamePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;


public class MenuState extends GameState
{
    private String[] options = {"1. Bölüm", "2. Bölüm", "    Quit"};
    
    private int currentSelection = 0;
    
    public MenuState(GameStateManager gsm)
    {
        super(gsm);
    }

    public void init() {}

    public void tick() {}

    public void draw(Graphics g) {
        
        //g.setColor(new Color(100 , 150, 200));
        //g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        
    	// arka plan resim �izimi
    	
        int baslangicX = 0;
        int baslangicY = 0;
    
        int bitisX = GamePanel.WIDTH;
        int bitisY = GamePanel.HEIGHT;
        
        Image image = new ImageIcon("res/Resim/zigresim.png").getImage();
        
        g.drawImage(image, baslangicX, baslangicY, bitisX, bitisY, null);
        
        for (int i = 0; i < options.length; i++) {
            if (i == currentSelection) {
                g.setColor(Color.ORANGE);
            }else{
                g.setColor(Color.BLACK);
            }
            
            //g.drawLine(GamePanel.WIDTH / 2, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT);
            g.setFont(new Font("Arial", Font.PLAIN, 45));
            g.drawString(options[i], GamePanel.WIDTH/2 - 100, i * 75 + 175);
        }
    }

    public void keyPressed(int k) {
    
        if (k == KeyEvent.VK_DOWN) {
            currentSelection++;
            if (currentSelection >= options.length) {
                currentSelection = 0;
            }
        }else if (k == KeyEvent.VK_UP){
            currentSelection--;
            if (currentSelection < 0) {
                currentSelection = options.length - 1;
            }
        }
        if (k == KeyEvent.VK_ENTER) {
        	/*bolumbir*/
            if (currentSelection == 0) {
                gsm.states.push(new Level1State(gsm));
            }
            /*bolumiki*/
            else if (currentSelection == 1) {
            	gsm.states.push(new Level2State(gsm));
            }
            /*quit*/
            else if (currentSelection == 2) {
                System.exit(0);
            }
        }
        
    }

    public void keyReleased(int k) {}
    
    
    
}
