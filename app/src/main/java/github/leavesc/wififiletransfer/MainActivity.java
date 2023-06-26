package github.leavesc.wififiletransfer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
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
    private  HashMap  map = new HashMap();

    private static  String  props[]={"ro.system.build.version.sdk","ro.build.fingerprint","ro.zygote","ro.product.cpu.abilist",
            "ro.boot.bootloader","dalvik.vm.isa.arm.features","dalvik.vm.isa.arm.variant", "ro.hardware"
            ,"ro.dalvik.vm.native.bridge","ro.bootloader","gsm.version.baseband","ro.hardware","dalvik.vm.isa.arm64.features","dalvik.vm.isa.arm64.variant"
    ,"ro.board.platform","ro.build.flavor"};

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
                        //load(info);
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
    public  String getCpuinfoProcessor() {
        String str = null;
        try {
            FileReader fileReader = new FileReader("/proc/cpuinfo");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String[] split = bufferedReader.readLine().split(":\\s+", 2);
            if (split.length > 1) {
                if(split[0].contains("Processor")) {
                    str = split[1];
                }
            }
            fileReader.close();
            bufferedReader.close();
            return str;
        } catch (Throwable th) {
            return str;
        }
    }
    public  String getCpuinfoHardware() {
        String str = null;
        String line = null;
        try {
            FileReader fileReader = new FileReader("/proc/cpuinfo");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                if(line.contains("Hardware")){
                    Log.e(TAG, "Hardware===" +line );
                    String[] split = line.split(":\\s+", 2);
                    if (split.length > 1) {
                        str = split[1];
                        map.put("Hardware",str);
                    }
                }
            }
            fileReader.close();
            bufferedReader.close();
            return str;
        } catch (Throwable th) {
            th.printStackTrace();
            return str;
        }
    }
    private static String SystemProperties(String p0,String p1){
        Class[] uClassArray;
        Object[] objArray;
        try{
            uClassArray = new Class[2];
            uClassArray[0] = String.class;
            uClassArray[1] = String.class;
            objArray = new Object[2];
            objArray[0] = p0;
            objArray[1] = p1;
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", uClassArray).invoke(null, objArray);
        }catch(java.lang.Exception e7){
            e7.printStackTrace();
            return p1;
        }
    }
    private void getCpufreq(){
        String[] list = {"cpuinfo_max_freq","cpuinfo_min_freq","scaling_cur_freq","scaling_min_freq"};
        for(int i =0;i < list.length;i++) {
            try {
                String line;
                BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/" + list[i]));
                if ((line = br.readLine()) != null) {
                    Log.e(TAG, "getCpufreq str__========== " + line);
                    map.put(list[i], line);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void getAudioInfo(){
        AudioManager audio = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int maxVoice = audio.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        map.put("maxVoice",maxVoice);
        int maxSystem = audio.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        map.put("maxSystem",maxSystem);
        int maxRing = audio.getStreamMaxVolume(AudioManager.STREAM_RING);
        map.put("maxRing",maxRing);
        int maxMusic = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        map.put("maxMusic",maxMusic);
        int maxAlarm = audio.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        map.put("maxAlarm",maxAlarm);
        int ringerMode = audio.getRingerMode();
        Log.e(TAG,"getAudioInfo=====" + maxVoice + "," + maxSystem + "," + maxRing + "," + maxMusic + "," + maxAlarm);
    }
    private void getSensorList() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor item : sensors) {
            String val = item.getVendor() + "," + item.getStringType() + "," + item.getVersion() + "," + item.getType();
            map.put(item.getName(),val);
        }
        for(Object key:map.keySet()){
            String value = map.get(key).toString();
            System.out.println("key=" + key + " vlaue="+value);
        }
    }
    private String getWebUa(Context context){

        String userAgentString = new WebView(context).getSettings().getUserAgentString();
        return  userAgentString;
    }
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

    private void getprops(){
        String ret ;
        for(int i = 0;i < props.length;i++) {
            ret = SystemProperties(props[i], "none");
            map.put(props[i],ret);
            Log.e(TAG, "SystemProperties key=" + props[i] + " val=" + SystemProperties(props[i],"none"));
        }
    }
    public String shellExec_app(String cmd) {
        Runtime mRuntime = Runtime.getRuntime();
        try {
            //Process中封装了返回的结果和执行错误的结果
            Process mProcess = mRuntime.exec(cmd);
            BufferedReader mReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
            StringBuffer mRespBuff = new StringBuffer();
            char[] buff = new char[1024];
            int ch = 0;
            while ((ch = mReader.read(buff)) != -1) {
                mRespBuff.append(buff, 0, ch);
            }
            mReader.close();
            return mRespBuff.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    private void  load(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //getprops();
                //getCpuinfoHardware();
                //getCpufreq();
                //getAudioInfo();
                //getSensorList();
                Log.e(TAG, "shellExec_app=" + shellExec_app("uname -a"));
                Log.e(TAG, "shellExec_app=" + shellExec_app("uname -r"));
                Log.e(TAG,"os.version======" + System.getProperty("os.version"));
                Log.e(TAG,"http.agent======" + System.getProperty("http.agent"));
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
        load();
    }

    public void startFileReceiverActivity(View view) {
        Log.e(TAG, "getWebUa=" + getWebUa(mContext));
    }

}