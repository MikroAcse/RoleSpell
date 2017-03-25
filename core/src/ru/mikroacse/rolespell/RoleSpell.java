package ru.mikroacse.rolespell;

import com.badlogic.gdx.Game;
import ru.mikroacse.rolespell.screens.GameScreen;

public class RoleSpell extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}
