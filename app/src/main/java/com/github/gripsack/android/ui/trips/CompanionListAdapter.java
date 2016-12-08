package com.github.gripsack.android.ui.trips;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.gripsack.android.R;
import com.github.gripsack.android.data.model.User;
import java.util.ArrayList;

/**
 * Created by Tugce on 12/7/2016.
 */

public class CompanionListAdapter extends BaseAdapter implements Filterable  {
    ArrayList<User> users;
    Context context;
    ArrayList<User> filterList;
    CustomFilter filter;
    private static class ViewHolderCompanion{
        ImageView ivImage;
        TextView tvName;
    }

    public CompanionListAdapter(Context context, ArrayList<User> users) {
        this.users=users;
        this.context=context;
        this.filterList=users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_companion,null);
        }
        ViewHolderCompanion holder=new ViewHolderCompanion();
        holder.ivImage=(ImageView)convertView.findViewById(R.id.ivUserImage);
        holder.tvName=(TextView)convertView.findViewById(R.id.tvUserName);
        holder.tvName.setText(users.get(position).displayName);
        Glide.with(context).load(users.get(position).profileImageUrl).into(holder.ivImage);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        // return a filter that filters data based on a constraint
        if(filter==null){
            filter=new CustomFilter();
        }
        return filter;
    }
    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0){
                constraint=constraint.toString().toUpperCase();
                ArrayList<User> filters=new ArrayList<User>();
                for(int i=0;i<filterList.size();i++){
                    if(filterList.get(i).displayName.toUpperCase().contains(constraint)){
                        User user= new User(filterList.get(i).displayName , filterList.get(i).profileImageUrl);
                        filters.add(user);
                    }

                }
                results.count=filters.size();
                results.values=filters;
            }
            else {
                results.count=filterList.size();
                results.values=filterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            users=(ArrayList<User>)results.values;
            notifyDataSetChanged();
        }
    }
}
