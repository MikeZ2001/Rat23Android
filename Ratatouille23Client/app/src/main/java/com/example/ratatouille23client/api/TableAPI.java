package com.example.ratatouille23client.api;

import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.model.Table;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TableAPI {

    String BASE_ENDPOINT = "api/v1/table";

    @GET(BASE_ENDPOINT + "/getAllAvailableTables/{storeId}")
    Call<List<Table>> getAllAvailableTables(@Path("storeId") Long id);

    @PUT(BASE_ENDPOINT + "/update/{tableId}")
    Call<Table> updateTable(@Path("tableId") Long id,@Body Table updatedTable);
}
