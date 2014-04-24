package com.osacky.hipsterviz.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.osacky.hipsterviz.models.TrackHistoryPage;
import com.osacky.hipsterviz.models.Track;
import com.osacky.hipsterviz.models.User;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class ApiAdapter {

    private static final String API_KEY = "6fd005476f0fdb0e31903f9867f995c2";
    private static final String API_URL = "http://ws.audioscrobbler.com/2.0";

    private static final RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade requestFacade) {
            requestFacade.addHeader("User-Agent", "Hipster-Visualizer-Android");
            requestFacade.addQueryParam("api_key", API_KEY);
            requestFacade.addQueryParam("format", "json");
        }
    };

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(User.class, new LastFmDeserializer<User>("user"))
            .registerTypeAdapter(Track.class, new LastFmDeserializer<Track>("track"))
            .registerTypeAdapter(TrackHistoryPage.class, new LastFmDeserializer<TrackHistoryPage>("recenttracks"))
            .create();

    private static final RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(API_URL)
            .setRequestInterceptor(requestInterceptor)
            .setConverter(new GsonConverter(gson))
            .setLogLevel(RestAdapter.LogLevel.BASIC)
            .build();

    private static final LastFmApi lastFmApi = restAdapter.create(LastFmApi.class);

    public static LastFmApi getLastFmApi() {
        return lastFmApi;
    }
}
