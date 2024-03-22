package com.example.ratatouille23client.api;

import com.example.ratatouille23client.model.Product;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductAPI {
    String BASE_ENDPOINT = "api/v1/product";

    @GET(BASE_ENDPOINT + "/{storeId}/getAllByCategoryId/{id}")
    Call<List<Product>> getAllByCategoryId(@Path("id") Long id, @Path("storeId") Long storeId);
}
