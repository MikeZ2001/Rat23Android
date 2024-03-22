package com.example.ratatouille23client.ui.orderManagement.readyOrder;

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

import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.databinding.FragmentReadyOrdersBinding;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.viewModel.orderManagement.readyOrder.ReadyOrdersViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ReadyOrdersFragment extends Fragment {

    private ReadyOrdersViewModel readyOrdersViewModel;
    private ReadyOrdersAdapter readyOrdersAdapter;
    private FragmentReadyOrdersBinding fragmentReadyOrdersBinding;

    private final Logger logger = LoggerUtility.getLogger();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        logger.info("Inizializzazione tab ordini pronti.");
        readyOrdersViewModel = new ViewModelProvider(this).get(ReadyOrdersViewModel.class);

        fragmentReadyOrdersBinding = FragmentReadyOrdersBinding.inflate(getLayoutInflater());


       return fragmentReadyOrdersBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setComponentsFunctionalities();
        logger.info("Terminata inizializzazione tab ordini pronti.");
    }


    private void setComponentsFunctionalities() {
        setLayoutManager();
        setListAdapter();
        setRefreshLayoutListener();
    }

    private void setRefreshLayoutListener() {
        fragmentReadyOrdersBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                logger.info("Refresh schermata.");
                logger.info("Avvio procedura ottenimento ordini pronti.");
                readyOrdersViewModel.callReadyOrders();
                logger.info("Terminata procedura ottenimento ordini pronti.");
                readyOrdersAdapter.notifyDataSetChanged();

                fragmentReadyOrdersBinding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        fragmentReadyOrdersBinding.readyOrdersRecyclerView.setLayoutManager(layoutManager);
    }

    private void setListAdapter() {
        readyOrdersAdapter = new ReadyOrdersAdapter();

        fragmentReadyOrdersBinding.readyOrdersRecyclerView.setAdapter(readyOrdersAdapter);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            fragmentReadyOrdersBinding.readyOrdersRecyclerView.smoothScrollToPosition(0);

        }, getResources().getInteger(android.R.integer.config_shortAnimTime));

        readyOrdersViewModel.getReadyOrdersList().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                if(orders != null){
                    logger.info("Lista degli ordini pronti aggiornata.");
                    readyOrdersAdapter.setReadyOrdersArrayList(orders);
                    readyOrdersAdapter.notifyDataSetChanged();
                    fragmentReadyOrdersBinding.readyOrdersRecyclerView.smoothScrollToPosition(0);
                }else{
                    Toast.makeText(getContext(), "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                    readyOrdersAdapter.setReadyOrdersArrayList(null);
                }
            }
        });
        logger.info("Avvio procedura ottenimento ordini pronti.");
        readyOrdersViewModel.callReadyOrders();
        logger.info("Terminata procedura ottenimento ordini pronti.");
    }



}