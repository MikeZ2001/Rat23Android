package com.example.ratatouille23client.ui.orderManagement.deletedOrder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratatouille23client.CustomLogger;
import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.databinding.FragmentVisualizationDeletedOrdersBinding;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.model.Product;
import com.example.ratatouille23client.model.Table;
import com.example.ratatouille23client.model.enumerations.Status;
import com.example.ratatouille23client.viewModel.orderManagement.deletedOrder.VisualizationDeletedOrdersViewModel;

import java.util.ArrayList;
import java.util.logging.Logger;

public class VisualizationDeletedOrdersFragment extends Fragment {

    private ArrayList<Product> deletedOrderProductArrayList = new ArrayList<>();

    private FragmentVisualizationDeletedOrdersBinding fragmentVisualizationDeletedOrdersBinding;
    private VisualizationDeletedOrdersViewModel visualizationDeletedOrdersViewModel;
    private VisualizationDeletedOrdersAdapter visualizationDeletedOrdersAdapter;

    private Order deletedOrder;

    private final Logger logger = LoggerUtility.getLogger();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setComponentsFunctionalities();
        logger.info("Terminata inizializzazione visualizzazione comanda annullata.");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logger.info("Inizializzazione visualizzazione comanda annullata.");
        visualizationDeletedOrdersViewModel = new ViewModelProvider(this).get(VisualizationDeletedOrdersViewModel.class);

        fragmentVisualizationDeletedOrdersBinding = FragmentVisualizationDeletedOrdersBinding.inflate(getLayoutInflater());

        return fragmentVisualizationDeletedOrdersBinding.getRoot();
    }


    private void setComponentsFunctionalities() {
        setLayoutManager();
        setOrderInformation();
        setListAdapter();
        setDeletedOrdersObserver();
        setButtonListener();
    }

    private void setDeletedOrdersObserver() {
        visualizationDeletedOrdersViewModel.getUpdatedOrder().observe(getViewLifecycleOwner(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                if(order != null) {
                    logger.info("Informazioni ordine aggiornate.");
                    Toast.makeText(getContext(), "La comanda è stata spostata tra le accettate", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getView()).popBackStack();
                }else{
                    logger.severe("Ordine diventato null.");
                    Toast.makeText(getContext(), "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                    visualizationDeletedOrdersAdapter.setDeletedOrderProductArrayList(null);
                }
            }
        });

        visualizationDeletedOrdersViewModel.getUpdatedTable().observe(getViewLifecycleOwner(), new Observer<Table>() {
            @Override
            public void onChanged(Table table) {
                logger.warning("Tavolo ordine aggiornato.");
                if(table == null){
                    logger.severe("Tavolo ordine diventato null.");
                    Toast.makeText(getContext(), "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setButtonListener() {
        fragmentVisualizationDeletedOrdersBinding.restoreOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su Ripristina Comanda.");
                deletedOrder.setStatus(Status.ACCEPTED);
                logger.info("Avvio procedura aggiornamento stato ordine.");
                visualizationDeletedOrdersViewModel.callUpdateOrder(deletedOrder);
                logger.info("Terminata procedura aggiornamento stato ordine.");
                logger.info("Avvio procedura aggiornamento stato tavolo dell'ordine.");
                visualizationDeletedOrdersViewModel.updateTable(deletedOrder.getTable());
                logger.info("Terminata procedura aggiornamento stato tavolo dell'ordine.");

            }
        });
    }

    private void setOrderInformation() {
        logger.info("Inizializzazione informazioni ordine annullato.");
        deletedOrder = (Order) getArguments().getSerializable("currentDeletedOrder");

        int itemTotalQuantity = 0;

        for(int i=0; i < deletedOrder.getItems().size(); i++)
            itemTotalQuantity = itemTotalQuantity + deletedOrder.getItems().get(i).getQuantity();

        fragmentVisualizationDeletedOrdersBinding.totalPiecesTextView.setText(String.valueOf("Totale pezzi: "+itemTotalQuantity));
        logger.info("Terminata inizializzazione informazioni ordine annullato.");
    }

    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        fragmentVisualizationDeletedOrdersBinding.productRecyclerView.setLayoutManager(layoutManager);
    }

    private void setListAdapter() {

        visualizationDeletedOrdersAdapter = new VisualizationDeletedOrdersAdapter();

        fragmentVisualizationDeletedOrdersBinding.productRecyclerView.setAdapter(visualizationDeletedOrdersAdapter);

        visualizationDeletedOrdersAdapter.setDeletedOrderProductArrayList(deletedOrder.getItems());      //cartProductArraylist  = get from DB = viewModel.getCartFromDB
    }

}