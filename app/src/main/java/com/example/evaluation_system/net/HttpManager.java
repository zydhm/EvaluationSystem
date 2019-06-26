package com.example.evaluation_system.net;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {

    private static final String TAG = "HttpManager";



    private HttpLoggingInterceptor httpLoggingInterceptor;

    //保存用户的cookies
    private static HashSet<String> cookies;

    private HttpManager() {
        httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e(TAG, "log: " + message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        cookies = new HashSet<>();
    }

    ;

    /**
     * 单例模式得到HttpManager实例
     *
     * @return
     */
    private static volatile HttpManager mHttpManager;
    public static HttpManager getInstance() {
        if (mHttpManager == null) {
            synchronized (HttpManager.class) {
                if (mHttpManager == null) {
                    mHttpManager = new HttpManager();
                }
            }
        }
        return mHttpManager;
    }

    /**
     * 传入一个baseUrl得到Api的一个实例，另外设置网络链接超时时间和读取超时时间等
     *
     * @param baseUrl
     * @return
     */
    public Api getApiService(final String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new ResponseInterceptor())
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new ReceivedCookies());
                //.addInterceptor(new AddCookiesInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        return api;
    }

    /**
     * 拦截器，用户查询网络请求返回的原始数据
     */
    static class ResponseInterceptor implements Interceptor {

        private static final Charset UTF8 = Charset.forName("UTF-8");

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request oldRequest = chain.request();
            Response originResponse = chain.proceed(oldRequest);
            if (originResponse.isSuccessful()) {
                ResponseBody responseBody = originResponse.body();
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.buffer();
                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                String responseBodyStr = buffer.clone().readString(charset);
                //Log.e(TAG, "intercept: " + responseBodyStr);
            }
            return originResponse;
        }
    }

    static class ReceivedCookies implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Log.d(TAG, "intercept: before ReceivedCookies");
            Response originResponse = chain.proceed(chain.request());
            Log.d(TAG, "intercept: after ReceivedCookies"+originResponse);
            if (!originResponse.headers("Set-Cookie").isEmpty()) {
                for (String header : originResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                }
            }
            return originResponse;
        }

    }

    static class AddCookiesInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            if (cookies != null) {
                for (String cookie : cookies) {
                    builder.addHeader("Cookie", cookie);
                    Log.e(TAG, "intercept: cookie = " + cookie);
                }
            }
            return chain.proceed(builder.build());
        }
    }
}

