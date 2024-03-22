package com.example.ratatouille23client.viewModel.orderPurchase;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.model.OrderItem;


public class ProductDetailViewModel extends ViewModel {


    private MutableLiveData<Order> order;
    private MutableLiveData <OrderItem> orderItemMutableLiveData;

    public ProductDetailViewModel(){
        order = new MutableLiveData<>();
        orderItemMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Order> getOrderObserver(){
        return order;
    }

    public MutableLiveData<OrderItem> getOrderItemObserver(){
        return orderItemMutableLiveData;
    }


}