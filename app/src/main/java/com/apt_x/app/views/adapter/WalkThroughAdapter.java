package com.apt_x.app.views.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.apt_x.app.R;

import java.util.ArrayList;


@SuppressWarnings("ALL")
public class WalkThroughAdapter extends PagerAdapter {

    public ArrayList<Integer> images;
    private LayoutInflater inflater = null;
    private Context mContext;


    public WalkThroughAdapter(Context c, ArrayList<Integer> image) {
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = c;
        this.images = image;

    }


    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup parent, final int position) {
        View convertView = inflater.inflate(R.layout.walk_through_item, parent, false);
        ImageView walkThroughImages = convertView.findViewById(R.id.ivLogo);
        TextView tvHeader = convertView.findViewById(R.id.tvHeader);
        TextView tvDesc = convertView.findViewById(R.id.tvDesc);
        walkThroughImages = convertView.findViewById(R.id.ivLogo);
        walkThroughImages.setBackgroundResource(images.get(position));
        if(position==0){
            tvHeader.setText(mContext.getString(R.string.cross_borde));
            tvDesc.setText(mContext.getString(R.string.send_money_));
        }
        else if(position==1){
            tvHeader.setText(mContext.getString(R.string.rewards));
            tvDesc.setText(mContext.getString(R.string.our_flexibl));
        }
        else if(position==2){
            tvHeader.setText(mContext.getString(R.string.experience));
            tvDesc.setText(mContext.getString(R.string.our_flexibl));
        }

        (parent).addView(convertView, 0);



        return convertView;
    }


    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    private void addOrRemoveProperty(View view, int property, boolean flag){
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if(flag){
            layoutParams.addRule(property);
        }else {
            layoutParams.removeRule(property);
        }
        view.setLayoutParams(layoutParams);
    }

}
