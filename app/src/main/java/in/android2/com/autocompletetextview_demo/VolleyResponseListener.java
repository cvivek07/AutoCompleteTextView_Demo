package in.android2.com.autocompletetextview_demo;

import org.json.JSONException;
import org.json.JSONObject;


public interface VolleyResponseListener {
    void onResponse(JSONObject response) throws JSONException;

    void onError(String message, String title);
}
