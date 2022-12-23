package com.apt_x.app.views.activity.loadfund;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.apt_x.app.R;
import com.apt_x.app.databinding.ActivityAddCardNewBinding;
import com.apt_x.app.model.CardDetaildemomodel;
import com.apt_x.app.views.activity.card.CardPinActivity;
import com.apt_x.app.views.activity.home.HomeActivity;
import com.apt_x.app.views.adapter.AddCardDetailsAdapter;
import com.apt_x.app.views.base.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddCardNewActivity extends BaseActivity {

    private ActivityAddCardNewBinding binding;
    AddCardDetailsAdapter myAdapter;
    final ArrayList<String> cardsdetaillist = new ArrayList<>();

    ArrayList<CardDetaildemomodel> cardsdetaillist2 = new ArrayList<>();



    CardDetaildemomodel cardsdetaillist1 = new CardDetaildemomodel();
    CardDetaildemomodel cardsdetaillist3 = new CardDetaildemomodel();
    CardDetaildemomodel cardsdetaillist4 = new CardDetaildemomodel();
    CardDetaildemomodel cardsdetaillist5 = new CardDetaildemomodel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_card_new);

        binding.ivBack.setOnClickListener(this);
        cardsdetaillist.add("Issue physical Card");
        cardsdetaillist.add("See Card Details");
        cardsdetaillist.add("Change Pin");
        cardsdetaillist.add("Block Card");



        cardsdetaillist1.setName("Issue physical Card");
        cardsdetaillist3.setName("See Card Details");
        cardsdetaillist4.setName("Change Pin");
        cardsdetaillist5.setName("Block Card");

        cardsdetaillist1.setType("SampleType");
        cardsdetaillist3.setType("SampleType1");
        cardsdetaillist4.setType("SampleType2");
        cardsdetaillist5.setType("SampleType2");



       cardsdetaillist2.add(cardsdetaillist1);
       cardsdetaillist2.add(cardsdetaillist3);
       cardsdetaillist2.add(cardsdetaillist4);
       cardsdetaillist2.add(cardsdetaillist5);



        myAdapter = new AddCardDetailsAdapter(this, cardsdetaillist2);
        binding.rvAddcad.setAdapter(myAdapter);

    }

    @Override
    public void initializeViews() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ivBack:
                onBackPressed();
                break;

        }
    }
}