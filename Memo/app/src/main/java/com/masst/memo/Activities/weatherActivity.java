package com.masst.memo.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.masst.memo.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class weatherActivity extends AppCompatActivity implements LocationListener {

    private static final String TAG="";
    private static final int REQ_PERMISSION = 0;
     TextView temperaturetext, nametext, descriptText;

    int MY_PERMISSION = 0;
    LocationManager locationManager;
    Location mCurrentLocation;
    private String mLastUpdateTime;
    private ProgressBar spinner;
    String provider;
    double lat, lng;

    private Snackbar snackbar;
    public View snackview;
    public Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE
            }, MY_PERMISSION);}

        if (!isLocationEnabled())
        {
            showSettingAlert();
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, true);
            location = locationManager.getLastKnownLocation(provider);
        }
        snackview= findViewById(android.R.id.content);

        spinner=(ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        temperaturetext = (TextView) findViewById(R.id.temperature);
        nametext = (TextView) findViewById(R.id.name);
        descriptText = (TextView) findViewById(R.id.description);
        //Log.i("MYMSG", " Location Manager");


        if (location != null) {
            onLocationChanged(location);
        }
        if(!isInternetConnection())
        {
            snackbar= Snackbar.make(snackview, "There is No Internet connection", Snackbar.LENGTH_LONG)
                    .setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
            snackbar.show();
        }
        spinner.setVisibility(View.VISIBLE);
        GetWeather task = new GetWeather();

        task.execute("http://api.openweathermap.org/data/2.5/weather?lat="+String.valueOf(lat)+"&lon="+String.valueOf(lng)+"&appid=4126359df82c135c666d58fc5eb40daf&units=metric");
        // Log.i("MYMSG","task executed");
    }
    public  boolean isInternetConnection()
    {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        return connected;
    }
    public void showSettingAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS setting!");
        alertDialog.setMessage("GPS is not enabled, Do you want to go to settings menu? ");
        alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private class GetWeather extends AsyncTask<String,Void,String>
    {
        String result ="";
        URL url;
        HttpURLConnection urlconnection=null;

        @Override
        protected String doInBackground(String... urls) {


            try {
                url = new URL(urls[0]);
                urlconnection =(HttpURLConnection) url.openConnection();
                if(urlconnection.getResponseCode() == 200)     // 200 means OK
                {
                    // Log.i("MYMSG", "connection created");
                    InputStream in = urlconnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    // Log.i("MYMSG", " reader read stream");
                    int data = reader.read();
                    while (data != -1) {
                        char current = (char) data;

                        result += current;
                        data = reader.read();
                    }
                    urlconnection.disconnect();
                }
                else
                {
                    Log.i("MYMSG","file not found");
                }
                urlconnection.disconnect();
            }
            catch (Exception e)
            {
                Log.i("MYMSG",e.toString());
                e.printStackTrace();
            }
            // result has json data

            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            try {
                JSONObject jsonObject = new JSONObject(result);              // whole data copy in one json object
                //   Log.i("MYMSG","Json object");

                JSONObject weatherData=new JSONObject(jsonObject.getString("main"));
                double temperature = Double.parseDouble(weatherData.getString("temp"));
                int tempIn = (int) (temperature);      // converting in fahernhiet from kelvin
                //   Log.i("MYMSG",String.valueOf(tempIn));

                String placeName=jsonObject.getString("name");
                //   Log.i("MYMSG",placeName);

                JSONArray weatherarr=new JSONArray(jsonObject.getString("weather"));
                String desc="";
                for(int i=0;i<weatherarr.length();i++)
                {
                    JSONObject obj = weatherarr.getJSONObject(i);
                    if(i!=0)
                    {
                        desc+=" and ";
                    }
                    desc+=obj.getString("description");

                }
                temperaturetext.setText(String.valueOf(tempIn)+"°C");
                nametext.setText(jsonObject.getString("name"));
                descriptText.setText(desc);
                //   Log.i("MYMSG","fill data");


            } catch (Exception e) {
                Log.i("MYMSG",e.toString());
                e.printStackTrace();
            }
            if(temperaturetext.getText()!=null&& nametext.getText()!=null && descriptText.getText()!=null)
            {
                spinner.setVisibility(View.GONE);

            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(!isInternetConnection())
        {
            snackbar= Snackbar.make(snackview, "There is No Internet connection", Snackbar.LENGTH_LONG)
                    .setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
            snackbar.show();
        }
        else
        {

        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE
            }, MY_PERMISSION);
        }
        // locationManager.requestLocationUpdates(provider,400,1,(android.location.LocationListener)this);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        GetWeather task = new GetWeather();
        task.execute("http://api.openweathermap.org/data/2.5/weather?lat="+String.valueOf(lat)+"&lon="+String.valueOf(lng)+"&appid=4126359df82c135c666d58fc5eb40daf&units=metric");
    }
    protected boolean isLocationEnabled(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE
            }, MY_PERMISSION);
        }
        String le = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(le);
        if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

