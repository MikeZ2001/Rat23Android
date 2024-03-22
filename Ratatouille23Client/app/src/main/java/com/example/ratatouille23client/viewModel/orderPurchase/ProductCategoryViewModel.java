package com.example.ratatouille23client.viewModel.orderPurchase;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ratatouille23client.RetrofitInstance;
import com.example.ratatouille23client.api.OrderAPI;
import com.example.ratatouille23client.api.OrderItemAPI;
import com.example.ratatouille23client.api.ProductAPI;
import com.example.ratatouille23client.caching.RAT23Cache;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.model.OrderItem;
import com.example.ratatouille23client.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCategoryViewModel extends ViewModel {

    private MutableLiveData<List<Product>> productList;

    private MutableLiveData <Order> order;

    private MutableLiveData <OrderItem> orderItemMutableLiveData;

    private MutableLiveData<String> errorMessage;


    public ProductCategoryViewModel(){
        productList = new MutableLiveData<>();
        order = new MutableLiveData<>();
        orderItemMutableLiveData = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public MutableLiveData<List<Product>> getProductListObserver(){
        return productList;
    }

    public MutableLiveData<Order> getOrderObserver(){
        return order;
    }

    public MutableLiveData<OrderItem> getOrderItemObserver(){
        return orderItemMutableLiveData;
    }


    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void getProductsByCategoryId(Long id){
        RAT23Cache cache = RAT23Cache.getCacheInstance();
        Long storeId = Long.valueOf(cache.get("currentStore").toString());

        ProductAPI productAPI = RetrofitInstance.getRetrofitInstance().create(ProductAPI.class);
        Call<List<Product>> call = productAPI.getAllByCategoryId(id, storeId);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                productList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                productList.postValue(null);
                errorMessage.postValue(t.getMessage());
                Log.e("Error: ",t.getMessage().toString());
            }
        });
    }
}
