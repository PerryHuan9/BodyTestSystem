package com.example.perry.yoursidesystem.fragment.heathfragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.perry.yoursidesystem.R;
import com.example.perry.yoursidesystem.test.LogUtil;

import java.util.List;

/**
 * Created by perry on 2017/12/18.
 */

public class HeathAdapter extends RecyclerView.Adapter<HeathAdapter.ViewHolder> {
    public final static String TITLE_ARG="title";
    private List<HeathTitle> titleList;
    private Context context;

    public HeathAdapter(List<HeathTitle> titleList,Context context) {
//        this.context=context;
        this.titleList = titleList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        ImageView imageView1, imageView2, imageView3;
        View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            titleView = view.findViewById(R.id.heath_question);
            imageView1=view.findViewById(R.id.heath_image1);
            imageView2=view.findViewById(R.id.heath_image2);
            imageView3=view.findViewById(R.id.heath_image3);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context=parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout
                .recycler_item, parent, false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HeathTitle title=titleList.get(viewHolder.getAdapterPosition());
                Intent intent=new Intent("com.example.perry.yoursidessystem.Heath");
                intent.putExtra(TITLE_ARG,title.getTitle());
                context.startActivity(intent);
            }
        });
        LogUtil.v("test","into onCreateViewHolder");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LogUtil.v("test","into onBindViewHolder");
        HeathTitle title=titleList.get(position);
        holder.titleView.setText(context.getString(title.getTitle()));
        Glide.with(context).load(title.getImage1()).into(holder.imageView1);
        Glide.with(context).load(title.getImage2()).into(holder.imageView2);
        Glide.with(context).load(title.getImage3()).into(holder.imageView3);
        
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }


}
