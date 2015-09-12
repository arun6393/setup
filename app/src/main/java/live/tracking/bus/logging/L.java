package live.tracking.bus.logging;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import live.tracking.bus.util.Constants;

public class L {

    public static void m(String message) {
        Log.d(Constants.TAG, message + "");
    }

    public static void t(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
