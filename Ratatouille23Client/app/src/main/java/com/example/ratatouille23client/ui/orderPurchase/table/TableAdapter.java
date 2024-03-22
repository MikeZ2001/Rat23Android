package com.example.ratatouille23client.ui.orderPurchase.table;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.R;

import com.example.ratatouille23client.caching.RAT23Cache;
import com.example.ratatouille23client.databinding.TableItemBinding;
import com.example.ratatouille23client.model.Employee;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.model.Table;
import com.example.ratatouille23client.viewModel.orderPurchase.TableViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    List<Table> tablesArrayList;

    TableViewModel tableViewModel;

    private final Logger logger = LoggerUtility.getLogger();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        TableItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.table_item, parent, false);

        tableViewModel = new TableViewModel();

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TableAdapter.ViewHolder holder, int position) {
        holder.tableItemBinding.setTable(tablesArrayList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su un tavolo.");
                Bundle bundle = new Bundle();

                String pattern = "dd/MM/yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String date = simpleDateFormat.format(new Date());

                Locale locale = new Locale("it", "IT");
                DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT, locale);
                String time = timeFormat.format(new Date());

                Order newOrder = new Order();
                newOrder.setTable(holder.tableItemBinding.getTable());
                newOrder.setDate(date);
                newOrder.setTime(time);
                newOrder.setTotal(0d);

                List<Employee> employees;
                employees = new ArrayList<>();
                Employee employee = new Employee();
                employee.setId(Long.valueOf(RAT23Cache.getCacheInstance().get("currentUserId").toString()));
                employees.add(employee);
                newOrder.setEmployeesOfTheOrder(employees);
                logger.info("Creato ordine provvisorio. Reindirizzamento alla schermata delle categorie.");
                bundle.putSerializable("order", newOrder);
//                bundle.putLong("tableId", holder.tableItemBinding.getTable().getId());
                Navigation.findNavController(view).navigate(R.id.action_nav_ordinazione_to_menuCategoryFragment, bundle);

                tableViewModel.getCreatedOrder().observeForever(new Observer<Order>() {
                    @Override
                    public void onChanged(Order order) {
                        if(order != null) {
                            System.out.println(order);//TESTING
                        }else{
                            tableViewModel.getmErrorMessage().observeForever(new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    setTablesArrayList(null);
                                    Toast.makeText(view.getContext(), s,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });
                tableViewModel.getCreatedOrder().postValue(newOrder);
                //tableViewModel.createOrder(newOrder);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(tablesArrayList != null)
            return tablesArrayList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TableItemBinding tableItemBinding;

        public ViewHolder(@NonNull TableItemBinding tableItemBinding) {
            super(tableItemBinding.getRoot());

            this.tableItemBinding = tableItemBinding;
        }
    }

    public void setTablesArrayList(List<Table> tablesArrayList) {
        this.tablesArrayList = tablesArrayList;
        notifyDataSetChanged();
    }

    public List<Table> getTablesArrayList(){
        notifyDataSetChanged();
        return this.tablesArrayList;
    }
}
