package com.example.ratatouille23client.ui.orderManagement.payedOrder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratatouille23client.R;
import com.example.ratatouille23client.databinding.VisualizationPayedOrderItemBinding;
import com.example.ratatouille23client.model.OrderItem;

import java.util.List;

public class VisualizationPayedOrderAdapter extends RecyclerView.Adapter<VisualizationPayedOrderAdapter.ViewHolder> {


    List<OrderItem> payedOrderArrayList;

    @NonNull
    @Override
    public VisualizationPayedOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VisualizationPayedOrderItemBinding visualizationPayedOrderItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.visualization_payed_order_item,parent,false);

        return new VisualizationPayedOrderAdapter.ViewHolder(visualizationPayedOrderItemBinding);
    }
    @Override
    public void onBindViewHolder(@NonNull VisualizationPayedOrderAdapter.ViewHolder holder, int position) {

        holder.visualizationPayedOrderItemBinding.setOrderItem(payedOrderArrayList.get(position));
    }


    @Override
    public int getItemCount() {
        if(payedOrderArrayList != null)
            return payedOrderArrayList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        VisualizationPayedOrderItemBinding visualizationPayedOrderItemBinding;
        public ViewHolder(@NonNull VisualizationPayedOrderItemBinding visualizationPayedOrderItemBinding) {
            super(visualizationPayedOrderItemBinding.getRoot());

            this.visualizationPayedOrderItemBinding = visualizationPayedOrderItemBinding;

        }
    }

    public List<OrderItem> getPayedOrderArrayList() {
        notifyDataSetChanged();
        return this.payedOrderArrayList;
    }

    public void setPayedOrderArrayList(List<OrderItem> payedOrderArrayList) {
        this.payedOrderArrayList = payedOrderArrayList;

        //get from DB
        notifyDataSetChanged();
    }
}
