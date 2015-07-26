package com.bkdgds.lockr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class PiSetupActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pi_setup);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pi_setup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //sets the device entered
    public void onSetClicked(View view){
        TextView textView = (TextView)findViewById(R.id.lockId);
        String id = textView.getText().toString();

        TextView textView2 = (TextView)findViewById(R.id.lockNameTxt);
        final String name = textView2.getText().toString();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Lock");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    ParseUser user = ParseUser.getCurrentUser();
                    object.put("owner", user);
                    object.saveInBackground();
                    object.put("name", name);

                    ParseObject access = new ParseObject("Access");
                    access.put("user", user);
                    access.put("lock", object);
                    access.saveInBackground();
                } else {
                    // something went wrong
                    Toast.makeText(PiSetupActivity.this, "Lock ID Incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
     }
}
