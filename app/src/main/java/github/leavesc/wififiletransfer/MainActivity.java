package github.leavesc.wififiletransfer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
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
                    Toast.makeText(getApplicationContext(), (CharSequence) msg.obj, Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };
    public  String getCpuinfo() {
        String str = null;
        String line = null;
        try {
            FileReader fileReader = new FileReader("/proc/cpuinfo");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                if(line.contains("Processor")){
                    String[] split = line.split(":\\s+", 2);
                    if (split.length > 1) {
                        str = split[1] + "|";
                    }
                } else if(line.contains("Hardware")){
                    String[] split = line.split(":\\s+", 2);
                    if (split.length > 1) {
                        str = str + split[1];
                    }
                }
            }
            fileReader.close();
            bufferedReader.close();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return str;
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
        HashMap smap = new HashMap();
        for(int i =0;i < list.length;i++) {
            try {
                String line;
                BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/" + list[i]));
                if ((line = br.readLine()) != null) {
                    Log.e(TAG, "getCpufreq str__========== " + line);
                    smap.put(list[i], line);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        map.put("cpufreq",smap);
    }
    public void getAudioInfo(){
        AudioManager audio = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int maxVoice = audio.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        int maxSystem = audio.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        int maxRing = audio.getStreamMaxVolume(AudioManager.STREAM_RING);
        int maxMusic = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int maxAlarm = audio.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        int ringerMode = audio.getRingerMode();
        String audioinfo = maxVoice + "," + maxSystem + "," + maxRing + "," + maxMusic + "," + maxAlarm +  "," + ringerMode;
        Log.e(TAG,"getAudioInfo=====" + audioinfo);
        map.put("audioinfo",audioinfo);
    }
    private void getSensorList() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        HashMap smap = new HashMap();
        for (Sensor item : sensors) {
            String val = item.getVendor() + "," + item.getStringType() + "," + item.getVersion() + "," + item.getType();
            smap.put(item.getName(),val);
        }
        map.put("sensorInfo",smap);
    }
    public String getSimNetWork() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            String networkCountryIso = telephonyManager.getNetworkCountryIso();
            String networkOperatorName = telephonyManager.getNetworkOperatorName();
            String networkOperator = telephonyManager.getNetworkOperator();
            String simOperator = telephonyManager.getSimOperator();
            String SimOperatorName = telephonyManager.getSimOperatorName();
            String SimCountryIso = telephonyManager.getSimCountryIso();
            Log.e(TAG,"networkOperatorName=" + networkOperatorName);
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "权限获取失败";
            }
            String softVersion = telephonyManager.getDeviceSoftwareVersion();
            String siminfo = simOperator + "," + SimOperatorName + "," + SimCountryIso + "," + networkOperator + "," + networkOperatorName + "," + networkCountryIso + "," + softVersion;
            map.put("siminfo",siminfo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
    private void getTimeZone(){
        TimeZone timeZone = TimeZone.getDefault();
        String id = timeZone.getID();
        String name = timeZone.getDisplayName(); //获取名字，如“”
        String shotName = timeZone.getDisplayName(false, TimeZone.SHORT); //获取名字，如“GMT+08:00”
        int time = timeZone.getOffset(System.currentTimeMillis());
        String mTimeZone = id +  "," + name + "," + shotName + "," + time;
        map.put("TimeZone",mTimeZone);
        Log.e(TAG,"getTimeZone id=" + id +  " name=" + name + " shotName=" + shotName + " time=" + time);
    }
    private void getSsytemRomSpace() {
        File path = Environment.getDataDirectory();
        StatFs dataFs = new StatFs(path.getPath());
        String dataInfo = dataFs.getBlockCountLong() + "," +  dataFs.getAvailableBlocksLong() + "," + dataFs.getFreeBlocksLong();
        map.put("dataInfo",dataInfo);
        String exsd = Environment.getExternalStorageDirectory().getPath();
        StatFs exsdFs = new StatFs(exsd);
        String exsdInfo = exsdFs.getBlockCountLong() + ","  + exsdFs.getAvailableBlocksLong() + "," +  "," + exsdFs.getFreeBlocksLong();
        map.put("exsdInfo",exsdInfo);
        File root = Environment.getRootDirectory();
        StatFs rootFs = new StatFs(root.getPath());
        String rootInfo = rootFs.getBlockCountLong() + ","  + rootFs.getAvailableBlocksLong() + "," +  "," + rootFs.getFreeBlocksLong();
        map.put("rootInfo",rootInfo);
    }
    private void showBuild() {
        HashMap smap = new HashMap();
        smap.put("board",Build.BOARD);
        smap.put("bootloader",Build.BOOTLOADER);
        smap.put("brand",Build.BRAND);
        smap.put("product",Build.PRODUCT);
        smap.put("device",Build.DEVICE);
        smap.put("display",Build.DISPLAY);
        smap.put("fingerprint",Build.FINGERPRINT);
        smap.put("hardware",Build.HARDWARE);
        smap.put("host",Build.HOST);
        smap.put("id",Build.ID);
        smap.put("manufacturer",Build.MANUFACTURER);
        smap.put("model",Build.MODEL);
        smap.put("tags",Build.TAGS);
        smap.put("type",Build.TYPE);
        smap.put("user",Build.USER);
        smap.put("time",Build.TIME);
        smap.put("base_os",Build.VERSION.BASE_OS);
        smap.put("sdk_int",Build.VERSION.SDK_INT);
        smap.put("codename",Build.VERSION.CODENAME);
        smap.put("incremental",Build.VERSION.INCREMENTAL);
        smap.put("release",Build.VERSION.RELEASE);
        smap.put("security",Build.VERSION.SECURITY_PATCH);
        smap.put("sdk",Build.VERSION.SDK);
        smap.put("radioVersion",Build.getRadioVersion());
        Log.e(TAG, "Build.SERIAL=" + Build.SERIAL);
        smap.put("serial",Build.SERIAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            smap.put("serial",Build.getSerial());
        }
        map.put("buildinfo",smap);
    }
    private String getProcMemInfo(){
        String str = null;
        try {
            BufferedReader var3 = new BufferedReader(new FileReader("/proc/meminfo"), 4096);
            String meminfo;
            do {
                meminfo= var3.readLine();
                if (meminfo!= null) {
                    String[] var10000 = meminfo.split("\\s+");
                    if(meminfo.contains("MemTotal")){
                        str = var10000[1] + ",";
                    }else if(meminfo.contains("MemFree")){
                        str = str + var10000[1] + ",";
                    }else if(meminfo.contains("MemAvailable")){
                        str = str + var10000[1];
                    }
                }
            }
            while(meminfo!= null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  str;
    }
    private void getMemInfo(){
        String mProcMeminfo = getProcMemInfo();
        ActivityManager mActivityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo() ;
        mActivityManager.getMemoryInfo(memoryInfo) ;
        String ret = mProcMeminfo + "," + memoryInfo.threshold;
        map.put("meminfo", ret);
        Log.e(TAG,"getMemInfo ret=== " + ret);
    }
    private void  getlang(){
        map.put("lang", Locale.getDefault().getDisplayLanguage());
        map.put("lang_code", Locale.getDefault().getLanguage());
        map.put("country", Locale.getDefault().getCountry());
    }
    private void getDisplayInfo(){
        DisplayMetrics dm1 = getResources().getDisplayMetrics();
        String info1 = dm1.density + "," + dm1.densityDpi  + "," + dm1.heightPixels  + "," +  dm1.widthPixels  + "," + dm1.scaledDensity  + ","+ dm1.xdpi  + ","+ dm1.ydpi;
        map.put("diaplayinfo1",info1);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm2 = new DisplayMetrics();
        display.getMetrics(dm2);
        String info2 = dm2.density + "," + dm2.densityDpi  + "," + dm2.heightPixels  + "," +  dm2.widthPixels  + "," + dm2.scaledDensity  + ","+ dm2.xdpi  + ","+ dm2.ydpi;
        map.put("diaplayinfo2",info2);
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
    private void uploadPhone(String info,String model,String cpu){
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            String ss = "model=" + model + "&cpu=" + cpu + "&info=" + info;
            RequestBody body = RequestBody.create(mediaType, ss);
            Request request = new Request.Builder()
                .url("http://kig.adszt.com/api/cpu/post")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
            Response response = client.newCall(request).execute();
            String  ret = response.body().string();
            JSONObject resultdata = JSONObject.parseObject(ret);
            String code =  String.valueOf(resultdata.get("code"));
            Log.e(TAG,"code=====" + code);
            if(code.equals("0")){
                sendmsg(3,"上传成功!!!!!",10);
            }else{
                sendmsg(3,"上传失败!!!!!",10);
            }
        }catch (Exception e){
            e.printStackTrace();
            sendmsg(3,e.toString(),10);
        }

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
                showBuild();
                getprops();
                String cpu = getCpuinfo();
                map.put("cpuinfo",cpu);
                getCpufreq();
                getAudioInfo();
                getSensorList();
                String uname_a = shellExec_app("uname -a");
                map.put("uname_a", uname_a);
                String uname_r = shellExec_app("uname -r");
                map.put("uname_r", uname_r);
                String os_version = System.getProperty("os.version");
                map.put("os_version", os_version);
                String http_agent = System.getProperty("http.agent");
                map.put("http_agent", http_agent);
                Log.e(TAG, "shellExec_app=" + uname_a);
                Log.e(TAG, "uname_r=" + uname_r);;
                Log.e(TAG,"http.agent======" + http_agent);
                Log.e(TAG,"os_version======" + os_version);
                String model = SystemProperties("ro.product.model","none");
                getMemInfo();
                getProcMemInfo();
                getSsytemRomSpace();
                getDisplayInfo();
                getlang();
                getTimeZone();
                getSimNetWork();
                String str =JSON.toJSONString(map);
                Log.e(TAG, "propmap----------" + str);
                //uploadPhone(str,model,cpu);
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
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION}, CODE_REQ_PERMISSIONS);
    }

    public void startFileSenderActivity(View view) {
        load();
    }

    public void startFileReceiverActivity(View view) {
        Log.e(TAG, "getWebUa=" + getWebUa(mContext));
    }

}