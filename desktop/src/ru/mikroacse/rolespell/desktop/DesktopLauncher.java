package ru.mikroacse.rolespell.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.mikroacse.rolespell.RoleSpell;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		// TODO: magic numbers
		config.width = 1024;
		config.height = 576;

		LwjglApplication app = new LwjglApplication(new RoleSpell(), config);
	}
}
