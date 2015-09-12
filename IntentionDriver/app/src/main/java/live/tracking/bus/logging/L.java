package live.tracking.bus.logging;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class L {

    public static final String TAG="BUS";
    public static void m(String message)
    {
        Log.d(TAG,message+"");
    }
	public static void t(Context context, String message)
	{
		Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
	}
}
