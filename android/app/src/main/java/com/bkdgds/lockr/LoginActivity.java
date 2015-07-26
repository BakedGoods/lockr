package com.bkdgds.lockr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.HashMap;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if(toolbar != null)
            setSupportActionBar(toolbar);
        setContentView(R.layout.activity_login);
        Button btn =(Button)findViewById(R.id.login);
        btn.setOnClickListener(loginListener);

        Button btn2 = (Button)findViewById(R.id.buttonText);
        btn2.setOnClickListener(registerTextListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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

    //user clicked login button
    public void loginClicked(View view){
        TextView nameField = (TextView) findViewById(R.id.username);
        TextView passField = (TextView) findViewById(R.id.password);

        String email = nameField.getText().toString();
        final String password = passField.getText().toString();

        final Intent intent = new Intent(this, MainActivity.class);

        ParseUser.logInInBackground(email, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
            if (user != null) {
                // Hooray! The user is logged in.
                startActivity(intent);

            } else {
                // Signup failed. Look at the ParseException to see what happened.
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    public void setupPi(){
        Intent intent = new Intent(this, PiSetupActivity.class);
        startActivity(intent);
    }

    //register button clicked
    public void registerClicked(View view) {
        TextView nameField = (TextView) findViewById(R.id.username);
        TextView passField = (TextView) findViewById(R.id.password);

        String email = nameField.getText().toString();
        final String password = passField.getText().toString();

        final Intent intent = new Intent(this, PiSetupActivity.class);
        ParseUser user = new ParseUser();

        user.setUsername(email);
        user.setPassword(password);
        user.setEmail(email);
        user.put("currentState", false);
        user.put("lightSensor", false);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
            if (e == null) {
                // Hooray! Let them use the app now.
                setupPi();
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    final View.OnClickListener registerTextListener = new View.OnClickListener() {
        public void onClick(final View v) {
            Button btn =(Button)findViewById(R.id.login);
            btn.setText("Register");
            btn.setOnClickListener(registerListener);
            Button button = (Button)v;
            button.setOnClickListener(loginTextListener);
            button.setText("Or login here");
        }
    };

    final View.OnClickListener loginTextListener = new View.OnClickListener() {
        public void onClick(final View v) {
            Button btn =(Button)findViewById(R.id.login);
            btn.setText("Login");
            btn.setOnClickListener(loginListener);
            Button button = (Button)v;
            button.setOnClickListener(registerTextListener);
            button.setText("Need an account? Register here");
        }
    };

    final View.OnClickListener loginListener = new View.OnClickListener() {
        public void onClick(final View v) {
            loginClicked(v);
        }
    };

    final View.OnClickListener registerListener = new View.OnClickListener() {
        public void onClick(final View v) {
            registerClicked(v);
        }
    };

}
