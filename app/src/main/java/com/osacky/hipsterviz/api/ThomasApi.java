package com.osacky.hipsterviz.api;

import com.osacky.hipsterviz.models.ArtistDataResponse;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

@SuppressWarnings("unused")
public interface ThomasApi {

    @GET("requestArtists")
    List<ArtistDataResponse> requestArtists(@Query("limit") int limit);

    @POST("rankArtists")
    List<ArtistDataResponse> rankArtists(@Body List<String> string);
}
