package main;

import java.awt.BorderLayout;
import main.GamePanel;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainGame extends JFrame
{
	
	
    public MainGame()
    {
        this.setTitle("PLATFORMER");         
        this.setLocationRelativeTo(null);  
        this.setResizable(false);         
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.add(new GamePanel(), BorderLayout.CENTER);
        this.pack();
        this.setLocationRelativeTo(null);
        
    }
    
}
