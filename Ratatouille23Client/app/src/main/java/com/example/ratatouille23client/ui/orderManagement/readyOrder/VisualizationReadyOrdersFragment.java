package com.example.ratatouille23client.ui.orderManagement.readyOrder;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.LoginActivity;
import com.example.ratatouille23client.MainActivity;
import com.example.ratatouille23client.R;
import com.example.ratatouille23client.caching.RAT23Cache;
import com.example.ratatouille23client.databinding.FragmentVisualizationReadyOrdersBinding;

import com.example.ratatouille23client.model.Employee;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.model.Product;
import com.example.ratatouille23client.model.enumerations.Role;
import com.example.ratatouille23client.model.enumerations.Status;
import com.example.ratatouille23client.viewModel.orderManagement.readyOrder.VisualizationReadyOrdersViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class VisualizationReadyOrdersFragment extends Fragment {
    private Order readyOrder;

    private FragmentVisualizationReadyOrdersBinding fragmentVisualizationReadyOrdersBinding;
    private VisualizationReadyOrdersViewModel visualizationReadyOrdersViewModel;
    private VisualizationReadyOrdersAdapter visualizationReadyOrdersAdapter;

    private final Logger logger = LoggerUtility.getLogger();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setComponentsFunctionalities();
        logger.info("Terminata inizializzazione visualizzazione comanda ordine pronto.");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logger.info("Inizializzazione visualizzazione comanda ordine pronto.");
        visualizationReadyOrdersViewModel = new ViewModelProvider(this).get(VisualizationReadyOrdersViewModel.class);
        fragmentVisualizationReadyOrdersBinding = FragmentVisualizationReadyOrdersBinding.inflate(getLayoutInflater());

        return fragmentVisualizationReadyOrdersBinding.getRoot();
    }

    private void setComponentsFunctionalities() {
        setLayoutManager();
        setOrderInformation();
        setListAdapter();
        setUpdatedOrderObserver();
        setButtonListener();
        setUserAccess();
    }

    private void setUserAccess() {
        RAT23Cache cache = RAT23Cache.getCacheInstance();
        String loggedUserRole = String.valueOf(Role.valueOf(cache.get("currentUserRole").toString()));
        String chefRole = (Role.CHEF.toString());

        if (loggedUserRole.equals(chefRole)) {
            fragmentVisualizationReadyOrdersBinding.addProductToOrder.setEnabled(false);
            fragmentVisualizationReadyOrdersBinding.addProductToOrder.setBackgroundColor(Color.GRAY);

            fragmentVisualizationReadyOrdersBinding.completeOrderButton.setEnabled(false);
            fragmentVisualizationReadyOrdersBinding.completeOrderButton.setBackgroundColor(Color.GRAY);
        }

    }

    private void setUpdatedOrderObserver() {
        visualizationReadyOrdersViewModel.getUpdatedOrder().observe(getViewLifecycleOwner(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                if(order != null) {
                    logger.info("Informazioni ordine aggiornate.");
                    Toast.makeText(getContext(), "La comanda è stata spostata tra le servite", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getView()).navigateUp();
                }else{
                    logger.severe("Ordine diventato null.");
                    Toast.makeText(getContext(), "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                    visualizationReadyOrdersAdapter.setReadyOrderProductArrayList(null);
                }

            }
        });
    }

    private void setButtonListener() {
        fragmentVisualizationReadyOrdersBinding.addProductToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su Modifica Ordine.");
                Bundle bundle = new Bundle();
                bundle.putLong("tableId",readyOrder.getTable().getId());
                bundle.putSerializable("order",readyOrder);
                logger.info("Reindirizzamento alla schermata delle categorie.");
                Navigation.findNavController(view).navigate(R.id.action_visualizationReadyOrdersFragment_to_menuCategoryFragment,bundle);
            }
        });

        fragmentVisualizationReadyOrdersBinding.completeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su Comanda Servita.");
                readyOrder.setStatus(Status.COMPLETED);
                logger.info("Avvio procedura aggiornamento stato ordine.");
                visualizationReadyOrdersViewModel.callUpdateOrder(readyOrder);
                logger.info("Terminata procedura aggiornamento stato ordine.");
            }
        });
    }

    private void setOrderInformation() {
        logger.info("Inizializzazione informazioni ordine selezionato.");
        int itemTotalQuantity = 0;

        readyOrder = (Order) getArguments().getSerializable("currentReadyOrder");

        for(int i=0; i < readyOrder.getItems().size(); i++)
            itemTotalQuantity = itemTotalQuantity + readyOrder.getItems().get(i).getQuantity();

        fragmentVisualizationReadyOrdersBinding.totalPiecesTextView.setText(String.valueOf("Totale pezzi: "+itemTotalQuantity));

        fragmentVisualizationReadyOrdersBinding.tableNameTextView.setText(readyOrder.getTable().getName());

        fragmentVisualizationReadyOrdersBinding.orderNotesTextView.setText("Note ordine: "+readyOrder.getNotes());
        logger.info("Terminata inizializzazione informazioni ordine selezionato.");
    }


    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        fragmentVisualizationReadyOrdersBinding.productRecyclerView.setLayoutManager(layoutManager);
    }

    private void setListAdapter() {
        visualizationReadyOrdersAdapter = new VisualizationReadyOrdersAdapter();
        fragmentVisualizationReadyOrdersBinding.productRecyclerView.setAdapter(visualizationReadyOrdersAdapter);
        visualizationReadyOrdersAdapter.setReadyOrderProductArrayList(readyOrder.getItems());
    }

}