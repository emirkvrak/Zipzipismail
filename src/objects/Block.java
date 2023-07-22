
package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import gamestate.GameState;
import resources.Images;


public class Block extends Rectangle
{
    private static final long serialVersionUID = 1L;

    public static  final int blockSize = 40;
    private int id;
    
    
    public Block(int x, int y, int id)
    {
        setBounds(x, y, blockSize, blockSize);
        this.id = id;
    }
    
    public void tick()
    {
      
    }
    
    public void draw(Graphics g)
    {
        g.setColor(Color.BLACK);
        //g.drawRect(x - (int)GameState.xOffset, y - (int)GameState.yOffset, width, height);
        if (id == 1) {
            g.drawImage(Images.blocks[id - 1], x - (int)GameState.xOffset, y - (int)GameState.yOffset, width, height, null);
            
        	
        }
        if (id == 9) {
            g.drawImage(Images.diken[id - 1], x - (int)GameState.xOffset, y - (int)GameState.yOffset, width, height, null);
            
        	
        }
        if (id == 7) {
            g.drawImage(Images.pota[id - 1], x - (int)GameState.xOffset, y - (int)GameState.yOffset, width, height, null);
            
        	
        }
        
        
        
    }

    	//getters and setters
    public void setID(int id) {
    	this.id = id;
    }
    
    public int getID() {
    	return id;
    }
}

