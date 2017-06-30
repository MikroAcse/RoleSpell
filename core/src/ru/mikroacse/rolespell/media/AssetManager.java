package ru.mikroacse.rolespell.media;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.engine.config.providers.YamlProvider;
import ru.mikroacse.engine.media.AssetBundleManager;
import ru.mikroacse.engine.util.FileUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

/**
 * Created by MikroAcse on 08.07.2016.
 */
public class AssetManager extends AssetBundleManager<Bundle, AssetBundle> {
    private static final String ASSETS_DIRECTORY = "";

    public static final String APP_CONFIG = ASSETS_DIRECTORY + "config.yaml";

    public static final String LANGUAGE_BUNDLE = ASSETS_DIRECTORY + "languages/bundle";

    private static final String BUNDLE_DIRECTORY = ASSETS_DIRECTORY + "resources/%s/";

    public AssetManager(int initialWidth, int initialHeight) {
        super(initialWidth, initialHeight);
    }

    @Override
    public void loadBundle(Bundle bundle, boolean sync) {
        System.out.println("Loading resources bundle: " + bundle);

        ConfigurationNode config;

        try {
            FileReader configReader = new FileReader(getBundleMainConfigPath(bundle));

            config = new ConfigurationNode(new YamlProvider(configReader));
        } catch (FileNotFoundException e) {
            System.err.println("No bundle configuration found! (" + bundle.getName() + ")");
            return;
        }

        ConfigurationNode files = config.extractNode("files");

        AssetBundle assetBundle = getBundle(bundle);

        if (assetBundle == null) {
            assetBundle = new AssetBundle();

            addBundle(bundle, assetBundle);
        }

        // TODO: beautify

        List<String> textures = files.get("textures", null);
        if (textures != null)
            loadAssets(assetBundle, textures, getBundleTexturePath(bundle), Texture.class);

        List<String> sounds = files.get("sounds", null);
        if (sounds != null)
            loadAssets(assetBundle, sounds, getBundleSoundPath(bundle), Sound.class);

        List<String> music = files.get("music", null);
        if (music != null)
            loadAssets(assetBundle, music, getBundleMusicPath(bundle), Music.class);

        List<String> fonts = files.get("fonts", null);
        if (fonts != null)
            loadAssets(assetBundle, fonts, getBundleFontPath(bundle), BitmapFont.class);

        List<String> maps = files.get("maps", null);
        if (maps != null)
            loadAssets(assetBundle, maps, getBundleMapPath(bundle), TiledMap.class);

        List<String> atlases = files.get("atlases", null);
        if (atlases != null)
            loadAssets(assetBundle, atlases, getBundleAtlasPath(bundle), TextureAtlas.class);

        List<String> shaders = files.get("shaders", null);
        if (shaders != null)
            loadAssets(assetBundle, shaders, getBundleShaderPath(bundle), ShaderProgram.class);

        List<String> configs = files.get("configs", null);
        if (configs != null)
            loadAssets(assetBundle, configs, getBundleConfigPath(bundle), ConfigurationNode.class);

        if (sync) {
            assetBundle.finishLoading();
        }
    }

    private void loadAssets(AssetBundle bundle, List<String> assets, String path, Class assetClass) {
        for (String asset : assets) {
            String assetPath = path + asset;

            // recursively load all files in a path (i.e. "files/*")
            if (asset.endsWith("*")) {
                asset = asset.substring(0, asset.length() - 1);

                FileHandle assetHandle = Gdx.files.internal(assetPath.substring(0, assetPath.length() - 1));

                Array<FileHandle> subHandles = new Array<>();
                FileUtil.getHandles(assetHandle, subHandles);

                for (FileHandle subHandle : subHandles) {
                    assetPath = subHandle.file().getPath();
                    assetPath = assetPath.replaceAll("\\\\", "/");

                    asset = assetPath.replace(path, "");

                    bundle.loadAsset(FileUtil.getFilename(asset), assetPath, assetClass);
                }

                continue;
            }

            bundle.loadAsset(FileUtil.getFilename(asset), assetPath, assetClass);
        }
    }

    public BitmapFont getGlobalFont(String name) {
        return getBundle(Bundle.GLOBAL).getFont(name);
    }

    private String getAssetBundlePath(Bundle bundle) {
        return String.format(BUNDLE_DIRECTORY, bundle.getName());
    }

    private String getBundleTexturePath(Bundle bundle, int scale) {
        String path = getAssetBundlePath(bundle) + "textures/x%d/";
        return String.format(path, scale);
    }

    private String getBundleTexturePath(Bundle bundle) {
        return getBundleTexturePath(bundle, 1);
    }

    private String getBundleSoundPath(Bundle bundle) {
        return getAssetBundlePath(bundle) + "sounds/";
    }

    private String getBundleMusicPath(Bundle bundle) {
        return getAssetBundlePath(bundle) + "music/";
    }

    private String getBundleFontPath(Bundle bundle) {
        return getAssetBundlePath(bundle) + "fonts/";
    }

    private String getBundleAtlasPath(Bundle bundle) {
        return getAssetBundlePath(bundle) + "atlases/";
    }

    private String getBundleShaderPath(Bundle bundle) {
        return getAssetBundlePath(bundle) + "shaders/";
    }

    private String getBundleMapPath(Bundle bundle) {
        return getAssetBundlePath(bundle) + "maps/";
    }

    private String getBundleConfigPath(Bundle bundle) {
        return getAssetBundlePath(bundle) + "configs/";
    }

    private String getBundleMainConfigPath(Bundle bundle) {
        return getAssetBundlePath(bundle) + "config.yaml";
    }

    private String getAppConfigPath() {
        return ASSETS_DIRECTORY + "config.yaml";
    }
}
