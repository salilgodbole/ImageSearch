package in.nurturetech.imagesearch.imagesearch.daemons;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.List;

import in.nurturetech.imagesearch.imagesearch.Callback;
import in.nurturetech.imagesearch.imagesearch.Constants;
import in.nurturetech.imagesearch.imagesearch.NTError;
import in.nurturetech.imagesearch.imagesearch.entities.ImageModel;

/**
 * Created by salil on 6/5/16.
 */
public class ImageClient extends BaseClient {
    private static ImageClient mClient = null;

    private ImageClient(Context context) {
        super(context);
    }

    public static ImageClient getClient(Context context) {
        if (mClient == null) {
            synchronized (ImageClient.class) {
                if (mClient == null) {
                    mClient = new ImageClient(context);
                }
            }
        }

        return mClient;
    }


    @Override
    public void destroy() {
        super.destroy();
        mClient = null;
        mNetworkManager.destroy();
    }

    /**
     * Get map of all the products against the category.
     *
     * @param keyword
     * @param callback
     */
    public void getImages(String keyword, final Callback<List<ImageModel>> callback) {
        String url = Constants.IMAGE_FETCH_URL + "&gpssearch=" + keyword;
        JsonObjectRequest request = new JsonObjectRequest(url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject object) {
                        List<ImageModel> productList = ImageModel.parseResponse(object);
                        if (productList != null) {
                            sendResponse(callback, productList);
                        } else {
                            sendError(callback, new NTError("No Images found this keyword"));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendError(callback, new NTError(error));
            }
        });

        mNetworkManager.addToRequestQueue(request);
    }

    public void cancelPendingRequests() {
        mNetworkManager.cancelPendingRequests();
    }
}