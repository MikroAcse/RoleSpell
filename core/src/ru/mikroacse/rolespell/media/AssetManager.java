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
import com.badlogic.gdx.utils.JsonValue;
import ru.mikroacse.engine.media.AssetBundleManager;
import ru.mikroacse.engine.util.FileUtil;
import ru.mikroacse.engine.util.JsonLoader;

/**
 * Created by MikroAcse on 08.07.2016.
 */
public class AssetManager extends AssetBundleManager<AssetManager.Bundle, AssetBundle> {
    public static final String ASSETS_DIRECTORY = "";
    public static final String BUNDLE_DIRECTORY = ASSETS_DIRECTORY + "resources/%s/";

    public AssetManager(int initialWidth, int initialHeight) {
        super(initialWidth, initialHeight);
    }

    @Override
    public void loadBundle(Bundle bundle, boolean sync) {
        Gdx.app.log("LOADING", "loading bundle: " + bundle);

        JsonValue config = JsonLoader.load(getBundleMainConfigPath(bundle));
        JsonValue files = config.get("files");

        AssetBundle assetBundle = getBundle(bundle);

        if (assetBundle == null) {
            assetBundle = new AssetBundle();

            addBundle(bundle, assetBundle);
        }

        // TODO: beautify

        JsonValue textures = files.get("textures");
        if (textures != null)
            loadAssets(assetBundle,
                    textures.asStringArray(),
                    getBundleTexturePath(bundle), Texture.class);

        JsonValue sounds = files.get("sounds");
        if (sounds != null)
            loadAssets(assetBundle,
                    sounds.asStringArray(),
                    getBundleSoundPath(bundle), Sound.class);

        JsonValue music = files.get("music");
        if (music != null)
            loadAssets(assetBundle,
                    music.asStringArray(),
                    getBundleMusicPath(bundle), Music.class);

        JsonValue fonts = files.get("fonts");
        if (fonts != null)
            loadAssets(assetBundle,
                    fonts.asStringArray(),
                    getBundleFontPath(bundle), BitmapFont.class);

        JsonValue maps = files.get("maps");
        if (maps != null)
            loadAssets(assetBundle,
                    maps.asStringArray(),
                    getBundleMapPath(bundle), TiledMap.class);

        JsonValue atlases = files.get("atlases");
        if (atlases != null)
            loadAssets(assetBundle,
                    atlases.asStringArray(),
                    getBundleAtlasPath(bundle), TextureAtlas.class);

        JsonValue shaders = files.get("shaders");
        if (shaders != null)
            loadAssets(assetBundle,
                    shaders.asStringArray(),
                    getBundleShaderPath(bundle), ShaderProgram.class);

        JsonValue configs = files.get("configs");
        if (configs != null)
            loadAssets(assetBundle,
                    configs.asStringArray(),
                    getBundleConfigPath(bundle), JsonValue.class);

        if (sync) {
            assetBundle.finishLoading();
        }
    }

    private void loadAssets(AssetBundle bundle, String[] assets, String path, Class assetClass) {
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
        return getAssetBundlePath(bundle) + "config.json";
    }

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
}
