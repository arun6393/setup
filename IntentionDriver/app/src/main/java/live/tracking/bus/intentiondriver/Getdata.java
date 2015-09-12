package live.tracking.bus.intentiondriver;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by bunty on 2/1/2015.
 */
public class Getdata extends AsyncTask<Void, Void, String[]> {

    String bid;
    String output1=null;
    String no1[]=new String[3];
    public Getdata(String id)
    {
    this.bid=id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String[] doInBackground(Void... params) {
        String requesturl = "http://whereisthebus.in/busdata.php?id="
                +bid;
        HttpClient client = new DefaultHttpClient();

        HttpGet g = new HttpGet(requesturl);
        try {

            HttpResponse response = client.execute(g);
            HttpEntity entity = response.getEntity();

            if (!entity.equals("")) {

                output1 = EntityUtils.toString(entity);
                Log.i("output", output1);

                JSONObject json = new JSONObject(output1);
                // add the string name
                JSONArray items = json.getJSONArray("row");
                //no1 = new String[items.length()];
                Log.i("length", no1.length + "");
                // bus_no1=new String[items.length()*2];
                // int ji = 0;
                for (int i = 0; i < items.length(); i++) {
                    JSONObject no = items.getJSONObject(i);
                    no1[0] = no.getString("no");
                    no1[1] = no.getString("Source");
                    no1[2] = no.getString("dest");
                    Log.i("1", no1[0]);
                    // [ji++]=items.getJSONObject(i).getString("lat");
                    // latlng[ji++]=items.getJSONObject(i).getString("long");
                    // Log.i("value", latlng[ji-2].toString()
                    // +" "+latlng[ji-1].toString());
                }

            } else {
                Log.i("async", "10");
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.i("exit", "back");
        // return output1;
        //return no1;
        return no1;
    }

    @Override
    protected void onPostExecute(String [] result) {
        super.onPostExecute(result);


    }
}
