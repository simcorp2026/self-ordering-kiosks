package com.emprendimiento.multiproductosmascotas.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emprendimiento.multiproductosmascotas.Activity.ListProductsActivity;
import com.emprendimiento.multiproductosmascotas.Domain.CategoryTwo;
import com.emprendimiento.multiproductosmascotas.R;

import java.util.ArrayList;

public class CategoryAdapterTwo extends RecyclerView.Adapter<CategoryAdapterTwo.viewholder> {

    ArrayList<CategoryTwo> items;
    Context context;

    public CategoryAdapterTwo(ArrayList<CategoryTwo> items){
        this.items = items;
    }
    @NonNull
    @Override
    public CategoryAdapterTwo.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context  = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_category_two,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapterTwo.viewholder holder, int position) {
        holder.tittleTxt.setText(items.get(position).getName());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ListProductsActivity.class);
            //estos datos son de Category
            intent.putExtra("CategoryId",items.get(position).getId());
            intent.putExtra("CategoryName",items.get(position).getName());
            context.startActivity(intent);
        });



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
