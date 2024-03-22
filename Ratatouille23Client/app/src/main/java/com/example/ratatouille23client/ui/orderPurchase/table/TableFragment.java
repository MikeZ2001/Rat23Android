package com.example.ratatouille23client.ui.orderPurchase.table;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.databinding.FragmentTablesBinding;
import com.example.ratatouille23client.model.Table;
import com.example.ratatouille23client.viewModel.orderPurchase.TableViewModel;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TableFragment extends Fragment {

    private FragmentTablesBinding binding;

    private TableViewModel tableViewModel;
    private TableAdapter tableAdapter;

    List<Table> tablesArrayList = new ArrayList<>();

    List<Table> searchedTableArrayList;

    private final Logger logger = LoggerUtility.getLogger();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logger.info("Inizializzazione fragment Tavoli.");

        //set viewModel
        tableViewModel = new ViewModelProvider(this).get(TableViewModel.class);

        //set binding
        binding = FragmentTablesBinding.inflate(getLayoutInflater());

        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setComponentsFunctionalities();

        logger.info("Terminata inizializzazione fragment Tavoli.");
    }

    private void setComponentsFunctionalities() {
        setLayoutManager();
        setListAdapter();
        setSearchViewListener();
        setRefreshLayoutListener();
    }

    private void setRefreshLayoutListener() {
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                logger.info("Refresh schermata.");
                logger.info("Avvio procedura ottenimento tavoli.");
                tableViewModel.getAllAvailableTables();
                logger.info("Terminata procedura ottenimento tavoli.");
                tableAdapter.notifyDataSetChanged();

                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setSearchViewListener() {
        binding.tableSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                logger.info("Filtrando tavoli per \"" + query + "\"");
                setListAdapter();

                searchedTableArrayList = new ArrayList<>();
                tablesArrayList = tableAdapter.getTablesArrayList();

                if(query.length()>0){
                        for (int i = 0; i < tablesArrayList.size(); i++) {
                            if (tablesArrayList.get(i).getName().toUpperCase().contains(query.toUpperCase())) {
                                Table searchedTable = new Table();
                                searchedTable.setName(tablesArrayList.get(i).getName());
                                searchedTable.setSeatsNumber(tablesArrayList.get(i).getSeatsNumber());

                                searchedTableArrayList.add(searchedTable);
                            }

                            TableAdapter tavoloAdapter = new TableAdapter();
                            binding.tableRecyclerView.setAdapter(tavoloAdapter);

                            tavoloAdapter.setTablesArrayList(searchedTableArrayList);

                        }
                    }
                else{
                    logger.info("Filtri per tavoli vuoti.");
                    setListAdapter();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                logger.info("Filtrando tavoli per \"" + newText + "\"");
                searchedTableArrayList = new ArrayList<>();

                tablesArrayList = tableAdapter.getTablesArrayList();

                if(newText.length()>0 && tablesArrayList != null){

                        for (int i = 0; i < tablesArrayList.size(); i++) {
                            if (tablesArrayList.get(i).getName().toUpperCase().contains(newText.toUpperCase())) {
                                Table searchedTable = new Table();
                                searchedTable.setId(tablesArrayList.get(i).getId());
                                searchedTable.setName(tablesArrayList.get(i).getName());
                                searchedTable.setSeatsNumber(tablesArrayList.get(i).getSeatsNumber());
                                searchedTable.setAvailable(tablesArrayList.get(i).getAvailable());

                                searchedTableArrayList.add(searchedTable);
                            }

                            TableAdapter tavoloAdapter = new TableAdapter();
                            tavoloAdapter.setTablesArrayList(searchedTableArrayList);

                            binding.tableRecyclerView.setAdapter(tavoloAdapter);

                    }
                }
                else{
                    logger.info("Filtri per tavoli vuoti.");
                    setListAdapter();
                }
                return false;
            }
        });
    }

    private void setListAdapter() {
        tableAdapter = new TableAdapter();

        binding.tableRecyclerView.setAdapter(tableAdapter);

        try {
            tableViewModel.getTableListObserver().observe(getViewLifecycleOwner(), new Observer<List<Table>>() {
                @Override
                public void onChanged(List<Table> tableList) {
                    logger.info("Aggiornata lista dei tavoli.");
                    if (tableList != null) {
                        tableAdapter.setTablesArrayList(tableList);
                        tableAdapter.notifyDataSetChanged();
                    }else{
                        tableViewModel.getmErrorMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                logger.severe(s);
                                tableAdapter.setTablesArrayList(null);
                                Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            });
        }
        catch (Exception ex){
            logger.severe(ex.getMessage());
            ex.printStackTrace();
        }
        logger.info("Avvio procedura ottenimento tavoli.");
        tableViewModel.getAllAvailableTables();
        logger.info("Terminata procedura ottenimento tavoli.");
    }

    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        binding.tableRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}