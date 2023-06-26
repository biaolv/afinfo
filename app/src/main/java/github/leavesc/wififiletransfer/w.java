package github.leavesc.wififiletransfer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/* access modifiers changed from: package-private */
public final class w implements SensorEventListener {
    private static String TAG="W";
    long AFInAppEventParameterName;
    long looptime;
    final float[][] AFInAppEventType = new float[2][];
    double AFKeystoreWrapper;
    private final Executor AFVersionDeclaration;
    private final int AppsFlyer2dXConversionCallback;
    private final String getLevel;
    private final String init;
    private final int valueOf;
    private  int num = 10;
    private  int i = 0;
    private Handler mHandler;
    private HashMap keyMap = new HashMap();
    private HashMap sensorMap = new HashMap();
    public  ArrayList<String> mlist  = new ArrayList<>();
    final long[] values = new long[2];

    static /* synthetic */ double values(float[] fArr, float[] fArr2) {
        int min = Math.min(fArr.length, fArr2.length);
        double d = 0.0d;
        for (int i = 0; i < min; i++) {
            d += StrictMath.pow((double) (fArr[i] - fArr2[i]), 2.0d);
        }
        return Math.sqrt(d);
    }
    public  void clearList(){
        Log.e(TAG,"clearList src keyMap size===========" + keyMap.size());
        keyMap.clear();
        looptime = 0;
        i =0;
        Log.e(TAG,"clearList size=" + keyMap.size());
    }
    w(Sensor sensor, Executor executor) {
        this.AFVersionDeclaration = executor;
        int type = sensor.getType();
        this.valueOf = type;
        String name = sensor.getName();
        String str = "";
        name = name == null ? str : name;
        this.init = name;
        String vendor = sensor.getVendor();
        str = vendor != null ? vendor : str;
        this.getLevel = str;
        this.AppsFlyer2dXConversionCallback = ((((type + 31) * 31) + name.hashCode()) * 31) + str.hashCode();
    }

    private static List<Float> valueOf(float[] fArr) {
        ArrayList<Float> arrayList = new ArrayList<>(fArr.length);
        for (float f : fArr) {
            arrayList.add(Float.valueOf(f));
        }
        return arrayList;
    }

