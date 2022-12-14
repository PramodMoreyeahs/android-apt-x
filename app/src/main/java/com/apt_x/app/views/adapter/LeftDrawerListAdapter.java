package com.apt_x.app.views.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.apt_x.app.R;
import com.apt_x.app.model.LeftMenuDrawerItems;
import com.apt_x.app.databinding.LeftDrawerItemsBinding;

import java.util.List;

/**
 */
public class LeftDrawerListAdapter extends RecyclerView.Adapter<LeftDrawerListAdapter.BindingHolder> {

    Context context;
    private List<LeftMenuDrawerItems> leftDrawerMenuList;
    OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public LeftDrawerListAdapter(Context context, List<LeftMenuDrawerItems> leftDrawerMenuList) {
        this.context = context;
        this.leftDrawerMenuList = leftDrawerMenuList;
    }


    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LeftDrawerItemsBinding commentsHeaderBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.left_drawer_items,
                parent,
                false);

        return new BindingHolder(commentsHeaderBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {

        LeftMenuDrawerItems leftMenuDrawerItems = leftDrawerMenuList.get(position);

        holder.binding.leftDrawerTxt.setText(leftMenuDrawerItems.getTitle());

       /* if (leftDrawerMenuList.get(position).isMenuIsSelected()) {

            holder.binding.leftDrawerTxt.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.binding.rootLayout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
           holder.binding.leftDrawerIv.setImageResource(leftDrawerMenuList.get(position).getSelectedIcon());
        } else {

            holder.binding.leftDrawerTxt.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.binding.rootLayout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.binding.leftDrawerIv.setImageResource(leftDrawerMenuList.get(position).getDeSelectedIcon());
        }*/

        holder.binding.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
            }
        });

        holder.binding.leftDrawerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return leftDrawerMenuList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public static class BindingHolder extends RecyclerView.ViewHolder {
        private LeftDrawerItemsBinding binding;

        public BindingHolder(LeftDrawerItemsBinding binding) {
            super(binding.rootLayout);
            this.binding = binding;
        }
    }


//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView leftMenuTxt;
//        LinearLayout rootLayout;
//        ImageView menuIcon;
//
//        public MyViewHolder(View view) {
//            super(view);
//            leftMenuTxt = (TextView) view.findViewById(R.id.leftDrawerTxt);
//            rootLayout = (LinearLayout) view.findViewById(R.id.rootLayout);
//            menuIcon = (ImageView) view.findViewById(R.id.left_drawer_iv);
//        }
//    }

}
