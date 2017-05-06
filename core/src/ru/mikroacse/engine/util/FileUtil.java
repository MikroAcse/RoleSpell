package ru.mikroacse.engine.util;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by MikroAcse on 29-Apr-17.
 */
public class FileUtil {
    public static void getHandles(FileHandle begin, Array<FileHandle> handles) {
        FileHandle[] newHandles = begin.list();

        for (FileHandle f : newHandles) {
            if (f.isDirectory()) {
                getHandles(f, handles);
            } else {
                handles.add(f);
            }
        }
    }
}
