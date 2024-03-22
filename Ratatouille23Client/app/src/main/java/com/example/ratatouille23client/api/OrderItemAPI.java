package com.example.ratatouille23client.api;

import com.example.ratatouille23client.model.OrderItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrderItemAPI {
    String BASE_ENDPOINT = "api/v1/orderItem";

    @GET(BASE_ENDPOINT + "/getAllByOrderId/{id}")
    Call<List<OrderItem>> getAllOrderItemByOrderId(@Path("id")Long id);

    @PUT(BASE_ENDPOINT + "/update/{id}")
    Call<Void> updateOrderItem(@Path("id") Long id,@Body OrderItem orderItem);

    @PUT(BASE_ENDPOINT + "/updateOrderItemStatus/{id}")
    Call<Void> updateOrderItemStatus(@Path("id") Long id,@Body OrderItem orderItem);

    @DELETE(BASE_ENDPOINT + "/delete/{id}")
    Call<Void>deleteOrderItemById(@Path("id") Long id);
}
