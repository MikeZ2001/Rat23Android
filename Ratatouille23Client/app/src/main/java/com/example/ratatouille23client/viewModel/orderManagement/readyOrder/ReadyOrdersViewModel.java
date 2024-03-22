package com.example.ratatouille23client.viewModel.orderManagement.readyOrder;

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

public class ReadyOrdersViewModel extends ViewModel {

    private MutableLiveData<List<Order>> readyOrdersList;

    public ReadyOrdersViewModel(){
        readyOrdersList = new MutableLiveData<>();
    }

    public MutableLiveData<List<Order>> getReadyOrdersList() {
        return readyOrdersList;
    }

    public void callReadyOrders(){
        RAT23Cache cache = RAT23Cache.getCacheInstance();
        Long storeId = Long.valueOf(cache.get("currentStore").toString());

        OrderAPI orderAPI = RetrofitInstance.getRetrofitInstance().create(OrderAPI.class);
        Call<List<Order>> call = orderAPI.getAllReadyOrders(storeId);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                readyOrdersList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                readyOrdersList.postValue(null);
                Log.e("Error: ",t.getMessage().toString());
            }
        });
    }
}