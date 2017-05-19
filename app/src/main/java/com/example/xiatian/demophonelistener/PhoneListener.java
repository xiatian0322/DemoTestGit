package com.example.xiatian.demophonelistener;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by xiatian on 2017/5/11.
 */

public class PhoneListener extends BroadcastReceiver {
    private static final String TAG = "PhoneListener";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: action=" + intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            Log.d(TAG, "onReceive: 拨出");
        } else {
            Log.d(TAG, "onReceive: 接入");
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        }


    }

    public static boolean phoneIsInUse(Context context) {
        boolean isCall = false;
        if (Build.VERSION.SDK_INT >= 21) {
            TelecomManager manager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return false;
            }
            isCall = manager.isInCall();
        }

        return isCall;
    }


    private PhoneStateListener listener = new PhoneStateListener() {
        //state 当前状态 incomingNumber,
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(TAG, "onCallStateChanged: 暂时未接");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG, "onCallStateChanged:摘机");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(TAG, "onCallStateChanged: 响铃" + incomingNumber);
                    break;
            }
        }

    };


}