    public final void onSensorChanged(SensorEvent sensorEvent) {
        final long j = sensorEvent.timestamp;
        final float[] srcArr = sensorEvent.values;
        //Log.e(TAG, "onSensorChanged Name=" + sensorEvent.sensor.getName() + " vendor= " + sensorEvent.sensor.getName() + " type=" + sensorEvent.sensor.getType());
        this.AFVersionDeclaration.execute(new Runnable() {
            public final void run() {
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                long currentTimeMillis = System.currentTimeMillis();
                Log.e(TAG, "onSensorChanged ----------" + j);
                float[] firstValue = w.this.AFInAppEventType[0];
//                if((i < num)) {
//                    BigDecimal v0 = new BigDecimal(sensorEvent.values[0]);
//                    BigDecimal v1 = new BigDecimal(sensorEvent.values[1]);
//                    BigDecimal v2 = new BigDecimal(sensorEvent.values[2]);
//                    String val = v0.toString() + "," + v1.toString() + "," + v2.toString();
//                    keyMap.put(i,val);
//                    i++;
//                }else{
//                    //Log.e(TAG, "keyMap size > " + i);
//                }
//                if(looptime ==0){
//                    looptime = j;
//                    BigDecimal v0 = new BigDecimal(sensorEvent.values[0]);
//                    BigDecimal v1 = new BigDecimal(sensorEvent.values[1]);
//                    BigDecimal v2 = new BigDecimal(sensorEvent.values[2]);
//                    String val = sensorEvent.values[0] + "," + sensorEvent.values[0] + "," + sensorEvent.values[0];
//                    keyMap.put(0,val);
//                }else if((10000000< (j - looptime) )&& ((j - looptime)< 20000000)){
//                    BigDecimal v0 = new BigDecimal(sensorEvent.values[0]);
//                    BigDecimal v1 = new BigDecimal(sensorEvent.values[1]);
//                    BigDecimal v2 = new BigDecimal(sensorEvent.values[2]);
//                    String val = sensorEvent.values[0] + "," + sensorEvent.values[0] + "," + sensorEvent.values[0];
//                    keyMap.put(1,val);
//                } else if((20000000< (j - looptime) )&& ((j - looptime)< 30000000)){
//                    BigDecimal v0 = new BigDecimal(sensorEvent.values[0]);
//                    BigDecimal v1 = new BigDecimal(sensorEvent.values[1]);
//                    BigDecimal v2 = new BigDecimal(sensorEvent.values[2]);
//                    String val = sensorEvent.values[0] + "," + sensorEvent.values[0] + "," + sensorEvent.values[0];
//                    keyMap.put(2,val);
//                }else if((30000000< (j - looptime) )&& ((j - looptime)< 40000000)){
//                    BigDecimal v0 = new BigDecimal(sensorEvent.values[0]);
//                    BigDecimal v1 = new BigDecimal(sensorEvent.values[1]);
//                    BigDecimal v2 = new BigDecimal(sensorEvent.values[2]);
//                    String val = sensorEvent.values[0] + "," + sensorEvent.values[0] + "," + sensorEvent.values[0];
//                    keyMap.put(3,val);
//                }else if((40000000< (j - looptime) )&& ((j - looptime)< 50000000)){
//                    BigDecimal v0 = new BigDecimal(sensorEvent.values[0]);
//                    BigDecimal v1 = new BigDecimal(sensorEvent.values[1]);
//                    BigDecimal v2 = new BigDecimal(sensorEvent.values[2]);
//                    String val = sensorEvent.values[0] + "," + sensorEvent.values[0] + "," + sensorEvent.values[0];
//                    keyMap.put(4,val);
//                }else if((50000000< (j - looptime) )&& ((j - looptime)< 60000000)){
//                    BigDecimal v0 = new BigDecimal(sensorEvent.values[0]);
//                    BigDecimal v1 = new BigDecimal(sensorEvent.values[1]);
//                    BigDecimal v2 = new BigDecimal(sensorEvent.values[2]);
//                    String val = sensorEvent.values[0] + "," + sensorEvent.values[0] + "," + sensorEvent.values[0];
//                    keyMap.put(5,val);
//                }else if((60000000< (j - looptime) )&& ((j - looptime)< 70000000)){
//                    BigDecimal v0 = new BigDecimal(sensorEvent.values[0]);
//                    BigDecimal v1 = new BigDecimal(sensorEvent.values[1]);
//                    BigDecimal v2 = new BigDecimal(sensorEvent.values[2]);
//                    String val = sensorEvent.values[0] + "," + sensorEvent.values[0] + "," + sensorEvent.values[0];
//                    keyMap.put(6,val);
//                }else if((70000000< (j - looptime) )&& ((j - looptime)< 80000000)){
//                    BigDecimal v0 = new BigDecimal(sensorEvent.values[0]);
//                    BigDecimal v1 = new BigDecimal(sensorEvent.values[1]);
//                    BigDecimal v2 = new BigDecimal(sensorEvent.values[2]);
//                    String val = sensorEvent.values[0] + "," + sensorEvent.values[0] + "," + sensorEvent.values[0];
//                    keyMap.put(7,val);
//                }else if((80000000< (j - looptime) )&& ((j - looptime)< 90000000)){
//                    BigDecimal v0 = new BigDecimal(sensorEvent.values[0]);
//                    BigDecimal v1 = new BigDecimal(sensorEvent.values[1]);
//                    BigDecimal v2 = new BigDecimal(sensorEvent.values[2]);
//                    String val = sensorEvent.values[0] + "," + sensorEvent.values[0] + "," + sensorEvent.values[0];
//                    keyMap.put(8,val);
//                }else if((90000000< (j - looptime) )&& ((j - looptime)< 100000000)){
//                    BigDecimal v0 = new BigDecimal(sensorEvent.values[0]);
//                    BigDecimal v1 = new BigDecimal(sensorEvent.values[1]);
//                    BigDecimal v2 = new BigDecimal(sensorEvent.values[2]);
//                    //String val = v0.toString() + "," + v1.toString() + "," + v2.toString();
//                    String val = sensorEvent.values[0] + "," + sensorEvent.values[0] + "," + sensorEvent.values[0];
//                    keyMap.put(9,val);
//                }

                Log.e(TAG, "keyMap add value=" + sensorEvent.sensor.getType() + " ,"+ sensorEvent.values[0] + " ," + sensorEvent.values[1] + " ," + sensorEvent.values[2]);
                if (firstValue == null) {
                    float[][] fArr2 = w.this.AFInAppEventType;
                    fArr2[0] = Arrays.copyOf(srcArr, srcArr.length);
                    w.this.values[0] = currentTimeMillis;
                    //Log.e(TAG, "firstValue type=" + sensorEvent.sensor.getType() + " ,"+ fArr2[0][0] + " ," + fArr2[0][1] + " ," + fArr2[0][2]);
                    return;
                }
                float[] fArr4 = w.this.AFInAppEventType[1];
                if (fArr4 == null) {
                    float[] copyOf = Arrays.copyOf(srcArr, srcArr.length);
                    w.this.AFInAppEventType[1] = copyOf;
                    w.this.values[1] = currentTimeMillis;
                    w.this.AFKeystoreWrapper = w.values(srcArr, copyOf);
                } else if (50000000 <= j - w.this.AFInAppEventParameterName) {
                    w.this.AFInAppEventParameterName = j;
                    if (Arrays.equals(fArr4, srcArr)) {
                        Log.e(TAG, "firstValue fArr4====srcArr " + sensorEvent.sensor.getType() );
                        w.this.values[1] = currentTimeMillis;
                        return;
                    }
                    double values2 = w.values(firstValue, srcArr);
                    if (values2 > w.this.AFKeystoreWrapper) {
                        float[][] fArr6 = w.this.AFInAppEventType;
                        fArr6[1] = Arrays.copyOf(srcArr, srcArr.length);
                        w.this.values[1] = currentTimeMillis;
                        w.this.AFKeystoreWrapper = values2;
                        Log.e(TAG, "fArr6 type0=" + sensorEvent.sensor.getType() + " ,"+ fArr6[0][0] + " ," + fArr6[0][1] + " ," + fArr6[0][2]);
                        Log.e(TAG, "fArr6 type1=" + sensorEvent.sensor.getType() + " ,"+ fArr6[1][0] + " ," + fArr6[1][1] + " ," + fArr6[1][2]);
                    }
                }
            }
        });
    }

