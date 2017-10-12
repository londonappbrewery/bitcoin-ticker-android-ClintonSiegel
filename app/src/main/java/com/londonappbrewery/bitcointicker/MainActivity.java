package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    // Constants:
    String BTC_LIT = "BTC";
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/";

    // Member Variables:
    TextView mPriceTextView;
    String mCurrencyPair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Bitcoin", "" + parent.getItemAtPosition(position));
                mCurrencyPair = BTC_LIT + parent.getItemAtPosition(position);
                Log.d("Bitcoin", "Currency Pair " + mCurrencyPair);

              //RequestParams params = new RequestParams();
                //params.put("market", "global");
                //params.put("symbol", mCurrencyPair);
                letsDoSomeNetWorking();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Bitcoin", "Nothing selected");

            }
        });
    }

    // Update the UI
    private void updateUI(QuoteDataModel quote) {
       mPriceTextView.setText(quote.getLast());
        Log.d("Bitcoin", "mLast Text view: " + quote.getLast());
    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetWorking() {
    AsyncHttpClient client = new AsyncHttpClient();
      // client.get(BASE_URL, params, new JsonHttpResponseHandler() {
      client.get(BASE_URL + mCurrencyPair, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                Log.d("bitcoin", "JSON: " + response.toString());
               QuoteDataModel quoteData = QuoteDataModel.fromJson(response);
               updateUI(quoteData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("bitcoin", "Request fail! Status code: " + statusCode);
                Log.d("bitcoin", "Fail response: " + response);
                Log.e("ERROR", e.toString());
            }
        });
    }


    public static class QuoteDataModel {
    private String mLast;
    public String getLast() {
            return mLast;
        }
// TODO: Create a QuoteDataModel from a JSON:
     public static QuoteDataModel fromJson(JSONObject jsonObject) {
        try {
             QuoteDataModel quoteData = new QuoteDataModel();
             quoteData.mLast = jsonObject.getString("last");
             Log.d("Bitcoin", "mLast Text view: " + quoteData.mLast);
             return quoteData;
            } catch (JSONException e) {
                e.printStackTrace();
                return null; }
       }
    }
   }
