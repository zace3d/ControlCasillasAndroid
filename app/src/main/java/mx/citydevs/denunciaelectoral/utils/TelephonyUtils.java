package mx.citydevs.denunciaelectoral.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by zace3d on 6/3/15.
 */
public class TelephonyUtils {

    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}
