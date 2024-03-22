package com.example.ratatouille23client.ui.orderManagement.inProgressOrder;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.example.ratatouille23client.R;
import com.example.ratatouille23client.databinding.VisualizationInprogressOrdersItemBinding;
import com.example.ratatouille23client.model.Employee;
import com.example.ratatouille23client.model.OrderItem;
import com.example.ratatouille23client.model.enumerations.Role;
import com.example.ratatouille23client.viewModel.orderManagement.inProgressOrder.VisualizationInProgressOrderViewModel;

import java.util.List;

public class VisualizationInProgressOrderAdapter extends RecyclerView.Adapter<VisualizationInProgressOrderAdapter.ViewHolder> {

    List<OrderItem> inProgressOrderArrayList;
    private VisualizationInProgressOrderViewModel visualizationInProgressOrderViewModel;
    private Employee currentUser;

    @NonNull
    @Override
    public VisualizationInProgressOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VisualizationInprogressOrdersItemBinding visualizationInprogressOrdersItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.visualization_inprogress_orders_item,parent,false);

        visualizationInProgressOrderViewModel = new VisualizationInProgressOrderViewModel();

        return new ViewHolder(visualizationInprogressOrdersItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VisualizationInProgressOrderAdapter.ViewHolder holder, int position) {

        holder.visualizationInprogressOrdersItemBinding.setOrderItem(inProgressOrderArrayList.get(position));

        if(inProgressOrderArrayList.get(position).getOrderItemStatus() == OrderItem.OrderItemStatus.READY){
            holder.visualizationInprogressOrdersItemBinding.orderItemStatusCheckBox.setChecked(true);
            holder.visualizationInprogressOrdersItemBinding.orderItemStatusCheckBox.setText("Ready");

        }

        setLoggedUserObserver(holder);


        holder.visualizationInprogressOrdersItemBinding.orderItemStatusCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                System.out.println("Ready");


                OrderItem orderItem = holder.visualizationInprogressOrdersItemBinding.getOrderItem();

                if (b == true) {
                    orderItem.setOrderItemStatus(OrderItem.OrderItemStatus.READY);
                    holder.visualizationInprogressOrdersItemBinding.orderItemStatusCheckBox.setText("Ready");
                    visualizationInProgressOrderViewModel.updateOrderItemStatus(orderItem);

                }else {
                    orderItem.setOrderItemStatus(OrderItem.OrderItemStatus.NOT_READY);
                    holder.visualizationInprogressOrdersItemBinding.orderItemStatusCheckBox.setText("Not Ready");
                    visualizationInProgressOrderViewModel.updateOrderItemStatus(orderItem);
                }
                System.out.println(b);
            }
        });

    }

    private void setLoggedUserObserver(ViewHolder holder) {

        Amplify.Auth.fetchUserAttributes(results-> {

                    parseUserAttributes(results);

                    new Handler(Looper.getMainLooper()).post(()->{
                        if (currentUser.getRole().equals("WAITER")){

                            holder.visualizationInprogressOrdersItemBinding.orderItemStatusCheckBox.setEnabled(false);

                        }

                    });

                }
                ,error-> Log.e("AmplifyQuickstart", error.toString()));
    }

    private void parseUserAttributes(List<AuthUserAttribute> result) {

        currentUser = new Employee();

        for (AuthUserAttribute attribute: result) {
            if (attribute.getKey().getKeyString().equals("email"))
                currentUser.setEmail(attribute.getValue());
            else if (attribute.getKey().getKeyString().equals("given_name"))
                currentUser.setName(attribute.getValue());
            else if (attribute.getKey().getKeyString().equals("family_name"))
                currentUser.setSurname(attribute.getValue());
            else if (attribute.getKey().getKeyString().equals("custom:role"))
                currentUser.setRole(Role.valueOf(attribute.getValue()));
            else
                System.out.println(attribute.getKey().getKeyString() + " :" + attribute.getValue()); //TESTING
        }
    }

    @Override
    public int getItemCount() {
        if(inProgressOrderArrayList != null)
            return inProgressOrderArrayList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        VisualizationInprogressOrdersItemBinding visualizationInprogressOrdersItemBinding;

        public ViewHolder(@NonNull VisualizationInprogressOrdersItemBinding visualizationInprogressOrdersItemBinding) {
            super(visualizationInprogressOrdersItemBinding.getRoot());

            this.visualizationInprogressOrdersItemBinding = visualizationInprogressOrdersItemBinding;

        }
    }

    public List<OrderItem> getInProgressOrderArrayList() {
        notifyDataSetChanged();
        return this.inProgressOrderArrayList;
    }

    public void setInProgressOrderArrayList(List<OrderItem> inProgressOrderArrayList) {
        this.inProgressOrderArrayList = inProgressOrderArrayList;

        notifyDataSetChanged();
    }


    }

