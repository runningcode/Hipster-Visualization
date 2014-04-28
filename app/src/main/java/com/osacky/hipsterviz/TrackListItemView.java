package com.osacky.hipsterviz;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.osacky.hipsterviz.models.track.TrackListTrack;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.jetbrains.annotations.NotNull;

@EViewGroup(R.layout.item_track)
public class TrackListItemView extends LinearLayout {

    private final Picasso mPicasso;

    @NotNull
    Context mContext;

    @ViewById(R.id.track_image)
    ImageView imageView;

    @ViewById(R.id.track_title)
    TextView trackTitle;

    @ViewById(R.id.track_artist)
    TextView artistName;

    @ViewById(R.id.track_list_time)
    TextView timeView;

    public TrackListItemView(@NotNull Context context) {
        super(context);
        mContext = context;
        mPicasso = Picasso.with(context);
    }

    public void bind(TrackListTrack track) {
        trackTitle.setText(track.getName());
        artistName.setText(track.getArtist().getName());
        timeView.setText(track.getDateTime().toString());
        mPicasso.load(track.getImage(mContext)).into(imageView);
    }
}
