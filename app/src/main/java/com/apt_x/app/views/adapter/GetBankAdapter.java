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
import com.apt_x.app.databinding.BankListItemBinding;
import com.apt_x.app.model.GetBankModel;
import com.apt_x.app.views.activity.getBank.BankDepositActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GetBankAdapter extends RecyclerView.Adapter<GetBankAdapter.ServicesViewHolder> {
    private final BankDepositActivity context;
    private List<GetBankModel.DataEntity> serviceList;

    public GetBankAdapter(BankDepositActivity context, List<GetBankModel.DataEntity> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NotNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BankListItemBinding bannerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.bank_list_item,
                parent, false
        );
        return new ServicesViewHolder(bannerBinding);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        GetBankModel.DataEntity obj=serviceList.get(position);
        holder.binding.setData(obj);
       // holder.binding.ivImage.setImageDrawable(obj.getImage());
        holder.binding.llMain.setOnClickListener(view -> {
            context.navigate(obj);
        });

    }

    public void setList(List<GetBankModel.DataEntity> dataEntityList) {
        this.serviceList = dataEntityList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServicesViewHolder extends RecyclerView.ViewHolder {
        private final BankListItemBinding binding;

        public ServicesViewHolder(BankListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
