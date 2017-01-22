package gllc.tech.dateapp.Automation;



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

    public static void suggestions(String activity, Callback callback) {
        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer("9NoUvgcQUJtc5YQr1ZprkA", "GUx2lLxlGQ1IYyf0gO5hldiZvx8");
        consumer.setTokenWithSecret("oBw736I8_FYg-Oc1qSEXHCOb0S6guA34", "YQ0B1OrIXxDksLtndsMPNiXhsWY");

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new SigningInterceptor(consumer))
                .build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.yelp.com/v2/search?").newBuilder();
        urlBuilder.addQueryParameter("ll", Double.toString(+MyApplication.currentUser.getLatitude())+","+Double.toString(MyApplication.currentUser.getLongitude()));
        //urlBuilder.addQueryParameter("limit", "2");

        String categoryFilter = "";
        if (activity.equals("MiniGolf")) {categoryFilter = "mini_golf";}
        if (activity.equals("Dinner")) {categoryFilter = "restaurants";}

        urlBuilder.addQueryParameter("category_filter", categoryFilter);

        String url = urlBuilder.build().toString();
        Log.i("--All", "Url: " + url);

        Request request= new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

}
