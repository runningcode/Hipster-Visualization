package com.osacky.hipsterviz;

public interface LoadingInterface {

    public abstract void onLoadingStarted();
    public abstract void onLoadingProgressUpdate(float progress);
    public abstract void onLoadingFinished();

}
