package com.example.ratatouille23client.api;

import com.example.ratatouille23client.model.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryAPI {

    String BASE_ENDPOINT = "api/v1/category";

    @GET(BASE_ENDPOINT + "/getByStore/{storeId}")
    Call<List<Category>> getAllCategories(@Path("storeId") Long id);
}
