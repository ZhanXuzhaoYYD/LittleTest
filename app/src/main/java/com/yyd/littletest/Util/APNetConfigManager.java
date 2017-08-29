package com.yyd.littletest.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.id.list;

/**
 * <pre>
 *     author : ZhanXuzhao
 *     e-mail : zhanxuzhao@yydrobot.com
 *     time   : 2017/08/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class APNetConfigManager {
    public static final String ROBOT_SSID = "SLAMWARE-C146F3";
    public static final String ROBOT_SSID_WRAPPER = "\"+" + ROBOT_SSID + "\"";
    public static final String ROBOT_AP_HOST = "192.168.1.1";
    public static final int ROBOT_AP_PORT = 8888;

    private Socket mSocket;
    private Timer mTimer;
    private Thread mThread;
    private Context mContext;
    private NetConfigCallback mCallback;
    private String mSsid;
    private String mPwd;
    private static APNetConfigManager sInstance;

    public static APNetConfigManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (APNetConfigManager.class) {
                if (sInstance == null) {
                    sInstance = new APNetConfigManager(context);
                }
            }
        }
        return sInstance;
    }

    private APNetConfigManager(Context context) {
        mContext = context;
    }


    public interface NetConfigCallback {
        void onConnectRobotAp();

        void onSendSSIDSuccess();

        void onError();
    }

    /**
     * 记得手动调用stop()终止配网，否则会导致内存泄漏
     *
     * @param ssid
     * @param pwd
     * @param userId
     * @param callback
     */
    public void startConfig(String ssid, String pwd, String userId, NetConfigCallback callback) {
        mSsid = ssid;
        mPwd = pwd;
        mCallback = callback;
        setWifiEnabled(mContext, true);
        startSendSSID(mSsid, mPwd, userId);
    }

    public void stop() {
        stopTimer();
        closeSocket();
    }

    private void closeSocket() {
        try {
            if (mSocket != null) {
                if (!mSocket.isClosed()) {
                    mSocket.close();
                }
                mSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void startSendSSID(final String ssid, final String pwd, final String userId) {
        stopTimer();
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                mTimer = new Timer();
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            String currentSsid = getCurrentSsid(mContext);
                            System.out.println("currentSsid: " + currentSsid);
                            if (TextUtils.isEmpty(currentSsid)) {
                                // 未连接到任何wifi（wifi切换过程出现），跳过等待
                                return;
                            }
                            if (!ROBOT_SSID_WRAPPER.equals(currentSsid)) {
                                // 连接到其它wifi（robot_ap未开启时，手机会自动连上其它wifi），需要切换到robot_ap
                                changeWifi(ROBOT_SSID);
                                return;
                            }
                            System.out.println("start send data");
                            // 已连接到robot_ap，开始发送数据
                            mCallback.onConnectRobotAp();
                            String data = getSocketData(ssid, pwd, userId);
                            if (mSocket == null) {
                                mSocket = new Socket(ROBOT_AP_HOST, ROBOT_AP_PORT);
                            }
                            OutputStream out = mSocket.getOutputStream();
                            PrintWriter output = new PrintWriter(out);
                            output.println(data);
                            output.flush();
                            mCallback.onSendSSIDSuccess();
                        } catch (final IOException e) {
                            e.printStackTrace();
                            mCallback.onError();
                        }
                    }
                }, 0, 500);
            }
        });
        mThread.start();

    }

    private String getSocketData(String ssid, String pwd, String userId) {
        return String.format("[%1$s]<%2$s>{%3$s}", ssid, pwd, userId);
    }

    private void changeWifi(String ssid) {
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + ssid + "\"";   // Please note the quotes. String should contain ssid in quotes
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();

                break;
            }
        }
    }

    public static String getCurrentSsid(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid;
    }

    private void setWifiEnabled(Context context, boolean enable) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enable);
    }

    private boolean isWifiEnable(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

}
