package com.example.ratatouille23client.ui.orderManagement.payedOrder;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.lifecycle.Observer;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ratatouille23client.CustomLogger;
import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.databinding.FragmentPayedOrderBinding;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.viewModel.orderManagement.payedOrder.PayedOrderViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PayedOrderFragment extends Fragment {

    private FragmentPayedOrderBinding fragmentPayedOrderBinding;
    private PayedOrderViewModel payedOrderViewModel;
    private PayedOrderAdapter payedOrderAdapter;

    private final Logger logger = LoggerUtility.getLogger();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        logger.info("Inizializzazione tab ordini pagati.");
        payedOrderViewModel = new ViewModelProvider(this).get(PayedOrderViewModel.class);

        fragmentPayedOrderBinding = FragmentPayedOrderBinding.inflate(getLayoutInflater());

        return fragmentPayedOrderBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setComponentsFunctionalities();
        logger.info("Terminata inizializzazione tab ordini pagati.");
    }

    private void setComponentsFunctionalities() {
        setLayoutManager();
        setListAdapter();
        setRefreshLayoutListener();
    }

    private void setRefreshLayoutListener() {
        fragmentPayedOrderBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                logger.info("Refresh schermata.");
                logger.info("Avvio procedura ottenimento ordini pagati.");
                payedOrderViewModel.callPayedOrders();
                logger.info("Terminata procedura ottenimento ordini pagati.");
                payedOrderAdapter.notifyDataSetChanged();

                fragmentPayedOrderBinding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    private void setListAdapter() {
        payedOrderAdapter = new PayedOrderAdapter();
        fragmentPayedOrderBinding.payedOrdersRecyclerView.setAdapter(payedOrderAdapter);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            fragmentPayedOrderBinding.payedOrdersRecyclerView.smoothScrollToPosition(0);

        }, getResources().getInteger(android.R.integer.config_shortAnimTime));

        payedOrderViewModel.getPayedOrderList().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                if(orders != null){
                    logger.info("Aggiornata lista ordini pagati.");
                    payedOrderAdapter.setPayedOrdersArrayList(orders);
                    payedOrderAdapter.notifyDataSetChanged();
                    fragmentPayedOrderBinding.payedOrdersRecyclerView.smoothScrollToPosition(0);
                }else{
                    Toast.makeText(getContext(), "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                    payedOrderAdapter.setPayedOrdersArrayList(null);
                }
            }
        });
        logger.info("Avvio procedura ottenimento ordini pagati.");
        payedOrderViewModel.callPayedOrders();
        logger.info("Terminata procedura ottenimento ordini pagati.");
    }

    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        fragmentPayedOrderBinding.payedOrdersRecyclerView.setLayoutManager(layoutManager);
    }



}