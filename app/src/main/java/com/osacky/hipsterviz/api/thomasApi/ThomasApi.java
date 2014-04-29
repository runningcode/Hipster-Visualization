package com.osacky.hipsterviz.api.thomasApi;

import com.osacky.hipsterviz.models.ArtistDataResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.mime.TypedInput;

@SuppressWarnings("unused")
interface ThomasApi {

    @GET("/requestArtists")
    ArtistDataResponse.ArtistList requestArtists(@Query("limit") int limit);

    @POST("/rankArtists")
    ArtistDataResponse.ArtistList rankArtists(@Body TypedInput body);

    @POST("/rank")
    void rank(@Query("id") String id, @Query("classification") String classification, Callback<String> callback);

    @POST("/rank")
    String rank(@Query("id") String id, @Query("classification") String classification);
}
