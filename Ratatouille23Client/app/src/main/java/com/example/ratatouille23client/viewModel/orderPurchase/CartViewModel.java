package com.example.ratatouille23client.viewModel.orderPurchase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ratatouille23client.RetrofitInstance;
import com.example.ratatouille23client.api.OrderAPI;
import com.example.ratatouille23client.api.OrderItemAPI;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.model.OrderItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartViewModel extends ViewModel {

    private MutableLiveData<List<OrderItem>> orderItemByOrderIdList;

    private MutableLiveData <Order> order;

    private MutableLiveData <Order> updatedOrder;


    public CartViewModel(){

        orderItemByOrderIdList = new MutableLiveData<>();

        order = new MutableLiveData<>();

        updatedOrder = new MutableLiveData<>();
    }

    public MutableLiveData<List<OrderItem>> getOrderItemByOrderIdList(){
        return orderItemByOrderIdList;
    }

    public MutableLiveData<Order> getOrderById(){
        return order;
    }


    public MutableLiveData<Order> getUpdatedOrder(){
        return updatedOrder;
    }


    public void callGetOrderItemByOrderId(Long id){
        OrderItemAPI orderItemAPI = RetrofitInstance.getRetrofitInstance().create(OrderItemAPI.class);
        Call<List<OrderItem>> call = orderItemAPI.getAllOrderItemByOrderId(id);
        call.enqueue(new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                orderItemByOrderIdList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {
                orderItemByOrderIdList.postValue(null);
                Log.e("Error: ",t.getMessage().toString());
            }
        });
    }

    public void callCreateOrder(Context context, Order order){
        Retrofit retrofitInstance = RetrofitInstance.getRetrofitInstance();
        OrderAPI api = retrofitInstance.create(OrderAPI.class);
        Call<Order> call = api.addOrder(order);

        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
              updatedOrder.postValue(response.body());
              Toast.makeText(context,"L'ordine è stato emesso correttamente",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                updatedOrder.postValue(null);
                Toast.makeText(context, "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                Log.e("Error: updateOrder ",t.getLocalizedMessage());
            }
        });
    }

    public void callUpdateOrderItem(Context context,Long id,OrderItem orderItem){
        OrderItemAPI orderItemAPI = RetrofitInstance.getRetrofitInstance().create(OrderItemAPI.class);
        Call<Void> call = orderItemAPI.updateOrderItem(id,orderItem);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(context, "Prodotto modificato con successo.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                orderItemByOrderIdList.postValue(null);
                Toast.makeText(context, "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();

                Log.e("Error: ",t.getMessage().toString());
            }
        });
    }

    public void callDeleteOrderItemById(Context context, Long id){
        OrderItemAPI orderItemAPI = RetrofitInstance.getRetrofitInstance().create(OrderItemAPI.class);
        Call<Void> call = orderItemAPI.deleteOrderItemById(id);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Toast.makeText(context, "Prodotto eliminato con successo!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                orderItemByOrderIdList.postValue(null);
               // Toast.makeText(context, "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}