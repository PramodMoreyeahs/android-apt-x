package com.apt_x.app.views.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apt_x.app.R;
import com.apt_x.app.databinding.RecipientItemBinding;
import com.apt_x.app.model.GetUserByEmail;

import com.apt_x.app.views.activity.newTransactions.RecipientActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserRecipientAdapter extends RecyclerView.Adapter<UserRecipientAdapter.ServicesViewHolder> {
    private final RecipientActivity context;
    private List<GetUserByEmail.Data> userList;
    private boolean clickStatus = false;
    private int selectedPosition = -1;


    public UserRecipientAdapter(RecipientActivity context, List<GetUserByEmail.Data> serviceList) {
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
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GetUserByEmail.Data obj = userList.get(position);
        // holder.binding.setData(obj);
        holder.binding.tvName.setText(obj.getFirstName() + " " + obj.getLastName());
        holder.binding.tvEmail.setText(obj.getEmail_id());
        holder.binding.tvIcon.setText(obj.getFirstName().substring(0, 1).toUpperCase());
        holder.binding.llView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if (clickStatus == false) {
                    context.navigate(obj);
              /*  } else {
                    clickStatus = false;
                    holder.binding.llView.setBackgroundColor(context.getResources().getColor(R.color.background));
                    holder.binding.llDelete.setVisibility(View.GONE);
                    holder.binding.ivDelete.setVisibility(View.GONE);
                }*/

            }
        });
       /* if (selectedPosition == position) {
            holder.binding.llView.setBackgroundColor(context.getResources().getColor(R.color.red_transparent));
            holder.binding.llDelete.setVisibility(View.VISIBLE);
            holder.binding.ivDelete.setVisibility(View.VISIBLE);
        } else {
            holder.binding.llView.setBackgroundColor(context.getResources().getColor(R.color.background));
            holder.binding.llDelete.setVisibility(View.GONE);
            holder.binding.ivDelete.setVisibility(View.GONE);
        }*/

     /*   holder.binding.llView.setBackgroundColor(context.getResources().getColor(R.color.background));
        holder.binding.llDelete.setVisibility(View.GONE);
        holder.binding.ivDelete.setVisibility(View.GONE);*/


      /*  holder.binding.llView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                        clickStatus = true;
                        holder.binding.llView.setBackgroundColor(context.getResources().getColor(R.color.red_transparent));
                        holder.binding.llDelete.setVisibility(View.VISIBLE);
                        holder.binding.ivDelete.setVisibility(View.VISIBLE);
                if (selectedPosition >= 0)
                    notifyItemChanged(selectedPosition);
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(selectedPosition);
                        return true;
            }
        });*/
        holder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.confirmDialog(obj, position);
            }
        });
       /* holder.binding.tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.binding.tvMenu);

                popup.inflate(R.menu.bottom_menu);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_1:
                                context.confirmDialog(obj, position);
                                break;

                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });*/


    }


    public void setList(List<GetUserByEmail.Data> basecategoryWithOffers) {
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
