package live.tracking.bus.intentiondriver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity {

        EditText busno,busid;
        Spinner source;
        String data[]=new String[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        busid=(EditText)findViewById(R.id.driverId);
        busno=(EditText)findViewById(R.id.busNumber);
        source = (Spinner) findViewById(R.id.busSource);
       /* ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
        s.setAdapter(adapter);*/
        busid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    Toast.makeText(getApplicationContext(),"focus changed",Toast.LENGTH_SHORT).show();
                    loaddata();
                }
            }
        });
    }

    private void loaddata() {
        String id=busid.getText().toString();
        Getdata g=new Getdata(id);
        g.execute();
        try {
            data=g.get();
            Toast.makeText(this,"info is"+data[0]+data[1]+data[2],Toast.LENGTH_SHORT).show();
            busno.setText(data[0]);
            String d[]={"select the source",data[1],data[2]};
            ArrayAdapter adapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_item,d);
            source.setAdapter(adapter);




        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


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
    public void startTransmitting(View view){
        Intent intent=new Intent(this, LiveActivity.class);
        startActivity(intent);
    }
}
