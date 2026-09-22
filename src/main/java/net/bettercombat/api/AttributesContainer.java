package net.bettercombat.api;

import javax.annotation.Nullable;

public final class AttributesContainer {
    @Nullable
    private final String parent;
    private final WeaponAttributes attributes;

    public AttributesContainer(@Nullable String parent, WeaponAttributes attributes) {
        this.parent = parent;
        this.attributes = attributes;
    }

    @Nullable
    public String parent() {
        return parent;
    }

    public WeaponAttributes attributes() {
        return attributes;
    }
}
