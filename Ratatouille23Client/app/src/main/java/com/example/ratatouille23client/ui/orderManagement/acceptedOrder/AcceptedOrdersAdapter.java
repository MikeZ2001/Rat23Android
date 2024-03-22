package com.example.ratatouille23client.ui.orderManagement.acceptedOrder;

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

import com.example.ratatouille23client.databinding.AcceptedOrdersItemBinding;
import com.example.ratatouille23client.model.Order;

import java.util.List;
import java.util.logging.Logger;

public class AcceptedOrdersAdapter extends RecyclerView.Adapter<AcceptedOrdersAdapter.ViewHolder> {

    List<Order> acceptedOrdersArrayList;

    private final Logger logger = LoggerUtility.getLogger();

    @NonNull
    @Override
    public AcceptedOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AcceptedOrdersItemBinding acceptedOrdersItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.accepted_orders_item,parent,false);
        return new ViewHolder(acceptedOrdersItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedOrdersAdapter.ViewHolder holder, int position) {

        Order acceptedOrder = acceptedOrdersArrayList.get(position);
        holder.acceptedOrdersItemBinding.setOrder(acceptedOrder);

        int itemTotalQuantity = 0;

        for(int i=0; i < acceptedOrder.getItems().size(); i++)
            itemTotalQuantity = itemTotalQuantity + acceptedOrder.getItems().get(i).getQuantity();

        holder.acceptedOrdersItemBinding.totalAcceptedOrderPzTextView.setText(String.valueOf("Pezzi: "+itemTotalQuantity));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su un ordine accettato.");
                Bundle bundle = new Bundle();
                logger.info("Selezionato ordine accettato. Reindirizzamento alla visualizzazione della comanda.");
                bundle.putSerializable("currentAcceptedOrder", acceptedOrder);
                Navigation.findNavController(view).navigate(R.id.action_nav_comande_to_visualizationAcceptedOrdersFragment,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(acceptedOrdersArrayList != null)
            return acceptedOrdersArrayList.size();
        else
            return 0;
    }

    public void setAcceptedOrdersArrayList(List<Order> acceptedOrders) {

        this.acceptedOrdersArrayList = acceptedOrders;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AcceptedOrdersItemBinding acceptedOrdersItemBinding;

        public ViewHolder(@NonNull AcceptedOrdersItemBinding acceptedOrdersItemBinding) {
            super(acceptedOrdersItemBinding.getRoot());

            this.acceptedOrdersItemBinding = acceptedOrdersItemBinding;
        }
    }
}
