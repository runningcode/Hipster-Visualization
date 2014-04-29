package com.osacky.hipsterviz.api;

public interface LoadingInterface {

    public abstract void onLoadingStarted();

    public abstract void onLoadingProgressUpdate(float progress);

    public abstract void onLoadingFinished();

}
