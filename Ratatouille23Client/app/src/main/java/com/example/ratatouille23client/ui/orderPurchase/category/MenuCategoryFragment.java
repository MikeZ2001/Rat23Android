package com.example.ratatouille23client.ui.orderPurchase.category;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.databinding.FragmentMenuCategoryBinding;
import com.example.ratatouille23client.model.Category;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.viewModel.orderPurchase.MenuCategoryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MenuCategoryFragment extends Fragment {

    private List<Category> categoryArrayList = new ArrayList<>();
    private List<Category> searchedCategoryArrayList;
    private FragmentMenuCategoryBinding fragmentMenuCategoryBinding;
    private MenuCategoryViewModel menuCategoryViewModel;
    private CategoriaMenuAdapter categoryMenuAdapter;

    private final Logger logger = LoggerUtility.getLogger();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setComponentsFunctionalities();
        logger.info("Terminata inizializzazione fragment Categorie.");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        logger.info("Inizializzazione fragment Categorie.");
        menuCategoryViewModel = new ViewModelProvider(this).get(MenuCategoryViewModel.class);

        fragmentMenuCategoryBinding = FragmentMenuCategoryBinding.inflate(getLayoutInflater());

        return fragmentMenuCategoryBinding.getRoot();
    }

    private void setComponentsFunctionalities() {
        setLayoutManager();
        setListAdapter();
        setSearchViewListener();
        setRefreshLayoutListener();
    }

    private void setRefreshLayoutListener() {
        fragmentMenuCategoryBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                logger.info("Refresh schermata.");
                logger.info("Avvio procedura ottenimento categorie.");
                menuCategoryViewModel.callGetAllCategories();
                logger.info("Terminata procedura ottenimento categorie.");
                categoryMenuAdapter.notifyDataSetChanged();

                fragmentMenuCategoryBinding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        fragmentMenuCategoryBinding.menuCategoryRecyclerView.setLayoutManager(layoutManager);
    }

    private void setListAdapter() {
        Order order = (Order) getArguments().getSerializable("order");
        categoryMenuAdapter = new CategoriaMenuAdapter(order);

        fragmentMenuCategoryBinding.menuCategoryRecyclerView.setAdapter(categoryMenuAdapter);

        try {
            menuCategoryViewModel.getCategoryListObserver().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
                @Override
                public void onChanged(List<Category> categoryList) {
                    if (categoryList != null) {
                        logger.info("Aggiornata lista categorie.");
                        categoryMenuAdapter.setCategoriesArrayList(categoryList);
                    }else {
                        menuCategoryViewModel.getErrorMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                logger.severe(s);
                                categoryMenuAdapter.setCategoriesArrayList(null);
                                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
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
        logger.info("Avvio procedura ottenimento categorie.");
        menuCategoryViewModel.callGetAllCategories();
        logger.info("Terminata procedura ottenimento categorie.");
    }

    private void setSearchViewListener() {
        fragmentMenuCategoryBinding.menuCategorySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                logger.info("Filtrando le categorie per \""+query+"\"");
                searchedCategoryArrayList = new ArrayList<>();
                categoryArrayList = categoryMenuAdapter.getCategoriesArrayList();

                if(query.length()>0){
                    for(int i = 0; i < categoryArrayList.size(); i++){
                        if(categoryArrayList.get(i).getName().toUpperCase().contains(query.toUpperCase())){

                            Category searchedCategory = new Category();
                            searchedCategory.setName(categoryArrayList.get(i).getName());

                            searchedCategoryArrayList.add(searchedCategory);
                        }

                        Order order = (Order) getArguments().getSerializable("order");
                        CategoriaMenuAdapter categoriaMenuAdapter = new CategoriaMenuAdapter(order);
                        fragmentMenuCategoryBinding.menuCategoryRecyclerView.setAdapter(categoriaMenuAdapter);

                        categoriaMenuAdapter.setCategoriesArrayList(searchedCategoryArrayList);
                    }
                }
                else{
                    logger.info("Filtri per categorie svuotati.");
                    setListAdapter();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                logger.info("Filtrando le categorie per \""+newText+"\"");
                searchedCategoryArrayList = new ArrayList<>();

                categoryArrayList = categoryMenuAdapter.getCategoriesArrayList();

                if(newText.length()>0 && categoryArrayList != null){
                    for(int i = 0; i < categoryArrayList.size(); i++){
                        if(categoryArrayList.get(i).getName().toUpperCase().contains(newText.toUpperCase())){

                            Category searchedCategory = new Category();
                            searchedCategory.setId(categoryArrayList.get(i).getId());
                            searchedCategory.setName(categoryArrayList.get(i).getName());
                            searchedCategory.setDescription(categoryArrayList.get(i).getDescription());

                            searchedCategoryArrayList.add(searchedCategory);
                        }

                        Order order = (Order) getArguments().getSerializable("order");
                        CategoriaMenuAdapter categoriaMenuAdapter = new CategoriaMenuAdapter(order);
                        categoriaMenuAdapter.setCategoriesArrayList(searchedCategoryArrayList);
                        fragmentMenuCategoryBinding.menuCategoryRecyclerView.setAdapter(categoriaMenuAdapter);
                    }
                }
                else{
                    logger.info("Filtri per categorie svuotati.");
                    setListAdapter();
                }

                return false;
            }
        });
    }
}