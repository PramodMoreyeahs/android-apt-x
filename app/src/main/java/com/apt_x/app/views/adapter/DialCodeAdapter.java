package com.apt_x.app.views.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.apt_x.app.R;
import com.apt_x.app.model.CountriesResponse;
import com.apt_x.app.databinding.ListItemDialCodeBinding;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.annotations.NonNull;

public class DialCodeAdapter extends RecyclerView.Adapter<DialCodeAdapter.BindingHolder> implements Filterable {

    Context context;
    private ArrayList<CountriesResponse.Data> countryCodeResponses;
    public List<CountriesResponse.Data> filterList;
    public List<CountriesResponse.Data> filteredUserList;
    DialCodeAdapter.OnItemClickListener onItemClickListener;
    DialCodeAdapter.UserFilter valueFilter;


    public void setOnItemClickListener(DialCodeAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public DialCodeAdapter(Context context, ArrayList<CountriesResponse.Data> countryCodeResponses) {
        this.context = context;
        this.countryCodeResponses = countryCodeResponses;
        this.filteredUserList = countryCodeResponses;
        notifyDataSetChanged();
    }

    public void setUpdatedList(ArrayList<CountriesResponse.Data> clinicProviderResponses) {
        this.countryCodeResponses = clinicProviderResponses;
        this.filteredUserList = clinicProviderResponses;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public DialCodeAdapter.BindingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemDialCodeBinding commentsHeaderBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item_dial_code,
                parent,
                false);

        return new DialCodeAdapter.BindingHolder(commentsHeaderBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(DialCodeAdapter.BindingHolder holder, final int position) {

        holder.binding.countryTv.setText(countryCodeResponses.get(position).getName());
        holder.binding.dialcodeTv.setText(countryCodeResponses.get(position).getCountryCode());


        holder.binding.rootLayout.setOnClickListener(view -> {
            onItemClickListener.onItemClick(position, view.getId());
        });


    }

    @Override
    public int getItemCount() {
        return countryCodeResponses.size();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new DialCodeAdapter.UserFilter();
        }
        return valueFilter;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int id);
    }


    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ListItemDialCodeBinding binding;

        public BindingHolder(ListItemDialCodeBinding binding) {
            super(binding.rootLayout);
            this.binding = binding;
        }
    }

    public ArrayList<CountriesResponse.Data> getList() {
        return countryCodeResponses;
    }


    private class UserFilter extends Filter {
        FilterResults results = new FilterResults();

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null && constraint.length() > 0) {
                filterList = new ArrayList<>();
                for (int i = 0; i < filteredUserList.size(); i++) {
                    if ((filteredUserList.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase())
                    ||filteredUserList.get(i).getCountryCode().contains(constraint.toString())) {
                        filterList.add(filteredUserList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = filteredUserList.size();
                results.values = filteredUserList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            countryCodeResponses = (ArrayList<CountriesResponse.Data>) filterResults.values;
            notifyDataSetChanged();
        }
    }

}
