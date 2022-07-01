package com.niazitvpro.official.utils;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitUtils {

    private static final String CACHE_CONTROL = "Cache-Control";
    private static Interceptor REWRITE_RESPONSE_INTERCEPTOR_OFFLINE;
    private Context context;
    private static Cache cache;


    public static ApiInterface callAPi(Context context) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().
                addInterceptor(interceptor).
                connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES).build();


//
//        OkHttpClient client = new OkHttpClient.Builder().
//                addInterceptor(interceptor).
//                connectTimeout(3, TimeUnit.MINUTES)
//                .readTimeout(3, TimeUnit.MINUTES)
//                .writeTimeout(3, TimeUnit.MINUTES).build();

//        int cacheSize = 10 * 1024 * 1024; // 10 MB
//
//        if(context.getCacheDir()!=null){
//
//             cache = new Cache(context.getCacheDir(), cacheSize);
//
//        }else {
//
//
//        }
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(offlineInterceptor)
//                .addNetworkInterceptor(onlineInterceptor)
//                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//                .cache(cache)
//                .connectTimeout(3, TimeUnit.MINUTES)
//                .readTimeout(3, TimeUnit.MINUTES)
//                .writeTimeout(3, TimeUnit.MINUTES)
//                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(ApiInterface.class);


    }

    public static ApiInterface callYoutubeAPi(String url) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().
                addInterceptor(interceptor).
                connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES).build();


//
//        OkHttpClient client = new OkHttpClient.Builder().
//                addInterceptor(interceptor).
//                connectTimeout(3, TimeUnit.MINUTES)
//                .readTimeout(3, TimeUnit.MINUTES)
//                .writeTimeout(3, TimeUnit.MINUTES).build();

//        int cacheSize = 10 * 1024 * 1024; // 10 MB
//
//        if(context.getCacheDir()!=null){
//
//             cache = new Cache(context.getCacheDir(), cacheSize);
//
//        }else {
//
//
//        }
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(offlineInterceptor)
//                .addNetworkInterceptor(onlineInterceptor)
//                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//                .cache(cache)
//                .connectTimeout(3, TimeUnit.MINUTES)
//                .readTimeout(3, TimeUnit.MINUTES)
//                .writeTimeout(3, TimeUnit.MINUTES)
//                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(ApiInterface.class);


    }


//    public static Interceptor onlineInterceptor = new Interceptor() {
//        @Override
//        public okhttp3.Response intercept(Chain chain) throws IOException {
//            okhttp3.Response response = chain.proceed(chain.request());
//            int maxAge = 60; // read from cache for 60 seconds even if there is internet connection
//            return response.newBuilder()
//                    .header("Cache-Control", "public, max-age=" + maxAge)
//                    .removeHeader("Pragma")
//                    .build();
//        }
//    };
//
//   public static Interceptor offlineInterceptor= new Interceptor() {
//        @Override
//        public okhttp3.Response intercept(Chain chain) throws IOException {
//            Request request = chain.request();
//            if (!isNetworkAvailable()) {
//                int maxStale = 60 * 60 * 24 * 30; // Offline cache available for 30 days
//                request = request.newBuilder()
//                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                        .removeHeader("Pragma")
//                        .build();
//            }
//            return chain.proceed(request);
//        }
//    };

}
