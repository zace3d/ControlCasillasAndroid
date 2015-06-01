package mx.citydevs.denunciaelectoral.httpconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by zace3d on 5/31/15.
 */
public class NetworkUtils {

	public static boolean isNetworkConnectionAvailable(Context context) {
		if (context != null) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			return networkInfo != null && networkInfo.isConnected();
		}

		return false;
	}
}