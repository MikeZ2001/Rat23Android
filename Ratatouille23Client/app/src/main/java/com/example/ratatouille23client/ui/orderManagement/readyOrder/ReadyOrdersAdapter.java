package com.example.ratatouille23client.ui.orderManagement.readyOrder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.R;
import com.example.ratatouille23client.databinding.ReadyOrdersItemBinding;
import com.example.ratatouille23client.model.Order;

import java.util.List;
import java.util.logging.Logger;

public class ReadyOrdersAdapter extends RecyclerView.Adapter<ReadyOrdersAdapter.ViewHolder> {

    List<Order> readyOrdersArrayList;

    private final Logger logger = LoggerUtility.getLogger();


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReadyOrdersItemBinding readyOrdersItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.ready_orders_item , parent , false);
        return new ViewHolder(readyOrdersItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Order readyOrder = readyOrdersArrayList.get(position);

        holder.readyOrdersItemBinding.setOrder(readyOrder);

        int itemTotalQuantity = 0;

        for(int i=0; i < readyOrder.getItems().size(); i++)
            itemTotalQuantity = itemTotalQuantity + readyOrder.getItems().get(i).getQuantity();

        holder.readyOrdersItemBinding.totalReadyOrderPzTextView.setText(String.valueOf("Pezzi: "+itemTotalQuantity));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su ordine.");
                Bundle bundle = new Bundle();

                //currentReadyOrder = holder.readyOrdersItemBinding.getOrder();

                bundle.putSerializable("currentReadyOrder", readyOrder);
                logger.info("Ordine selezionato. Reindirizzamento alla visualizzazione della comanda.");
                Navigation.findNavController(view).navigate(R.id.action_nav_comande_to_visualizationReadyOrdersFragment,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(readyOrdersArrayList != null)
            return readyOrdersArrayList.size();
        else
            return 0;
    }

    public void setReadyOrdersArrayList(List<Order> readyOrders) {
        this.readyOrdersArrayList = readyOrders;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ReadyOrdersItemBinding readyOrdersItemBinding;

        public ViewHolder(@NonNull ReadyOrdersItemBinding readyOrdersItemBinding) {
            super(readyOrdersItemBinding.getRoot());

            this.readyOrdersItemBinding = readyOrdersItemBinding;
        }
    }
}
