package com.example.ratatouille23client.api;

import com.example.ratatouille23client.model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrderAPI {
    String BASE_ENDPOINT = "api/v1/order";

    @POST(BASE_ENDPOINT + "/add")
    Call<Order>addOrder(@Body Order order);

    @PUT(BASE_ENDPOINT + "/update/{id}")
    Call<Order>updateOrderById(@Path("id")Long id, @Body Order order);

    @GET(BASE_ENDPOINT + "/{storeId}/getAllAcceptedOrders")
    Call<List<Order>>getAllAcceptedOrders(@Path("storeId") Long storeId);

    @GET(BASE_ENDPOINT + "/{storeId}/getAllReadyOrders")
    Call<List<Order>>getAllReadyOrders(@Path("storeId") Long storeId);

    @GET(BASE_ENDPOINT + "/{storeId}/getAllCompletedOrders")
    Call<List<Order>>getAllCompletedOrders(@Path("storeId") Long storeId);

    @GET(BASE_ENDPOINT + "/{storeId}/getAllPayedOrders")
    Call<List<Order>>getAllPayedOrders(@Path("storeId") Long storeId);

    @GET(BASE_ENDPOINT + "/{storeId}/getAllDeletedOrders")
    Call<List<Order>>getAllDeletedOrders(@Path("storeId") Long storeId);

    @GET(BASE_ENDPOINT + "/{storeId}/getAllInProgressOrders")
    Call<List<Order>> getAllInProgressOrders(@Path("storeId") Long storeId);
}
