package com.tecnologiabasica.jettyapiclient;

import com.tecnologiabasica.jettyapicommons.JAppCommons;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
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

        TrustManagerFactory trustManagerFactory = null;
        try {
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(JApiClientSetup.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            trustManagerFactory.init(readKeyStore());
        } catch (KeyStoreException ex) {
            Logger.getLogger(JApiClientSetup.class.getName()).log(Level.SEVERE, null, ex);
        }
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(JApiClientSetup.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            sslContext.init(null, new TrustManager[]{trustManager}, null);
        } catch (KeyManagementException ex) {
            Logger.getLogger(JApiClientSetup.class.getName()).log(Level.SEVERE, null, ex);
        }
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient okClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManager)
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

    private static KeyStore readKeyStore() {
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException ex) {
            Logger.getLogger(JApiClientSetup.class.getName()).log(Level.SEVERE, null, ex);
        }

        // get user password and file input stream
        String pass = "123456";
        char[] password = pass.toCharArray();

        java.io.FileInputStream fis = null;
        try {
            try {
                String keyPath = "/home/afonso/Downloads/keystore.jks";
                File file = new File(keyPath);
                if (file.exists()) {
                    fis = new java.io.FileInputStream(keyPath);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(JApiClientSetup.class.getName()).log(Level.SEVERE, null, ex);
            }
            ks.load(fis, password);
        } catch (IOException ex) {
            Logger.getLogger(JApiClientSetup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(JApiClientSetup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(JApiClientSetup.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(JApiClientSetup.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return ks;
    }

}
