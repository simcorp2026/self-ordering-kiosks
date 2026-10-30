package com.emprendimiento.multiproductosmascotas.Domain;

public class CategoryTwo {
    private int Id;
    private String Name;
    private int CategoryOneId;
    public CategoryTwo(){

    }
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getIdCategoryOne() {
        return CategoryOneId;
    }

    public void setIdCategoryOne(int idCategoryOne) {
        CategoryOneId = idCategoryOne;
    }
}
