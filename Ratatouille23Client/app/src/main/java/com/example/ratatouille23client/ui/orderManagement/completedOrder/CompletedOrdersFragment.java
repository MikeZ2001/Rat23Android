package com.example.ratatouille23client.ui.orderManagement.completedOrder;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ratatouille23client.CustomLogger;
import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.databinding.FragmentCompletedOrdersBinding;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.viewModel.orderManagement.completedOrder.CompletedOrdersViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CompletedOrdersFragment extends Fragment {

    private FragmentCompletedOrdersBinding fragmentCompletedOrdersBinding;
    private CompletedOrdersViewModel completedOrdersViewModel;

    private CompletedOrdersAdapter completedOrdersAdapter;
    private final Logger logger = LoggerUtility.getLogger();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        logger.info("Inizializzazione tab ordini completati.");

        completedOrdersViewModel = new ViewModelProvider(this).get(CompletedOrdersViewModel.class);

        fragmentCompletedOrdersBinding = FragmentCompletedOrdersBinding.inflate(getLayoutInflater());

        return fragmentCompletedOrdersBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setComponentsFunctionalities();

        logger.info("Terminata inizializzazione tab ordini completati.");
    }

    private void setComponentsFunctionalities() {
        setLayoutManager();
        setListAdapter();
        setRefreshLayoutListener();
    }



    private void setRefreshLayoutListener() {
        fragmentCompletedOrdersBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                logger.info("Refresh schermata.");
                logger.info("Avvio procedura ottenimento ordini completati.");
                completedOrdersViewModel.callCompletedOrders();
                logger.info("Terminata procedura ottenimento ordini completati.");
                completedOrdersAdapter.notifyDataSetChanged();

                fragmentCompletedOrdersBinding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    private void setListAdapter() {
        completedOrdersAdapter = new CompletedOrdersAdapter();

        fragmentCompletedOrdersBinding.completedOrdersRecyclerView.setAdapter(completedOrdersAdapter);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            fragmentCompletedOrdersBinding.completedOrdersRecyclerView.smoothScrollToPosition(0);


        }, getResources().getInteger(android.R.integer.config_shortAnimTime));

        completedOrdersViewModel.getCompletedOrdersList().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                if(orders != null){
                    logger.info("Lista ordini aggiornata.");
                    completedOrdersAdapter.setCompletedOrdersArrayList(orders);
                    completedOrdersAdapter.notifyDataSetChanged();
                    fragmentCompletedOrdersBinding.completedOrdersRecyclerView.smoothScrollToPosition(0);
                }else{
                    Toast.makeText(getContext(), "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                    completedOrdersAdapter.setCompletedOrdersArrayList(null);
                }
            }
        });
        logger.info("Avvio procedura ottenimento ordini completati.");
        completedOrdersViewModel.callCompletedOrders();
        logger.info("Terminata procedura ottenimento ordini completati.");
    }

    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        fragmentCompletedOrdersBinding.completedOrdersRecyclerView.setLayoutManager(layoutManager);
    }


}