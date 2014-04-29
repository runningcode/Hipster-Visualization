package com.osacky.hipsterviz.api.thomasApi;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class ThomasApiService extends RetrofitGsonSpiceService {

    private static final String API_URL = "http://thomasdeegan.com:3030";

    @Override
    protected String getServerUrl() {
        return API_URL;
    }

    private static final RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade requestFacade) {
            requestFacade.addHeader("Content-Type", "application/json");
        }
    };

    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        return new RestAdapter.Builder()
                .setEndpoint(getServerUrl())
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.BASIC);
    }
}
