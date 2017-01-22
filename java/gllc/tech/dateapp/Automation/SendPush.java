package gllc.tech.dateapp.Automation;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class SendPush {

    AsyncHttpClient client;

    public SendPush(String message, String receiver, String title) {

        client = new AsyncHttpClient();

        Log.i("--All", "In SendPush");

        RequestParams params = new RequestParams();

        params.put("to", receiver);
        params.put("title",title);
        params.put("message", message);

        client.post("https://calm-oasis-32938.herokuapp.com//sendPush", params,
                new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("--All", "In Failure");
                        Log.i("--All", "Failure response: " + responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("--All", "Success Sending Push Notifications");
                        Log.i("--All", "Result: " + responseString);
                    }
                });

    }



}
