package com.apt_x.app.views.adapter;

import static com.apt_x.app.privacy.netcom.Keys.CURRENCY_CODE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.apt_x.app.R;
import com.apt_x.app.databinding.NewTransactionsItemBinding;
import com.apt_x.app.model.GetCountryServiceResponse;
import com.apt_x.app.model.GetExchangeRateModel;
import com.apt_x.app.privacy.netcom.Keys;
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls;
import com.apt_x.app.preferences.MyPref;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.utils.Utils;
import com.apt_x.app.views.activity.activeCountry.ActiveCountryViewModel;
import com.apt_x.app.views.activity.exchangeRate.CountryListActivity;
import com.apt_x.app.views.activity.exchangeRate.CurrencyConverterActivity;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ServicesViewHolder> {
    private final CountryListActivity context;
    private List<GetCountryServiceResponse.DataEntity> serviceList;
    String service="";
    float rate=0;
    ActiveCountryViewModel viewModel;
    ApiCalls apicalls;
    String c_code="",c_name="",c_flag="",currence_code="",dial_code="";
    boolean currencyStatus=false;
    GetCountryServiceResponse.DataEntity dataobj=new GetCountryServiceResponse.DataEntity();


    public CountryAdapter(CountryListActivity context, List<GetCountryServiceResponse.DataEntity> serviceList,
                          ActiveCountryViewModel viewModel, ApiCalls apicalls) {
        this.context = context;
        this.serviceList = serviceList;
        this.viewModel=viewModel;
        this.apicalls=apicalls;
    }

    @NotNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewTransactionsItemBinding bannerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.new_transactions_item,
                parent, false
        );
        return new ServicesViewHolder(bannerBinding);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        viewModel.response_validator_exchange.observe(context,response_observer_exchange);
        GetCountryServiceResponse.DataEntity obj=serviceList.get(position);
       /* if (obj.getBankdeposit()) holder.binding.setData(obj);
        else serviceList.remove(position);*/
        holder.binding.setData(obj);


        byte[] b = Base64.decode(obj.getFlag(), Base64.DEFAULT);
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(b, 0, b.length);
        holder.binding.ivFlag.setImageBitmap(bitmapImage);
        holder.binding.llMain.setOnClickListener(view -> {

            MyPref.getInstance(context).writePrefs(Pref.COUNTRY_DATA,new Gson().toJson(obj));
            c_code=obj.getCode();
            c_name=obj.getName();
            c_flag=obj.getFlag();
            dial_code=obj.getDialCode();
            currence_code=obj.getCurrency().getCode();
            getConverter(obj.getCode());

          /*  context.startActivity(new Intent(context, AddNewRecipientWithAddressActivity.class)
                    .putExtra(Keys.COUNTRY_CODE ,c_code)
                    .putExtra(Keys.COUNTRY,c_name)
                    .putExtra(Keys.COUNTRY_FLAG,c_flag)
                    .putExtra(Keys.COUNTRY_DIAL_CODE,dial_code)
                    .putExtra(Keys.COUNTRY_RATE,rate)
                    .putExtra(CURRENCY_CODE,currence_code)


            );*/
           /* context.startActivity(new Intent(context, MoneyConverterActivity.class)
                    .putExtra("countryCode" ,obj.getCode())
                    .putExtra("countryName",obj.getName())
                    .putExtra("flag",obj.getFlag())

                    .putExtra("rate",rate)

                    .putExtra("currencyCode",obj.getCurrency().getCode())
            );*/
        });

    }

    public void setList(List<GetCountryServiceResponse.DataEntity> basecategoryWithOffers) {
        this.serviceList = basecategoryWithOffers;
        for (int i=0;i<serviceList.size();i++){
            if (serviceList.get(i).getBankdeposit()){
            }
            else {
                serviceList.remove(i);
            }
        }
        notifyDataSetChanged();

    }
    private void getConverter(String code) {
        Utils.showDialog(context,"Loading");
        viewModel.getExchangeRate(apicalls,code);

    }
    Observer<GetExchangeRateModel> response_observer_exchange= new Observer<GetExchangeRateModel>() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@Nullable GetExchangeRateModel getExchangeRateModel) {
            if(getExchangeRateModel.getStatus())
            {

                for(int i=0;i<getExchangeRateModel.getData().size();i++)
                {
                    if(getExchangeRateModel.getData().get(i).getModeofpayment().equals("BANK DEPOSIT"))
                    {
                        currencyStatus=true;

                        rate= (float) getExchangeRateModel.getData().get(i).getEndrate();
                        service=getExchangeRateModel.getData().get(i).getServiceFee();


                    }
                    else
                    {
                      //  currencyStatus=false;

                        //Utils.showToast(context,"exchange rate not found");
                    }
                }
                if(currencyStatus)
                {
                    context.startActivity(new Intent(context, CurrencyConverterActivity.class)
                            .putExtra(Keys.COUNTRY_CODE ,c_code)
                            .putExtra(Keys.COUNTRY,c_name)
                            .putExtra(Keys.COUNTRY_FLAG,c_flag)
                            .putExtra(Keys.COUNTRY_DIAL_CODE,dial_code)
                            .putExtra(Keys.COUNTRY_RATE,rate)
                            .putExtra(Keys.SERVICE_FEE,service)
                            .putExtra(CURRENCY_CODE,currence_code)


                    );
                }
                else
                {
                    Utils.showToast(context,"exchange rate not found");
                }

            }

        }
    };


    @Override
    public int getItemCount() {
        //Toast.makeText(context, serviceList.size(), Toast.LENGTH_SHORT).show();
        return serviceList.size();
    }

    public static class ServicesViewHolder extends RecyclerView.ViewHolder {
        private final NewTransactionsItemBinding binding;

        public ServicesViewHolder(NewTransactionsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
