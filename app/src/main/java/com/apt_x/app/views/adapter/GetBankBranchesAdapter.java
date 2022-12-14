package com.apt_x.app.views.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apt_x.app.R;
import com.apt_x.app.databinding.BankBranchesListItemBinding;
import com.apt_x.app.model.GetBankBranchesResponse;
import com.apt_x.app.views.activity.getBank.BankBranchesActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GetBankBranchesAdapter extends RecyclerView.Adapter<GetBankBranchesAdapter.ServicesViewHolder> {
    private final BankBranchesActivity context;
    private List<GetBankBranchesResponse.DataEntity> serviceList;

    public GetBankBranchesAdapter(BankBranchesActivity context, List<GetBankBranchesResponse.DataEntity> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NotNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BankBranchesListItemBinding bannerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.bank_branches_list_item,
                parent, false
        );
        return new ServicesViewHolder(bannerBinding);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        GetBankBranchesResponse.DataEntity obj=serviceList.get(position);
        holder.binding.setData(obj);
        holder.binding.llMain.setOnClickListener(view -> {

            context.navigate(obj);




        });


    }

    public void setList(List<GetBankBranchesResponse.DataEntity> basecategoryWithOffers) {
        this.serviceList = basecategoryWithOffers;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServicesViewHolder extends RecyclerView.ViewHolder {
        private final BankBranchesListItemBinding binding;

        public ServicesViewHolder(BankBranchesListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
