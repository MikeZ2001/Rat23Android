package com.example.ratatouille23client.ui.orderManagement.completedOrder;

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

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.example.ratatouille23client.CustomLogger;
import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.R;
import com.example.ratatouille23client.caching.RAT23Cache;
import com.example.ratatouille23client.databinding.FragmentVisualizationCompletedOrderBinding;
import com.example.ratatouille23client.model.Employee;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.model.Product;
import com.example.ratatouille23client.model.Table;
import com.example.ratatouille23client.model.enumerations.Role;
import com.example.ratatouille23client.viewModel.orderManagement.completedOrder.VisualizationCompletedOrderViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class VisualizationCompletedOrderFragment extends Fragment {

    private ArrayList<Product> completedOrderProductArrayList = new ArrayList<>();
    private FragmentVisualizationCompletedOrderBinding fragmentVisualizationCompletedOrderBinding;
    private VisualizationCompletedOrderViewModel visualizationCompletedOrderViewModel;
    private VisualizationCompletedOrderAdapter visualizationCompletedOrderAdapter;

    Order completedOrder;

    private final Logger logger = LoggerUtility.getLogger();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setComponentsFunctionalities();
        logger.info("Terminata inizializzazione fragment comanda dell'ordine completato.");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logger.info("Inizializzazione fragment comanda dell'ordine completato.");
        visualizationCompletedOrderViewModel = new ViewModelProvider(this).get(VisualizationCompletedOrderViewModel.class);

        fragmentVisualizationCompletedOrderBinding = FragmentVisualizationCompletedOrderBinding.inflate(getLayoutInflater());

        return fragmentVisualizationCompletedOrderBinding.getRoot();
    }

    private void setComponentsFunctionalities() {
        setLayoutManager();
        setOrderInformation();
        setListAdapter();
        setUpdatedOrderObserver();
        setOnAddProductToOrderListener();
        setUserAccess();
    }

    private void setUserAccess() {
        RAT23Cache cache = RAT23Cache.getCacheInstance();
        String loggedUserRole = String.valueOf(Role.valueOf(cache.get("currentUserRole").toString()));
        String chefRole = (Role.CHEF.toString());

        if (loggedUserRole.equals(chefRole)) {
            fragmentVisualizationCompletedOrderBinding.addProductToOrder.setEnabled(false);
            fragmentVisualizationCompletedOrderBinding.addProductToOrder.setBackgroundColor(Color.GRAY);
        }

    }

    private void setOnAddProductToOrderListener() {
        fragmentVisualizationCompletedOrderBinding.addProductToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su Modifica Ordine.");
                Bundle bundle = new Bundle();
                bundle.putLong("tableId",completedOrder.getTable().getId());
                bundle.putSerializable("order",completedOrder);
                logger.info("Reindirizzamento alla schermata categorie.");
                Navigation.findNavController(view).navigate(R.id.action_visualizationCompletedOrderFragment_to_menuCategoryFragment,bundle);
            }
        });
    }

    private void setUpdatedOrderObserver() {
        visualizationCompletedOrderViewModel.getUpdatedOrder().observe(getViewLifecycleOwner(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                if(order != null) {
                    logger.info("Informazioni ordine aggiornate.");
                    Toast.makeText(getContext(), "La comanda è stata spostata tra le pagate", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getView()).navigateUp();
                }else {
                    logger.severe("Ordine diventato null.");
                    Toast.makeText(getContext(), "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                    visualizationCompletedOrderAdapter.setCompletedOrderArrayList(null);
                }
            }
        });

        visualizationCompletedOrderViewModel.getUpdatedTable().observe(getViewLifecycleOwner(), new Observer<Table>() {
            @Override
            public void onChanged(Table table) {
                logger.warning("Tavolo dell'ordine aggiornato.");
                if(table == null){
                    logger.severe("Tavolo dell'ordine diventato null.");
                    Toast.makeText(getContext(), "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setOrderInformation() {
        logger.info("Inizializzazione informazioni ordine.");
        completedOrder = (Order) getArguments().getSerializable("currentCompletedOrder");

        int itemTotalQuantity = 0;

        for(int i=0; i < completedOrder.getItems().size(); i++)
            itemTotalQuantity = itemTotalQuantity + completedOrder.getItems().get(i).getQuantity();

        fragmentVisualizationCompletedOrderBinding.totalPiecesTextView.setText(String.valueOf("Totale pezzi: "+itemTotalQuantity));
        fragmentVisualizationCompletedOrderBinding.totalTextView.setText(String.valueOf("Totale ordine: "+String.format("%.2f",completedOrder.getTotal())+"€"));
        fragmentVisualizationCompletedOrderBinding.tableNameTextView.setText(completedOrder.getTable().getName());

        logger.info("Terminata inizializzazione informazioni ordine.");
    }

    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        fragmentVisualizationCompletedOrderBinding.productRecyclerView.setLayoutManager(layoutManager);
    }

    private void setListAdapter() {
        visualizationCompletedOrderAdapter = new VisualizationCompletedOrderAdapter();

        fragmentVisualizationCompletedOrderBinding.productRecyclerView.setAdapter(visualizationCompletedOrderAdapter);

        visualizationCompletedOrderAdapter.setCompletedOrderArrayList(completedOrder.getItems());
    }
}