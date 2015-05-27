package mx.citydevs.denunciaelectoral.dialogues;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by zace3d on 5/26/15.
 */
public class Dialogues {
	private static Toast toast;
	
	public static void Toast(Context context, String text, int duration) {
		if(toast != null)
			toast.cancel();
		
		toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	public static void Toast(Context context, int text, int duration) {
		if(toast != null)
			toast.cancel();
		
		toast = Toast.makeText(context, context.getString(text), duration);
		toast.show();
	}

	public static void Log(String tag, String text, int type) {
		if (type == Log.DEBUG) {
			Log.d(tag, text + "");
		} else if (type == Log.ERROR) {
			Log.e(tag, text + "");
		} else if (type == Log.INFO) {
			Log.i(tag, text + "");
		} else if (type == Log.VERBOSE) {
			Log.v(tag, text + "");
		} else if (type == Log.WARN) {
			Log.w(tag, text + "");
		}
	}
}
