package com.example.ratatouille23client.ui.orderManagement.completedOrder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratatouille23client.R;
import com.example.ratatouille23client.databinding.VisualizationCompletedOrderItemBinding;
import com.example.ratatouille23client.model.OrderItem;

import java.util.List;


public class VisualizationCompletedOrderAdapter extends RecyclerView.Adapter<VisualizationCompletedOrderAdapter.ViewHolder> {

    List<OrderItem> completedOrderArrayList;

    @NonNull
    @Override
    public VisualizationCompletedOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VisualizationCompletedOrderItemBinding visualizationCompletedOrderItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.visualization_completed_order_item,parent,false);

        return new VisualizationCompletedOrderAdapter.ViewHolder(visualizationCompletedOrderItemBinding);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.visualizationCompletedOrderItemBinding.setOrderItem(completedOrderArrayList.get(position));
    }


    @Override
    public int getItemCount() {
        if(completedOrderArrayList != null)
            return completedOrderArrayList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        VisualizationCompletedOrderItemBinding visualizationCompletedOrderItemBinding;
        public ViewHolder(@NonNull VisualizationCompletedOrderItemBinding visualizationCompletedOrderItemBinding) {
            super(visualizationCompletedOrderItemBinding.getRoot());

            this.visualizationCompletedOrderItemBinding = visualizationCompletedOrderItemBinding;

        }
    }

    public List<OrderItem> getCompletedOrderArrayList() {
        notifyDataSetChanged();
        return this.completedOrderArrayList;
    }

    public void setCompletedOrderArrayList(List<OrderItem> completedOrderArrayList) {
        this.completedOrderArrayList = completedOrderArrayList;

        notifyDataSetChanged();
    }
}
