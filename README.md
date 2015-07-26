# Lockr
![alt text](https://github.com/BakedGoods/lockr/raw/master/lockr-banner.png "Banner")
Lockr is a smart lock platform. You can control a connected lock from a mobile application and share digital "keys" to others to give them access.

### Features:
- Lock controlled via a REST API module
- Control via mobile application
- Share access via digital keys

Current server is built for a Raspberry Pi.

### Set up

Lockr uses Parse.com to store user data. Create a free account and a new application for your lock. In your application add a new class called *Lock*. Add a new column  called *ip* then add a new row with your device's ip in the appropriate column.

##### Raspberry Pi
Get your application keys from Parse and copy into the `parse_settings.py` file.
Run the `server.py` file and you should be able to control the lock locally via the API.

```
Open lock: /lock?state=open
Close lock: /lock?state=close
```
##### Mobile App
The Android app is compatible with API 14+. Add your application keys to the `App.java` file.

```
public class App extends Application {

	@Override
	public void onCreate() {
	    super.onCreate();
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
	}
}
```

Build and install the application. Follow the on screen details using the *objectId* of the *Lock* you created online in Parse as the *Lock Id*. 

##### You're done!

### Version
1.0.0

### Tech

Lockr uses the following open source software,
- [Python 2.7](http://www.python.org)
- [Twisted](https://twistedmatrix.com/trac/)
- [Ion](https://github.com/koush/ion)
- [Android-Pin](https://github.com/venmo/android-pin)
- [Floating Action Button](https://github.com/shamanland/floating-action-button)

### Development

Want to contribute? Take a look at the list of todos or anything else you think would be useful.

##### Todos

 - iOS
 - Web Application
 - Support for other hardware devices

License
```
Copyright 2015 Baked Goods

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```