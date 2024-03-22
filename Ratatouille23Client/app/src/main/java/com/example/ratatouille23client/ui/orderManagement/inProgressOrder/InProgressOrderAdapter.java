package com.example.ratatouille23client.ui.orderManagement.inProgressOrder;

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
import com.example.ratatouille23client.databinding.InProgressOrdersItemBinding;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.ui.orderManagement.acceptedOrder.AcceptedOrdersAdapter;

import java.util.List;
import java.util.logging.Logger;

public class InProgressOrderAdapter extends RecyclerView.Adapter<InProgressOrderAdapter.ViewHolder> {

    List<Order> inProgressOrdersArrayList;

    private final Logger logger = LoggerUtility.getLogger();

    @NonNull
    @Override
    public InProgressOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InProgressOrdersItemBinding inProgressOrdersItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.in_progress_orders_item,parent,false);
        return new ViewHolder(inProgressOrdersItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull InProgressOrderAdapter.ViewHolder holder, int position) {

        Order inProgressOrder = inProgressOrdersArrayList.get(position);
        holder.inProgressOrdersItemBinding.setOrder(inProgressOrder);

        int itemTotalQuantity = 0;

        for(int i=0; i < inProgressOrder.getItems().size(); i++)
            itemTotalQuantity = itemTotalQuantity + inProgressOrder.getItems().get(i).getQuantity();

        holder.inProgressOrdersItemBinding.totalInProgressOrderPzTextView.setText(String.valueOf("Pezzi: "+itemTotalQuantity));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su ordine.");
                Bundle bundle = new Bundle();

                bundle.putSerializable("currentInProgressOrder", inProgressOrder);
                logger.info("Ordine selezionato. Reindirizzamento alla visualizzazione della comanda.");
                Navigation.findNavController(view).navigate(R.id.action_nav_comande_to_visualizationInProgressOrderFragment,bundle);
            }
        });
    }


    @Override
    public int getItemCount() {
        if(inProgressOrdersArrayList != null)
            return inProgressOrdersArrayList.size();
        else
            return 0;
    }

    public void setInProgressOrdersArrayList(List<Order> inProgressOrdersArrayList) {
        this.inProgressOrdersArrayList = inProgressOrdersArrayList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        InProgressOrdersItemBinding inProgressOrdersItemBinding;

        public ViewHolder(@NonNull InProgressOrdersItemBinding inProgressOrdersItemBinding) {
            super(inProgressOrdersItemBinding.getRoot());

            this.inProgressOrdersItemBinding = inProgressOrdersItemBinding;
        }
    }

}
