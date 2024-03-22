package com.example.ratatouille23client.ui.orderManagement.completedOrder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratatouille23client.CustomLogger;
import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.R;
import com.example.ratatouille23client.databinding.CompletedOrdersItemBinding;
import com.example.ratatouille23client.model.Order;

import java.util.List;
import java.util.logging.Logger;

public class CompletedOrdersAdapter extends RecyclerView.Adapter<CompletedOrdersAdapter.ViewHolder> {

    List<Order> completedOrdersArrayList;

    private final Logger logger = LoggerUtility.getLogger();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CompletedOrdersItemBinding completedOrdersItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.completed_orders_item,parent,false);
        return new ViewHolder(completedOrdersItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedOrdersAdapter.ViewHolder holder, int position) {

        Order completedOrder = completedOrdersArrayList.get(position);

        holder.completedOrdersItemBinding.setOrder(completedOrder);

        int itemTotalQuantity = 0;

        for(int i=0; i < completedOrder.getItems().size(); i++)
            itemTotalQuantity = itemTotalQuantity + completedOrder.getItems().get(i).getQuantity();

        holder.completedOrdersItemBinding.totalCompletedOrderPzTextView.setText(String.valueOf("Pezzi: "+itemTotalQuantity));

        holder.completedOrdersItemBinding.totalPriceCompletedOrderTextView.setText(String.format("%.2f", completedOrder.getTotal()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su ordine completato.");

                Bundle bundle = new Bundle();

                bundle.putSerializable("currentCompletedOrder",completedOrder);
                logger.info("Ordine selezionato. Reindirizzamento alla visualizzazione della comanda.");
                Navigation.findNavController(view).navigate(R.id.action_nav_comande_to_visualizationCompletedOrderFragment,bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(completedOrdersArrayList != null)
            return completedOrdersArrayList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CompletedOrdersItemBinding completedOrdersItemBinding;

        public ViewHolder(@NonNull CompletedOrdersItemBinding completedOrdersItemBinding) {
            super(completedOrdersItemBinding.getRoot());

            this.completedOrdersItemBinding = completedOrdersItemBinding;
        }
    }

    public void setCompletedOrdersArrayList(List<Order> completedOrdersArrayList) {
        this.completedOrdersArrayList = completedOrdersArrayList;
        notifyDataSetChanged();
    }
}
