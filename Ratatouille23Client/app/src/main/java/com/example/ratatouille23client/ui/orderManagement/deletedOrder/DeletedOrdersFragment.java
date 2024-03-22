package com.example.ratatouille23client.ui.orderManagement.deletedOrder;

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
import com.example.ratatouille23client.databinding.FragmentDeletedOrdersBinding;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.viewModel.orderManagement.deletedOrder.DeletedOrdersViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DeletedOrdersFragment extends Fragment {

    private DeletedOrdersViewModel deletedOrdersViewModel;
    private FragmentDeletedOrdersBinding fragmentDeletedOrdersBinding;
    private DeletedOrdersAdapter deletedOrdersAdapter;

    private final Logger logger = LoggerUtility.getLogger();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        logger.info("Inizializzazione tab ordini annullati.");
        deletedOrdersViewModel = new ViewModelProvider(this).get(DeletedOrdersViewModel.class);

        fragmentDeletedOrdersBinding = FragmentDeletedOrdersBinding.inflate(getLayoutInflater());

        return fragmentDeletedOrdersBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setComponentsFunctionalities();
        logger.info("Terminata inizializzazione tab ordini annullati.");
    }

    private void setComponentsFunctionalities() {
        setLayoutManager();
        setListAdapter();
        setRefreshLayoutListener();
    }


    private void setRefreshLayoutListener() {
        fragmentDeletedOrdersBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                logger.info("Refresh schermata.");
                logger.info("Avvio procedura ottenimento ordini annullati.");
                deletedOrdersViewModel.callDeletedOrders();
                logger.info("Terminata procedura ottenimento ordini annullati.");
                deletedOrdersAdapter.notifyDataSetChanged();

                fragmentDeletedOrdersBinding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        fragmentDeletedOrdersBinding.deletedOrdersRecyclerView.setLayoutManager(layoutManager);
    }

    private void setListAdapter() {
        deletedOrdersAdapter = new DeletedOrdersAdapter();

        fragmentDeletedOrdersBinding.deletedOrdersRecyclerView.setAdapter(deletedOrdersAdapter);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            fragmentDeletedOrdersBinding.deletedOrdersRecyclerView.smoothScrollToPosition(0);

        }, getResources().getInteger(android.R.integer.config_shortAnimTime));


        deletedOrdersViewModel.getDeletedOrdersList().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                if (orders != null){
                    logger.info("Aggiornata lista ordini annullati.");
                    deletedOrdersAdapter.setDeletedOrdersArrayList(orders);
                    deletedOrdersAdapter.notifyDataSetChanged();
                    fragmentDeletedOrdersBinding.deletedOrdersRecyclerView.smoothScrollToPosition(0);
                }else{
                    Toast.makeText(getContext(), "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                    deletedOrdersAdapter.setDeletedOrdersArrayList(null);
                }
            }
        });
        logger.info("Avvio procedura ottenimento ordini annullati.");
        deletedOrdersViewModel.callDeletedOrders();
        logger.info("Terminata procedura ottenimento ordini annullati.");
    }

}