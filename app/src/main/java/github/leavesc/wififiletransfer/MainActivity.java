package github.leavesc.wififiletransfer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @Author: leavesC
 * @Date: 2018/4/3 14:56
 * @Desc:
 * @Github：https://github.com/leavesC
 */
public class MainActivity extends BaseActivity {

    private static final int CODE_REQ_PERMISSIONS = 665;
    private Context mContext = null;
    private String TAG = "registerListener";
    private ThreadPoolExecutor threadPoolExecutor;
    private ab mab;
    private int flag = 0;
    private static boolean stop = true;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "采集完成开始上传!!!!!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "handleMessage ----------" + flag);
                    HashMap map = (HashMap) msg.obj;
                    String str =JSON.toJSONString(map);
                    Log.e(TAG, "map----------" + str);
                    sendmsg(2,str,1000);
                    break;
                case 2:
                    Log.e(TAG, "handleMessage 2 start---");
                    String info = (String) msg.obj;
                    if(!TextUtils.isEmpty(info)) {
                        load(info);
                    }else {
                        Toast.makeText(getApplicationContext(), "传感器数据为空!!!!!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    if(!stop) {
                        Log.e(TAG, "开始新的采集----------");
                        Toast.makeText(getApplicationContext(), "继续采集参数!!!!!", Toast.LENGTH_SHORT).show();
                        mab = ab.AFKeystoreWrapper(mContext, mHandler);
                        mab.clearList();
                        mab.start();
                    }else {
                        Toast.makeText(getApplicationContext(), "停止采集!!!!!", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    };
    private void sendmsg(int i,String str,int delay){
        Message message = new Message();
        message.what = i;
        message.obj = str;
        mHandler.sendMessageDelayed(message,delay);
    }
    private void uploadinfo(String info){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String val = "sensor=" + info;
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType,val);
        Request request = new Request.Builder()
                .url("http://kig.adszt.com/api/sensor/post")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String  ret = response.body().string();
            JSONObject resultdata = JSONObject.parseObject(ret);
            String code =  String.valueOf(resultdata.get("code"));
            Log.e(TAG,"code=====" + code);
            if(code.equals("0")){
                mHandler.sendEmptyMessage(3);
            }else{
                Toast.makeText(getApplicationContext(), "上传失败!!!!!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
    private void  load(String info){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                uploadinfo(info);
            }
        };
        threadPoolExecutor.execute(runnable);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        threadPoolExecutor = new ThreadPoolExecutor(3,5,1, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(25));
        mContext = this;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_REQ_PERMISSIONS) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    showToast("缺少权限，请先授予权限");
                    return;
                }
            }
            showToast("已获得权限");
        }
    }

    public void checkPermission(View view) {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CHANGE_NETWORK_STATE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION}, CODE_REQ_PERMISSIONS);
    }

    public void startFileSenderActivity(View view) {
        stop = false;
        Log.e(TAG, "startFileSenderActivity ----------");
        Toast.makeText(getApplicationContext(), "开始采集数据!!!!!", Toast.LENGTH_SHORT).show();
        mab =  ab.AFKeystoreWrapper(mContext,mHandler);
        mab.clearList();
        mab.start();
    }

    public void startFileReceiverActivity(View view) {
        stop = true;
    }

}