package com.dev.e_auctions.Client;

import android.util.Log;

import com.dev.e_auctions.Utilities.Utils;

import java.io.File;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UploadImageClient {


    private static OkHttpClient client = getUnsafeOkHttpClientUploadImage();


    public static void uploadImage(String token, String auctionId, File file) {

        Log.d("Auction ", "before the body builder ");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .addFormDataPart("auctionId", auctionId)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse ("image/jpeg"), file))
                .build();

        System.out.println(Utils.gson.toJson(requestBody));

        Log.d("Auction ", "Before the request builder");
        Request request = new Request.Builder()
                .url("https://83.212.109.213:8080/image/upload")
                .post(requestBody)
                .build();

        try  {
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                Log.d("TEST", response.toString());
            }

            System.out.println(response.body().string());
        } catch (Exception e) {
            Log.d("Auction ", "Not successful upload");
            e.printStackTrace();
        }
    }

    private static OkHttpClient getUnsafeOkHttpClientUploadImage() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}