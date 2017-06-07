package ru.mikroacse.rolespell.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import ru.mikroacse.rolespell.RoleSpell;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1024;
        config.height = 576;

        /*config.width = 1366;
		config.height = 768;
		config.fullscreen = true;*/

        LwjglApplication app = new LwjglApplication(new RoleSpell(), config);
	}
}
