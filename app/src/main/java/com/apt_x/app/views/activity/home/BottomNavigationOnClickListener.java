package com.apt_x.app.views.activity.home;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;


import com.apt_x.app.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 🔥 MVP-I Architecture and Android Jetpack 🔥
 * 🍴 Focused on Clean Architecture
 *
 * @author Created by 🔱 Pratik Kataria 🔱 on 12-07-2021.
 */


public class BottomNavigationOnClickListener implements View.OnClickListener {
    HomeActivity _activity;
    Map<Integer, Integer> _routes = new HashMap<>();

    BottomNavigationOnClickListener() {
        _routes.put(Routes.Home.getRoute(), R.id.homeFragment);
        _routes.put(Routes.Transaction.getRoute(), R.id.transactionFragment);
        //    _routes.put(Routes.P2p.getRoute(), R.id.mycardFragment);
        // _routes.put(Routes.Purchase.getRoute(), R.id.btm_nav_purchase);
    }

    @Override
    public void onClick(View v) {
        _activity = (HomeActivity) v.getContext();
        if (_routes.containsKey(v.getId())) _activity.binding.btmNavigation.setSelectedItemId(_routes.get(v.getId()));

        for (int i : _routes.keySet()) {
            int color = v.getId() == i ? ContextCompat.getColor(v.getContext(), R.color.white) : ContextCompat.getColor(v.getContext(), R.color.colorPrimary);
            View pView = ((View) v.getParent()).findViewById(i);
            ImageView imageView = pView.findViewWithTag("image");
            imageView.setColorFilter(color);
            //  TextView textView = pView.findViewWithTag("text");
            // textView.setTextColor(color);
        }
    }
}

enum Routes {
    Home(R.id.ll_home),
    Transaction(R.id.ll_transaction),
    P2p(R.id.p2p);

    /*Ticket(R.id.ll_ticket),
    Purchase(R.id.ll_purchase);*/

    private final int routeId;

    Routes(int routeId) {
        this.routeId = routeId;
    }
    int getRoute() {
        return routeId;
    }


}

