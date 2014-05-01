package com.osacky.hipsterviz.api.thomasApi;

import com.google.gson.GsonBuilder;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;
import com.osacky.hipsterviz.api.lastFmApi.ArtistDeserializer;
import com.osacky.hipsterviz.models.artist.RealBaseArtist;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

public class ThomasApiService extends RetrofitGsonSpiceService {

    private static final String API_URL = "http://thomasdeegan.com:3030";
    private static final RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade requestFacade) {
            requestFacade.addHeader("Content-Type", "application/json");
        }
    };

    @Override
    protected String getServerUrl() {
        return API_URL;
    }

    @Override
    protected Converter createConverter() {
        return new GsonConverter(new GsonBuilder()
                .registerTypeAdapter(RealBaseArtist.class, new ArtistDeserializer<RealBaseArtist>())
                .create());
    }

    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        return new RestAdapter.Builder()
                .setEndpoint(getServerUrl())
                .setRequestInterceptor(requestInterceptor)
                .setConverter(getConverter())
                .setLogLevel(RestAdapter.LogLevel.BASIC);
    }
}
