package com.example.ratatouille23client.ui.orderManagement.payedOrder;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import com.example.ratatouille23client.CustomLogger;
import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.R;
import com.example.ratatouille23client.databinding.PayedOrderItemBinding;
import com.example.ratatouille23client.model.Order;

import java.util.List;
import java.util.logging.Logger;

public class PayedOrderAdapter extends RecyclerView.Adapter<PayedOrderAdapter.ViewHolder> {

    List<Order> payedOrdersArrayList;

    private final Logger logger = LoggerUtility.getLogger();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PayedOrderItemBinding payedOrderItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.payed_order_item,parent,false);
        return new ViewHolder(payedOrderItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Order payedOrder = payedOrdersArrayList.get(position);

        holder.payedOrderItemBinding.setOrder(payedOrder);

        int itemTotalQuantity = 0;

        for(int i=0; i < payedOrder.getItems().size(); i++)
            itemTotalQuantity = itemTotalQuantity + payedOrder.getItems().get(i).getQuantity();

        holder.payedOrderItemBinding.totalPzOrderTextView.setText(String.valueOf("Pezzi: "+itemTotalQuantity));

        holder.payedOrderItemBinding.totalPriceOrderTextView.setText(String.format("%.2f", payedOrder.getTotal()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su ordine.");
                Bundle bundle = new Bundle();

                bundle.putSerializable("currentPayedOrder",payedOrder);
                logger.info("Ordine selezionato. Reindirizzamento alla visualizzazione della comanda.");
                Navigation.findNavController(view).navigate(R.id.action_nav_comande_to_visualizationPayedOrderFragment,bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(payedOrdersArrayList != null)
            return payedOrdersArrayList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PayedOrderItemBinding payedOrderItemBinding;

        public ViewHolder(@NonNull PayedOrderItemBinding payedOrderItemBinding) {
            super(payedOrderItemBinding.getRoot());

            this.payedOrderItemBinding = payedOrderItemBinding;
        }
    }

    public void setPayedOrdersArrayList(List<Order> payedOrdersArrayList) {
        this.payedOrdersArrayList = payedOrdersArrayList;
        notifyDataSetChanged();
    }
}

