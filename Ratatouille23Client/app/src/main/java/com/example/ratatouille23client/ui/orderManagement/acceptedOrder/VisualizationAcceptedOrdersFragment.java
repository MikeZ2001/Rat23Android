package com.example.ratatouille23client.ui.orderManagement.acceptedOrder;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
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

import com.example.ratatouille23client.CustomLogger;
import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.R;
import com.example.ratatouille23client.caching.RAT23Cache;
import com.example.ratatouille23client.databinding.FragmentVisualizationAcceptedOrdersBinding;
import com.example.ratatouille23client.model.Employee;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.model.Product;
import com.example.ratatouille23client.model.Table;
import com.example.ratatouille23client.model.enumerations.Role;
import com.example.ratatouille23client.model.enumerations.Status;
import com.example.ratatouille23client.viewModel.orderManagement.acceptedOrder.VisualizationAcceptedOrdersViewModel;

import java.util.ArrayList;
import java.util.logging.Logger;

public class VisualizationAcceptedOrdersFragment extends Fragment {

    private FragmentVisualizationAcceptedOrdersBinding fragmentVisualizationAcceptedOrdersBinding;
    private VisualizationAcceptedOrdersViewModel visualizationAcceptedOrdersViewModel;
    private VisualizationAcceptedOrdersAdapter visualizationAcceptedOrdersAdapter;

    private Order currentAcceptedOrder;

    private final Logger logger = LoggerUtility.getLogger();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setComponentsFunctionalities();
        logger.info("Terminata inizializzazione fragment comanda accettata.");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logger.info("Inizializzazione fragment comanda accettata.");
        visualizationAcceptedOrdersViewModel = new ViewModelProvider(this).get(VisualizationAcceptedOrdersViewModel.class);

        fragmentVisualizationAcceptedOrdersBinding = FragmentVisualizationAcceptedOrdersBinding.inflate(getLayoutInflater());

        return fragmentVisualizationAcceptedOrdersBinding.getRoot();
    }

    private void setComponentsFunctionalities() {
        setLayoutManager();
        setOrderInformation();
        setListAdapter();
        setAcceptedOrderObserver();
        setButtonListener();
        setUserAccess();
    }

    private void setUserAccess() {
        RAT23Cache cache = RAT23Cache.getCacheInstance();
        String loggedUserRole = String.valueOf(Role.valueOf(cache.get("currentUserRole").toString()));
        String waiterRole = (Role.WAITER.toString());

        if (loggedUserRole.equals(waiterRole)) {
            fragmentVisualizationAcceptedOrdersBinding.inProgressOrderButton.setEnabled(false);
            fragmentVisualizationAcceptedOrdersBinding.inProgressOrderButton.setBackgroundColor(Color.GRAY);
        }

    }

    private void setAcceptedOrderObserver() {
        visualizationAcceptedOrdersViewModel.getUpdatedOrder().observe(getViewLifecycleOwner(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                logger.info("Ordine aggiornato.");
                if(order != null) {
                    if (order.getStatus().equals(Status.CANCELLED)) {
                        logger.info("L'ordine è stato spostato negli annullati.");
                        Toast.makeText(getContext(), "La comanda è stata spostata tra le annullate", Toast.LENGTH_SHORT).show();
                    } else if (order.getStatus().equals(Status.IN_PROGRESS)) {
                        logger.info("L'ordine è stato spostato nella categoria in preparazione.");
                        Toast.makeText(getContext(), "La comanda è stata spostata tra le in preparazione", Toast.LENGTH_SHORT).show();
                    }
                    logger.info("Reindirizzamento alla schermata degli ordini accettati.");
                    Navigation.findNavController(getView()).popBackStack();
                }else{
                    Toast.makeText(getContext(), "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                    visualizationAcceptedOrdersAdapter.setAcceptedOrderProductArrayList(null);
                }
            }
        });

        visualizationAcceptedOrdersViewModel.getUpdatedTable().observe(getViewLifecycleOwner(), new Observer<Table>() {
            @Override
            public void onChanged(Table table) {
                logger.warning("Il tavolo dell'ordine è cambiato.");
                if(table == null){
                    logger.severe("Il tavolo dell'ordine è diventato null.");
                    Toast.makeText(getContext(), "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setButtonListener() {
        fragmentVisualizationAcceptedOrdersBinding.deleteOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su Annulla Ordine.");
                currentAcceptedOrder.setStatus(Status.CANCELLED);
                logger.info("Avvio procedura per annullamento dell'ordine.");
                visualizationAcceptedOrdersViewModel.callUpdateOrder(currentAcceptedOrder);
                logger.info("Terminata procedura per annullamento dell'ordine.");
                logger.info("Avvio procedura aggiornamento stato del tavolo.");
                visualizationAcceptedOrdersViewModel.updateTable(currentAcceptedOrder.getTable());
                logger.info("Terminata procedura aggiornamento stato del tavolo.");
            }
        });

        fragmentVisualizationAcceptedOrdersBinding.inProgressOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su In Preparazione.");
                currentAcceptedOrder.setStatus(Status.IN_PROGRESS);
                logger.info("Avvio procedura aggiornamento stato dell'ordine.");
                visualizationAcceptedOrdersViewModel.callUpdateOrder(currentAcceptedOrder);
                logger.info("Terminata procedura aggiornamento stato dell'ordine.");
            }
        });

        fragmentVisualizationAcceptedOrdersBinding.addProductToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su Modifica Ordine.");
                Bundle bundle = new Bundle();
                bundle.putLong("tableId",currentAcceptedOrder.getTable().getId());
                bundle.putSerializable("order",currentAcceptedOrder);
                logger.info("Reindirizzamento alla schermata delle categorie.");
                Navigation.findNavController(view).navigate(R.id.action_visualizationAcceptedOrdersFragment_to_menuCategoryFragment,bundle);
            }
        });
    }


    private void setOrderInformation() {
        logger.info("Inizializzazione informazioni dell'ordine.");
        int itemTotalQuantity = 0;

        currentAcceptedOrder = (Order) getArguments().getSerializable("currentAcceptedOrder");

        for(int i=0; i < currentAcceptedOrder.getItems().size(); i++)
            itemTotalQuantity = itemTotalQuantity + currentAcceptedOrder.getItems().get(i).getQuantity();

         fragmentVisualizationAcceptedOrdersBinding.totalPiecesTextView.setText(String.valueOf("Totale pezzi: "+itemTotalQuantity));

         fragmentVisualizationAcceptedOrdersBinding.orderNotesTextView.setText("Note ordine: "+currentAcceptedOrder.getNotes());
        logger.info("Terminata inizializzazione informazioni dell'ordine.");
    }


    private void setListAdapter() {

        visualizationAcceptedOrdersAdapter = new VisualizationAcceptedOrdersAdapter();

        fragmentVisualizationAcceptedOrdersBinding.productRecyclerView.setAdapter(visualizationAcceptedOrdersAdapter);

        visualizationAcceptedOrdersAdapter.setAcceptedOrderProductArrayList(currentAcceptedOrder.getItems());
    }

    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        fragmentVisualizationAcceptedOrdersBinding.productRecyclerView.setLayoutManager(layoutManager);
    }

}