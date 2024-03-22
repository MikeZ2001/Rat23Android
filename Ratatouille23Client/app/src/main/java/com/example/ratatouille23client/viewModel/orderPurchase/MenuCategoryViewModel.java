package com.example.ratatouille23client.viewModel.orderPurchase;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ratatouille23client.RetrofitInstance;
import com.example.ratatouille23client.api.CategoryAPI;
import com.example.ratatouille23client.caching.RAT23Cache;
import com.example.ratatouille23client.model.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuCategoryViewModel extends ViewModel {

    private MutableLiveData<List<Category>> categoryList;

    private MutableLiveData<String> errorMessage;

    public MenuCategoryViewModel(){
        categoryList = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public MutableLiveData<List<Category>> getCategoryListObserver(){
        return categoryList;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void callGetAllCategories(){
        RAT23Cache cache = RAT23Cache.getCacheInstance();
        Long storeId = Long.valueOf(cache.get("currentStore").toString());

        CategoryAPI categoryAPI = RetrofitInstance.getRetrofitInstance().create(CategoryAPI.class);
        Call<List<Category>> call = categoryAPI.getAllCategories(storeId);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                categoryList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                categoryList.postValue(null);
                errorMessage.postValue(t.getMessage());
                Log.e("Error: ",t.getMessage().toString());
            }
        });
    }

}
