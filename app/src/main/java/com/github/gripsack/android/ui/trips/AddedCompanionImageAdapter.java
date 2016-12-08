package com.github.gripsack.android.ui.trips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Tugce on 12/8/2016.
 */

public class AddedCompanionImageAdapter extends ArrayAdapter<User> {
    private int orientation;

    private static class ViewHolderProfilePhoto{
        CircleImageView ivImage;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public AddedCompanionImageAdapter(Context context, List<User> userImages) {
        super(context, android.R.layout.simple_expandable_list_item_1,userImages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        final User user=getItem(position);

        LayoutInflater inflater= LayoutInflater.from(getContext());
        int type = getItemViewType(position);

        convertView = inflater.inflate(R.layout.item_profile_photo, parent, false);
        ViewHolderProfilePhoto holder=new ViewHolderProfilePhoto();
            holder.ivImage=(CircleImageView) convertView.findViewById(R.id.ivAddedUserImage);
            holder.ivImage.setImageResource(0);
            Glide.with(getContext()).load(user.profileImageUrl)
                   .into(holder.ivImage);
        return convertView;
    }}
