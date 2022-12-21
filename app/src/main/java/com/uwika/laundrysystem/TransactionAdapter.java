package com.uwika.laundrysystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ShopModel> modelShop;
    private String URL = "https://andreditvirs.com/laundry-aje/";

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtV_title;
        TextView txtV_open_close_time;
        ImageView imgV_cover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtV_title = itemView.findViewById(R.id.txtV_grid_journal_item_title);
            txtV_open_close_time = itemView.findViewById(R.id.txtV_open_close_time);
            imgV_cover = itemView.findViewById(R.id.imgV_grid_journal_item_journal);
        }
    }

    TransactionAdapter(Context context, ArrayList<ShopModel> data) {
        this.context = context;
        this.modelShop = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_shops_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailShopActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uuid", modelShop.get(holder.getAdapterPosition()).getUuid());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        TextView txtV_title = holder.txtV_title;
        ImageView imgV_cover = holder.imgV_cover;
        TextView txtV_time = holder.txtV_open_close_time;

        txtV_time.setText("Waktu buka : "+ new String(modelShop.get(position).getOpen_time()).substring(0, 5) + " s/d " + new String(modelShop.get(position).getClose_time()).substring(0, 5));
        txtV_title.setText(modelShop.get(position).getName());
        Glide.with(context).load(URL + modelShop.get(position).getImage_url()).into(imgV_cover);
    }

    @Override
    public int getItemCount() {
        return modelShop.size();
    }

}
