package com.example.ratatouille23client.ui.orderManagement.payedOrder;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ratatouille23client.CustomLogger;
import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.databinding.FragmentVisualizationPayedOrderBinding;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.model.Product;
import com.example.ratatouille23client.viewModel.orderManagement.payedOrder.VisualizationPayedOrderViewModel;

import java.util.ArrayList;
import java.util.logging.Logger;

public class VisualizationPayedOrderFragment extends Fragment {

    private ArrayList<Product> completedOrderProductArrayList = new ArrayList<>();
    private FragmentVisualizationPayedOrderBinding fragmentVisualizationPayedOrderBinding;
    private VisualizationPayedOrderViewModel visualizationPayedOrderViewModel;
    private VisualizationPayedOrderAdapter visualizationPayedOrderAdapter;

    Order payedOrder;

    private final Logger logger = LoggerUtility.getLogger();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setComponentsFunctionalities();
        logger.info("Terminata inizializzazione fragment comanda ordine pagato.");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logger.info("Inizializzazione fragment comanda ordine pagato.");
        visualizationPayedOrderViewModel = new ViewModelProvider(this).get(VisualizationPayedOrderViewModel.class);

        fragmentVisualizationPayedOrderBinding = FragmentVisualizationPayedOrderBinding.inflate(getLayoutInflater());

        return fragmentVisualizationPayedOrderBinding.getRoot();
    }

    private void setComponentsFunctionalities() {
        setLayoutManager();
        setOrderInformation();
        setListAdapter();

    }
    private void setOrderInformation() {
        logger.info("Inizializzazione informazioni ordine selezionato.");
        payedOrder = (Order) getArguments().getSerializable("currentPayedOrder");

        int itemTotalQuantity = 0;

        for(int i = 0; i < payedOrder.getItems().size(); i++)
            itemTotalQuantity = itemTotalQuantity + payedOrder.getItems().get(i).getQuantity();

        fragmentVisualizationPayedOrderBinding.totalPiecesTextView.setText(String.valueOf("Totale pezzi: "+itemTotalQuantity));
        fragmentVisualizationPayedOrderBinding.totalTextView.setText(String.valueOf("Totale ordine: "+ String.format("%.2f",payedOrder.getTotal())+"€"));
        logger.info("Terminata inizializzazione informazioni ordine selezionato.");
    }

    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        fragmentVisualizationPayedOrderBinding.productRecyclerView.setLayoutManager(layoutManager);
    }

    private void setListAdapter() {
        visualizationPayedOrderAdapter = new VisualizationPayedOrderAdapter();

        fragmentVisualizationPayedOrderBinding.productRecyclerView.setAdapter(visualizationPayedOrderAdapter);

        visualizationPayedOrderAdapter.setPayedOrderArrayList(payedOrder.getItems());
    }


}