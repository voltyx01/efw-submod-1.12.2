package com.voltyx.mwccf.srp;

import net.minecraft.client.audio.ISound;

public class SRPMusicTracker {
    public static ISound currentSrpMusic = null;

    public static void setPlayingSrpMusic(ISound sound) {
        currentSrpMusic = sound;
    }

    public static void reset() {
        currentSrpMusic = null;
    }
}
