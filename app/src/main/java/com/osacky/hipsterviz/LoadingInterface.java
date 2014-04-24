package com.osacky.hipsterviz;

public interface LoadingInterface {

    public abstract void onLoadingStarted();
    public abstract void onLoadingProgressUpdate(int progress);
    public abstract void onLoadingFinished();

}
