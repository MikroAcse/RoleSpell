package ru.mikroacse.engine.media.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.config.ConfigurationNode;
import ru.mikroacse.engine.config.providers.YamlProvider;

/**
 * Created by MikroAcse on 24.06.2017.
 */
public class YamlConfigurationLoader extends AsynchronousAssetLoader<ConfigurationNode, YamlConfigurationLoader.YamlParameter> {
    private ConfigurationNode node;

    public YamlConfigurationLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager assetManager, String fileName, FileHandle fileHandle, YamlParameter yamlParameter) {
        YamlProvider yamlProvider = new YamlProvider(fileHandle.reader());

        node = new ConfigurationNode(yamlProvider.get());
    }

    @Override
    public ConfigurationNode loadSync(AssetManager assetManager, String fileName, FileHandle fileHandle, YamlParameter yamlParameter) {
        return node;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle fileHandle, YamlParameter yamlParameter) {
        return null;
    }

    static class YamlParameter extends AssetLoaderParameters<ConfigurationNode> {

    }
}
