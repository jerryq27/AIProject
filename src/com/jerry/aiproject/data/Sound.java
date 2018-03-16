package com.jerry.aiproject.data;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * This class represents a sound
 * In the game. It also handles
 * The functionality needed to
 * Play or loop a certain sound.
 * @author Jerry
 */
public class Sound {

    private AudioInputStream audioStream;
    private Clip audioClip;

    public Sound(String soundFile) {
        try { audioStream = AudioSystem.getAudioInputStream(new File(soundFile)); }
        catch(UnsupportedAudioFileException e) { System.out.println("Audio file not supported: " + e.getMessage()); }
        catch(IOException e) { System.out.println("Error in loading the file: " + e.getMessage()); }
    }

    public void play(boolean isLooping) {
        try {
            // Passing null fixed the invalid format issue.
            audioClip = AudioSystem.getClip(null);
            audioClip.open(audioStream);
            if(isLooping) audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            else audioClip.start();
        }
        catch(LineUnavailableException e) { e.printStackTrace(); }
        catch(IOException e) { e.printStackTrace(); }
    }

    public void stop() {
        audioClip.stop();
    }

}
