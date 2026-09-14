package com.voltyx.mwccf.block.lamp;

public enum LampFlickerType {
    SOFT("soft"),
    BROKEN("broken"),
    DYING("dying");

    private final String name;

    LampFlickerType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
