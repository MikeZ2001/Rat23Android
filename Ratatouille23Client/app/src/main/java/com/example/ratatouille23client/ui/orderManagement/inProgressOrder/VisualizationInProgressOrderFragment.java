package com.example.ratatouille23client.ui.orderManagement.inProgressOrder;

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
import com.example.ratatouille23client.databinding.FragmentVisualizationInProgressOrderBinding;
import com.example.ratatouille23client.model.Employee;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.model.OrderItem;
import com.example.ratatouille23client.model.enumerations.Role;
import com.example.ratatouille23client.model.enumerations.Status;
import com.example.ratatouille23client.viewModel.orderManagement.inProgressOrder.VisualizationInProgressOrderViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class VisualizationInProgressOrderFragment extends Fragment {

   private FragmentVisualizationInProgressOrderBinding fragmentVisualizationInProgressOrderBinding;
   private VisualizationInProgressOrderViewModel visualizationInProgressOrderViewModel;
   private VisualizationInProgressOrderAdapter visualizationInProgressOrderAdapter;

    private Order currentInProgressOrder;

    private final Logger logger = LoggerUtility.getLogger();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setComponentsFunctionalities();
        logger.info("Terminata inizializzazione fragment visualizzazione comanda ordine in preparazione.");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logger.info("Inizializzazione fragment visualizzazione comanda ordine in preparazione.");
        visualizationInProgressOrderViewModel = new ViewModelProvider(this).get(VisualizationInProgressOrderViewModel.class);

        fragmentVisualizationInProgressOrderBinding = FragmentVisualizationInProgressOrderBinding.inflate(getLayoutInflater());

        return fragmentVisualizationInProgressOrderBinding.getRoot();
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
            fragmentVisualizationInProgressOrderBinding.readyOrderButton.setEnabled(false);
            fragmentVisualizationInProgressOrderBinding.readyOrderButton.setBackgroundColor(Color.GRAY);
        }

    }

    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        fragmentVisualizationInProgressOrderBinding.productRecyclerView.setLayoutManager(layoutManager);
    }


    private void setOrderInformation() {
        logger.info("Inizializzazione informazioni ordine selezionato.");
        int itemTotalQuantity = 0;

        currentInProgressOrder = (Order) getArguments().getSerializable("currentInProgressOrder");

        for(int i=0; i < currentInProgressOrder.getItems().size(); i++)
            itemTotalQuantity = itemTotalQuantity + currentInProgressOrder.getItems().get(i).getQuantity();

        fragmentVisualizationInProgressOrderBinding.totalPiecesTextView.setText(String.valueOf("Totale pezzi: "+itemTotalQuantity));

        fragmentVisualizationInProgressOrderBinding.orderNotesTextView.setText("Note ordine: "+currentInProgressOrder.getNotes());
        logger.info("Terminata inizializzazione informazioni ordine selezionato.");
    }


    private void setListAdapter() {
        visualizationInProgressOrderAdapter = new VisualizationInProgressOrderAdapter();

        fragmentVisualizationInProgressOrderBinding.productRecyclerView.setAdapter(visualizationInProgressOrderAdapter);

        visualizationInProgressOrderAdapter.setInProgressOrderArrayList(currentInProgressOrder.getItems());
    }

    private void setAcceptedOrderObserver() {
        visualizationInProgressOrderViewModel.getUpdatedOrder().observe(getViewLifecycleOwner(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                if(order != null) {
                    logger.info("Informazioni ordine aggiornate.");
                    if (order.getStatus().equals(Status.CANCELLED)) {
                        logger.info("Stato ordine cambiato ad Annullato.");
                        Toast.makeText(getContext(), "La comanda è stata spostata tra le annullate", Toast.LENGTH_SHORT).show();
                    } else if (order.getStatus().equals(Status.READY)) {
                        logger.info("Stato ordine cambiato a Pronto.");
                        Toast.makeText(getContext(), "La comanda è stata spostata tra le pronte", Toast.LENGTH_SHORT).show();
                    }
                    logger.info("Reindirizzamento alla tab degli ordini in preparazione.");
                    Navigation.findNavController(getView()).popBackStack();
                }else{
                    logger.severe("Ordine diventato null.");
                    Toast.makeText(getContext(), "Ops. Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                    visualizationInProgressOrderAdapter.setInProgressOrderArrayList(null);
                }

            }
        });


    }

    private void setButtonListener() {
        fragmentVisualizationInProgressOrderBinding.readyOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su Comanda Pronta.");
                Boolean allReady = true;

                for (int i = 0; i < currentInProgressOrder.getItems().size();i++){
                    if(currentInProgressOrder.getItems().get(i).getOrderItemStatus() == OrderItem.OrderItemStatus.NOT_READY){
                        allReady = false;
                        break;
                    }
                }

                if(allReady == true) {
                    currentInProgressOrder.setStatus(Status.READY);
                    List<Employee> employees;
                    employees = currentInProgressOrder.getEmployeesOfTheOrder();
                    Employee employee = new Employee();
                    employee.setId(Long.valueOf(RAT23Cache.getCacheInstance().get("currentUserId").toString()));
                    employees.add(employee);
                    currentInProgressOrder.setEmployeesOfTheOrder(employees);
                    logger.info("Avvio procedura aggiornamento stato dell'ordine.");
                    visualizationInProgressOrderViewModel.callUpdateOrder(currentInProgressOrder);
                    logger.info("Terminata procedura aggiornamento stato dell'ordine.");
                }else {
                    logger.info("L'utente non ha settato pronti tutti i prodotti dell'ordine. Impossibile proseguire.");
                    Toast.makeText(getContext(),"Attenzione! Non tutti i prodotti dell'ordine sono pronti.",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}