package com.apt_x.app.views.activity;

import static com.apt_x.app.authsdk.verifyAucant.Constants.AddressSelected;
import static com.apt_x.app.privacy.netcom.Keys.CURRENCY_CODE;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Config;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.apt_x.app.R;
import com.apt_x.app.authsdk.verifyAucant.Constants;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.views.activity.Model.MainPojo;
import com.apt_x.app.views.activity.newTransactions.RecipientActivity;
import com.apt_x.app.views.activity.recipient.AddNewRecipientWithAddressActivity;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleplacesActivity extends AppCompatActivity {

    EditText editText;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    ApiInterface apiInterface;

    public static String AddressSel="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_places);
        editText =  findViewById(R.id.edittext);
        recyclerView =  findViewById(R.id.recylerview);
        relativeLayout =  findViewById(R.id.notdata_found);
        relativeLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .build();
        apiInterface = retrofit.create(ApiInterface.class);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                getData(editable.toString());
            }
        });
    }

    private void getData(String text)
    {
        apiInterface.getPlace(text,getString(R.string.api_key)).enqueue(new Callback<MainPojo>() {
            @Override
            public void onResponse(Call<MainPojo> call, Response<MainPojo> response) {
                if(response.isSuccessful()){
                    relativeLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(response.body().getpredictions(), GoogleplacesActivity.this);
                    recyclerView.setAdapter(recyclerviewAdapter);

                }else {
                    relativeLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }


            @Override
            public void onFailure(Call<MainPojo> call, Throwable t) {

                relativeLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

            }
        });
    }


    public void onClick(String address) {
        System.out.println("selected address::" + address );

    }
}