package test.bwie.com.okhttp3rikao;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;

/**
 * Created by 白玉春 on 2017/10/12.
 */

public interface  Icalect<T> {



    public   void Result(T response);
    public  void onError(Request request, Exception e);

}
