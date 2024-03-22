package com.example.ratatouille23client.ui.orderPurchase.cart;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.R;
import com.example.ratatouille23client.databinding.FragmentCartBinding;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.model.OrderItem;
import com.example.ratatouille23client.model.enumerations.Status;
import com.example.ratatouille23client.viewModel.orderPurchase.CartViewModel;

import java.util.List;
import java.util.logging.Logger;

public class CartFragment extends Fragment {

    public FragmentCartBinding fragmentCartBinding;
    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;

    private final Logger logger = LoggerUtility.getLogger();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setComponentsFunctionalities();
        logger.info("Terminata inizializzazione fragment carrello.");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logger.info("Inizializzazione fragment carrello.");
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        fragmentCartBinding = FragmentCartBinding.inflate(getLayoutInflater());

        return fragmentCartBinding.getRoot();

    }

    private void setComponentsFunctionalities() {
        setLayoutManager();
        setListAdapter();
        setOnClickListener();
        disableSwipe();
    }

    private void setOnClickListener() {
        setOnConfirmOrderListener();
    }

    private void setOnConfirmOrderListener() {
        fragmentCartBinding.confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su Conferma Ordine.");

                cartViewModel.getOrderById().observe(getViewLifecycleOwner(), new Observer<Order>() {
                    @Override
                    public void onChanged(Order order) {
                        if(order != null && order.getItems().size() > 0) {
                            String orderNotes = fragmentCartBinding.particularRequestsEditText.getText().toString();

                            List<OrderItem> orderItemsList = cartViewModel.getOrderItemByOrderIdList().getValue();

                            order.setItems(orderItemsList);
                            order.setStatus(Status.ACCEPTED);
                            order.setNotes(orderNotes);

                            Double total = 0.0;

                            for (int i = 0; i < orderItemsList.size(); i++) {
                                total = total + orderItemsList.get(i).getProduct().getPrice() * orderItemsList.get(i).getQuantity();
                            }

                            order.setTotal(total);
                            if (order.getId() == null)
                                order.setId(0l);
                            order.getTable().setAvailable(false);

                            logger.info("Avvio procedura invio ordine.");
                            cartViewModel.callCreateOrder(view.getContext(), order);
                            logger.info("Terminata procedura invio ordine. Reindirizzamento alla schermata delle ordinazioni.");

                            Navigation.findNavController(view).navigate(R.id.action_cartFragment_to_nav_ordinazione);
                        }else{
                            logger.severe("L'ordine temporaneo è risultato null.");
                            Toast.makeText(view.getContext(), "Ops. Qualcosa è andato storto. Controlla di aver inserito almeno un elemento", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                Order order = (Order) getArguments().getSerializable("order");
                cartViewModel.getOrderById().postValue(order);

                cartAdapter.serCartProductArrayList(null);
            }
        });
    }

    private void disableSwipe() {
        fragmentCartBinding.swipeRefreshLayout.setEnabled(false);
    }

    private void setListAdapter() {
        cartAdapter = new CartAdapter(this,(Order) getArguments().getSerializable("order"));

        fragmentCartBinding.cartProductRecyclerView.setAdapter(cartAdapter);

        try {
            cartViewModel.getOrderItemByOrderIdList().observe(getViewLifecycleOwner(), new Observer<List<OrderItem>>() {
                @Override
                public void onChanged(List<OrderItem> orderItems) {
                    logger.info("Aggiornati oggetti ordine.");
                    if(orderItems != null) {
                        cartAdapter.serCartProductArrayList(orderItems);

                        double subTotal = 0.0;
                        for(int i = 0 ; i < orderItems.size() ; i++){
                            subTotal = subTotal + orderItems.get(i).getProduct().getPrice() * orderItems.get(i).getQuantity();
                        }

                        fragmentCartBinding.totalOrderTextView.setText("Totale: "+String.format("%.2f",subTotal)+" €");

                    }else{
                        Toast.makeText(getContext(), "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                        cartAdapter.serCartProductArrayList(null);
                    }

                }
            });
        }catch (Exception ex){
            logger.severe(ex.getMessage());
            ex.printStackTrace();
        }

        Order order = (Order) getArguments().getSerializable("order");
        cartViewModel.getOrderItemByOrderIdList().postValue(order.getItems());
    }

    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        fragmentCartBinding.cartProductRecyclerView.setLayoutManager(layoutManager);
    }
}