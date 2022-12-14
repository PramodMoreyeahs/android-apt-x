package com.apt_x.app.views.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apt_x.app.R;
import com.apt_x.app.databinding.RecipientItemBinding;
import com.apt_x.app.model.UserListP2p.DataEntity;
import com.apt_x.app.model.UserListP2p;
import com.apt_x.app.views.activity.sendMoney.SendingActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecipientAdapter extends RecyclerView.Adapter<RecipientAdapter.ServicesViewHolder> {
    private final Context context;
    private List<UserListP2p.DataEntity> userList;

    public RecipientAdapter(Context context, List<UserListP2p.DataEntity> serviceList) {
        this.context = context;
        this.userList = serviceList;
    }

    @NotNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecipientItemBinding bannerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.recipient_item,
                parent, false
        );
        return new ServicesViewHolder(bannerBinding);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        UserListP2p.DataEntity obj=userList.get(position);
       // holder.binding.setData(obj);
        holder.binding.tvName.setText(obj.getFirstName());
        holder.binding.tvEmail.setText(obj.getEmailid());
        holder.binding.tvIcon.setText(obj.getFirstName().substring(0,1).toUpperCase());
        holder.binding.llView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SendingActivity.class)
                        .putExtra("name",obj.getFirstName().trim())
                        .putExtra("userID",obj.getPayee_id())
                                .putExtra("existing",obj.getId())
                        .putExtra("email",obj.getEmailid().trim()));
            }
        });



    }

    public void setList(List<UserListP2p.DataEntity> basecategoryWithOffers) {
        this.userList = basecategoryWithOffers;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ServicesViewHolder extends RecyclerView.ViewHolder {
        private final RecipientItemBinding binding;

        public ServicesViewHolder(RecipientItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
