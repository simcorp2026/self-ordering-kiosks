package com.emprendimiento.multiproductosmascotas.Helper;

import android.content.Context;
import android.widget.Toast;

import com.emprendimiento.multiproductosmascotas.Domain.Product;

import java.util.ArrayList;


public class ManagmentCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB=new TinyDB(context);
    }

    public void insertFood(Product item) {
        ArrayList<Product> listpop = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int i = 0; i < listpop.size(); i++) {
            if (listpop.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = i;
                break;
            }
        }
        if(existAlready){
            listpop.get(n).setNumberInCart(item.getNumberInCart());
        }else{
            listpop.add(item);
        }
        tinyDB.putListObject("CartList",listpop);
        //Toast.makeText(context, "Agregado al carrito", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Product> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    public Double getTotalFee(){
        ArrayList<Product> listItem=getListCart();
        double fee=0;
        for (int i = 0; i < listItem.size(); i++) {
            fee=fee+(listItem.get(i).getPrice()*listItem.get(i).getNumberInCart());
        }
        return fee;
    }
    public void minusNumberItem(ArrayList<Product> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        if(listItem.get(position).getNumberInCart()==1){
            listItem.remove(position);
        }else{
            listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()-1);
        }
        tinyDB.putListObject("CartList",listItem);
        changeNumberItemsListener.change();
    }
    public  void plusNumberItem(ArrayList<Product> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()+1);
        tinyDB.putListObject("CartList",listItem);
        changeNumberItemsListener.change();
    }
    // Nuevo método para vaciar el carrito
    public void clearCart() {
        // ArrayList<Product> listpop = getListCart();
        ArrayList<Product> emptyList = new ArrayList<>();
        tinyDB.putListObject("CartList", emptyList);
        //Toast.makeText(context,"Carrito vaciado",Toast.LENGTH_SHORT).show();
        //Toast.makeText(context, "Carrito vaciado", Toast.LENGTH_SHORT).show();
    }
    public void removeFromCart(ArrayList<Product> list, int position) {
        list.remove(position);
        tinyDB.putListObject("CartList", list);
    }

    public void removeFroms(ArrayList<Product> list, int position){
        list.remove(position);

    }


}
