package com.example.ratatouille23client.ui.orderManagement.inProgressOrder;

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
import com.example.ratatouille23client.databinding.FragmentInProgressOrderBinding;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.viewModel.orderManagement.inProgressOrder.InProgressOrderViewModel;

import java.util.List;
import java.util.logging.Logger;

public class InProgressOrderFragment extends Fragment {

    private FragmentInProgressOrderBinding fragmentInProgressOrderBinding;
    private InProgressOrderViewModel inProgressOrderViewModel;
    private InProgressOrderAdapter inProgressOrderAdapter;

    private final Logger logger = LoggerUtility.getLogger();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        logger.info("Inizializzazione tab ordini in preparazione.");

        inProgressOrderViewModel = new ViewModelProvider(this).get(InProgressOrderViewModel.class);

        fragmentInProgressOrderBinding = FragmentInProgressOrderBinding.inflate(getLayoutInflater());

        return fragmentInProgressOrderBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setComponentsFunctionalities();
        logger.info("Terminata inizializzazione tab ordini in preparazione.");
    }

    private void setComponentsFunctionalities() {
        setLayoutManager();
        setListAdapter();
        setRefreshLayoutListener();
    }


    private void setRefreshLayoutListener() {
        fragmentInProgressOrderBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                logger.info("Refresh schermata.");
                logger.info("Avvio procedura ottenimento ordini in preparazione.");
                inProgressOrderViewModel.callInProgressOrders();
                logger.info("Terminata procedura ottenimento ordini in preparazione.");
                inProgressOrderAdapter.notifyDataSetChanged();

                fragmentInProgressOrderBinding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    private void setListAdapter() {
        inProgressOrderAdapter = new InProgressOrderAdapter();

        fragmentInProgressOrderBinding.inProgressOrdersRecyclerView.setAdapter(inProgressOrderAdapter);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            fragmentInProgressOrderBinding.inProgressOrdersRecyclerView.smoothScrollToPosition(0);

        }, getResources().getInteger(android.R.integer.config_shortAnimTime));

        inProgressOrderViewModel.getInProgressOrdersList().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                if(orders != null){
                    logger.info("Lista ordini in preparazione aggiornata.");
                    inProgressOrderAdapter.setInProgressOrdersArrayList(orders);
                    inProgressOrderAdapter.notifyDataSetChanged();
                    fragmentInProgressOrderBinding.inProgressOrdersRecyclerView.smoothScrollToPosition(0);
                }else {
                    Toast.makeText(getContext(), "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                    inProgressOrderAdapter.setInProgressOrdersArrayList(null);
                }
            }
        });
        logger.info("Avvio procedura ottenimento ordini in preparazione.");
        inProgressOrderViewModel.callInProgressOrders();
        logger.info("Terminata procedura ottenimento ordini in preparazione.");
    }

    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        fragmentInProgressOrderBinding.inProgressOrdersRecyclerView.setLayoutManager(layoutManager);
    }

}