package com.example.ratatouille23client.ui.orderPurchase.category;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ratatouille23client.LoggerUtility;
import com.example.ratatouille23client.R;
import com.example.ratatouille23client.databinding.CategoryItemBinding;
import com.example.ratatouille23client.model.Category;
import com.example.ratatouille23client.model.Order;
import com.example.ratatouille23client.viewModel.orderPurchase.MenuCategoryViewModel;

import java.util.List;
import java.util.logging.Logger;

public class CategoriaMenuAdapter extends RecyclerView.Adapter<CategoriaMenuAdapter.ViewHolder> {

    List<Category> categoriesArrayList;
    Order order;

    MenuCategoryViewModel menuCategoryViewModel;

    private final Logger logger = LoggerUtility.getLogger();

    public CategoriaMenuAdapter(Order order) {
        this.order = order;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryItemBinding categoryItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.category_item,parent,false);

        menuCategoryViewModel = new MenuCategoryViewModel();

        return new ViewHolder(categoryItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaMenuAdapter.ViewHolder holder, int position) {

        holder.categoryItemBinding.setCategory(categoriesArrayList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su una categoria.");
                Bundle bundle = new Bundle();

                Long id = holder.categoryItemBinding.getCategory().getId();
                String categoryName= holder.categoryItemBinding.getCategory().getName();

                logger.info("Categoria selezionata.");

                bundle.putLong("categoryId", id);
                bundle.putString("categoryName",categoryName);
                bundle.putSerializable("order",order);

                logger.info("Reindirizzamento alla schermata prodotti della categoria.");
                Navigation.findNavController(view).navigate(R.id.action_menuCategoryFragment_to_menuItemFragment,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(categoriesArrayList != null)
            return categoriesArrayList.size();
        else
            return 0;
    }

    public List<Category> getCategoriesArrayList() {
        notifyDataSetChanged();
        return this.categoriesArrayList;
    }

    public void setCategoriesArrayList(List<Category> categoriesArrayList) {
        this.categoriesArrayList = categoriesArrayList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CategoryItemBinding categoryItemBinding;

        public ViewHolder(@NonNull CategoryItemBinding categoryItemBinding) {
            super(categoryItemBinding.getRoot());

            this.categoryItemBinding = categoryItemBinding;
        }
    }

}
