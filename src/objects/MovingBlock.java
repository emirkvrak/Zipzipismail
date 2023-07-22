package objects;

import java.awt.Graphics;
import java.awt.Rectangle;

import gamestate.GameState;
import resources.Images;

public class MovingBlock extends Rectangle {
	
	private static final long serialVersionUID = 1L;
	
	private int leftbound, rightBound;
	private int move = 2;
	private int id;
	
	public MovingBlock(int x, int y, int id, int leftBound, int rightBound)
	{
		setBounds(x, y, Block.blockSize, Block.blockSize);
		this.id = id;
		this.rightBound = rightBound;
		this.leftbound = leftBound;
	}
	
	public void tick() {
		if (x + width - GameState.xOffset >= rightBound - GameState.xOffset && move != -1) {
			move *= -1;
			
		}
		
		if (x - GameState.xOffset <= leftbound - GameState.xOffset && move != 1) {
			move *= -1;
					
		}
		x += move;
	}
	
	
	
	public void draw(Graphics g) {
		if (id == 1) {
			g.drawImage(Images.blocks[id - 1], x - (int)GameState.xOffset , y - (int)GameState.yOffset, width, height, null);
		}
		if (id == 9) {
			g.drawImage(Images.diken[id - 1], x - (int)GameState.xOffset , y - (int)GameState.yOffset, width, height, null);
		}
		
	}
	
	public int getMove()
	{
		return move;
	}
	
	public int getID()
	{
		return id;
	}
}
