package com.mmsstudio.htmldesignsample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private SlideItem[] items;
    private LayoutInflater inflater;

    public SliderAdapter(Context ctx, SlideItem[] items) {
        this.items = items;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(inflater.inflate(R.layout.item_slide, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder h, int pos) {
        SlideItem item = items[pos];
        h.icon.setImageResource(item.iconRes);
        h.title.setText(item.title);
        h.desc.setText(item.desc);
        h.iconContainer.startAnimation(AnimationUtils.loadAnimation(
                h.iconContainer.getContext(), item.animRes));
    }

    @Override public int getItemCount() { return items.length; }

    static class SliderViewHolder extends RecyclerView.ViewHolder {
        View iconContainer;
        ImageView icon;
        TextView title, desc;
        SliderViewHolder(View v) {
            super(v);
            iconContainer = v.findViewById(R.id.iconContainer);
            icon = v.findViewById(R.id.icon);
            title = v.findViewById(R.id.title);
            desc  = v.findViewById(R.id.desc);
        }
    }
}
