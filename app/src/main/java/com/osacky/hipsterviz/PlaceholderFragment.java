package com.osacky.hipsterviz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.osacky.hipsterviz.api.ApiAdapter;
import com.osacky.hipsterviz.api.LastFmApi;
import com.osacky.hipsterviz.models.User;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PlaceholderFragment extends Fragment {

    private static final String TAG = "PlaceHolderFragment";

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        LastFmApi lastFmApi = ApiAdapter.getLastFmApi();
        lastFmApi.getUserInfo("nosacky", new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                Log.i(TAG, "user name is " + user.getName());
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }
}
