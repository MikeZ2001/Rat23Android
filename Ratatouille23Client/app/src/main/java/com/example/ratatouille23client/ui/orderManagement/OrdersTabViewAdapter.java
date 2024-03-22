package com.example.ratatouille23client.ui.orderManagement;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ratatouille23client.ui.orderManagement.acceptedOrder.AcceptedOrdersFragment;
import com.example.ratatouille23client.ui.orderManagement.deletedOrder.DeletedOrdersFragment;
import com.example.ratatouille23client.ui.orderManagement.completedOrder.CompletedOrdersFragment;
import com.example.ratatouille23client.ui.orderManagement.inProgressOrder.InProgressOrderFragment;
import com.example.ratatouille23client.ui.orderManagement.payedOrder.PayedOrderFragment;
import com.example.ratatouille23client.ui.orderManagement.readyOrder.ReadyOrdersFragment;

import java.util.logging.Logger;

public class OrdersTabViewAdapter extends FragmentStateAdapter {

    public OrdersTabViewAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return new AcceptedOrdersFragment();
        else if (position == 1)
            return new InProgressOrderFragment();
        else if (position == 2)
           return new ReadyOrdersFragment();
        else if (position == 3)
           return new CompletedOrdersFragment();
        else if (position == 4)
            return new DeletedOrdersFragment();
        else if (position == 5)
            return new PayedOrderFragment();

        return new AcceptedOrdersFragment();
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}

