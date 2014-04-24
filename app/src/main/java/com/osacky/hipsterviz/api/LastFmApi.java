package com.osacky.hipsterviz.api;

import com.osacky.hipsterviz.models.Track;
import com.osacky.hipsterviz.models.TrackHistoryPage;
import com.osacky.hipsterviz.models.User;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

@SuppressWarnings("unused")
public interface LastFmApi {
    @GET("/?method=user.getinfo")
    User getUserInfo(@Query("user") String user);

    @GET("/?method=user.getinfo")
    void getUserInfo(
            @Query("user") String user,
            Callback<User> callback);

    @GET("/?method=user.getrecenttracks&limit=200")
    TrackHistoryPage getRecentTracks(
            @Query("user") String user,
            @Query("page") int page
    );

    @GET("/?method=user.getrecenttracks&limit=200")
    void getRecentTracks(
            @Query("user") String user,
            @Query("page") int page,
            Callback<TrackHistoryPage> callback
    );

    @GET("/?method=track.getinfo")
    Track getTrackInfo(
            @Query("track") String track,
            @Query("artist") String artist
    );

    @GET("/?method=track.getinfo")
    void getTrackInfo(
            @Query("track") String track,
            @Query("artist") String artist,
            Callback<Track> callback
    );

    @GET("/?method=track.getinfo")
    Track getTrackInfo(@Query("mbid") String mbid);

    @GET("/?method=track.getinfo")
    void getTrackInfo(
            @Query("mbid") String mbid,
            Callback<Track> callback
    );
}
