package com.apt_x.app.views.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apt_x.app.R;
import com.apt_x.app.databinding.AddcarddetailsBinding;
import com.apt_x.app.databinding.BankListItemBinding;
import com.apt_x.app.model.CardDetaildemomodel;
import com.apt_x.app.views.activity.card.BlockCardActivity;
import com.apt_x.app.views.activity.card.CardDetailNewActivity;
import com.apt_x.app.views.activity.card.CardPinActivity;
import com.apt_x.app.views.activity.card.ChangePinActivity;
import com.apt_x.app.views.activity.kyc.KYCDetailsActivity;
import com.apt_x.app.views.activity.loadfund.AddCardNewActivity;

import java.util.List;

public class AddCardDetailsAdapter extends RecyclerView.Adapter<AddCardDetailsAdapter.ServicesViewHolder>{

    private final AddCardNewActivity context;
    private List<CardDetaildemomodel> addcardList;

    public AddCardDetailsAdapter(AddCardNewActivity context, List<CardDetaildemomodel> addcardList) {
        this.context = context;
        this.addcardList = addcardList;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        AddcarddetailsBinding addcarddetailedBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.addcarddetails,
                parent, false
        );

        return new ServicesViewHolder(addcarddetailedBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddCardDetailsAdapter.ServicesViewHolder holder, int position) {
        //GetBankModel.DataEntity obj=serviceList.get(position);
        CardDetaildemomodel obj = addcardList.get(position);
        holder.binding.tvcardfunctions.setText(obj.getName());
        System.out.println("tvfun ::" + obj.getType());
        if(obj.getName().equals("Issue physical Card")){
            holder.binding.llMain.setBackground(ContextCompat.getDrawable(context, R.drawable.pinkbg));
            holder.binding.ivImage.setBackground(ContextCompat.getDrawable(context, R.drawable.whitebg));
            holder.binding.ivImage.setImageDrawable(context.getDrawable(R.drawable.physicalcard));
        }
        if(obj.getName().equals("Block Card")){
            holder.binding.tvcardfunctions.setTextColor(0xFFFF0000);
        }
        if(obj.getName().equals("Change Pin")){
            holder.binding.ivImage.setImageDrawable(context.getDrawable(R.drawable.cpin));;
        }
        if(obj.getName().equals("Block Card")){
            holder.binding.ivImage.setImageDrawable(context.getDrawable(R.drawable.lock));;
        }

        holder.binding.llMain.setOnClickListener(view -> {
            if(obj.getName().equals("Issue physical Card")){
                context.startActivity(new Intent(context, CardPinActivity.class));
            }
            if(obj.getName().equals("Change Pin")) {
                context.startActivity(new Intent(context, ChangePinActivity.class));
            }
            if(obj.getName().equals("See Card Details")){
                context.startActivity(new Intent(context, CardDetailNewActivity.class));
            }
            if(obj.getName().equals("Block Card")){
                context.startActivity(new Intent(context, BlockCardActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return addcardList.size();
    }

    public static class ServicesViewHolder extends RecyclerView.ViewHolder {
        private final AddcarddetailsBinding binding;

        public ServicesViewHolder(AddcarddetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
