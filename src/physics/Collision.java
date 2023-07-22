
package physics;

import java.awt.Point;
import objects.Block;
import objects.MovingBlock;


public class Collision {
    
    public static boolean  playerBlock(Point p, Block b)
    {
        
        
        return b.contains(p);
    }
    
    public static boolean playerMovingBlock(Point p, MovingBlock b) {
    	return b.contains(p);
    }
    
}
