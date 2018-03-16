package com.jerry.aiproject.utils;

import com.jerry.aiproject.data.Sound;

import java.util.HashMap;

/**
 * This class will hold all the sounds
 * of the game and handle playing them.
 * @author Jerry
 */
public class SoundPlayer {

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
        sounds.put(AX_PICKUP, new Sound("res/ax-pickup.wav"));
        sounds.put(BOW_PICKUP, new Sound("res/bow-pickup.wav"));
        sounds.put(SWORD_PICKUP, new Sound("res/sword-pickup.wav"));
    }

    public void play(int key) {
        sounds.get(key).play(false);
    }
}
