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

    public enum SoundType {
        EFFECT,
        MUSIC
    }
    private SoundType soundType;
    private File soundFile;

    public Sound(String path, SoundType type) {
        soundFile = new File(path);
        soundType = type;
    }

    public File getSoundFile() {
        return soundFile;
    }

    public SoundType getSoundType() {
        return soundType;
    }

}
