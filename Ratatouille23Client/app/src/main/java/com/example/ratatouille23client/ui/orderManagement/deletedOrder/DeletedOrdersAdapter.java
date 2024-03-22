package com.example.ratatouille23client.ui.orderManagement.deletedOrder;

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
import com.example.ratatouille23client.databinding.DeletedOrdersItemBinding;
import com.example.ratatouille23client.model.Order;

import java.util.List;
import java.util.logging.Logger;

public class DeletedOrdersAdapter extends RecyclerView.Adapter<DeletedOrdersAdapter.ViewHolder> {


    List<Order> deletedOrdersArrayList;

    private final Logger logger = LoggerUtility.getLogger();

    @NonNull
    @Override
    public DeletedOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeletedOrdersItemBinding deletedOrdersItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.deleted_orders_item,parent,false);

        return new ViewHolder(deletedOrdersItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeletedOrdersAdapter.ViewHolder holder, int position) {

        Order deletedOrder = deletedOrdersArrayList.get(position);

        holder.deletedOrdersItemBinding.setOrder(deletedOrder);

        int itemTotalQuantity = 0;

        for(int i=0; i < deletedOrder.getItems().size(); i++)
            itemTotalQuantity = itemTotalQuantity + deletedOrder.getItems().get(i).getQuantity();

        holder.deletedOrdersItemBinding.totalDeletedOrderPzTextView.setText(String.valueOf("Pezzi: "+itemTotalQuantity));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su ordine annullato.");

                Bundle bundle = new Bundle();

                bundle.putSerializable("currentDeletedOrder",deletedOrder);
                logger.info("Ordine selezionato. Reindirizzamento alla schermata delle categorie.");
                Navigation.findNavController(view).navigate(R.id.action_nav_comande_to_visualizationDeletedOrdersFragment,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(deletedOrdersArrayList != null)
            return deletedOrdersArrayList.size();
        else
            return 0;
    }

    public void setDeletedOrdersArrayList(List<Order> deletedOrdersArrayList) {
        this.deletedOrdersArrayList = deletedOrdersArrayList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        DeletedOrdersItemBinding deletedOrdersItemBinding;

        public ViewHolder(@NonNull DeletedOrdersItemBinding deletedOrdersItemBinding) {
            super(deletedOrdersItemBinding.getRoot());

            this.deletedOrdersItemBinding = deletedOrdersItemBinding;
        }
    }
}
