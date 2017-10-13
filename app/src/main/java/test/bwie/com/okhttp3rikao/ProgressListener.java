package test.bwie.com.okhttp3rikao;

/**
 * Created by 白玉春 on 2017/10/12.
 */

public interface ProgressListener {
    //已完成的 总的文件长度 是否完成
    void onProgress(long currentBytes, long contentLength, boolean done);
}
