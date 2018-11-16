package com.novip;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Http {

    private static OkHttpClient client;

    private static String host;
    private static int port;
    private Http(){ }

    private static synchronized OkHttpClient getClient(){
        if(client == null){
            client =  new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
        }
        return  client;
    }

    public static void login(String phone,String password,Callback cb){
        HttpUrl url = new HttpUrl.Builder().host(host).scheme("http").port(port)
                .addPathSegment("novip")
                .addPathSegment("user")
                .addPathSegment("login")
                .addEncodedQueryParameter("phone",phone)
                .addEncodedQueryParameter("password",password).build();
        Request request = new Request.Builder().url(url).build();
        getClient().newCall(request).enqueue(cb);
    }

    public static void registe(String phone,String password,String deviceId,Callback cb){
        HttpUrl url = new HttpUrl.Builder().host(host).scheme("http").port(port)
                .addPathSegment("novip")
                .addPathSegment("user")
                .addPathSegment("registe")
                .addEncodedQueryParameter("phone",phone)
                .addEncodedQueryParameter("password",password)
                .addEncodedQueryParameter("device_id",deviceId).build();
        Request request = new Request.Builder().url(url).build();
        getClient().newCall(request).enqueue(cb);
    }

    public static void getVParser(Callback cb){
        HttpUrl url = new HttpUrl.Builder().host(host).scheme("http").port(port)
                .addPathSegment("novip")
                .addPathSegment("video")
                .addPathSegment("get_v_parser")
                .build();
        Request request = new Request.Builder().url(url).build();
        getClient().newCall(request).enqueue(cb);
    }

    public static void checkUpdate(Callback cb){
        HttpUrl url = new HttpUrl.Builder().host(host).scheme("http").port(port)
                .addPathSegment("novip")
                .addPathSegment("update")
                .addPathSegment("check")
                .build();
        Request request = new Request.Builder().url(url).build();
        getClient().newCall(request).enqueue(cb);
    }


    public static void checkCode(String phone,String  code,Callback cb){
        HttpUrl url = new HttpUrl.Builder().host(host).scheme("http").port(port)
                .addPathSegment("novip")
                .addPathSegment("vip")
                .addPathSegment("check_code")
                .addEncodedQueryParameter("phone", phone)
                .addEncodedQueryParameter("code", code)
                .build();
        Request request = new Request.Builder().url(url).build();
        getClient().newCall(request).enqueue(cb);
    }

    public static Response getNovip() throws IOException{
        Request request = new Request.Builder().url("http://novip.oss-cn-shenzhen.aliyuncs.com/novip.json").build();
        return getClient().newCall(request).execute();
    }


    public static void getPlatforms(Callback cb){
        HttpUrl url = new HttpUrl.Builder().host(host).scheme("http").port(port)
                .addPathSegment("novip")
                .addPathSegment("video")
                .addPathSegment("get_platform")
                .build();
        Request request = new Request.Builder().url(url).build();
        getClient().newCall(request).enqueue(cb);
    }


    public static void getMainTabAD(Callback cb){
        HttpUrl url = new HttpUrl.Builder().host(host).scheme("http").port(port)
                .addPathSegment("novip")
                .addPathSegment("ad")
                .addPathSegment("main_tab_ad")
                .build();
        Request request = new Request.Builder().url(url).build();
        getClient().newCall(request).enqueue(cb);
    }

    public static void changePassword(String phone,String password,String new_password,Callback cb){
        HttpUrl url = new HttpUrl.Builder().host(host).scheme("http").port(port)
                .addPathSegment("novip")
                .addPathSegment("user")
                .addPathSegment("change_password")
                .addEncodedQueryParameter("phone",phone)
                .addEncodedQueryParameter("password",password)
                .addEncodedQueryParameter("new_password",new_password)
                .build();
        Request request = new Request.Builder().url(url).build();
        getClient().newCall(request).enqueue(cb);
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        Http.host = host;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        Http.port = port;
    }
}
