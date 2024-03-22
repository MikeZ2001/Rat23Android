package com.example.ratatouille23client.ui.orderPurchase.product;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.databinding.FragmentProductDetailBinding;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.model.OrderItem;
import com.example.ratatouille23client.model.Product;
import com.example.ratatouille23client.viewModel.orderPurchase.ProductDetailViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProductDetailFragment extends Fragment {

    private ProductDetailViewModel productDetailViewModel;
    private FragmentProductDetailBinding fragmentProductDetailBinding;
    private int quantity = 0;
    private Product product;

    private Order order;

    private final Logger logger = LoggerUtility.getLogger();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setProduct();
        setOnQuantityListener();
        setOnAddToOrderListener();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        productDetailViewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);

        fragmentProductDetailBinding = FragmentProductDetailBinding.inflate(getLayoutInflater());

        return fragmentProductDetailBinding.getRoot();

    }

    private void setOnAddToOrderListener() {
        this.order = (Order) getArguments().getSerializable("order");
//        productDetailViewModel.getOrderObserver().observeForever(new Observer<Order>() {
//            @Override
//            public void onChanged(Order order) {
//                if(order != null){
//                    ProductDetailFragment.this.order = order;
//                }else{
//                    Toast.makeText(getContext(), "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

        //productDetailViewModel.getOrderById(getArguments().getLong("orderId"));

//        productDetailViewModel.getOrderItemObserver().observeForever(new Observer<OrderItem>() {
//            @Override
//            public void onChanged(OrderItem orderItem) {
//                if(orderItem != null){
//                    Toast.makeText(getContext(), "I prodotti sono stati aggiunti all'ordine", Toast.LENGTH_LONG).show();
//
//                    Navigation.findNavController(getView()).navigateUp();
//                }else{
//                    Toast.makeText(getContext(), "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        fragmentProductDetailBinding.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su Aggiungi al Carrello.");
                Integer quantity = Integer.valueOf(fragmentProductDetailBinding.counterProductDetailQuantityTextView.getText().toString());

                if (quantity > 0) {
                    OrderItem newOrderItem = new OrderItem();

                    newOrderItem.setProduct(product);
                    newOrderItem.setQuantity(quantity);
                    newOrderItem.setOrderItemStatus(OrderItem.OrderItemStatus.NOT_READY);
                    newOrderItem.setParticularRequests(fragmentProductDetailBinding.particularRequestsEditText.getText().toString());
                    //productDetailViewModel.callAddOrderItem(newOrderItem);

                    List<OrderItem> items;
                    if (order.getItems() != null)
                        items = order.getItems();
                    else
                        items = new ArrayList<>();
                    items.add(newOrderItem);
                    order.setItems(items);
                    logger.info("Prodotto aggiunto al carrello. Reindirizzamento alla schermata dei prodotti.");
                    Navigation.findNavController(getView()).navigateUp();
                }else{
                    logger.info("L'utente ha specificato una quantità non valida del prodotto. Impossibile proseguire.");
                    Toast.makeText(getContext(),"Inserisci una quantità maggiore di 0",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setOnQuantityListener() {
        fragmentProductDetailBinding.minusProductDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity > 0){
                    quantity = quantity - 1;

                    double currentPrice = product.getPrice() * quantity;

                    fragmentProductDetailBinding.counterProductDetailQuantityTextView.setText(String.valueOf(quantity));

                    String productQuantity = fragmentProductDetailBinding.counterProductDetailQuantityTextView.getText().toString();
                    fragmentProductDetailBinding.addToCartButton.setText("Aggiungi "+productQuantity+" all'ordine - "+currentPrice+" €");
                }
            }
        });

        fragmentProductDetailBinding.plusProductDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity = quantity + 1;

                double currentPrice = product.getPrice() * quantity;

                fragmentProductDetailBinding.counterProductDetailQuantityTextView.setText(String.valueOf(quantity));

                String productQuantity = fragmentProductDetailBinding.counterProductDetailQuantityTextView.getText().toString();
                fragmentProductDetailBinding.addToCartButton.setText("Aggiungi "+productQuantity+" all'ordine - "+currentPrice+" €");
            }
        });
    }

    private void setProduct() {
        this.product = (Product) getArguments().getSerializable("product");
        fragmentProductDetailBinding.setProduct(product);
    }


}