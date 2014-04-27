package com.osacky.hipsterviz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.osacky.hipsterviz.models.Track;

import java.util.Collection;

public class TrackListAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private static final String TAG = "TrackListAdapter";

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final SharedPreferences mSharedPreferences;
    private final Object mLock = new Object();

    private Track.List mTracks = new Track.List();

    private long firstMillis;
    private long lastMillis;

    public TrackListAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        new LoadDataTask(context).forceLoad();
    }

    @Override
    public int getCount() {
        return mTracks.size();
    }

    @Override
    public Track getItem(int position) {
        return mTracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_track, parent, false);
            viewHolder = new ViewHolder();
            assert convertView != null;
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.track_title);
            viewHolder.artistTextView = (TextView) convertView.findViewById(R.id.track_artist);
            viewHolder.albumTextView = (TextView) convertView.findViewById(R.id.track_album);
            viewHolder.listTime = (TextView) convertView.findViewById(R.id.track_list_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Track track = getItem(position);
        if (track != null) {
            viewHolder.nameTextView.setText(track.getName());
            viewHolder.artistTextView.setText(track.getArtist().name);
            viewHolder.albumTextView.setText(track.getDateTime().toString());
        }
        return convertView;
    }

    public void addData(Track.List data) {
        if (data != null) {
            if (isEmpty()) {
                addAll(data);
                setMillis();
            } else {
                long millis;
                for (Track track : data) {
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

    public void add(Track track) {
        synchronized (mLock) {
            mTracks.add(track);
        }
    }

    public void addAll(Collection<Track> collection) {
        synchronized (mLock) {
            mTracks.addAll(collection);
        }
        notifyDataSetChanged();
    }

    public void insert(Track track, int index) {
        synchronized (mLock) {
            mTracks.add(index, track);
        }
    }

    public Track.List getTracks() {
        return mTracks;
    }

    private void setMillis() {
        firstMillis = mTracks.get(0).getDateTime().getMillis();
        lastMillis = mTracks.get(getCount()-1).getDateTime().getMillis();
    }

    static class ViewHolder {
        TextView nameTextView;
        TextView artistTextView;
        TextView albumTextView;
        TextView listTime;
    }

    class LoadDataTask extends AsyncTaskLoader<Void> {

        public LoadDataTask(Context context) {
            super(context);
        }

        @Override
        public Void loadInBackground() {
            String savedPrefs = mSharedPreferences.getString(mContext.getString(R.string.PREF_SAVED_HISTORY), "");
            if (!savedPrefs.equals("")) {
                mTracks = new Gson().fromJson(savedPrefs, Track.List.class);
                setMillis();
            }
            new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    notifyDataSetChanged();
                }
            };
            return null;
        }
    }
}
