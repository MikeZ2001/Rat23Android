package com.example.ratatouille23client.ui.orderPurchase.product;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.R;
import com.example.ratatouille23client.databinding.ProductItemBinding;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.model.OrderItem;
import com.example.ratatouille23client.model.Product;
import com.example.ratatouille23client.viewModel.orderPurchase.ProductCategoryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.ViewHolder> {

    List<Product> productCategoryArrayList;
    ProductCategoryViewModel productCategoryViewModel;
    Order order;
    Product product;

    private final Logger logger = LoggerUtility.getLogger();

    public ProductCategoryAdapter(Order order) {
        this.order = order;
    }

    @NonNull
    @Override
    public ProductCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductItemBinding productItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.product_item, parent,false);

        productCategoryViewModel = new ProductCategoryViewModel();

        return new ViewHolder(productItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCategoryAdapter.ViewHolder holder, int position) {
        holder.productItemBinding.setProduct(productCategoryArrayList.get(position));

//        productCategoryViewModel.getOrderObserver().observeForever(new Observer<Order>() {
//            @Override
//            public void onChanged(Order order) {
//                if(order != null) {
//                    this.order = order;
//                }else{
//                    productCategoryViewModel.getErrorMessage().observeForever(new Observer<String>() {
//                        @Override
//                        public void onChanged(String s) {
//                            setProductCategoryArrayList(null);
//                            Toast.makeText(holder.itemView.getContext(), s, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        });

        //productCategoryViewModel.getOrderById(order.getId());

        holder.productItemBinding.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click sul pulsante + del prodotto.");
//                productCategoryViewModel.getOrderItemObserver().observeForever(new Observer<OrderItem>() {
//                    @Override
//                    public void onChanged(OrderItem orderItem) {
//                        if(orderItem != null) {
//                            Toast.makeText(view.getContext(),"Prodotto aggiunto con successo",Toast.LENGTH_SHORT).show();
//                        }else{
//                            productCategoryViewModel.getErrorMessage().observeForever(new Observer<String>() {
//                                @Override
//                                public void onChanged(String s) {
//                                    setProductCategoryArrayList(null);
//                                    Toast.makeText(view.getContext(), s, Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                    }
//                });

                OrderItem newOrderItem = new OrderItem();

                newOrderItem.setProduct(holder.productItemBinding.getProduct());
                newOrderItem.setQuantity(1);
                newOrderItem.setOrderItemStatus(OrderItem.OrderItemStatus.NOT_READY);
                List<OrderItem> items;
                if (order.getItems() != null)
                    items = order.getItems();
                else
                    items = new ArrayList<>();
                items.add(newOrderItem);
                order.setItems(items);
                logger.info("Aggiunto prodotto all'ordine.");
                Toast.makeText(view.getContext(), "Prodotto aggiunto con successo.", Toast.LENGTH_SHORT).show();
                //productCategoryViewModel.callAddOrderItem(newOrderItem);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click sul prodotto.");
                Bundle bundle = new Bundle();

                product = holder.productItemBinding.getProduct();

                bundle.putSerializable("product", product);
                bundle.putSerializable("order",order);
                logger.info("Selezionato il prodotto. Reindirizzamento alla schermata dettagli del prodotto.");
                Navigation.findNavController(view).navigate(R.id.action_menuItemFragment_to_productDetailFragment,bundle);

            }
        });

    }

    @Override
    public int getItemCount() {
        if(productCategoryArrayList != null)
            return productCategoryArrayList.size();
            else
                return 0;

    }

    public List<Product> getProductCategoryArrayList() {
        notifyDataSetChanged();
        return this.productCategoryArrayList;
    }

    public void setProductCategoryArrayList(List<Product> productCategoryArrayList) {
        this.productCategoryArrayList = productCategoryArrayList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ProductItemBinding productItemBinding;

        public ViewHolder(@NonNull ProductItemBinding productItemBinding) {
            super(productItemBinding.getRoot());

            this.productItemBinding = productItemBinding;
        }
    }
}
