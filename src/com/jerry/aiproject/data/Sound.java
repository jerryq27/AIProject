package com.jerry.aiproject.data;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {

    public enum SoundType {
        MUSIC,
        EFFECT
    }
    private AudioInputStream audioStream;
    private Clip audioClip;

    public Sound(String soundFile) {
        try { audioStream = AudioSystem.getAudioInputStream(new File(soundFile)); }
        catch(UnsupportedAudioFileException e) { System.out.println("Audio file not supported: " + e.getMessage()); }
        catch(IOException e) { System.out.println("Error in loading the file: " + e.getMessage()); }
    }

    public void play() {
        try {
            audioClip = AudioSystem.getClip(null);
            audioClip.open(audioStream);
            audioClip.start();
        }
        catch(LineUnavailableException e) { e.printStackTrace(); }
        catch(IOException e) { e.printStackTrace(); }
    }
}
