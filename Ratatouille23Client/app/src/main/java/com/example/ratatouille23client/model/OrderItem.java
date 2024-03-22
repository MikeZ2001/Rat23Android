package com.example.ratatouille23client.model;


public class OrderItem {

    public enum OrderItemStatus{
        READY, NOT_READY
    }

    private Long id;

    private Integer quantity;

    private String particularRequests;

    private Product product;

    private OrderItemStatus orderItemStatus;

    public OrderItem(){}

    public OrderItem(Long id, Integer quantity, String particularRequests, Product product, OrderItemStatus orderItemStatus) {
        this.id = id;
        this.quantity = quantity;
        this.particularRequests = particularRequests;
        this.product = product;
        this.orderItemStatus = orderItemStatus;
    }

    public OrderItemStatus getOrderItemStatus() {
        return orderItemStatus;
    }

    public void setOrderItemStatus(OrderItemStatus orderItemStatus) {
        this.orderItemStatus = orderItemStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getParticularRequests() {
        return particularRequests;
    }

    public void setParticularRequests(String particularRequests) {
        this.particularRequests = particularRequests;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", product=" + product +
                '}';
    }
}
