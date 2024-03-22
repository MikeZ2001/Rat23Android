package com.example.ratatouille23client.ui.orderPurchase.cart;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratatouille23client.CustomLogger;
import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.R;
import com.example.ratatouille23client.databinding.CartItemBinding;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.model.OrderItem;
import com.example.ratatouille23client.viewModel.orderPurchase.CartViewModel;

import java.util.List;
import java.util.logging.Logger;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    List<OrderItem> orderItemList;

    CartViewModel cartViewModel;

    CartFragment cartFragment;

    Order order;

    private final Logger logger = LoggerUtility.getLogger();

    public CartAdapter(CartFragment cartFragment, Order order) {
        this.cartFragment = cartFragment;
        this.order = order;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartItemBinding cartItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cart_item,parent,false);

        cartViewModel = new CartViewModel();

        return new ViewHolder(cartItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.cartItemBinding.setOrderItem(orderItemList.get(position));

        holder.cartItemBinding.removeProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su Rimuovi Prodotto.");
                cartViewModel.callDeleteOrderItemById(view.getContext(), orderItemList.get(position).getId());
                orderItemList.remove(position);

                Double subTotal = 0.0;

                for(int i = 0 ; i < getCartProductArrayList().size() ; i++){
                    subTotal  = subTotal + getCartProductArrayList().get(i).getProduct().getPrice() * getCartProductArrayList().get(i).getQuantity();
                }

                cartFragment.fragmentCartBinding.totalOrderTextView.setText("Totale: "+String.format("%.2f",subTotal)+" €");

                notifyDataSetChanged();

                logger.info("Rimosso prodotto.");
            }
        });

        holder.cartItemBinding.minusCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click sul pulsante - del prodotto.");
                int quantity = orderItemList.get(position).getQuantity() - 1;

                if(quantity > 0){
                    orderItemList.get(position).setQuantity(quantity);

                    OrderItem currentOrderItem = orderItemList.get(position);

                    currentOrderItem.setQuantity(quantity);
                    currentOrderItem.setOrderItemStatus(OrderItem.OrderItemStatus.NOT_READY);

//                    cartViewModel.callUpdateOrderItem(view.getContext(), currentOrderItem.getId(),currentOrderItem);

                    Double subTotal = 0.0;

                    for(int i = 0 ; i < getCartProductArrayList().size() ; i++){
                        subTotal  = subTotal + getCartProductArrayList().get(i).getProduct().getPrice() * getCartProductArrayList().get(i).getQuantity();
                    }

                    cartFragment.fragmentCartBinding.totalOrderTextView.setText("Totale: "+String.format("%.2f",subTotal)+" €");
                    logger.info("Ridotta quantità del prodotto.");
                }else{
                    //cartViewModel.callDeleteOrderItemById(view.getContext(),orderItemList.get(position).getId());

                    orderItemList.remove(position);

                    Double subTotal = 0.0;

                    for(int i = 0 ; i < getCartProductArrayList().size() ; i++){
                        subTotal  = subTotal + getCartProductArrayList().get(i).getProduct().getPrice() * getCartProductArrayList().get(i).getQuantity();
                    }

                    cartFragment.fragmentCartBinding.totalOrderTextView.setText("Totale: "+String.format("%.2f",subTotal)+" €");

                    notifyDataSetChanged();
                    logger.info("Rimosso totalmente il prodtto perchè raggiunta quantità 0.");
                    Toast.makeText(view.getContext(), "Prodotto rimosso con successo.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.cartItemBinding.plusCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click sul pulsante + del prodotto.");
                int quantity = orderItemList.get(position).getQuantity() + 1;

                orderItemList.get(position).setQuantity(quantity);

                OrderItem currentOrderItem = orderItemList.get(position);

                currentOrderItem.setQuantity(quantity);
                currentOrderItem.setOrderItemStatus(OrderItem.OrderItemStatus.NOT_READY);

                //cartViewModel.callUpdateOrderItem(view.getContext(), currentOrderItem.getId(),currentOrderItem);

                Double subTotal = 0.0;

                for(int i = 0 ; i < getCartProductArrayList().size() ; i++){
                   subTotal  = subTotal + getCartProductArrayList().get(i).getProduct().getPrice() * getCartProductArrayList().get(i).getQuantity();
                }

                cartFragment.fragmentCartBinding.totalOrderTextView.setText("Totale: "+String.format("%.2f",subTotal)+" €");
                logger.info("Quantità del prodotto aumentata.");
            }
        });

    }

    @Override
    public int getItemCount() {
        if(orderItemList != null)
            return orderItemList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CartItemBinding cartItemBinding;

        public ViewHolder(@NonNull CartItemBinding cartItemBinding) {
            super(cartItemBinding.getRoot());

            this.cartItemBinding = cartItemBinding;
        }
    }

    public List<OrderItem> getCartProductArrayList() {
        notifyDataSetChanged();
        return this.orderItemList;
    }

    public void serCartProductArrayList(List<OrderItem> cartArrayList) {
        this.orderItemList = cartArrayList;

        notifyDataSetChanged();
    }
}
