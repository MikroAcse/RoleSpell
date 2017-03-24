package com.quillraven.quillyscastle.profile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.quillraven.quillyscastle.utils.Utils;

public class ProfileManager extends ProfileSubject {
    private static final String TAG = ProfileManager.class.getSimpleName();

    private static final String SAVE_GAME_SUFFIX = ".sav";
    private static final String DEFAULT_PROFILE_NAME = "default";

    private static ProfileManager instance;

    private final ObjectMap<String, FileHandle> profiles;
    private ObjectMap<String, Object> profileProperties;

    private String currentProfileName;

    private ProfileManager() {
        profiles = new ObjectMap<String, FileHandle>();
        profileProperties = new ObjectMap<String, Object>();
        currentProfileName = DEFAULT_PROFILE_NAME;
        loadAllProfiles();
    }

    public static final ProfileManager getInstance() {
        if (instance == null) {
            instance = new ProfileManager();
        }

        return instance;
    }

    /**
     * loads all save files from the storage and stores them in them in our profiles map
     */
    private void loadAllProfiles() {
        if (Gdx.files.isLocalStorageAvailable()) {
            FileHandle[] saveFiles = Gdx.files.local(".").list(SAVE_GAME_SUFFIX);
            Gdx.app.debug(TAG, "Found " + saveFiles.length + " savefiles");

            for (FileHandle file : saveFiles) {
                Gdx.app.debug(TAG, "Storing savefile: " + file.nameWithoutExtension());
                profiles.put(file.nameWithoutExtension(), file);
            }
        } else {
            // TODO: try external directory
            Gdx.app.debug(TAG, "External storage for profiles not supported yet!");
        }
    }

    /**
     * @return array of available profiles that can be loaded with the {@link #loadProfile()} method
     */
    public Array<String> getAllProfiles() {
        Array<String> result = new Array<String>();

        for (String key : profiles.keys()) {
            result.add(key);
        }

        return result;
    }

    public boolean containsProfile(String profile) {
        return profiles.containsKey(profile);
    }

    public FileHandle getProfileFile(String profile) {
        if (!containsProfile(profile)) {
            return null;
        }

        return profiles.get(profile);
    }

    public void setProperty(String key, Object object) {
        profileProperties.put(key, object);
    }

    public <T> T getProperty(String key, Class<T> type) {
        if (!profileProperties.containsKey(key)) {
            return null;
        }

        return type.cast(profileProperties.get(key));
    }

    public void saveCurrentProfile(boolean overwrite) {
        Gdx.app.debug(TAG, "Saving profile " + currentProfileName + " ...");
        fireSave(this);

        writeProfileToStorage(overwrite);
        Gdx.app.debug(TAG, "Saving successful!");
    }

    private void writeProfileToStorage(boolean overwrite) {
        final FileHandle file;
        if (Gdx.files.isLocalStorageAvailable()) {
            final String fileName = currentProfileName + SAVE_GAME_SUFFIX;

            if (Gdx.files.internal(fileName).exists() && !overwrite) {
                Gdx.app.debug(TAG, "Trying to overwrite an existing file - abort!");
                return;
            }

            file = Gdx.files.local(fileName);
            file.writeString(Utils.prettyPrintJson(Utils.toJson(profileProperties)), false);
        } else {
            Gdx.app.debug(TAG, "External storage for profiles not supported yet!");
            file = null;
        }

        if (file != null) {
            profiles.put(currentProfileName, file);
        }
    }

    public void newProfile(String profile) {
        currentProfileName = profile;

        profileProperties.clear();
        writeProfileToStorage(true);
        loadProfile(currentProfileName);
    }

    public void loadProfile(String profile) {
        final String fileName = profile + SAVE_GAME_SUFFIX;

        if (!Gdx.files.internal(fileName).exists()) {
            Gdx.app.debug(TAG, "Trying to load non-existing profile: " + profile);
            return;
        }

        currentProfileName = profile;
        Gdx.app.debug(TAG, "Loading profile " + currentProfileName + " ...");

        ObjectMap<?, ?> loadedProperties = Utils.fromJson(ObjectMap.class, profiles.get(currentProfileName));

        profileProperties.clear();

        for (Entry<?, ?> entry : loadedProperties.entries()) {
            profileProperties.put((String) entry.key, entry.value);
        }

        fireLoad(this);
        Gdx.app.debug(TAG, "Loading profile successful!");
    }
}
