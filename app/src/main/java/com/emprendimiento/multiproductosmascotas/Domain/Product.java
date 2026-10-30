package com.emprendimiento.multiproductosmascotas.Domain;

import java.io.Serializable;

public class Product implements Serializable {
    private int CategoryOneId;
    private int CategoryTwoId;
    private int Id;
    private boolean BestProduct;
    private int numberInCart;
    private String Mark;

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public String getMark() {
        return Mark;
    }

    public void setMark(String mark) {
        Mark = mark;
    }

    public boolean isBestProduct() {
        return BestProduct;
    }

    public void setBestProduct(boolean bestProduct) {
        BestProduct = bestProduct;
    }

    private String Description;
    private String ImagePath;
    private double Price;
    private String Title;

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }

    private int Stock;
    public Product(){

    }

    @Override
    public String toString() {
        return Title;
    }

    public int getCategoryOneId() {
        return CategoryOneId;
    }

    public void setCategoryOneId(int categoryOneId) {
        CategoryOneId = categoryOneId;
    }

    public int getCategoryTwoId() {
        return CategoryTwoId;
    }

    public void setCategoryTwoId(int categoryTwoId) {
        CategoryTwoId = categoryTwoId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }


    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        this.Price = price;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }
}
