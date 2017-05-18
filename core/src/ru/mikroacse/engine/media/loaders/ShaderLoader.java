package ru.mikroacse.engine.media.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

/**
 * Created by MikroAcse on 07.08.2016.
 */
public class ShaderLoader extends AsynchronousAssetLoader<ShaderProgram, ShaderLoader.ShaderParameter> {
    private ShaderProgram shader;
    private String vertProgram;
    private String fragProgram;

    public ShaderLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, ShaderParameter parameter) {
        String vertPath = fileName.concat(".vert");
        String fragPath = fileName.concat(".frag");

        FileHandle vertFile = Gdx.files.internal(vertPath);
        FileHandle fragFile = Gdx.files.internal(fragPath);

        if (vertFile.exists() && fragFile.exists()) {
            vertProgram = vertFile.readString();
            fragProgram = fragFile.readString();
        }
    }

    @Override
    public ShaderProgram loadSync(AssetManager manager, String fileName, FileHandle file, ShaderParameter parameter) {
        ShaderProgram.pedantic = false;
        shader = new ShaderProgram(vertProgram, fragProgram);
        return shader;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, ShaderParameter parameter) {
        return null;
    }

    class ShaderParameter extends AssetLoaderParameters<ShaderProgram> {

    }
}