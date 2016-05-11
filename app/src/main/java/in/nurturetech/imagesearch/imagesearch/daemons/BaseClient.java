package in.nurturetech.imagesearch.imagesearch.daemons;

import android.content.Context;

import in.nurturetech.imagesearch.imagesearch.Callback;
import in.nurturetech.imagesearch.imagesearch.NTError;

/**
 * Created by salil on 6/5/16.
 */
public class BaseClient {

    protected NetworkManager mNetworkManager = null;
    protected Context mContext;

    protected BaseClient(Context context) {
        mContext = context;
        mNetworkManager = NetworkManager.getInstance(context);
    }

    public void destroy() {
        mContext = null;
        mNetworkManager.destroy();
    }


    protected static <T> void sendResponse(Callback<T> callback, T t) {
        if (callback != null) {
            callback.onSuccess(t);
        }
    }

    protected static void sendError(Callback callback, NTError error) {
        if (callback != null) {
            callback.onError(error);
        }
    }
}
