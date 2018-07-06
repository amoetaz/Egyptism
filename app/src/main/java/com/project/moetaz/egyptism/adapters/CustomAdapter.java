package com.project.moetaz.egyptism.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.moetaz.egyptism.R;
import com.project.moetaz.egyptism.interfaces.SiteListener;
import com.project.moetaz.egyptism.models.Region;
import com.project.moetaz.egyptism.models.Site;
import com.project.moetaz.egyptism.ui.activities.RegionActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private SiteListener siteListener;
    private Context context;
    private List<? extends Region> list = new ArrayList<>();

    public CustomAdapter(Context context, List<? extends Region> list, SiteListener siteListener) {
        this.context = context;
        this.list = list;
        this.siteListener = siteListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if (list.get(position) != null && !(list.get(position) instanceof Site)) {
            final Region region = list.get(position);
            holder.textView.setText(region.getName());
            Picasso.get().load(region.getImage()).error(R.drawable.avatar).into(holder.imageView);
            holder.imageView.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, RegionActivity.class);
                    intent.putExtra("regionName", region.getName());
                    context.startActivity(intent);
                }

            });
        } else {
            final Site site = (Site) list.get(position);
            holder.mView.setTag(site.getName() + "&" + position);
            holder.textView.setText(site.getName());
            Picasso.get().load(site.getImage()).error(R.drawable.avatar).into(holder.imageView);
            holder.imageView.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    siteListener.onSiteClickedListener(getSiteBundle(site.getImage(), site.getDesc()
                            , site.getName(), site.getLatLong()), holder.imageView);
                }

            });
        }

    }

    private Bundle getSiteBundle(String image, String desc, String name, String latLong) {
        Bundle bundle = new Bundle();
        bundle.putString(context.getString(R.string.image_envelope), image);
        bundle.putString(context.getString(R.string.desc_envelope), desc);
        bundle.putString(context.getString(R.string.name_envelope), name);
        bundle.putString(context.getString(R.string.latong_envelope), latLong);
        return bundle;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        View mView;

        MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            textView = itemView.findViewById(R.id.region_name);
            imageView = itemView.findViewById(R.id.region_image);

        }
    }
}
