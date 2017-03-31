package in.android2.com.autocompletetextview_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView mAutoCompleteTextView;
    //ARRAYLIST FOR HOLDING CUSTOM OBJECTS
    private ArrayList<MutualFund_CustomClass> mSuggestionArrayList;
    //REFERENCE OF OUR CUSTOM CLASS
    private MutualFund_CustomClass mutualFundHouseObj;
    //CUSTOM ADAPTER
    private MutualFund_CustomAdapter mMutualFundAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autocompletetextview);
        //ARRAYLIST WHICH WILL HOLD THE MUTUAL FUND DATA i.e MutualFund_CustomClass objects.
        mSuggestionArrayList = new ArrayList<MutualFund_CustomClass>();
        //CALLING WEBSERVICE FOR LOADING SUGGESTIONS FROM WEBSERVICE
        loadSuggestions(mSuggestionArrayList);
    }

    private void loadSuggestions(final ArrayList<MutualFund_CustomClass> mSuggestionArrayList) {
        //REPLACE YOUR WEBSERICE URL AND JSONOBJECT
        new VolleyClass(MainActivity.this, "MainActivity").volleyPostData("<YOUR_WEBSERVICE_URL>", /*"<YOUR_JSON_OBJECT>"*/, new VolleyResponseListener() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                    mutualFundHouseObj = new MutualFund_CustomClass();
                    mutualFundHouseObj.setFundId(response.getJSONArray("data").optJSONObject(i).optString("fundid"));
                    mutualFundHouseObj.setFundName(response.getJSONArray("data").optJSONObject(i).optString("fundname"));
                    mSuggestionArrayList.add(mutualFundHouseObj);
                }
                //INSTANTIATING CUSTOM ADAPTER
                mMutualFundAdapter = new MutualFund_CustomAdapter(MainActivity.this, mSuggestionArrayList);
                mAutoCompleteTextView.setThreshold(1);//will start working from first character
                mAutoCompleteTextView.setAdapter(mMutualFundAdapter);//setting the adapter data into the AutoCompleteTextView

            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }
}
