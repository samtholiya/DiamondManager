package diamond.com.comp.sam.diamondmanager;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import diamond.com.comp.sam.diamondmanager.models.Orders;

/**
 * Created by shubh on 22-02-2017.
 */

public class App extends Application {
    public static final String HOST_ADDRESS = "http://35.154.113.0:1337";
    public static final String HOST_PATH = "/parse-server";

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Orders.class);
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .enableLocalDataStore()
                .applicationId("myApp")
                .clientKey("myApp")
                .server(HOST_ADDRESS + HOST_PATH + "/")
                .build());
    }
}
