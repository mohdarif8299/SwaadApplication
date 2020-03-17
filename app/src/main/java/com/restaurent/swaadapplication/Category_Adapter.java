package com.restaurent.swaadapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.ViewHolder> {
    private Context ctx;
    private List<Categroy_Items> list;
    public Category_Adapter(List<Categroy_Items> listFeed, Context context) {
        this.ctx = context;
        this.list = listFeed;
    }
    @NonNull
    @Override
    public Category_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.category_item, parent, false);
        Category_Adapter.ViewHolder viewHolder = new Category_Adapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Category_Adapter.ViewHolder holder, int position) {
        Categroy_Items items = list.get(holder.getAdapterPosition());
        Glide.with(ctx).load(items.getImage()).fitCenter().centerCrop().into(holder.item_image);
        holder.item_name.setText(items.getName());
    }
    @Override
    public int getItemCount() { return list.size(); }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView item_image;
        public TextView item_name;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.item_image = itemView.findViewById(R.id.category_image);
            this.item_name = itemView.findViewById(R.id.category_name);
            this.linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
