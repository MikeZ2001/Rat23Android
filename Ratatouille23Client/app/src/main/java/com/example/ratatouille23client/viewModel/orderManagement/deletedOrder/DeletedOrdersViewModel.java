package com.example.ratatouille23client.viewModel.orderManagement.deletedOrder;

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

public class DeletedOrdersViewModel extends ViewModel {

    private MutableLiveData<List<Order>> deletedOrdersList;

    public DeletedOrdersViewModel(){
        deletedOrdersList = new MutableLiveData<>();
    }

    public MutableLiveData<List<Order>> getDeletedOrdersList() {
        return deletedOrdersList;
    }

    public void callDeletedOrders(){
        RAT23Cache cache = RAT23Cache.getCacheInstance();
        Long storeId = Long.valueOf(cache.get("currentStore").toString());

        OrderAPI orderAPI = RetrofitInstance.getRetrofitInstance().create(OrderAPI.class);
        Call<List<Order>> call = orderAPI.getAllDeletedOrders(storeId);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                deletedOrdersList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                deletedOrdersList.postValue(null);
                Log.e("Error: ",t.getMessage().toString());
            }
        });
    }
}