package com.example.ratatouille23client.ui.orderManagement.acceptedOrder;

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
import com.example.ratatouille23client.databinding.FragmentAcceptedOrdersBinding;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.viewModel.orderManagement.acceptedOrder.AcceptedOrdersViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import okhttp3.internal.concurrent.Task;

public class AcceptedOrdersFragment extends Fragment {

    private FragmentAcceptedOrdersBinding fragmentAcceptedOrdersBinding;
    private AcceptedOrdersViewModel acceptedOrdersViewModel;
    private AcceptedOrdersAdapter acceptedOrdersAdapter;

    private final Logger logger = LoggerUtility.getLogger();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        logger.info("Inizializzazione tab ordini accettati.");

        acceptedOrdersViewModel = new ViewModelProvider(this).get(AcceptedOrdersViewModel.class);

        fragmentAcceptedOrdersBinding = FragmentAcceptedOrdersBinding.inflate(getLayoutInflater());

        return fragmentAcceptedOrdersBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setComponentsFunctionalities();
        logger.info("Terminata inizializzazione tab ordini accettati.");
    }

    private void setComponentsFunctionalities() {
        setLayoutManager();
        setListAdapter();
        setRefreshLayoutListener();
    }

    private void setRefreshLayoutListener() {
        fragmentAcceptedOrdersBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                logger.info("Refresh schermata.");
                logger.info("Avvio procedura ottenimento ordini accettati.");
                acceptedOrdersViewModel.callAcceptedOrders();
                logger.info("Terminata procedura ottenimento ordini accettati.");
                acceptedOrdersAdapter.notifyDataSetChanged();

                fragmentAcceptedOrdersBinding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setListAdapter() {
        acceptedOrdersAdapter = new AcceptedOrdersAdapter();

        fragmentAcceptedOrdersBinding.acceptedOrdersRecyclerView.setAdapter(acceptedOrdersAdapter);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            fragmentAcceptedOrdersBinding.acceptedOrdersRecyclerView.smoothScrollToPosition(0);

        }, getResources().getInteger(android.R.integer.config_shortAnimTime));

        acceptedOrdersViewModel.getAcceptedOrdersList().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                if(orders != null){
                    logger.info("Lista ordini accettati aggiornati.");
                    acceptedOrdersAdapter.setAcceptedOrdersArrayList(orders);
                    acceptedOrdersAdapter.notifyDataSetChanged();
                    fragmentAcceptedOrdersBinding.acceptedOrdersRecyclerView.smoothScrollToPosition(0);
                }else {
                    Toast.makeText(getContext(), "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                    acceptedOrdersAdapter.setAcceptedOrdersArrayList(null);
                }
            }
        });

        logger.info("Avvio procedura ottenimento ordini accettati.");
        acceptedOrdersViewModel.callAcceptedOrders();
        logger.info("Terminata procedura ottenimento ordini accettati.");
    }

    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        fragmentAcceptedOrdersBinding.acceptedOrdersRecyclerView.setLayoutManager(layoutManager);
    }
}