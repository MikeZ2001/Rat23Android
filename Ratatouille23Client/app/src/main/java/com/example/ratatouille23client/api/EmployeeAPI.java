package com.example.ratatouille23client.api;

import com.example.ratatouille23client.model.Employee;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EmployeeAPI {
    String BASE_ENDPOINT = "api/v1/employee";

    @POST(BASE_ENDPOINT + "/getByEmail")
    Call<Employee> getEmployeeByEmail(@Body String email);
}
