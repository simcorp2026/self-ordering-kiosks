package com.emprendimiento.multiproductosmascotas.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emprendimiento.multiproductosmascotas.Activity.ListCategoryActivity;
import com.emprendimiento.multiproductosmascotas.Domain.CategoryOne;
import com.emprendimiento.multiproductosmascotas.R;

import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewholder> {

    ArrayList<CategoryOne> items;
    Context context;

    public CategoryAdapter(ArrayList<CategoryOne> items){
        this.items = items;
    }
    @NonNull
    @Override
    public CategoryAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context  = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category_one,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.viewholder holder, int position) {
        holder.tittleTxt.setText(items.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListCategoryActivity.class);
                //estos son de category one
                intent.putExtra("CategoryOneId",items.get(position).getId());
                intent.putExtra("CategoryName",items.get(position).getName());
                context.startActivity(intent);
            }
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
            tittleTxt=itemView.findViewById(R.id.catNameTxt);
        }
    }

}
