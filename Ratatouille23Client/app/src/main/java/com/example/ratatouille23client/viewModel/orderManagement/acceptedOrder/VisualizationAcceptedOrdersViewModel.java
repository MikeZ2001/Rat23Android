package com.example.ratatouille23client.viewModel.orderManagement.acceptedOrder;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ratatouille23client.RetrofitInstance;
import com.example.ratatouille23client.api.OrderAPI;
import com.example.ratatouille23client.api.OrderItemAPI;
import com.example.ratatouille23client.api.TableAPI;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.model.OrderItem;
import com.example.ratatouille23client.model.Table;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisualizationAcceptedOrdersViewModel extends ViewModel {

    private MutableLiveData<Order> updatedOrder;

    private MutableLiveData<Table> table;

    public VisualizationAcceptedOrdersViewModel(){
        updatedOrder = new MutableLiveData<>();
        table = new MutableLiveData<>();
    }


    public MutableLiveData<Order> getUpdatedOrder() {
        return updatedOrder;
    }

    public MutableLiveData<Table> getUpdatedTable() {
        return table;
    }

    public void callUpdateOrder(Order updateOrder){

        OrderAPI orderItemAPI = RetrofitInstance.getRetrofitInstance().create(OrderAPI.class);
        Call<Order> call = orderItemAPI.updateOrderById(updateOrder.getId(),updateOrder);

        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                updatedOrder.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                updatedOrder.postValue(null);
                Log.e("Error: updateOrder ",t.getLocalizedMessage());
            }
        });
    }

    public void updateTable(Table updatedTable) {

        updatedTable.setAvailable(true);

        TableAPI tableAPI = RetrofitInstance.getRetrofitInstance().create(TableAPI.class);
        Call<Table> call = tableAPI.updateTable((long) updatedTable.getId(),updatedTable);
        call.enqueue(new Callback<Table>() {
            @Override
            public void onResponse(Call<Table>call, Response<Table> response) {
                table.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Table> call, Throwable t) {
                table.postValue(null);
                Log.e("Error: ",t.getMessage().toString());
            }
        });
    }

    public void updateOrderItemStatus(OrderItem orderItem) {

        OrderItemAPI orderItemAPI = RetrofitInstance.getRetrofitInstance().create(OrderItemAPI.class);
        Call<Void> call = orderItemAPI.updateOrderItemStatus(orderItem.getId(), orderItem);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                Log.e("Error: ",t.getMessage().toString());
            }
        });
    }
}