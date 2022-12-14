package com.apt_x.app.views.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apt_x.app.R;
import com.apt_x.app.databinding.MyTransactionsItemBinding;
import com.apt_x.app.model.CrossBorderHistoryResponse;
import com.apt_x.app.utils.DateParser;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.fragment.home.HomeFragment;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ServicesViewHolder> {
    private final Context context;
    private Fragment fragment;
    private List<CrossBorderHistoryResponse.DataEntity> serviceList;
    private boolean type;

    public TransactionAdapter(Fragment fragment, Context context, List<CrossBorderHistoryResponse.DataEntity> serviceList, boolean type) {
        this.fragment = fragment;
        this.context = context;
        this.serviceList = serviceList;
        this.type = type;

    }

    @NotNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyTransactionsItemBinding bannerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.my_transactions_item,
                parent, false
        );
        return new ServicesViewHolder(bannerBinding);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        try {
            CrossBorderHistoryResponse.DataEntity obj = serviceList.get(position);
            holder.binding.tvName.setText(obj.getDescription());
            System.out.println("Date format chk in trans adapter" + obj.getDate());
            holder.binding.tvDate.setText(DateParser.simpleDatehistory(obj.getDate()));

            byte[] b = Base64.decode(obj.getCountry_flag() != null ? obj.getCountry_flag() : "", Base64.DEFAULT);
            Bitmap bitmapImage = BitmapFactory.decodeByteArray(b, 0, b.length);
            holder.binding.ivCountry.setImageBitmap(bitmapImage);

            if (obj.getAmount().contains("-")) {
                float amount= Float.parseFloat(obj.getAmount().replaceAll("-",""));

                String tvpriceamount = String.valueOf(amount+Float.parseFloat(obj.getTransferFee().toString()));
                  String[] decimalSplit = tvpriceamount.split("\\.");
                  System.out.println("Whole Number: " + decimalSplit[0]);
                  System.out.println("Decimal Part: " + decimalSplit[1]);

                System.out.println("tvpriceamount :: " + tvpriceamount);
                holder.binding.tvPrice.setText("-$" + Utils.convertDecimalFormate( (amount+Float.parseFloat(obj.getTransferFee()))));



/*                if(Objects.equals(decimalSplit[1], "00") || Objects.equals(decimalSplit[1], "0") ){


                    holder.binding.tvPrice.setText("-$" + decimalSplit[0]);
                } else{


                }*/



                holder.binding.ivTransaction.setImageDrawable(context.getDrawable(R.drawable.out));
                holder.binding.tvPrice.setTextColor(Color.parseColor("#FF4141"));

            } else {

                holder.binding.ivTransaction.setImageDrawable(context.getDrawable(R.drawable.in));
                holder.binding.tvPrice.setText("+$" + Utils.convertDecimalFormate(Float.parseFloat( obj.getAmount())));
                holder.binding.tvPrice.setTextColor(Color.parseColor("#00C899"));


            }
       /* if(!obj.getTransaction_id().isEmpty())
        {
            holder.binding.tvPrice.setText("-$"+Math.abs(obj.getAmount()));
            holder.binding.ivTransaction.setImageDrawable(context.getDrawable(R.drawable.out));
            holder.binding.tvPrice.setTextColor(Color.parseColor("#FF4141"));
        }*/

            switch (obj.getTransactionStatus()) {
                case "PROCESSING":
                    holder.binding.tvStatus.setText("Processing");
                    holder.binding.ivStatus.setImageDrawable(context.getDrawable(R.drawable.ic_pending));
                    break;
                case "FAILED":
                    holder.binding.tvStatus.setText("Failed");
                    holder.binding.ivStatus.setImageDrawable(context.getDrawable(R.drawable.ic_faild));
                    break;
                case "SUCCESS":
                    holder.binding.tvStatus.setText("Success");
                    holder.binding.ivStatus.setImageDrawable(context.getDrawable(R.drawable.ic_sucess));
                    break;
                default:
                    holder.binding.tvStatus.setText("");
                    holder.binding.ivStatus.setVisibility(View.GONE);


            }

            holder.binding.constL.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("bundle", (Serializable) obj);
                if (fragment instanceof HomeFragment) {
                    NavController navHostController = new NavHostFragment().findNavController(fragment);
                    navHostController.navigate(R.id.action_homeFragment_to_transaction_detail, bundle);
                } else {
                    NavController navHostController = new NavHostFragment().findNavController(fragment);
                    navHostController.navigate(R.id.action_transactionFragment_to_transaction_detail, bundle);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setList(List<CrossBorderHistoryResponse.DataEntity> basecategoryWithOffers) {
        this.serviceList = basecategoryWithOffers;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return type ? serviceList.size() > 5 ? 5 : serviceList.size() : serviceList.size();
    }

    public static class ServicesViewHolder extends RecyclerView.ViewHolder {
        private final MyTransactionsItemBinding binding;

        public ServicesViewHolder(MyTransactionsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
