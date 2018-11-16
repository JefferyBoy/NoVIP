package com.novip.utils;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.content.Context.TELEPHONY_SERVICE;

public class DeviceUtils {

    public static String getUniqueId(Context context) {
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        try {
            return toMD5(id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return id;
        }
    }

    private String getImei(Context context) {
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        String szImei = TelephonyMgr.getDeviceId();
        return szImei;
    }
    private String getAndroidId(Context context){
        String m_szAndroidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return m_szAndroidID;
    }

    /*
    * 返回的结果不正确，全部都返回了固定值：02:00:00:00:00:00
    * */
    public static String getWlanId(Context context){
        WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        return m_szWLANMAC;
    }

    private String getPseudo(){
        String m_szDevIDShort = "35" + "/"+//we make this look like a valid IMEI
                Build.BOARD.length()%10 +"/"+
                Build.BRAND.length()%10 +"/"+
               // Build.CPU_ABI.length()%10 +"/"+
                Build.DEVICE.length()%10 +"/"+
                Build.DISPLAY.length()%10 +"/"+
                Build.HOST.length()%10 +"/"+
                Build.ID.length()%10 +"/"+
                Build.MANUFACTURER.length()%10 +"/"+
                Build.MODEL.length()%10 +"/"+
                Build.PRODUCT.length()%10 +"/"+
                Build.TAGS.length()%10 +"/"+
                Build.TYPE.length()%10 +"/"+
                Build.USER.length()%10+"/"; //13 digits
        return m_szDevIDShort;
    }

    private static String toMD5(String text) throws NoSuchAlgorithmException {
        //获取摘要器 MessageDigest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        //通过摘要器对字符串的二进制字节数组进行hash计算
        byte[] digest = messageDigest.digest(text.getBytes());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            //循环每个字符 将计算结果转化为正整数;
            int digestInt = digest[i] & 0xff;
            //将10进制转化为较短的16进制
            String hexString = Integer.toHexString(digestInt);
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length() < 2) {
                sb.append(0);
            }
            //将循环结果添加到缓冲区
            sb.append(hexString);
        }
        //返回整个结果
        return sb.toString();
    }


    /**
     * 返回当前程序版本号
     */
    public static int getAppVersionCode(Context context) {
        int versioncode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            // versionName = pi.versionName;
            versioncode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode;
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName=null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

}