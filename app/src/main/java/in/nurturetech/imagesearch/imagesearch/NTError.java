package in.nurturetech.imagesearch.imagesearch;

import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by salil on 10/12/15.
 */
public class NTError {

    private final String message;

    public NTError(String message) {
        this.message = message;
    }

    public NTError(VolleyError error) {
        if (error instanceof TimeoutError) {
            this.message = "Request Timeout";
        } else if (error instanceof NoConnectionError) {
            this.message = "Internet Not Connected";
        } else if (error instanceof ServerError) {
            this.message = "We are facing some technical glitch at server.";
        } else {
            this.message = error.getMessage();
        }
    }

    public String getMessage() {
        return message;
    }
}
