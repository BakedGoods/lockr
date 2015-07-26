package com.bkdgds.lockr;

import android.app.Application;
import com.parse.Parse;

public class App extends Application {

	@Override
	public void onCreate() {
	    super.onCreate();
        Parse.initialize(this, "", "");

	}

}