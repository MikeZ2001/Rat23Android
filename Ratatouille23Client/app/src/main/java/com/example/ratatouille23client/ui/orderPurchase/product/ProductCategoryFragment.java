package com.example.ratatouille23client.ui.orderPurchase.product;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.R;
import com.example.ratatouille23client.databinding.FragmentProductCategoryBinding;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.model.Product;
import com.example.ratatouille23client.viewModel.orderPurchase.ProductCategoryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class ProductCategoryFragment extends Fragment {

    private List<Product> productCategoryArrayList = new ArrayList<>();
    private List<Product> searchedProductCategoryArrayList;
    private FragmentProductCategoryBinding fragmentProductCategoryBinding;
    private ProductCategoryViewModel productCategoryViewModel;
    private ProductCategoryAdapter productCategoryAdapter;

    private final Logger logger = LoggerUtility.getLogger();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setComponentsFunctionalities();
        logger.info("Terminata inizializzazione fragment prodotti della categoria.");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        logger.info("Inizializzazione fragment prodotti della categoria.");
        productCategoryViewModel = new ViewModelProvider(this).get(ProductCategoryViewModel.class);

        fragmentProductCategoryBinding = FragmentProductCategoryBinding.inflate(getLayoutInflater());

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if(actionBar != null)
            actionBar.setTitle(getArguments().getString("categoryName"));

        return fragmentProductCategoryBinding.getRoot();

    }

    private void setComponentsFunctionalities() {
        setLayoutManager();
        setListAdapter();
        setSearchViewListener();
        setCartButtonListener();
        setRefreshLayoutListener();
    }

    private void setRefreshLayoutListener() {
        fragmentProductCategoryBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                logger.info("Refresh schermata.");
                logger.info("Avvio procedura ottenimento prodotti della categoria.");
                productCategoryViewModel.getProductsByCategoryId(getArguments().getLong("categoryId"));
                logger.info("Terminata procedura ottenimento prodotti della categoria.");
                productCategoryAdapter.notifyDataSetChanged();

                fragmentProductCategoryBinding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void setCartButtonListener() {
        fragmentProductCategoryBinding.viewCartProductCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click sul pulsante Carrello.");
                Bundle bundle = new Bundle();

                Order order = (Order) getArguments().getSerializable("order");
                bundle.putSerializable("order",order);

                logger.info("Reindirizzamento alla schermata del carrello.");
                Navigation.findNavController(view).navigate(R.id.action_menuItemFragment_to_cartFragment,bundle);
            }
        });
    }

    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        fragmentProductCategoryBinding.menuCategoryItemRecyclerView.setLayoutManager(layoutManager);
    }

    private void setListAdapter() {
        Order order = (Order) getArguments().getSerializable("order");
        productCategoryAdapter = new ProductCategoryAdapter(order);

        fragmentProductCategoryBinding.menuCategoryItemRecyclerView.setAdapter(productCategoryAdapter);

        try {
            productCategoryViewModel.getProductListObserver().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
                @Override
                public void onChanged(List<Product> productsList) {
                    if (productsList != null) {
                        logger.info("Aggiornata lista prodotti.");
                        productCategoryAdapter.setProductCategoryArrayList(productsList);
                    }else{
                        productCategoryViewModel.getErrorMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                logger.severe(s);
                                productCategoryAdapter.setProductCategoryArrayList(null);
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
        logger.info("Avvio procedura ottenimento prodotti della categoria.");
        productCategoryViewModel.getProductsByCategoryId(getArguments().getLong("categoryId"));
        logger.info("Terminata procedura ottenimento prodotti della categoria.");

    }
    private void setSearchViewListener() {
        fragmentProductCategoryBinding.menuCategoryItemSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                logger.info("Filtrando prodotti per \""+query+"\"");
                searchedProductCategoryArrayList = new ArrayList<>();

                productCategoryArrayList = productCategoryAdapter.getProductCategoryArrayList();

                if(query.length()>0 && productCategoryArrayList != null){

                    for(int i = 0; i < productCategoryArrayList.size(); i++){

                        if(productCategoryArrayList.get(i).getName().toUpperCase().contains(query.toUpperCase())){

                            Product searchedProduct = new Product();

                            searchedProduct.setId(productCategoryArrayList.get(i).getId());
                            searchedProduct.setName(productCategoryArrayList.get(i).getName());
                            searchedProduct.setDescription(productCategoryArrayList.get(i).getDescription());
                            searchedProduct.setPrice(productCategoryArrayList.get(i).getPrice());
                            searchedProduct.setCategoryOfTheProduct(productCategoryArrayList.get(i).getCategoryOfTheProduct());

                            searchedProductCategoryArrayList.add(searchedProduct);
                        }

                        Order order = (Order) getArguments().getSerializable("order");
                        productCategoryAdapter = new ProductCategoryAdapter(order);

                        fragmentProductCategoryBinding.menuCategoryItemRecyclerView.setAdapter(productCategoryAdapter);

                        productCategoryAdapter.setProductCategoryArrayList(searchedProductCategoryArrayList);

                    }
                }
                else{
                    logger.info("Filtri per prodotti svuotati.");
                   setListAdapter();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                logger.info("Filtrando prodotti per \""+newText+"\"");
                searchedProductCategoryArrayList = new ArrayList<>();
                productCategoryArrayList = productCategoryAdapter.getProductCategoryArrayList();

                if(newText.length()>0 && productCategoryArrayList != null){

                    for(int i = 0; i < productCategoryArrayList.size(); i++){

                        if(productCategoryArrayList.get(i).getName().toUpperCase().contains(newText.toUpperCase())){

                            Product searchedProduct = new Product();
                            searchedProduct.setId(productCategoryArrayList.get(i).getId());
                            searchedProduct.setName(productCategoryArrayList.get(i).getName());
                            searchedProduct.setDescription(productCategoryArrayList.get(i).getDescription());
                            searchedProduct.setPrice(productCategoryArrayList.get(i).getPrice());
                            searchedProduct.setCategoryOfTheProduct(productCategoryArrayList.get(i).getCategoryOfTheProduct());


                            searchedProductCategoryArrayList.add(searchedProduct);
                        }


                        Order order = (Order) getArguments().getSerializable("order");
                        productCategoryAdapter = new ProductCategoryAdapter(order);
                        productCategoryAdapter.setProductCategoryArrayList(searchedProductCategoryArrayList);
                        fragmentProductCategoryBinding.menuCategoryItemRecyclerView.setAdapter(productCategoryAdapter);

                    }
                }
                else{
                    logger.info("Filtri per prodotti svuotati.");
                    setListAdapter();
                }
                return false;
            }
        });
    }

}