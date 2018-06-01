package com.sjsu.td.mytube;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[] mDataset;
    private  ArrayList<VideoItem> videoList;
    private RecyclerViewClickListener mListener;
    private Activity activity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView mVideoTitle;
        public ImageView mThumbnail;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mVideoTitle = (TextView)v.findViewById(R.id.tvVideoTitle);
            mThumbnail = (ImageView) v.findViewById(R.id.ivThumbnail);
        }


        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset, RecyclerViewClickListener listener) {
        mDataset = myDataset;
        mListener = listener;
    }

    public MyAdapter(Activity activity,ArrayList myVideoList, RecyclerViewClickListener listener) {
        videoList = myVideoList;
        mListener = listener;
        this.activity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mVideoTitle.setText(videoList.get(position).getTitle());
        Picasso.with(activity).load(videoList.get(position).getThumbnailURL()).into(holder.mThumbnail);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return videoList.size();
    }
}
