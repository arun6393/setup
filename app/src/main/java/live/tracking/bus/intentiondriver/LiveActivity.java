package live.tracking.bus.intentiondriver;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import live.tracking.bus.logging.L;
import live.tracking.bus.network.VolleySingleton;
import live.tracking.bus.util.Constants;

public class LiveActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String KEY_LOCATION_UPDATES_ENABLED = "KEY_LOCATION_UPDATES_ENABLED";
    private final String TAG = "VIVZ";
    private TextView mTextLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private String busId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        createGoogleClient();
        createLocationRequest();
        Intent intent = getIntent();
        if (intent != null) {
            L.t(this,busId+"");
            busId = intent.getExtras().getString(MainActivity.EXTRA_BUS_ID);
        }
        mTextLocation = (TextView) findViewById(R.id.message);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        //some changes



    }
    protected synchronized void createGoogleClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }
    protected synchronized void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(40000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
     //   mGoogleApiClient.disconnect();
        super.onStop();
    }


    public void stopTransmitting(View view) {
        L.m("stopTransmitting ");
      //extra

 /*       if (!Utils.backToMain(this, Constants.START_MAIN)) {
//            L.t(this, "first time");
            Utils.setBackToMainT(this,Constants.START_MAIN);
        }*/

        //ends
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
        finish();
    }



    @Override
    public void onConnected(Bundle bundle) {

        L.m("onConnected");
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        if (busId == null || busId.isEmpty()) {
            return;
        }
        if (location == null) {
            return;
        }
        mTextLocation.setText(mTextLocation.getText() + "\nLocation received: " + location.getLatitude()+" : "+location.getLongitude());
//            L.t(this, "onLocationChanged " + location.getLatitude() + ":" + location.getLongitude());

        L.m(Constants.LOCATION_UPDATE_URI +
                "?id=" + busId +
                "&lat=" + location.getLatitude() +
                "&long=" + location.getLongitude());
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constants.LOCATION_UPDATE_URI +
                        "?id=" + busId +
                        "&lat=" + location.getLatitude() +
                        "&long=" + location.getLongitude(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        L.t(LiveActivity.this, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.t(LiveActivity.this, "failure");
            }
        });
        requestQueue.add(stringRequest);
    }


}
