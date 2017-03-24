package com.quillraven.quillyscastle.profile;

public interface ProfileObserver {
    public void onSave(final ProfileManager manager);

    public void onLoad(final ProfileManager manager);
}
