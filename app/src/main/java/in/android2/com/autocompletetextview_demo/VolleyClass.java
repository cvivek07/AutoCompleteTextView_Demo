package in.android2.com.autocompletetextview_demo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class VolleyClass {
    private Context act;
    private String TAG = "";
    String networkErrorMessage = "Network error – please try again.";
    String poorNetwork = "Your data connection is too slow – please try again when you have a better network connection";
    String timeout = "Response timed out – please try again.";
    String authorizationFailed = "Authorization failed – please try again.";
    String serverNotResponding = "Server not responding – please try again.";
    String parseError = "JSON data not received from server – please try again.";
    String networkErrorTitle = "Network error";
    String poorNetworkTitle = "Poor Network Connection";
    String timeoutTitle = "Network Error";
    String authorizationFailedTitle = "Network Error";
    String serverNotRespondingTitle = "Server Error";
    String parseErrorTitle = "Network Error";
    RequestQueue queue; // declare a variable for request queue

    public VolleyClass(Context context, String TAG) {
        this.act = context;
        queue = Volley.newRequestQueue(context); //instantiate the request queue
        this.TAG = TAG + " WebService";
    }

    public void volleyPostData(final String url, JSONObject jsonObject, final VolleyResponseListener listener) {

        //SHOWING PROGRESS DIALOG
        final ProgressDialog pDialog = new ProgressDialog(act);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        Log.e(TAG, "volleyPostData request url - " + url);
        if (jsonObject != null) {
            Log.e(TAG, "volleyPostData request data - " + jsonObject.toString());
        }
        //CHECKING IF APP IS ONLINE
        if (isOnline()) {
            try {
                pDialog.show();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            //CREATING A JSONObject Request
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, "volleyPostData response - " + response.toString());
                            try {
                                listener.onResponse(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                pDialog.dismiss();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        pDialog.dismiss();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                    if (error instanceof TimeoutError) {
                        listener.onError(timeout, timeoutTitle);
                    } else if (error instanceof NoConnectionError) {
                        listener.onError(poorNetwork, poorNetworkTitle);
                    } else if (error instanceof AuthFailureError) {
                        listener.onError(authorizationFailed, authorizationFailedTitle);
                    } else if (error instanceof ServerError) {
                        listener.onError(serverNotResponding, serverNotRespondingTitle);
                    } else if (error instanceof NetworkError) {
                        listener.onError(networkErrorMessage, networkErrorTitle);
                    } else if (error instanceof ParseError) {
                        listener.onError(parseError, parseErrorTitle);
                    }
                }
            });
            int MY_SOCKET_TIMEOUT_MS = 30000;
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)); // setting default retry policy.
            queue.add(jsonObjReq); // adding the request object to request queue.
        } else {
            Log.e(TAG, "volleyPostData response - No Internet");
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    act);
            alertDialogBuilder.setTitle("Money Quotient")
                    .setIcon(android.R.drawable.stat_notify_error);
            alertDialogBuilder
                    .setMessage("No Internet Connection")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            listener.onError(networkErrorMessage, networkErrorMessage);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
