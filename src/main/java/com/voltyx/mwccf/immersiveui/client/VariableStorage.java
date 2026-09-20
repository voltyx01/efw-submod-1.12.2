package com.voltyx.mwccf.immersiveui.client;

public class VariableStorage {
    public static final long TARGET_INTERVAL_MS = 350;
    public static long lastExecutedTime = System.currentTimeMillis();
    public static long currentTime;
    public static long elapsedTime;

    public static boolean shakeScreen = false;
}
