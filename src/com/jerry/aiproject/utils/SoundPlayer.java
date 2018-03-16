package com.jerry.aiproject.utils;

import com.jerry.aiproject.data.Sound;

import java.util.HashMap;

/**
 * Created by jerry on 3/16/18.
 */
public class SoundPlayer {

    private HashMap<Integer, Sound> sounds;

    public SoundPlayer() {
        init();
    }

    public void init() {
        sounds = new HashMap<>();
        loadSounds();
    }

    private void loadSounds() {
        sounds.put(0, new Sound("res/ax-pickup.wav"));
        sounds.put(1, new Sound("res/bow-pickup.wav"));
        sounds.put(2, new Sound("res/sword-pickup.wav"));
    }

    public void play(int key) {
        sounds.get(key).play();
    }
}
