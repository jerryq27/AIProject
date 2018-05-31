package com.jerry.aiproject.utils;

import com.jerry.aiproject.data.Sound;
import com.jerry.aiproject.data.Sound.SoundType;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * This class will hold all the sounds
 * of the game and handle playing them.
 * @author Jerry
 */
public class SoundPlayer {

    private AudioInputStream audioStream;
    private Clip audioClip;
    public static final int AX_PICKUP = 0,
        BOW_PICKUP = 1, SWORD_PICKUP = 2, POTION_PICKUP = 3;

    private HashMap<Integer, Sound> sounds;

    public SoundPlayer() {
        init();
    }

    public void init() {
        sounds = new HashMap<>();
        loadSounds();
    }

    private void loadSounds() {
        sounds.put(AX_PICKUP, new Sound("res/ax-pickup.wav", SoundType.EFFECT));
        sounds.put(BOW_PICKUP, new Sound("res/bow-pickup.wav", SoundType.EFFECT));
        sounds.put(SWORD_PICKUP, new Sound("res/sword-pickup.wav", SoundType.EFFECT));
    }

    public void play(int key) {
        Sound playSound = sounds.get(key);

        new Thread(() -> {
            try {
                audioStream = AudioSystem.getAudioInputStream(playSound.getSoundFile());
                // Passing null fixed the invalid format issue.
                audioClip = AudioSystem.getClip(null);
                audioClip.open(audioStream);
                audioClip.start();
            }
            catch(UnsupportedAudioFileException e) { System.out.println("Audio file not supported: " + e.getMessage()); }
            catch(IOException e) { System.out.println("Error in loading the file: " + e.getMessage()); }
            catch(LineUnavailableException e) { e.printStackTrace(); }
        }).start();

    }

    public void stop() {
        audioClip.stop();
    }
}
