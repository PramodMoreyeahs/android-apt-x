package com.apt_x.app.views.activity;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apt_x.app.R;
import com.apt_x.app.views.activity.Model.Listclass;

import java.util.ArrayList;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {

    ArrayList<Listclass> list;
    private final GoogleplacesActivity context;

    public RecyclerviewAdapter(ArrayList<Listclass> list, GoogleplacesActivity context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textView.setText(list.get(position).getDescription());
        System.out.println("Address selected::" + list.get(position).getPostalcode());
        holder.selectedplaceid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.onClick(list.get(position).getDescription());
                /*context.startActivity(new Intent(context, AddNewRecipientWithAddressActivity.class)
                        .putExtra("SelectedAddress",list.get(position).getDescription()));*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RelativeLayout selectedplaceid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
            selectedplaceid = itemView.findViewById(R.id.placesselectid);

        }
    }


}
