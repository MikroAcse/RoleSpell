package ru.mikroacse.rolespell.media;

/**
 * Created by MikroAcse on 28.06.2017.
 */
public enum Bundle {
    GLOBAL("global"),
    LOADER("loader"),
    MENU("menu"),
    SETTINGS("settings"),
    GAME("game");

    private String name;

    Bundle(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
