package com.example.ratatouille23client.viewModel.orderManagement.acceptedOrder;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ratatouille23client.RetrofitInstance;
import com.example.ratatouille23client.api.OrderAPI;
import com.example.ratatouille23client.caching.RAT23Cache;
import com.example.ratatouille23client.model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptedOrdersViewModel extends ViewModel {

    private MutableLiveData<List<Order>> acceptedOrdersList;

    public AcceptedOrdersViewModel(){
        acceptedOrdersList = new MutableLiveData<>();
    }

    public MutableLiveData<List<Order>> getAcceptedOrdersList() {
        return acceptedOrdersList;
    }

    public void callAcceptedOrders(){
        RAT23Cache cache = RAT23Cache.getCacheInstance();
        Long storeId = 1L;

        OrderAPI orderAPI = RetrofitInstance.getRetrofitInstance().create(OrderAPI.class);
        Call<List<Order>> call = orderAPI.getAllAcceptedOrders(storeId);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                acceptedOrdersList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                acceptedOrdersList.postValue(null);
                Log.e("Error: ",t.getMessage().toString());
            }
        });
    }
}