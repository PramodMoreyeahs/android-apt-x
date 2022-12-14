package com.apt_x.app.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apt_x.app.R;
import com.apt_x.app.model.GetUserByEmail;
import com.apt_x.app.model.GetUserByEmail.Data;

import java.util.ArrayList;
import java.util.List;

public class UserAdaptor extends ArrayAdapter<GetUserByEmail.Data> {

    private Context context;
    private ArrayList<GetUserByEmail.Data> userList, suggestList, tempList;

    public UserAdaptor(Context context, int resource, ArrayList<GetUserByEmail.Data> list) {
        super(context, resource, list);
        this.context = context;
        this.userList = list;
        this.tempList = new ArrayList<>(userList);
        this.suggestList = new ArrayList<>();
    }

    public void setList(ArrayList<GetUserByEmail.Data> list) {
        this.userList = list;

        this.suggestList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.user_item, parent, false);
        }
        GetUserByEmail.Data data = userList.get(position);
        if (data != null) {
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_email = (TextView) view.findViewById(R.id.tv_email);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
            if (tv_name != null) {
                tv_name.setText(data.getEmail());
            }
            tv_name.setText(data.getFirstName()+" "+data.getLastName());
            tv_email.setText(data.getEmail());
            /*Glide
                    .with(context)
                    .asBitmap()
                    .load( data.getProfilePicture())
                    // .placeholder(bitmapImage)
                    .into(imageView);*/
        }
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestList.clear();
                for (GetUserByEmail.Data people : tempList) {
                    if (people.getEmail().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestList.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestList;
                filterResults.count = suggestList.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Data> filterList = (ArrayList<Data>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Data people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }

        }


    };
}

