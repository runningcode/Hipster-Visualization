package com.osacky.hipsterviz;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.gson.Gson;
import com.osacky.hipsterviz.models.track.TrackListTrack;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

import java.util.Collection;

@EBean
public class TrackListAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private static final String TAG = "TrackListAdapter";
    private final Object mLock = new Object();
    @RootContext
    Context mContext;
    private SharedPreferences mSharedPreferences;
    private TrackListTrack.BaseTrackList mTracks = new TrackListTrack.BaseTrackList();

    private long firstMillis;
    private long lastMillis;

    public TrackListAdapter() {
    }

    @AfterInject
    void initAdapter() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
//        loadSavedData();
    }

    @Override
    public int getCount() {
        return mTracks.size();
    }

    @Override
    public TrackListTrack getItem(int position) {
        return mTracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrackListItemView trackListItemView;

        if (convertView == null) {
            trackListItemView = TrackListItemView_.build(mContext);
        } else {
            trackListItemView = (TrackListItemView) convertView;
        }
        trackListItemView.bind(getItem(position));
        return trackListItemView;
    }

    public void addData(TrackListTrack.BaseTrackList data) {
        if (data != null) {
            if (isEmpty()) {
                addAll(data);
                setMillis();
            } else {
                long millis;
                for (TrackListTrack track : data) {
                    millis = track.getDateTime().getMillis();
                    if (millis > firstMillis) {
                        firstMillis = millis;
                        insert(track, 0);
                    } else if (millis < lastMillis) {
                        lastMillis = millis;
                        add(track);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public void add(TrackListTrack track) {
        synchronized (mLock) {
            mTracks.add(track);
        }
    }

    public void addAll(Collection<TrackListTrack> collection) {
        synchronized (mLock) {
            mTracks.addAll(collection);
        }
    }

    public void insert(TrackListTrack track, int index) {
        synchronized (mLock) {
            mTracks.add(index, track);
        }
    }

    public TrackListTrack.BaseTrackList getTracks() {
        return mTracks;
    }

    private void setMillis() {
        firstMillis = mTracks.get(0).getDateTime().getMillis();
        lastMillis = mTracks.get(getCount() - 1).getDateTime().getMillis();
    }

    @Background
    void loadSavedData() {
        String savedPrefs = mSharedPreferences.getString(mContext.getString(R.string.PREF_SAVED_HISTORY), "");
        if (!savedPrefs.equals("")) {
            notifyChanged(new Gson().fromJson(savedPrefs, TrackListTrack.BaseTrackList.class));
        }
    }

    @UiThread
    void notifyChanged(TrackListTrack.BaseTrackList tracks) {
        addData(tracks);
        notifyDataSetChanged();
        setMillis();
    }
}
