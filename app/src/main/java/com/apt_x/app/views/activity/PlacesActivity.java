package com.apt_x.app.views.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.apt_x.app.R;
import com.apt_x.app.preferences.Pref;
import com.apt_x.app.views.activity.recipient.AddNewRecipientWithAddressActivity;
import com.apt_x.app.views.activity.verification.AddAddressActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PlacesActivity extends AppCompatActivity {


    PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);


        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        placesClient = Places.createClient(this);


        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.

                System.out.println("Places results" + place.getName() + place.getAddress() + place.getLatLng() );
             //   Toast.makeText(getApplicationContext(), place.getName(), Toast.LENGTH_SHORT).show();

                LatLng latLng = place.getLatLng();

              //  getCompleteAddressString(9.9260717,78.1215208);
                getCompleteAddressString(latLng.latitude,latLng.longitude, place.getName() , place.getAddress());

            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
             //   Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    @SuppressLint("LongLogTag")
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE, String name, String fetchaddress) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

           //     String address = addresses.get(0).getAddressLine(0);   // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String address = name + addresses.get(0).getFeatureName();
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();


                System.out.println("splited address"  + city + state + postalCode);


                if(Pref.SIGNUPAUTO.equals("true")){
                    Pref.SIGNUPAUTO = "false";
                    startActivity(new Intent(getApplicationContext(), AddAddressActivity.class)
                            .putExtra("Selectedaddress", address)
                            .putExtra("Selectedcity", city)
                            .putExtra("SelectedpostalCode", postalCode)
                            .putExtra("Selectedstate", state)
                            .putExtra("isFirsttime", "false"));
                }else {

                    startActivity(new Intent(getApplicationContext(), AddNewRecipientWithAddressActivity.class)
                            .putExtra("IsShow", "false")
                            .putExtra("Selectedaddress", address)
                            .putExtra("Selectedcity", city)
                            .putExtra("SelectedpostalCode", postalCode)
                            .putExtra("Selectedstate", state));
                }

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                   // strReturnedAddress.append(returnedAddress.getLocality(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();

                System.out.println("Slected lat long::" + strReturnedAddress );
                Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

}