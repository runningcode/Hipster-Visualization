package com.osacky.hipsterviz.api;

import com.google.gson.GsonBuilder;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;
import com.osacky.hipsterviz.models.Track;
import com.osacky.hipsterviz.models.TrackHistoryPage;
import com.osacky.hipsterviz.models.User;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

public class LastFmSpiceService extends RetrofitGsonSpiceService {

    private static final String API_URL = "http://ws.audioscrobbler.com/2.0";
    private static final String API_KEY = "6fd005476f0fdb0e31903f9867f995c2";

    @Override
    protected String getServerUrl() {
        return API_URL;
    }

    @Override
    protected Converter createConverter() {
        return new GsonConverter(new GsonBuilder()
                .registerTypeAdapter(User.class, new LastFmDeserializer<User>("user"))
                .registerTypeAdapter(Track.class, new LastFmDeserializer<Track>("track"))
                .registerTypeAdapter(TrackHistoryPage.class, new LastFmDeserializer<TrackHistoryPage>("recenttracks"))
                .create());
    }

    private static final RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade requestFacade) {
            requestFacade.addHeader("User-Agent", "Hipster-Visualizer-Android");
            requestFacade.addQueryParam("api_key", API_KEY);
            requestFacade.addQueryParam("format", "json");
        }
    };

    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        return new RestAdapter.Builder()
                .setEndpoint(getServerUrl())
                .setRequestInterceptor(requestInterceptor)
                .setConverter(getConverter())
                .setLogLevel(RestAdapter.LogLevel.BASIC);
    }
}
