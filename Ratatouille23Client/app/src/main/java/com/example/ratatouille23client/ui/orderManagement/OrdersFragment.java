package com.example.ratatouille23client.ui.orderManagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ratatouille23client.CustomLogger;
import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.databinding.FragmentOrdersBinding;
import com.example.ratatouille23client.model.Employee;
import com.example.ratatouille23client.viewModel.orderManagement.OrdersViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.logging.Logger;

public class OrdersFragment extends Fragment {

    private FragmentOrdersBinding binding;
    private OrdersTabViewAdapter ordersTabViewAdapter;

    private final Logger logger = LoggerUtility.getLogger();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logger.info("Inizializzazione fragment ordini.");
        OrdersViewModel ordersViewModel =
                new ViewModelProvider(this).get(OrdersViewModel.class);

        binding = FragmentOrdersBinding.inflate(getLayoutInflater(), container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setComponentsFunctionalities();
        logger.info("Terminata inizializzazione fragment ordini.");
    }

    private void setComponentsFunctionalities() {
        setTabs();
        setFragmentManager();
        setTabListener();
        setViewPagerListener();
    }

    private void setTabs() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Accettate"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("In Preparazione"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Pronte"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Servite"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Annullate"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Pagate"));
    }

    private void setViewPagerListener() {
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                logger.info("Cambiata tab.");
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
            }
        });
    }

    private void setTabListener() {
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                logger.info("Tab selezionata.");
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    private void setFragmentManager() {
        FragmentManager fragmentManager = getChildFragmentManager();
        ordersTabViewAdapter = new OrdersTabViewAdapter(fragmentManager,getLifecycle());
        binding.viewPager.setAdapter(ordersTabViewAdapter);
    }
}