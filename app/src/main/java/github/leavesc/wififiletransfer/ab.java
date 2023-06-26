package github.leavesc.wififiletransfer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class ab {
    private static final BitSet AFLogger$LogLevel;
    private static final Handler AppsFlyer2dXConversionCallback = new Handler(Looper.getMainLooper());
    private static volatile ab init;
    final Object AFInAppEventParameterName = new Object();
    final Handler AFInAppEventType;
    final Runnable AFKeystoreWrapper;
    final Executor AFVersionDeclaration;
    final Runnable getLevel;
    private static  String TAG = "ab";
    private final SensorManager onAppOpenAttributionNative;
    private long onAttributionFailure;
    private final Map<w, Map<String, Object>> onAttributionFailureNative;
    private final Map<String, String> sensorInfoMap;
    private final Runnable onDeepLinking;
    private static HashMap<String,Object> sensorvalue;
    private final Map<w, w> onDeepLinkingNative;
    private int onInstallConversionDataLoadedNative;
    private boolean onInstallConversionFailureNative;
    final Runnable valueOf;
    boolean values;

    static {
        BitSet bitSet = new BitSet(6);
        AFLogger$LogLevel = bitSet;
        bitSet.set(1);
        bitSet.set(2);
        bitSet.set(4);
    }

    private ab(SensorManager sensorManager, Handler handler) {
        BitSet bitSet = AFLogger$LogLevel;
        this.onDeepLinkingNative = new HashMap(bitSet.size());
        this.onAttributionFailureNative = new ConcurrentHashMap(bitSet.size());
        sensorInfoMap = new HashMap();
        Log.e(TAG, "ab init00000----------");
        //w.clearList();
        this.valueOf = new Runnable() {
            /* class com.appsflyer.internal.ab.AnonymousClass4 */

            public final void run() {
                Log.e(TAG, "ab init1111----------");
                synchronized (ab.this.AFInAppEventParameterName) {
                    ab abVar = ab.this;
                    abVar.AFVersionDeclaration.execute(new Runnable() {
                        /* class com.appsflyer.internal.ab.AnonymousClass3 */

                        public final void run() {
                            try {
                                Log.e(TAG, "ab init2222----------");
                                for (Sensor sensor : ab.this.onAppOpenAttributionNative.getSensorList(-1)) {
                                    if (ab.AFInAppEventParameterName(sensor.getType())) {
                                        w wVar = new w(sensor, ab.this.AFVersionDeclaration);
                                        if (!ab.this.onDeepLinkingNative.containsKey(wVar)) {
                                            ab.this.onDeepLinkingNative.put(wVar, wVar);
                                        }
                                        Log.e(TAG, "registerListener----------");
                                        ab.this.onAppOpenAttributionNative.registerListener((SensorEventListener) ab.this.onDeepLinkingNative.get(wVar), sensor, 0);
                                    }
                                }
                            } catch (Throwable th) {
                            }
                            ab.this.onInstallConversionFailureNative = true;
                        }
                    });
                    ab.this.AFInAppEventType.postDelayed(ab.this.onDeepLinking, 100);
                    ab.this.values = true;
                }
            }
        };
        this.AFKeystoreWrapper = new Runnable() {
            /* class com.appsflyer.internal.ab.AnonymousClass5 */

            public final void run() {
                synchronized (ab.this.AFInAppEventParameterName) {
                    ab abVar = ab.this;
                    abVar.AFVersionDeclaration.execute(new Runnable() {
                        /* class com.appsflyer.internal.ab.AnonymousClass6 */

                        public final void run() {
                            try {
                                if (!ab.this.onDeepLinkingNative.isEmpty()) {
                                    for (w wVar : ab.this.onDeepLinkingNative.values()) {
                                        Log.e(TAG, "unregisterListener+++++++");
                                        ab.this.onAppOpenAttributionNative.unregisterListener(wVar);
                                        wVar.values(ab.this.onAttributionFailureNative, true);

//                                        ArrayList list = (ArrayList) sensorvalue.get("1");
//                                        if( (list!= null)&&(list.size() > 0)){
//                                            for (int i = 0;i < list.size();i++){
//                                                Log.e(TAG, "list value----" + list.get(i));
//                                            }
//                                        }
                                    }
                                }
                            } catch (Throwable th) {
                            }
                            ab.this.onInstallConversionDataLoadedNative = 0;
                            ab.this.onInstallConversionFailureNative = false;
                        }
                    });
                }
            }
        };
        this.getLevel = new Runnable() {
            /* class com.appsflyer.internal.ab.AnonymousClass1 */

            public final void run() {
                synchronized (ab.this.AFInAppEventParameterName) {
                    Log.e(TAG, "getLevel  values----------" + values);
                    if (true) {
                        ab.this.AFInAppEventType.removeCallbacks(ab.this.valueOf);
                        ab.this.AFInAppEventType.removeCallbacks(ab.this.AFKeystoreWrapper);
                        ab abVar = ab.this;
                        abVar.AFVersionDeclaration.execute(new Runnable() {
                            /* class com.appsflyer.internal.ab.AnonymousClass6 */

                            public final void run() {
                                try {
                                    int index = 0;
                                    if (!ab.this.onDeepLinkingNative.isEmpty()) {
                                        for (w wVar : ab.this.onDeepLinkingNative.values()) {
                                            Log.e(TAG, "getLevel  unregisterListener----------");
                                            //ab.this.onAppOpenAttributionNative.unregisterListener(wVar);
                                            wVar.values(ab.this.onAttributionFailureNative, true);
                                            wVar.getSensorMap(sensorInfoMap);
                                            index++;
                                            Log.e(TAG, "getLevel  index ----------" + index);
                                            if(index ==1) {
                                                Message message = new Message();
                                                message.what = 1;
                                                message.obj = sensorInfoMap;
                                                handler.sendMessage(message);
                                            }
                                            Log.e(TAG, "getLevel  sensorInfoMap ----------" + sensorInfoMap);

//                                            HashMap map =wVar.getSensorValue();
//                                            sensorvalue.putAll(map);
//                                            Log.e(TAG, "sensorvalue-+++-------" + sensorvalue);
//                                            for(String key:sensorvalue.keySet()){
//                                                String value = sensorvalue.get(key).toString();
//                                                Log.e(TAG, "list value----" + value);
//                                            }
                                        }
                                    }
                                } catch (Throwable th) {
                                }
                                ab.this.onInstallConversionDataLoadedNative = 0;
                                ab.this.onInstallConversionFailureNative = false;
                            }
                        });
                        ab.this.values = false;
                    }
                }
            }
        };
        this.onInstallConversionDataLoadedNative = 1;
        this.onAttributionFailure = 0;
        this.onDeepLinking = new Runnable() {
            /* class com.appsflyer.internal.ab.AnonymousClass2 */

            public final void run() {
                synchronized (ab.this.AFInAppEventParameterName) {
                    if (ab.this.onInstallConversionDataLoadedNative == 0) {
                        ab.this.onInstallConversionDataLoadedNative = 1;
                    }
                    ab.this.AFInAppEventType.postDelayed(ab.this.AFKeystoreWrapper, ((long) ab.this.onInstallConversionDataLoadedNative) * 500);
                }
            }
        };
        this.AFVersionDeclaration = Executors.newSingleThreadExecutor();
        this.onAppOpenAttributionNative = sensorManager;
        this.AFInAppEventType = handler;
    }

    static ab AFKeystoreWrapper(Context context ,Handler handler) {
        Log.e(TAG, "AFKeystoreWrapper0000 init----------"+ init);
        if (init != null) {
            return init;
        }
        Log.e(TAG, "AFKeystoreWrapper11111 ----------");
        return AFInAppEventParameterName((SensorManager) context.getApplicationContext().getSystemService(Context.SENSOR_SERVICE),handler );
    }

    private static ab AFInAppEventParameterName(SensorManager sensorManager, Handler handler) {
        Log.e(TAG, "AFInAppEventParameterName0000 ----------");
        if (init == null) {
            synchronized (ab.class) {
                if (init == null) {
                    Log.e(TAG, "AFInAppEventParameterName11111 ----------");
                    init = new ab(sensorManager, handler);
                }
            }
        }
        return init;
    }

    /* access modifiers changed from: private */
    public static boolean AFInAppEventParameterName(int i) {
        return i >= 0 && AFLogger$LogLevel.get(i);
    }

    /* access modifiers changed from: package-private */
    public final void sendDelayStop() {
        this.AFInAppEventType.postDelayed(this.getLevel,5000);
    }
    public final void start() {
        Log.e(TAG,"start---====");
        this.AFInAppEventType.postDelayed(this.valueOf,1000);
        this.AFInAppEventType.postDelayed(this.getLevel,2000);
    }
    public final void valueOf() {
        long currentTimeMillis = System.currentTimeMillis();
        long j = this.onAttributionFailure;
//        if (j != 0) {
//            this.onInstallConversionDataLoadedNative++;
//            if (j - currentTimeMillis < 500) {
//                this.AFInAppEventType.removeCallbacks(this.AFKeystoreWrapper);
//                this.AFInAppEventType.post(this.valueOf);
//            }
//        } else {
            //this.AFInAppEventType.postDelayed(this.getLevel,5000);
            Log.e(TAG,"start postDelayed valueOf====");
            this.AFInAppEventType.postDelayed(this.valueOf,2000);
        //}
        this.onAttributionFailure = currentTimeMillis;
    }
    public HashMap getsensor(HashMap dap){
        for (w wVar : this.onDeepLinkingNative.values()) {
            HashMap map = wVar.getSensorValue();
            dap.putAll(map);
        }
        return dap;
    }
    public void clearList(){
        for (w wVar : this.onDeepLinkingNative.values()) {
            wVar.clearList();
        }
    }
    /* access modifiers changed from: package-private */
    public final List<Map<String, Object>> AFInAppEventType() {
        for (w wVar : this.onDeepLinkingNative.values()) {
            wVar.values(this.onAttributionFailureNative, true);
        }
        Map<w, Map<String, Object>> map = this.onAttributionFailureNative;
        if (map == null || map.isEmpty()) {
            return new CopyOnWriteArrayList(Collections.emptyList());
        }
        return new CopyOnWriteArrayList(this.onAttributionFailureNative.values());
    }
    public HashMap getSensorinfo(){
        HashMap map = new HashMap<>();

        return map;
    }
    /* access modifiers changed from: package-private */
    public final List<Map<String, Object>> values() {
        synchronized (this.AFInAppEventParameterName) {
            if (!this.onDeepLinkingNative.isEmpty() && this.onInstallConversionFailureNative) {
                for (w wVar : this.onDeepLinkingNative.values()) {
                    wVar.values(this.onAttributionFailureNative, false);
                }
            }
            if (this.onAttributionFailureNative.isEmpty()) {
                return new CopyOnWriteArrayList(Collections.emptyList());
            }
            return new CopyOnWriteArrayList(this.onAttributionFailureNative.values());
        }
    }
}