    public final void onAccuracyChanged(Sensor sensor, int i) {
    }
    /* access modifiers changed from: package-private */
    public final void getSensorMap(Map<String, String> map) {
        if (AFKeystoreWrapper()) {
            String v0 = AFInAppEventType[0][0] + "," + AFInAppEventType[0][1] + "," +AFInAppEventType[0][2];
            map.put(this.valueOf + "" +  0 , v0);
            String v1 = AFInAppEventType[1][0] + "," + AFInAppEventType[1][1] + "," +AFInAppEventType[1][2];
            map.put(this.valueOf + "" +  1 , v1);
            Log.e(TAG, "show list size=" + mlist.size());
        }
    }
    /* access modifiers changed from: package-private */
    public final void values(Map<w, Map<String, Object>> map, boolean z) {
        if (AFKeystoreWrapper()) {
            map.put(this, AFInAppEventParameterName());
            Log.e(TAG, "show list size=" + mlist.size());
            for(int i =0;i < mlist.size();i++){
               // Log.e(TAG, "mlist[" + i +"]=" +  mlist.get(i));
            }
//            if (z) {
//                int length = this.AFInAppEventType.length;
//                for (int i = 0; i < length; i++) {
//                    this.AFInAppEventType[i] = null;
//                }
//                int length2 = this.values.length;
//                for (int i2 = 0; i2 < length2; i2++) {
//                    this.values[i2] = 0;
//                }
//                this.AFKeystoreWrapper = 0.0d;
//                this.AFInAppEventParameterName = 0;
//            }
        } else if (!map.containsKey(this)) {
            map.put(this, AFInAppEventParameterName());
        }
    }

    private boolean AFKeystoreWrapper(int i, String str, String str2) {
        return this.valueOf == i && this.init.equals(str) && this.getLevel.equals(str2);
    }
    public HashMap getSensorValue(){
        HashMap map = new HashMap<>();
        Log.e(TAG,"setSensorValue add type=" +this.valueOf + " value=" + keyMap);
        map.put(this.valueOf,keyMap);
        return map;
    }
    private Map<String, Object> AFInAppEventParameterName() {
        ConcurrentHashMap<String, Object> concurrentHashMap = new ConcurrentHashMap<String, Object>(7);
        concurrentHashMap.put("sT", Integer.valueOf(this.valueOf));
        concurrentHashMap.put("sN", this.init);
        concurrentHashMap.put("sV", this.getLevel);
        float[] fArr = this.AFInAppEventType[0];
        if (fArr != null) {
            concurrentHashMap.put("sVS", valueOf(fArr));
        }
        float[] fArr2 = this.AFInAppEventType[1];
        if (fArr2 != null) {
            concurrentHashMap.put("sVE", valueOf(fArr2));
        }
        Log.e(TAG,"concurrentHashMap value=" + concurrentHashMap.toString());
        return concurrentHashMap;
    }

    private boolean AFKeystoreWrapper() {
        return this.AFInAppEventType[0] != null;
    }

    public final int hashCode() {
        return this.AppsFlyer2dXConversionCallback;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof w)) {
            return false;
        }
        w wVar = (w) obj;
        return AFKeystoreWrapper(wVar.valueOf, wVar.init, wVar.getLevel);
    }
}