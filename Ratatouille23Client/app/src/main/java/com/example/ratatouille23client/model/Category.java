package com.example.ratatouille23client.model;


import java.util.Collection;
import java.util.Objects;

public class Category {

    private Long id;

    private String name;
    private String description;


    private Collection<Product> productsOfTheCategory;

    private Store store;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Collection<Product> getProductsOfTheCategory() {
        return productsOfTheCategory;
    }

    public void setProductsOfTheCategory(Collection<Product> productsOfTheCategory) {
        this.productsOfTheCategory = productsOfTheCategory;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", productsOfTheCategory=" + productsOfTheCategory +
                ", store=" + store +
                '}';
    }
}
