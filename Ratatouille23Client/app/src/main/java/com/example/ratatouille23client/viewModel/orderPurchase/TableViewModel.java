package com.example.ratatouille23client.viewModel.orderPurchase;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ratatouille23client.RetrofitInstance;
import com.example.ratatouille23client.api.TableAPI;
import com.example.ratatouille23client.caching.RAT23Cache;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.model.Table;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableViewModel extends ViewModel {

    private MutableLiveData<List<Table>> tableList;

    private MutableLiveData<Order> order;

    private MutableLiveData<String> mErrorMessage;

    public TableViewModel(){
        mErrorMessage = new MutableLiveData<>();
        tableList = new MutableLiveData<>();
        order = new MutableLiveData<>();
    }

    public MutableLiveData<List<Table>> getTableListObserver() {
        return tableList;
    }

    public MutableLiveData<String> getmErrorMessage() {
        return mErrorMessage;
    }

    public void getAllAvailableTables(){
        RAT23Cache cache = RAT23Cache.getCacheInstance();
        Long storeId = 1L;

        TableAPI tableAPI = RetrofitInstance.getRetrofitInstance().create(TableAPI.class);
        Call<List<Table>> call = tableAPI.getAllAvailableTables(storeId);
        call.enqueue(new Callback<List<Table>>() {
            @Override
            public void onResponse(Call<List<Table>> call, Response<List<Table>> response) {
                tableList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Table>> call, Throwable t) {
                tableList.postValue(null);
                mErrorMessage.postValue(t.getMessage());
                Log.e("Error: ",t.getMessage().toString());
            }
        });
    }

    public MutableLiveData<Order> getCreatedOrder() {
        return order;
    }
}