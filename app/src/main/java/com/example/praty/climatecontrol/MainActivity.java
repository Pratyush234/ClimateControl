package com.example.praty.climatecontrol;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    long MIN_TIME = 0;
    float MIN_DISTANCE = 0;
    final int COARSE_LOCATION_REQUEST_CODE = 102;
    String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;

    String URL_ID = "https://api.openweathermap.org/data/2.5/weather";
    String app_id = "628ea6eda6a89dd06081dd2113644d39";

    TextView mTemperatureText;
    TextView mLocationText;
    ImageView mWeatherImage;
    ImageButton mImageButton;

    LocationManager mlocationManager;
    LocationListener mlocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTemperatureText = (TextView) findViewById(R.id.temperatureText);
        mLocationText = (TextView) findViewById(R.id.locationText);
        mWeatherImage = (ImageView) findViewById(R.id.weatherImage);
        mImageButton= (ImageButton) findViewById(R.id.locationButton);

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent= new Intent(MainActivity.this,CityChange.class);
                startActivity(mIntent);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ClimateControl", "onResume was called");

        Intent mIntent=getIntent();
        String city=mIntent.getStringExtra("City");

        if(city!=null){
            getWeatherForNewCity(city);
        }
       else {
            getWeatherForCurrentLocation();
        }
    }

    private void getWeatherForNewCity(String city){
        RequestParams params= new RequestParams();
        params.put("q",city);
        params.put("appid",app_id);
        letsDoSomeNetworking(params);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mlocationManager!=null) mlocationManager.removeUpdates(mlocationListener);
    }

    private void getWeatherForCurrentLocation() {

        mlocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mlocationListener = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("ClimateControl", "onStatusChanged() has been called");

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("ClimateControl", "onProviderEnabled() has been called");


            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("ClimateControl", "onProviderDisabled() has been called");


            }

            @Override
            public void onLocationChanged(Location location) {
                Log.d("ClimateControl", "onLocationChanged() has been called");
                String longitude = String.valueOf(location.getLongitude());
                String latitude = String.valueOf(location.getLatitude());
                Toast.makeText(MainActivity.this, "Latitude:" + latitude + "and Longitude:" + longitude, Toast.LENGTH_SHORT).show();

                RequestParams params = new RequestParams();
                params.put("lat", latitude);
                params.put("lon", longitude);
                params.put("appid", app_id);
                letsDoSomeNetworking(params);
            }

        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, COARSE_LOCATION_REQUEST_CODE);
        }
        mlocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mlocationListener);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == COARSE_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("ClimateControl", "Request granted");
               getWeatherForCurrentLocation();

            }
            else{
                Log.d("ClimateControl","Request denied");
            }
        }
   }
    private void letsDoSomeNetworking(RequestParams params){
        AsyncHttpClient client= new AsyncHttpClient();
        client.setURLEncodingEnabled(false);
        client.get(URL_ID,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                Log.d("ClimateControl","Success! JSON: "+response.toString());

                WeatherData weatherData= WeatherData.fromJson(response);
                updateUI(weatherData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e,JSONObject response){
                Log.e("ClimateControl","Fail"+ e.toString());
                Log.d("ClimateControl","Status code:"+statusCode);
                Toast.makeText(MainActivity.this,"Request failed",Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void updateUI(WeatherData weatherData){
        mTemperatureText.setText(weatherData.getmTemperature());
        mLocationText.setText(weatherData.getmCity());

        int resourceID= getResources().getIdentifier(weatherData.getmIconName(),"drawable",getPackageName());
        mWeatherImage.setImageResource(resourceID);
    }


}
