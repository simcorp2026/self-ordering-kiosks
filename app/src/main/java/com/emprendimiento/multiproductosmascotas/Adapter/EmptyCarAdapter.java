package com.emprendimiento.multiproductosmascotas.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emprendimiento.multiproductosmascotas.Domain.CategoryOne;
import com.emprendimiento.multiproductosmascotas.R;

import java.util.ArrayList;

public class EmptyCarAdapter extends RecyclerView.Adapter<EmptyCarAdapter.viewholder> {

    ArrayList<CategoryOne> items;
    Context context;

    public EmptyCarAdapter(ArrayList<CategoryOne> items){
        this.items = items;
    }
    @NonNull
    @Override
    public EmptyCarAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context  = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_carito_vacio,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull EmptyCarAdapter.viewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder{
        TextView tittleTxt;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            tittleTxt=itemView.findViewById(R.id.catNameTxt2);
        }
    }

}
