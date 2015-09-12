package live.tracking.bus.intentiondriver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import live.tracking.bus.logging.L;
import live.tracking.bus.network.VolleySingleton;
import live.tracking.bus.util.Utils;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private static final String KEY_DATA_SOURCE = "key_data_source";
    private static final String KEY_DATA_DESTINATION = "key_data_destination";
    private static final String KEY_ALL_DATA = "key_all_data";
    private static final String KEY_FIRST_TIME = "keyFirstTime";
    private static final String START_MAIN = "startMain";
    public static final String EXTRA_BUS_ID = "extraBusId";
    private EditText textBusNumber;
    private EditText textBusId;
    private Spinner spinnerSource;
    private Spinner spinnerDestination;
    private String spinnerSourceSelected;
    private String spinnerDestinationSelected;
    private String updateurl;
    private Button buttonStart;
    private ArrayAdapter adapterSource;
    private ArrayAdapter adapterDestination;
    private ArrayList<String> listSource = new ArrayList<>();
    private ArrayList<String> listDestination = new ArrayList<>();
    private ArrayList<String> listData = new ArrayList<>();
    private KeyListener keyListener;


    @Override
    protected void onStart() {
        super.onStart();
        /*if (!Utils.backToMain(this, Constants.START_MAIN)) {
          L.t(this, "first time");
            Intent i=new Intent(this,LiveActivity.class);
            startActivity(i);
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        keyListener = textBusNumber.getKeyListener();
        if (Utils.isFirstTime(this, KEY_FIRST_TIME)) {
//            L.t(this, "first time");
            Utils.setFirstTime(this, KEY_FIRST_TIME);
        }
        if (savedInstanceState != null) {
            listSource = savedInstanceState.getStringArrayList(KEY_DATA_SOURCE);
            listDestination = savedInstanceState.getStringArrayList(KEY_DATA_DESTINATION);
            listData = savedInstanceState.getStringArrayList(KEY_ALL_DATA);
            adapterSource.clear();
            adapterDestination.clear();
            adapterSource.addAll(listSource);
            adapterDestination.addAll(listDestination);
        }
        spinnerSource.setAdapter(adapterSource);
        spinnerDestination.setAdapter(adapterDestination);
        spinnerSource.setOnItemSelectedListener(this);
        spinnerDestination.setOnItemSelectedListener(this);
        textBusId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (Utils.isNetworkAvailable(MainActivity.this)) {
                        textBusNumber.setKeyListener(keyListener);
                        loaddata(textBusId.getText().toString());
                        buttonStart.setEnabled(true);
                    } else {
                        buttonStart.setEnabled(false);
                    }
                }
            }
        });


    }

    private void init() {
        textBusId = (EditText) findViewById(R.id.driverId);
        textBusNumber = (EditText) findViewById(R.id.busNumber);
        spinnerSource = (Spinner) findViewById(R.id.busSource);
        spinnerDestination = (Spinner) findViewById(R.id.busDestination);
        buttonStart = (Button) findViewById(R.id.buttonStart);
        adapterSource = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listSource);
        adapterDestination = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listDestination);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(KEY_DATA_SOURCE, listSource);
        outState.putStringArrayList(KEY_DATA_DESTINATION, listDestination);
        outState.putStringArrayList(KEY_ALL_DATA, listData);
    }

    private void loaddata(String busId) {
//        L.t(this,busId);
        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();

        String requesturl = "http://whereisthebus.in/busdata.php?id=" + busId;
        StringRequest request = new StringRequest(Request.Method.GET, requesturl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.t(MainActivity.this, "onResponse " + response);
                if (response != null && response.length() > 0) {

                    try {
                        listData = parseJSON(response);
                        if (listData.size() == 3) {
                            L.t(MainActivity.this, "this was called " + listData);
                            ArrayList<String> listSpinnerData = new ArrayList<>();
                            listSpinnerData.add(listData.get(1));
                            listSpinnerData.add(listData.get(2));
                            textBusNumber.setText(listData.get(0));
                            textBusNumber.setSelection(textBusNumber.getText().length());
                            textBusNumber.setKeyListener(null);
                            adapterSource.clear();
                            adapterDestination.clear();
                            adapterSource.addAll(listSpinnerData);
                            adapterDestination.addAll(listSpinnerData);
                            buttonStart.setEnabled(true);
                        } else {
                            buttonStart.setEnabled(false);
                        }
                    } catch (JSONException e) {
                        textBusNumber.setText("");
                        adapterSource.clear();
                        adapterDestination.clear();
                        buttonStart.setEnabled(false);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                L.t(MainActivity.this, "onError "+error.networkResponse.toString());
                L.m("onError " + error.networkResponse + "");
                buttonStart.setEnabled(false);
            }
        });
        requestQueue.add(request);
    }

    private ArrayList<String> parseJSON(String output) throws JSONException {
        JSONObject json = new JSONObject(output);
        JSONArray data = json.getJSONArray("row");
        ArrayList<String> listItems = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            JSONObject object = data.getJSONObject(i);
            listItems.add(object.getString("no"));
            listItems.add(object.getString("Source"));
            listItems.add(object.getString("dest"));
        }
        return listItems;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startTransmitting(View view) {
        Intent intent = new Intent(this, LiveActivity.class);
        intent.putExtra(EXTRA_BUS_ID, textBusId.getText().toString());
        //update the source and destination
        updatebusinfo();
        //extra

       /* if (Utils.backToMain(this, Constants.START_MAIN)) {
            L.t(this, "first time");
            Utils.setBackToMain(this, Constants.START_MAIN);
        }*/

        //ends
        startActivity(intent);
    }

    private void updatebusinfo() {
        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
        spinnerSourceSelected = spinnerSource.getSelectedItem().toString();
        Toast.makeText(this, spinnerSourceSelected, Toast.LENGTH_SHORT).show();
        spinnerDestinationSelected = spinnerDestination.getSelectedItem().toString();
        Toast.makeText(this, spinnerDestinationSelected, Toast.LENGTH_SHORT).show();
        updateurl = "http://whereisthebus.in/updatesrc.php?id=" + textBusId.getText().toString() +
                "&source=" + spinnerSourceSelected + "&destination=" + spinnerDestinationSelected;

        StringRequest request1 = new StringRequest(Request.Method.GET, updateurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.t(MainActivity.this, "onResponse " + response);
                if (response != null && response.length() > 0) {
                    L.m("updated" + response);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                L.t(MainActivity.this, "onError "+error.networkResponse.toString());
                L.m("onError " + error.networkResponse + "");
                // buttonStart.setEnabled(false);
            }
        });
        requestQueue.add(request1);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.equals(spinnerSource)) {
            switch (position) {
                case 0:
                    spinnerDestination.setSelection(1, true);
                    break;
                case 1:
                    spinnerDestination.setSelection(0, true);
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    spinnerSource.setSelection(1, true);
                    break;
                case 1:
                    spinnerSource.setSelection(0, true);
                    break;
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void toggleSpinner(View view) {

        int position = spinnerSource.getSelectedItemPosition();
        L.t(this, "toggle Spinner " + position);
        if (position == 0) {
            spinnerDestination.setSelection(0, true);
            spinnerSource.setSelection(1, true);
        } else {
            spinnerDestination.setSelection(1, true);
            spinnerSource.setSelection(0, true);
        }

    }

    public static class BusInfo {
        String busId;
        String source;
        String destination;
    }

}
