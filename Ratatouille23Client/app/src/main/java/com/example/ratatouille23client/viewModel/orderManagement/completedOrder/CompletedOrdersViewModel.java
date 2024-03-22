package com.example.ratatouille23client.viewModel.orderManagement.completedOrder;

import android.util.Log;

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

public class CompletedOrdersViewModel extends ViewModel {
    private MutableLiveData<List<Order>> completedOrdersList;

    public CompletedOrdersViewModel(){
        completedOrdersList = new MutableLiveData<>();
    }

    public MutableLiveData<List<Order>> getCompletedOrdersList() {
        return completedOrdersList;
    }

    public void callCompletedOrders(){
        RAT23Cache cache = RAT23Cache.getCacheInstance();
        Long storeId = Long.valueOf(cache.get("currentStore").toString());

        OrderAPI orderAPI = RetrofitInstance.getRetrofitInstance().create(OrderAPI.class);
        Call<List<Order>> call = orderAPI.getAllCompletedOrders(storeId);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                completedOrdersList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                completedOrdersList.postValue(null);
                Log.e("Error: ",t.getMessage().toString());
            }
        });
    }
}