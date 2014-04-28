package com.osacky.hipsterviz.api.lastFmApi;

import com.osacky.hipsterviz.models.TrackHistoryPage;
import com.osacky.hipsterviz.models.User;
import com.osacky.hipsterviz.models.artist.RealArtist;
import com.osacky.hipsterviz.models.track.RealBaseTrack;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

@SuppressWarnings("unused")
interface LastFmApi {
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
    RealBaseTrack getTrackInfo(
            @Query("track") String track,
            @Query("artist") String artist
    );

    @GET("/?method=track.getinfo")
    void getTrackInfo(
            @Query("track") String track,
            @Query("artist") String artist,
            Callback<RealBaseTrack> callback
    );

    @GET("/?method=track.getinfo")
    RealBaseTrack getTrackInfo(@Query("mbid") String mbid);

    @GET("/?method=track.getinfo")
    void getTrackInfo(
            @Query("mbid") String mbid,
            Callback<RealBaseTrack> callback
    );

    @GET("/?method=artist.getInfo")
    RealArtist getArtistInfoByMbid(@Query("mbid") String mbid);

    @GET("/?method=artist.getInfo")
    RealArtist getArtistInfoByName(@Query("artist") String artist);
}
