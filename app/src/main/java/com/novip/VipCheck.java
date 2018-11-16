package com.novip;

import android.content.Context;

import com.novip.utils.SharedPrefernceUtils;

public class VipCheck {

    public static long getVipEnd(Context context){
        long v = SharedPrefernceUtils.getInstance(context).getLong("v_end");
        if(v == 0){
            long d =  System.currentTimeMillis()+24*60*60*1000l;
            SharedPrefernceUtils.getInstance(context).putLong("v_end",d);
            return d;
        }else {
            return v;
        }
    }

    public static void addVipEnd(Context context,int days){
        long d =  System.currentTimeMillis()+days*24*60*60*1000l;
        SharedPrefernceUtils.getInstance(context).putLong("v_end",d);
    }

    //14个字符长度，13个是时间戳，还有一个是VIP时间（1一周，2一个月，3永久）
    public static String encode(String str) {
        char[] data = str.toCharArray();
        int len = data.length;
        for(int i=0;i<len;i++) {
            data[i] = (char) (data[i]+i-1);
        }
        return new String(data);
    }

    //生成日期时间戳，VIP时间（1一周，2一个月，3永久）
    public static boolean decodeCode(Context context,String str) {
        long out_t = 10*60*1000;//过期时间10分钟
        boolean state = false;
        if(str.length() != 14){
            return false;
        }
        char[] data = str.toCharArray();
        int len = data.length;
        for(int i=0;i<len;i++) {
            data[i] = (char) (data[i]-i+1);
        }
        String s = new String(data);
        try {
            long date = Long.valueOf(s.substring(0,13));
            int p = Integer.valueOf(s.substring(13,14));
            if(System.currentTimeMillis() - date > out_t){
                //过期了
                state = false;
            }else {
                switch (p){
                    case 1:
                        addVipEnd(context,7);
                        state = true;
                        break;
                    case 2:
                        addVipEnd(context,30);
                        state = true;
                        break;
                    case 3:
                        addVipEnd(context,9999);
                        state = true;
                        break;
                }
            }

        }catch (Exception e){
            return false;
        }

        return state;
    }

}
