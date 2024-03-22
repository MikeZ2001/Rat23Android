package com.example.ratatouille23client.viewModel.orderManagement.payedOrder;

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

public class PayedOrderViewModel extends ViewModel {

    private MutableLiveData<List<Order>> payedOrdersList;

    public PayedOrderViewModel(){
        payedOrdersList = new MutableLiveData<>();
    }

    public MutableLiveData<List<Order>> getPayedOrderList() {
        return payedOrdersList;
    }

    public void callPayedOrders(){
        RAT23Cache cache = RAT23Cache.getCacheInstance();
        Long storeId = Long.valueOf(cache.get("currentStore").toString());

        OrderAPI orderAPI = RetrofitInstance.getRetrofitInstance().create(OrderAPI.class);
        Call<List<Order>> call = orderAPI.getAllPayedOrders(storeId);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                payedOrdersList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                payedOrdersList.postValue(null);
                Log.e("Error: ",t.getMessage().toString());
            }
        });
    }
}