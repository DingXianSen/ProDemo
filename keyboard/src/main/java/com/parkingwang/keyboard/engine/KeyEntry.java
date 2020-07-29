package com.parkingwang.keyboard.engine;

/**
 */
public class KeyEntry {
    public final String text;
    public final KeyType keyType;
    public final boolean isFunKey;
    public final boolean enabled;

    public KeyEntry(String text, KeyType keyType, boolean enabled) {
        this.text = text;
        this.keyType = keyType;
        this.enabled = enabled;
        this.isFunKey = !KeyType.GENERAL.equals(keyType);
    }

    public static KeyEntry newOfSetEnable(KeyEntry key, boolean enabled) {
        return new KeyEntry(key.text, key.keyType, enabled);
    }

    @Override
    public String toString() {
        return "KeyEntry{" +
                "text='" + text + '\'' +
                ", keyType=" + keyType +
                ", enabled=" + enabled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyEntry)) return false;
        KeyEntry keyEntry = (KeyEntry) o;
        return Utils.equals(text, keyEntry.text) &&
                keyType == keyEntry.keyType;
    }

    @Override
    public int hashCode() {
        return Utils.hash(text, keyType);
    }
}
