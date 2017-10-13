package test.bwie.com.okhttp3rikao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ProgressBar progressbar;
    Button button ;
    TextView textView;
    private ImageView imageView;
    private String url= "http://pic71.nipic.com/file/20150610/13549908_104823135000_2.jpg";
//    private String urls = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.billboard.billList&type=1&size=10&offset=0";
    private String urls ="http://api.expoon.com/AppNews/getNewsList/type/1/p/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressbar = (ProgressBar) findViewById(R.id.progressBar);
        button = (Button) findViewById(R.id.xiangce);
        button.setOnClickListener(this);
         imageView = (ImageView) findViewById(R.id.image);
         textView = (TextView) findViewById(R.id.tv);
//        OkhttpFeng.getOkhttpFeng().GetData(urls, MyData.class, new Icalect() {
//            @Override
//            public void Result(final Object response) {
////                MyData m = (MyData) response;
//                 runOnUiThread(new Runnable() {
//                     @Override
//                     public void run() {
//                        MyData m  = (MyData) response;
//                        List<MyData.DataBean> s =new ArrayList<MyData.DataBean>();
//                         s.addAll(m.getData());
//
//                         for(MyData.DataBean e : s){
//                             textView.setText(e.getNews_title());
//                         }
//                     }
//                 });
//            }
//
//            @Override
//            public void onError(Request request, Exception e) {
//
//            }
//        });
        Map<String,String> m = new HashMap<>();
        m.put("page","1");

//        OkhttpFeng.getOkhttpFeng().PostData(urls, m, MyData.class, new Icalect() {
//            @Override
//            public void Result(final Object response) {
//                MyData m = (MyData) response;
//                 runOnUiThread(new Runnable() {
//                     @Override
//                     public void run() {
//                        MyData m  = (MyData) response;
//                        List<MyData.DataBean> s =new ArrayList<MyData.DataBean>();
//                         s.addAll(m.getData());
//
//                         for(MyData.DataBean e : s){
//                             textView.setText(e.getNews_title());
//                         }
//                     }
//                 });
//            }
//
//            @Override
//            public void onError(Request request, Exception e) {
//
//            }
//        });




        Glide.with(MainActivity.this).load(url).into(imageView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.xiangce:

                OkhttpFeng.getOkhttpFeng().DrowLoad(url, "/sdcard/wangshu.jpg", new Icalect() {
                    @Override
                    public void Result(final Object response) {
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             Toast.makeText(MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                         }
                     });
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               Toast.makeText(MainActivity.this, "是啊比了", Toast.LENGTH_SHORT).show();
                           }
                       });
                    }
                }, new ProgressListener() {
                    @Override
                    public void onProgress(final long currentBytes, final long contentLength, boolean done) {
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               int progress = (int) (currentBytes * 100 / contentLength);
                               textView.setText(progress+"%");
                               progressbar.setProgress(progress);
                           }
                       });
                    }
                });

                break;
        }
    }
}

