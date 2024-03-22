package com.example.ratatouille23client.ui.orderManagement.deletedOrder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratatouille23client.R;
import com.example.ratatouille23client.databinding.VisualizationDeletedOrderItemBinding;
import com.example.ratatouille23client.model.OrderItem;

import java.util.List;

public class VisualizationDeletedOrdersAdapter extends RecyclerView.Adapter<VisualizationDeletedOrdersAdapter.ViewHolder> {

    List<OrderItem> deletedOrderProductArrayList;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VisualizationDeletedOrderItemBinding visualizationDeletedOrderItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.visualization_deleted_order_item,parent,false);

        return new ViewHolder(visualizationDeletedOrderItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.visualizationDeletedOrderItemBinding.setOrderItem(deletedOrderProductArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        if(deletedOrderProductArrayList != null)
            return deletedOrderProductArrayList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        VisualizationDeletedOrderItemBinding visualizationDeletedOrderItemBinding;
        public ViewHolder(@NonNull VisualizationDeletedOrderItemBinding visualizationDeletedOrderItemBinding) {
            super(visualizationDeletedOrderItemBinding.getRoot());

            this.visualizationDeletedOrderItemBinding = visualizationDeletedOrderItemBinding;

        }
    }

    public List<OrderItem> getDeletedOrderProductArrayList() {
        notifyDataSetChanged();
        return this.deletedOrderProductArrayList;
    }

    public void setDeletedOrderProductArrayList(List<OrderItem> deletedOrderProductArrayList) {
        this.deletedOrderProductArrayList = deletedOrderProductArrayList;

        //get from DB
        notifyDataSetChanged();
    }
}
