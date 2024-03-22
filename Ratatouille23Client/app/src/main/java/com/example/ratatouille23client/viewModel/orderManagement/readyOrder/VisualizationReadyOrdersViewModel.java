package com.example.ratatouille23client.viewModel.orderManagement.readyOrder;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ratatouille23client.RetrofitInstance;
import com.example.ratatouille23client.api.OrderAPI;
import com.example.ratatouille23client.model.Order;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisualizationReadyOrdersViewModel extends ViewModel {

    private MutableLiveData<Order> updatedOrder;

    public VisualizationReadyOrdersViewModel(){
        updatedOrder = new MutableLiveData<>();
    }

    public MutableLiveData<Order> getUpdatedOrder() {
        return updatedOrder;
    }

    public void callUpdateOrder(Order updateOrder){

        OrderAPI orderItemAPI = RetrofitInstance.getRetrofitInstance().create(OrderAPI.class);
        Call<Order> call = orderItemAPI.updateOrderById(updateOrder.getId(),updateOrder);

        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                updatedOrder.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                updatedOrder.postValue(null);
                Log.e("Error: updateOrder ",t.getLocalizedMessage());
            }
        });
    }
}