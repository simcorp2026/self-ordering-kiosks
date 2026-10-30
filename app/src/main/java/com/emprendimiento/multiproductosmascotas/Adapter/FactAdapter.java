package com.emprendimiento.multiproductosmascotas.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.emprendimiento.multiproductosmascotas.Domain.Product;
import com.emprendimiento.multiproductosmascotas.Helper.ChangeNumberItemsListener;
import com.emprendimiento.multiproductosmascotas.Helper.ManagmentCart;
import com.emprendimiento.multiproductosmascotas.R;

import java.util.ArrayList;

public class FactAdapter extends RecyclerView.Adapter<FactAdapter.viewholder>{
    ArrayList<Product> list = new ArrayList<>();
    private ManagmentCart managmentCart;
    ChangeNumberItemsListener changeNumberItemsListener;

    public FactAdapter(ArrayList<Product> list, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.list = list;
        managmentCart = new ManagmentCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }
    @NonNull
    @Override
    public FactAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_item_fact,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FactAdapter.viewholder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.feeEachItem.setText("$"+(list.get(position).getNumberInCart()*list.get(position).getPrice()));
        holder.totalEachItem.setText("$"+list.get(position).getPrice());
        holder.num.setText(list.get(position).getNumberInCart()+"");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView title,feeEachItem;
        TextView totalEachItem,num;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.titleFTxt);
            feeEachItem=itemView.findViewById(R.id.feeFEachitem);
            totalEachItem=itemView.findViewById(R.id.totalFaEachitem);
            num=itemView.findViewById(R.id.numberItemFTxt);
        }
    }
    public ArrayList<String> getProductDetails() {
        ArrayList<String> productDetails = new ArrayList<>();
        for (Product product : list) {
            String detail = "Nombre: " + product.getTitle() + ", Cantidad: " + product.getNumberInCart() + ", Precio: $" + product.getPrice();
            productDetails.add(detail);
        }
        return productDetails;
    }

}
