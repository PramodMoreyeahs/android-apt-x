package com.apt_x.app.views.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apt_x.app.R;
import com.apt_x.app.databinding.CardItemBinding;
import com.apt_x.app.model.bean.GetBankListResponse;
import com.apt_x.app.views.activity.withdraw.SendingPaymentActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GetCardAdapter extends RecyclerView.Adapter<GetCardAdapter.ServicesViewHolder> {
    private final Context context;
    private List<GetBankListResponse.DataEntity> serviceList;

    public GetCardAdapter(Context context, List<GetBankListResponse.DataEntity> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NotNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardItemBinding cardItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.card_item,
                parent, false
        );
        return new ServicesViewHolder(cardItemBinding);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        GetBankListResponse.DataEntity obj=serviceList.get(position);

        holder.binding.tvcardNumber.setText("XXXX XXXX XXXX "+obj.getDisbursementnumber().toUpperCase().substring(obj
                .getDisbursementnumber().length()-4));
        holder.binding.tvexpiry.setText(obj.getExpirationdate());
        if(obj.getType()==1)
    holder.binding.tvType.setText("Debit card");
        holder.binding.llMain.setOnClickListener(view -> {
            context.startActivity(new Intent(context, SendingPaymentActivity.class)
                    .putExtra("bandData",obj).putExtra("TYPE","card"));
        });


    }

    public void setList(List<GetBankListResponse.DataEntity> basecategoryWithOffers) {
        this.serviceList = basecategoryWithOffers;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServicesViewHolder extends RecyclerView.ViewHolder {
        private final CardItemBinding binding;

        public ServicesViewHolder(CardItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
