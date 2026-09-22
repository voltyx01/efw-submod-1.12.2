package net.bettercombat.api;

public final class ComboState {
    public final int current;
    public final int max;

    public ComboState(int current, int max) {
        this.current = current;
        this.max = max;
    }
}
