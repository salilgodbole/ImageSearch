package in.nurturetech.imagesearch.imagesearch;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by salil on 24/12/15.
 */
public class Utils {

    public static boolean isNetworkConnected(Context context) {

        boolean connected;

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        connected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return connected;
    }
}
