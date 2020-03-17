package com.restaurent.swaadapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class ItemsAdapter  extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> implements Filterable {
    private Context ctx;
    private List<Items> list;
    private List<Items> listFull;
    public ItemsAdapter(List<Items> listFeed, Context context) {
        this.ctx = context;
        this.list = listFeed;
        this.listFull = new ArrayList<>(list);
    }
    @NonNull
    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.items, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ViewHolder holder, int position) {
        Items items = list.get(holder.getAdapterPosition());
        Glide.with(ctx).load(items.getItem_image()).fitCenter().centerCrop().into(holder.item_image);
        holder.item_name.setText(items.getItem_name());
        holder.item_category.setText("In "+items.getItem_category());
        holder.item_price.setText("\u20B9 "+items.getItem_price());
        String item_type= items.getItem_type();
        if(Objects.equals("veg",item_type)){
            holder.typeVeg.setVisibility(View.VISIBLE);
            holder.typeNonVeg.setVisibility(View.GONE);
        }
        else {
            holder.typeVeg.setVisibility(View.GONE);
            holder.typeNonVeg.setVisibility(View.VISIBLE);
        }
        holder.linearLayout.setOnClickListener(view->{
            Intent intent = new Intent(ctx,SnacksActivity.class);
            intent.putExtra("item_name",holder.item_name.getText().toString());
            intent.putExtra("item_category",items.getItem_category());
            intent.putExtra("item_image",items.getItem_image());
            intent.putExtra("item_price",items.getItem_price());
            intent.putExtra("item_type",items.getItem_type());
            intent.putExtra("item_status",items.getItem_status());
            ctx.startActivity(intent);
        });
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView item_image;
        public TextView item_name,item_category,item_price,item_status;
        public LinearLayout linearLayout,typeVeg,typeNonVeg;
        public ViewHolder(View itemView) {
            super(itemView);
            this.item_image = itemView.findViewById(R.id.item_image);
            this.item_name = itemView.findViewById(R.id.item_name);
            this.item_category = itemView.findViewById(R.id.item_category);
            this.item_price = itemView.findViewById(R.id.item_price);
            this.typeVeg = itemView.findViewById(R.id.item_type);
            this.typeNonVeg = itemView.findViewById(R.id.item_type2);
            this.linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

    @Override
    public int getItemCount() {  return list.size(); }
    @Override
    public Filter getFilter() { return exampleFilter; }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Items> filteredList = new ArrayList<>();
            if (charSequence==null||charSequence.length()==0){
                filteredList.addAll(listFull);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Items items:listFull){
                    if(items.getItem_name().toLowerCase().contains(filterPattern)){
                        filteredList.add(items);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };
}
