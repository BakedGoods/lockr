package com.bkdgds.lockr;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.venmo.android.pin.PinFragment;
import com.venmo.android.pin.PinFragmentConfiguration;
import com.venmo.android.pin.TryDepletionListener;
import com.venmo.android.pin.util.PinHelper;

import java.util.HashMap;

public class MainActivity extends ActionBarActivity implements LockListFragment.OnFragmentInteractionListener, LockDetailsFragment.OnFragmentInteractionListener, PinFragment.Listener, TryDepletionListener{

    public Fragment toShow;
    LockDetailsFragment details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void onValidated(){
        FragmentManager fragmentManager = getFragmentManager();
        FrameLayout layout = (FrameLayout)findViewById(R.id.fragContainer);
        layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        fragmentManager.beginTransaction()
                .remove(toShow)
                .commit();
    }
    public void onPinCreated(){
        FragmentManager fragmentManager = getFragmentManager();
        FrameLayout layout = (FrameLayout)findViewById(R.id.fragContainer);
        layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        fragmentManager.beginTransaction()
                .remove(toShow)
                .commit();
    }

    public void onTriesDepleted(){
        ParseUser.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        PinHelper.resetDefaultSavedPin(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        PinFragmentConfiguration config = (new PinFragmentConfiguration(this)).maxTries(5, this);
        toShow = PinHelper.hasDefaultPinSaved(this) ?
                PinFragment.newInstanceForVerification(config) :
                PinFragment.newInstanceForCreation(config);
        FrameLayout layout = (FrameLayout)findViewById(R.id.fragContainer);
        layout.setBackgroundColor(getResources().getColor(R.color.lightergray));
        getFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, toShow)
                .commit();
    }

    public void controlLock(final ParseObject lock, final View view){
        //call cloud code
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("ip", lock.getString("ip"));
        params.put("state", lock.getString("state"));
        ParseCloud.callFunctionInBackground("toggleLock", params, new FunctionCallback<Object>() {
            public void done(Object ratings, ParseException e) {
                if (e == null) {
                    toggleIcon(lock.getString("state"), view, lock);
                } else {
                    Log.d("Error", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void lockClick(View view){
        ParseObject lock = (ParseObject)view.getTag();
        controlLock(lock, view);
    }

    public void detailsLockClick(View view){
        controlLock(details.lock, view);
    }

    public void deleteAccess(View view){
        ParseObject access = (ParseObject)view.getTag();
        access.deleteInBackground();
        Toast.makeText(this, "Access revoked", Toast.LENGTH_SHORT).show();
    }

    public void toggleIcon(String state, View view, ParseObject lock){
        ImageButton lockBtn = (ImageButton) view;
        if(state.equals("open")){
            lockBtn.setImageResource(R.drawable.ic_lock_outline_black_24dp);
            lock.put("state", "close");
        }
        else{
            lockBtn.setImageResource(R.drawable.ic_lock_open_black_24dp);
            lock.put("state","open");
        }
        lock.saveInBackground();
    }

    public void showDetails(View view){
        ParseObject lock = (ParseObject) view.getTag();
        details = LockDetailsFragment.newInstance(lock);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragContainer, details);
        ft.commit();
    }

    public void shareKey(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Share keys");
        alert.setMessage("Share access to your lock to another user");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setHint("Email Address");
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String email = input.getText().toString().toLowerCase();
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("email", email);
                query.getFirstInBackground(new GetCallback<ParseUser>() {
                    public void done(ParseUser user, ParseException e) {
                        if (e == null) {
                            ParseObject access = new ParseObject("Access");
                            access.put("user", user);
                            access.put("lock", details.lock);
                            access.saveInBackground();
                            Toast.makeText(MainActivity.this, "Access granted", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("score", "Error: " + e.getMessage());
                        }
                    }
                });
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    public void closeFrag(View view){
        getFragmentManager().beginTransaction().remove(details).commit();
    }

    public void onFragmentInteraction(Uri uri){}

    public void onFragmentInteraction(String id){}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            ParseUser.logOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
