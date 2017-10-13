package test.bwie.com.okhttp3rikao;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by 白玉春 on 2017/10/12.
 */

public class OkhttpFeng {

    private OkHttpClient mOkHttpClient;
     private  static volatile OkhttpFeng okhttpFeng;
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    private final Gson mgson;

    public static OkhttpFeng getOkhttpFeng(){
         if(okhttpFeng==null){
             synchronized (OkhttpFeng.class){
                 if(okhttpFeng==null){
                     okhttpFeng = new OkhttpFeng();
                 }
             }
         }
         return okhttpFeng;
     }

    public OkhttpFeng() {
        //初始化OkHttpClient
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();


        mgson = new Gson();
    }


    /**
     *  get
     * @param url
     * @param s
     * @param icalect
     */
    public void GetData(String url, final Class s , final Icalect icalect){
        final Request builder = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(builder).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                icalect.onError(builder,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    icalect.Result(mgson.fromJson(response.body().string(),s));
                }else{
                    icalect.Result("失败");
                }
            }
        });
    }

    /**
     *  post  同步
     * @param url
     * @param map
     * @param c
     * @param icalect
     */
    public void PostData(String url, Map<String,String> map, final Class c, final Icalect icalect){

        FormBody.Builder builder = new FormBody.Builder();
        Set<Map.Entry<String, String>> set= map.entrySet();
        for(Map.Entry<String,String> m:set){
            builder.add(m.getKey(),m.getValue());
        }

        final Request request = new Request.Builder().url(url).post(builder.build()).build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                 icalect.onError(request,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                 if(response.isSuccessful()){
                     icalect.Result(mgson.fromJson(response.body().string(),c));
                 }else{
                     icalect.Result("请求失败");
                 }
            }
        });

    }


    /**
     *   post  文件 上传文件
     */

    public void Shangchuan(String bengdiurl,String url , final Icalect icalect){
        File file = new File(bengdiurl);
        final Request request = new Request.Builder().url(url).post(RequestBody.create(MEDIA_TYPE_MARKDOWN,file)).build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                icalect.onError(request,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                icalect.Result(response.body().string());
            }
        });
    }


    /**
     *   刷新
     * @param url
     * @param xiazai
     * @param icalect
     */
    public void DrowLoad(String url, final String xiazai, final Icalect icalect, final ProgressListener listener){
        final Request request  = new Request.Builder().url(url).build();
        mOkHttpClient.newBuilder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response re = chain.proceed(chain.request());

                return re.newBuilder().body(new ProgressResponseBody(re.body(),listener)).build();
            }
        });
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                icalect.onError(request,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                InputStream inputStream = response.body().byteStream();

                FileOutputStream  fileOutputStream = new FileOutputStream(new File(xiazai));
                byte[] buffer =new byte[2048];
                int len = 0;
                while((len  = inputStream.read(buffer))!=-1){
                    fileOutputStream.write(buffer,0,len);
                }
                fileOutputStream.flush();

                fileOutputStream.close();

                icalect.Result(response.body().string());

                Log.d("wangshu", "文件下载成功");
            }
        });
    }
}
