package com.example.ratatouille23client.ui.orderManagement.readyOrder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratatouille23client.R;

import com.example.ratatouille23client.databinding.VisualizationReadyOrderItemBinding;
import com.example.ratatouille23client.model.OrderItem;

import java.util.List;

public class VisualizationReadyOrdersAdapter extends RecyclerView.Adapter<VisualizationReadyOrdersAdapter.ViewHolder> {

    List<OrderItem> readyOrderProductArrayList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VisualizationReadyOrderItemBinding visualizationReadyOrdersItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.visualization_ready_order_item,parent,false);

        return new ViewHolder(visualizationReadyOrdersItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.visualizationReadyOrderItemBinding.setOrderItem(readyOrderProductArrayList.get(position));
    }


    @Override
    public int getItemCount() {
        if(readyOrderProductArrayList != null)
            return readyOrderProductArrayList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        VisualizationReadyOrderItemBinding visualizationReadyOrderItemBinding;
        public ViewHolder(@NonNull VisualizationReadyOrderItemBinding visualizationReadyOrderItemBinding) {
            super(visualizationReadyOrderItemBinding.getRoot());

            this.visualizationReadyOrderItemBinding = visualizationReadyOrderItemBinding;

        }
    }

    public List<OrderItem> getReadyOrderProductArrayList() {
        notifyDataSetChanged();
        return this.readyOrderProductArrayList;
    }

    public void setReadyOrderProductArrayList(List<OrderItem> readyOrderProductArrayList) {
        this.readyOrderProductArrayList = readyOrderProductArrayList;

        //get from DB
        notifyDataSetChanged();
    }
}
