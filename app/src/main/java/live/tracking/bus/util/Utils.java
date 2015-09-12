package live.tracking.bus.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

/**
 * Created by Windows on 05-02-2015.
 */
public class Utils {
    public static boolean isWifiNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo;
        boolean isNetworkConnected = false;
        wifiNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo != null) {
            if (wifiNetworkInfo.isConnected()) {
                // Toast.makeText(context, "Wifi connected", Toast.LENGTH_SHORT)
                // .show();
                // Network WiFi is available, perform the appropriate actions
                // needed
                isNetworkConnected = true;
            } else {
                // Toast.makeText(context, "Wifi not connected",
                // Toast.LENGTH_SHORT).show();
                // Network WiFi is not available, perform appropriate actions
                isNetworkConnected = false;
            }
        }

        return isNetworkConnected;
    }

    public static boolean isMobileNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo;
        boolean isNetworkConnected = false;
        mobileNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (mobileNetworkInfo != null && activeNetworkInfo != null) {
            if (mobileNetworkInfo.isConnected()
                    && activeNetworkInfo.isConnectedOrConnecting()) {
                // Network Mobile is available, perform the appropriate actions
                // needed
                // Toast.makeText(context, "Mobile connected",
                // Toast.LENGTH_SHORT)
                // .show();
                isNetworkConnected = true;
            } else {
                // Network Mobile is not available, perform appropriate actions
                // Toast.makeText(context, "Mobile not connected",
                // Toast.LENGTH_SHORT).show();
                isNetworkConnected = false;
            }
        }
        return isNetworkConnected;
    }

    public static boolean isNetworkAvailable(Context context) {
        return isMobileNetworkAvailable(context)
                || isWifiNetworkAvailable(context);
    }
    public static boolean isFirstTime(Context context, String key){
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        boolean firstTime=sharedPreferences.getBoolean(key, true);
        return firstTime;
    }

    public static void setFirstTime(Context context, String key){
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(key, false).apply();
    }
//extra
   /* public static boolean backToMain(Context context, String key){
        SharedPreferences sharedPreferences1=PreferenceManager.getDefaultSharedPreferences(context);
        boolean firstTime1=sharedPreferences1.getBoolean(key, true);
        return firstTime1;
    }
    public static void setBackToMain(Context context, String key){
        SharedPreferences sharedPreferences1=PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences1.edit().putBoolean(key, false).apply();
      //  boolean firstTime2=sharedPreferences1.getBoolean(key,"");
    }

   public static void setBackToMainT(Context context, String key){
        SharedPreferences sharedPreferences1=PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences1.edit().putBoolean(key, true).apply();
    }*/
    //ends


}
