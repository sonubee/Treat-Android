package gllc.tech.dateapp;



import android.util.Log;

import gllc.tech.dateapp.Loading.MyApplication;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import okhttp3.Callback;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

/**
 * Created by bhangoo on 1/20/2017.
 */

public class YelpService {

    public static void findRestaurants(HttpUrl.Builder urlBuilder, Callback callback) {
        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer("9NoUvgcQUJtc5YQr1ZprkA", "GUx2lLxlGQ1IYyf0gO5hldiZvx8");
        consumer.setTokenWithSecret("oBw736I8_FYg-Oc1qSEXHCOb0S6guA34", "YQ0B1OrIXxDksLtndsMPNiXhsWY");

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new SigningInterceptor(consumer))
                .build();

        String url = urlBuilder.build().toString();

        Request request= new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

}
