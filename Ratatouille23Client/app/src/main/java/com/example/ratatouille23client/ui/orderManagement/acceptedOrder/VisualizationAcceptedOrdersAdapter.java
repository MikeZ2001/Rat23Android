package com.example.ratatouille23client.ui.orderManagement.acceptedOrder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratatouille23client.R;
import com.example.ratatouille23client.databinding.VisualizationAcceptedOrdersItemBinding;
import com.example.ratatouille23client.model.OrderItem;
import com.example.ratatouille23client.viewModel.orderManagement.acceptedOrder.VisualizationAcceptedOrdersViewModel;

import java.util.List;
import java.util.logging.Logger;

public class VisualizationAcceptedOrdersAdapter extends RecyclerView.Adapter<VisualizationAcceptedOrdersAdapter.ViewHolder> {
    List<OrderItem> acceptedOrderProductArrayList;

    private VisualizationAcceptedOrdersViewModel visualizationAcceptedOrdersViewModel;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VisualizationAcceptedOrdersItemBinding visualizationAcceptedOrdersItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.visualization_accepted_orders_item,parent,false);

        visualizationAcceptedOrdersViewModel = new VisualizationAcceptedOrdersViewModel();

        return new ViewHolder(visualizationAcceptedOrdersItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        /*
        if(acceptedOrderProductArrayList.get(position).getOrderItemStatus() == OrderItem.OrderItemStatus.ready){
            System.out.println("true");
            acceptedOrderProductArrayList.get(position).setParticularRequests(acceptedOrderProductArrayList.get(position).getParticularRequests()+"Product already done in previous order");
        }

         */
        holder.visualizationAcceptedOrdersItemBinding.setOrderItem(acceptedOrderProductArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        if(acceptedOrderProductArrayList != null)
            return acceptedOrderProductArrayList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        VisualizationAcceptedOrdersItemBinding visualizationAcceptedOrdersItemBinding;
        public ViewHolder(@NonNull VisualizationAcceptedOrdersItemBinding visualizationAcceptedOrdersItemBinding) {
            super(visualizationAcceptedOrdersItemBinding.getRoot());

            this.visualizationAcceptedOrdersItemBinding = visualizationAcceptedOrdersItemBinding;

        }
    }

    public List<OrderItem> getAcceptedOrderProductArrayList() {
        notifyDataSetChanged();
        return this.acceptedOrderProductArrayList;
    }

    public void setAcceptedOrderProductArrayList(List<OrderItem> acceptedOrderProductArrayList) {
        this.acceptedOrderProductArrayList = acceptedOrderProductArrayList;

        notifyDataSetChanged();
    }
}
