package com.github.gripsack.android.ui.trips;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.gripsack.android.R;
import com.vipul.hp_hp.timelineview.TimelineView;

/**
 * Created by tuze on 11/22/16.
 */

public class TimelineViewHolder extends RecyclerView.ViewHolder {
   // public TextView name;
    public TimelineView mTimelineView;

    public TimelineViewHolder(View itemView, int viewType) {
        super(itemView);
       // name = (TextView) itemView.findViewById(R.id.tx_name);
        mTimelineView = (TimelineView) itemView.findViewById(R.id.time_marker);
        mTimelineView.initLine(viewType);
    }
}