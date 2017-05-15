package com.tecnologiabasica.jettyapiclient;

import com.tecnologiabasica.jettyapicommons.JAppCommons;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * @author afonso
 */
public class JApiClientSetup {

    public static Retrofit getApiClient() {
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request original = chain.request();

                        // Request customization: add request headers
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", JAppCommons.API_TOKEN)
                                .header("Accept", "application/json,application/octet-stream")
                                .method(original.method(), original.body());

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .build();

        Retrofit client = new Retrofit.Builder()
                .baseUrl(JAppCommons.URL_API)
                .client(okClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return client;
    }

}
