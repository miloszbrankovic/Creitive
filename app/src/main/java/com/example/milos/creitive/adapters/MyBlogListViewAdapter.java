package com.example.milos.creitive.adapters;

import android.app.Activity;
import android.content.Context;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.milos.creitive.R;
import com.example.milos.creitive.utils.HtmlUtils;
import com.example.milos.creitive.utils.SharedPreferenceUtils;
import com.example.milos.creitive.activities.SingleBlogActivity;
import com.example.milos.creitive.models.Blog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by milos on 05/02/2018.
 */

public class MyBlogListViewAdapter extends RecyclerView.Adapter<MyBlogListViewAdapter.PostHolder> {

    public static final String TAG = MyBlogListViewAdapter.class.getSimpleName();

    private ArrayList<Blog> mData;
    private Activity mActivity;
    private Context mContext;

    public MyBlogListViewAdapter(ArrayList<Blog> data, Activity activity, Context context){
        this.mData = data;
        this.mActivity = activity;
        this.mContext = context;
    }

    @Override
    public MyBlogListViewAdapter.PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
        return  new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyBlogListViewAdapter.PostHolder holder, final int position) {
        final Blog blog = mData.get(position);

        holder.mTextViewTitle.setText(blog.getTitle());
        //holder.mTextViewDescription.setText(blog.getDescription());
        holder.mTextViewDescription.setText(HtmlUtils.removeHtml(blog.getDescription()));
        Picasso.with(mActivity)
                .load(blog.getImage_url())
                .into(holder.mImageView);
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //SharedPreferenceUtils.saveIntValue(mContext, "url_id", -1);
                SharedPreferenceUtils.saveLongValue(mContext, "url_id", (int) blog.getId());
                Intent mSingleBlogActivity = new Intent(mContext, SingleBlogActivity.class);
                mContext.startActivity(mSingleBlogActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mData == null){
            return 0;
        }
        return mData.size();
    }




    public class PostHolder extends RecyclerView.ViewHolder{

        public TextView mTextViewTitle;
        public TextView mTextViewDescription;
        public ImageView mImageView;
        public LinearLayout mLinearLayout;

        public PostHolder(View itemView) {
            super(itemView);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            mTextViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }
}

