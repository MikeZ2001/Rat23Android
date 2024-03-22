package com.example.ratatouille23client.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.ratatouille23client.model.enumerations.Status;
import com.fasterxml.jackson.annotation.JsonIdentityReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Order implements Serializable , Parcelable {

    private Long id;
    private Status status;
    private String date;
    private String time;
    private Double total;
    private String notes;

    private Table table;

    private List<Employee> employeesOfTheOrder;

    private List<OrderItem> items = new ArrayList<>();

    public Order(){

    }

    public Order(Long id, Status status, String date, String time, double total, String notes, Table table, List<Employee> employees, List<OrderItem> items) {
        this.id = id;
        this.status = status;
        this.date = date;
        this.time = time;
        this.total = total;
        this.notes = notes;
        this.table = table;
        this.employeesOfTheOrder = employees;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<Employee> getEmployeesOfTheOrder() {
        return employeesOfTheOrder;
    }

    public void setEmployeesOfTheOrder(List<Employee> employeesOfTheOrder) {
        this.employeesOfTheOrder = employeesOfTheOrder;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status=" + status +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", total=" + total +
                ", notes='" + notes + '\'' +
                ", table=" + table +
                ", employees=" + employeesOfTheOrder +
                ", items=" + items +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
    }
}
