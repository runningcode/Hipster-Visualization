package com.osacky.hipsterviz;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.osacky.hipsterviz.models.Track;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.item_track)
public class TrackListItemView extends LinearLayout {

    @ViewById(R.id.track_title)
    TextView trackTitle;

    @ViewById(R.id.track_artist)
    TextView artistName;

    @ViewById(R.id.track_album)
    TextView albumName;

    @ViewById(R.id.track_list_time)
    TextView timeView;

    public TrackListItemView(Context context) {
        super(context);
    }

    public void bind(Track track) {
        trackTitle.setText(track.getName());
        artistName.setText(track.getArtist().name);
        timeView.setText(track.getDateTime().toString());
    }
}
