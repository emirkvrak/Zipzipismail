
package main;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import main.MainGame;


public class Platform 
{

    
    public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException, IOException 
    {
        MainGame basla = new MainGame();
        basla.setVisible(true);
        
        
        //java sees
        File file = new File("res/Ses/zipzipses.wav");
        AudioInputStream audiostream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audiostream); 
        clip.start();
        
        
    }
    
}
