package de.tudortmund.cs.iltis.utils.general.viewcopier;

public class PrivateDefaultConstructor {
    private String value = null;

    private PrivateDefaultConstructor() {}

    public PrivateDefaultConstructor(String value) {
        this.value = value;
    }
}
