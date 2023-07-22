
package entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import physics.Collision;
import gamestate.GameState;
import main.GamePanel;
import objects.Block;
import objects.MovingBlock;


public class Player 
{
    //movement booleans
    private boolean  right = false, left = false , jumping = false, falling = false;
    private boolean topCollision = false;
    
    //bounds
    private double  x , y;
    private int width, height;
    
    //move speed
    private double moveSpeed = 2.5;
    
    
    //jump speed
    private double jumpSpeed = 4;
    private double currentJumpSpeed = jumpSpeed;
    
    //fall speed
    private double maxFallSpeed = 5;
    private double currentFallSpeed = 0.1;
    
    
    private boolean lose = false;
    private String Message = "";
    private Font font = new Font("Serif", Font.BOLD, 48);
    
    
    public Player(int width , int height)
    {
    	
        x = GamePanel.WIDTH / 2 -200;
        y = GamePanel.HEIGHT / 2;
        		
        this.width = width;
        this.height = height;
    }
    
    
    
    public void tick(Block[][] b, ArrayList<MovingBlock> movingBlocks) {
        
        int iX = (int)x;
        int iY = (int)y;
        
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) 
            {
            
            	if(b[i][j].getID() !=0) {
        	   
           
            				
            //right
            if (Collision.playerBlock(new Point(iX + width + (int)GameState.xOffset, iY + (int)GameState.yOffset + 2), b[i][j]) 
                    || Collision.playerBlock(new Point(iX + width + (int)GameState.xOffset, iY + height + (int)GameState.yOffset - 1), b[i][j])) {
                    right = false;
                   
                    if(b[i][j].getID() == 9) {
                    	if (Collision.playerBlock(new Point(iX + width + (int)GameState.xOffset, iY + (int)GameState.yOffset + 2), b[i][j]) 
                            && Collision.playerBlock(new Point(iX + width + (int)GameState.xOffset, iY + height + (int)GameState.yOffset - 1), b[i][j])) {
                            
                            Message = "GAME OVER !!!";
                            lose = true;
                    	}
                    }
                    
                    if(b[i][j].getID() == 7) {
                    	if (Collision.playerBlock(new Point(iX + width + (int)GameState.xOffset, iY + (int)GameState.yOffset + 2), b[i][j]) 
                            && Collision.playerBlock(new Point(iX + width + (int)GameState.xOffset, iY + height + (int)GameState.yOffset - 1), b[i][j])) {
                            
                            Message = "KAZANDIN !!!";
                            lose = true;
                    	}
                    }
            }
            
            //left
            if (Collision.playerBlock(new Point(iX +(int)GameState.xOffset - 1, iY + (int)GameState.yOffset + 2), b[i][j]) 
                    || Collision.playerBlock(new Point(iX +(int)GameState.xOffset - 1, iY + height + (int)GameState.yOffset - 1), b[i][j])) {
                    left = false;
                    
                    if(b[i][j].getID() == 9) {
                        if (Collision.playerBlock(new Point(iX +(int)GameState.xOffset - 1, iY + (int)GameState.yOffset + 2), b[i][j]) 
                                && Collision.playerBlock(new Point(iX +(int)GameState.xOffset - 1, iY + height + (int)GameState.yOffset - 1), b[i][j])) {
                                
                                Message = "GAME OVER !!!";
                                lose = true;
                                
                               
                        }
                    }
                    
                    if(b[i][j].getID() == 7) {
                        if (Collision.playerBlock(new Point(iX +(int)GameState.xOffset - 1, iY + (int)GameState.yOffset + 2), b[i][j]) 
                                && Collision.playerBlock(new Point(iX +(int)GameState.xOffset - 1, iY + height + (int)GameState.yOffset - 1), b[i][j])) {
                                
                                Message = "KAZANDIN !!!";
                                lose = true;
                                
                               
                        }
                    }
                    
            }
            
            //top
            if (Collision.playerBlock(new Point(iX +(int)GameState.xOffset + 1, iY + (int)GameState.yOffset), b[i][j]) 
                    || Collision.playerBlock(new Point(iX + width + (int)GameState.xOffset - 2, iY +(int)GameState.yOffset), b[i][j])) {
            	jumping = false;
                falling = true;
                
                
                	if(b[i][j].getID() == 9) {
                		if (Collision.playerBlock(new Point(iX +(int)GameState.xOffset + 1, iY + (int)GameState.yOffset), b[i][j]) 
                				&& Collision.playerBlock(new Point(iX + width + (int)GameState.xOffset - 2, iY +(int)GameState.yOffset), b[i][j])) {
                            
                            	Message = "GAME OVER !!!";
                            	lose = true;
                            
                            
                		}
                    }
                	
                	if(b[i][j].getID() == 7) {
                		if (Collision.playerBlock(new Point(iX +(int)GameState.xOffset + 1, iY + (int)GameState.yOffset), b[i][j]) 
                				&& Collision.playerBlock(new Point(iX + width + (int)GameState.xOffset - 2, iY +(int)GameState.yOffset), b[i][j])) {
                            
                            	Message = "KAZANDIN !!!";
                            	lose = true;
                            
                            
                		}
                    }
            }
            
            //bottom
            if (Collision.playerBlock(new Point(iX +(int)GameState.xOffset + 2, iY + height + (int)GameState.yOffset + 1), b[i][j]) 
                    || Collision.playerBlock(new Point(iX + width + (int)GameState.xOffset - 2, iY + height + (int)GameState.yOffset + 1), b[i][j])) {
                y = b[i][j].getY() - GameState.yOffset - height;
                
                //s�rekl� z�plama
                if (!lose) {
                	jumping = true;
				}
                
                falling = false;
                topCollision = true;   
                
                		if(b[i][j].getID() == 9) {
                				if (Collision.playerBlock(new Point(iX +(int)GameState.xOffset + 2, iY + height + (int)GameState.yOffset + 1), b[i][j]) 
                						&& Collision.playerBlock(new Point(iX + width + (int)GameState.xOffset - 2, iY + height + (int)GameState.yOffset + 1), b[i][j])) {
                            
                					Message = "GAME OVER !!!";
                					lose = true;
                            
                            
                			}
                		}
                		
                		if(b[i][j].getID() == 7) {
            				if (Collision.playerBlock(new Point(iX +(int)GameState.xOffset + 2, iY + height + (int)GameState.yOffset + 1), b[i][j]) 
            						&& Collision.playerBlock(new Point(iX + width + (int)GameState.xOffset - 2, iY + height + (int)GameState.yOffset + 1), b[i][j])) {
                        
            					Message = "KAZANDIN !!!";
            					lose = true;
                        
                        
            			}
            		}
                
                
                    }
            }else{
                if (!topCollision && !jumping) {
                    falling = true;
                    
                    
                }
            }
            
            	}
            
            
            
        }
        
        for (int i = 0; i < movingBlocks.size(); i++) {
			if (movingBlocks.get(i).getID() != 0) {
				
				//right testere
	            if (Collision.playerMovingBlock(new Point(iX + width + (int)GameState.xOffset, iY + (int)GameState.yOffset + 2), movingBlocks.get(i)) 
	                    || Collision.playerMovingBlock(new Point(iX + width + (int)GameState.xOffset, iY + height + (int)GameState.yOffset - 1), movingBlocks.get(i))) {
	                    right = false;
	                    
	                    if(movingBlocks.get(i).getID() == 9) {
	                        if (Collision.playerMovingBlock(new Point(iX + width + (int)GameState.xOffset, iY + (int)GameState.yOffset + 2), movingBlocks.get(i)) 
	        	                    && Collision.playerMovingBlock(new Point(iX + width + (int)GameState.xOffset, iY + height + (int)GameState.yOffset - 1), movingBlocks.get(i))) {
	                                
	                                Message = "GAME OVER !!!";
	                                lose = true;
	                                
	 
	                                
	                        }
	                        }
	            }
	            
	            //left testere
	            if (Collision.playerMovingBlock(new Point(iX +(int)GameState.xOffset - 1, iY + (int)GameState.yOffset + 2), movingBlocks.get(i)) 
	                    || Collision.playerMovingBlock(new Point(iX +(int)GameState.xOffset - 1, iY + height + (int)GameState.yOffset - 1), movingBlocks.get(i))) {
	                    left = false;
	                    
	                    if(movingBlocks.get(i).getID() == 9) {
	                        if (Collision.playerMovingBlock(new Point(iX +(int)GameState.xOffset - 1, iY + (int)GameState.yOffset + 2), movingBlocks.get(i)) 
	        	                    && Collision.playerMovingBlock(new Point(iX +(int)GameState.xOffset - 1, iY + height + (int)GameState.yOffset - 1), movingBlocks.get(i))) {
	                                
	                                Message = "GAME OVER !!!";
	                                lose = true;
	                                
	                                
	                        }
	                        }
	            }
	            
	            //top testere
	            if (Collision.playerMovingBlock(new Point(iX +(int)GameState.xOffset + 1, iY + (int)GameState.yOffset), movingBlocks.get(i)) 
	                    || Collision.playerMovingBlock(new Point(iX + width + (int)GameState.xOffset - 2, iY +(int)GameState.yOffset), movingBlocks.get(i))) {
	                jumping = false;
	                falling = true;
	                
	                if(movingBlocks.get(i).getID() == 9) {
                        if (Collision.playerMovingBlock(new Point(iX +(int)GameState.xOffset + 1, iY + (int)GameState.yOffset), movingBlocks.get(i)) 
        	                    && Collision.playerMovingBlock(new Point(iX + width + (int)GameState.xOffset - 2, iY +(int)GameState.yOffset), movingBlocks.get(i))) {
                                
                                Message = "GAME OVER !!!";
                                lose = true;
                                
                                
                        }
                        }
	            }
	            
	            //bottom testere
	            if (Collision.playerMovingBlock(new Point(iX +(int)GameState.xOffset + 2, iY + height + (int)GameState.yOffset + 1), movingBlocks.get(i)) 
	                    || Collision.playerMovingBlock(new Point(iX + width + (int)GameState.xOffset - 2, iY + height + (int)GameState.yOffset + 1), movingBlocks.get(i))) {
	                y = movingBlocks.get(i).getY() - height - GameState.yOffset;
	                falling = false;
	                topCollision = true;
	                
	                GameState.xOffset += movingBlocks.get(i).getMove();
	                
	                if(movingBlocks.get(i).getID() == 9) {
                        if (Collision.playerMovingBlock(new Point(iX +(int)GameState.xOffset + 2, iY + height + (int)GameState.yOffset + 1), movingBlocks.get(i)) 
        	                    && Collision.playerMovingBlock(new Point(iX + width + (int)GameState.xOffset - 2, iY + height + (int)GameState.yOffset + 1), movingBlocks.get(i))) {
                                
                                Message = "GAME OVER !!!";
                                lose = true;
                                
                                
                        }
                        }
	                
	            }else{
	                if (!topCollision && !jumping) {
	                    falling = true;
	                }
	            }
	            
	            	
			}
		}
        
        topCollision = false;
                
        
        if (right) {
        	if (x > 700) {
				GameState.xOffset += moveSpeed; 
			}else x += moveSpeed;
            
        }
        if (left) {
        	if (x < 100) {
				GameState.xOffset -= moveSpeed;
			}else x -= moveSpeed;
            
        }
        if (jumping) {
            y -= currentJumpSpeed;
            
            currentJumpSpeed -= .1;
            
            if (currentJumpSpeed<=0) {
                currentJumpSpeed = jumpSpeed;
                jumping = false;
                falling = true;
                        
            }
        }
        if (falling) {
            y += currentFallSpeed;
            
            if (currentFallSpeed <= maxFallSpeed) {
                currentFallSpeed += .1;
            }
        }
        if (!falling) {
            currentFallSpeed = .1;
        }
    }
    
    public void draw(Graphics g) {
        //g.setColor(Color.BLACK);
        //g.fillOval((int) x , (int) y , width, height);
        
    	//Topun resmi
        Image image = new ImageIcon("res/Resim/zigtop.png").getImage();
        g.drawImage(image, (int) x , (int) y , width, height, null);
        
        //Ekrana yaz� yazma
        if (lose) 
        {
            g.setColor(Color.ORANGE);
            g.setFont(font);
            g.drawString(Message, GamePanel.WIDTH / 2 - 150, GamePanel.HEIGHT / 2);
            
            
        }
    }
    
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_RIGHT) 
        {
        	if (!lose) {
				right = true;
			}
        	
        }
        if (k == KeyEvent.VK_LEFT) 
        	if (!lose) {
				left = true;
			}
        
        if (k == KeyEvent.VK_SPACE && !jumping && !falling)
        	if (!lose) {
				jumping = true;
			}
        
            
        
    }
    
    public void keyReleased(int k) {
        if (k == KeyEvent.VK_RIGHT) right = false;
        if (k == KeyEvent.VK_LEFT) left = false;
            
        
    }
}
