package com.osacky.hipsterviz.api.lastFmApi;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.osacky.hipsterviz.models.User;

public class UserSpiceRequest extends RetrofitSpiceRequest<User, LastFmApi> {

    private final String mUsername;

    public UserSpiceRequest(String username) {
        super(User.class, LastFmApi.class);
        mUsername = username;
    }

    @Override
    public User loadDataFromNetwork() throws Exception {
        return getService().getUserInfo(mUsername);
    }
}
