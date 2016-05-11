package in.nurturetech.imagesearch.imagesearch.daemons;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by salil on 6/5/16.
 */
public class NetworkManager {
    private static String TAG = NetworkManager.class.getSimpleName();
    private static NetworkManager instance = null;

    private RequestQueue mRequestQueue = null;
    private Context mContext = null;

    public static NetworkManager getInstance(Context context) {
        if (instance == null) {
            synchronized (NetworkManager.class) {
                if (instance == null) {
                    instance = new NetworkManager(context);
                }
            }
        }

        return instance;
    }

    private NetworkManager(Context context) {
        this.mContext = context;
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        mRequestQueue.add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        mRequestQueue.add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void cancelPendingRequests() {
        cancelPendingRequests(TAG);
    }

    public void destroy() {
        cancelPendingRequests();
        mRequestQueue = null;
        instance = null;
    }
}