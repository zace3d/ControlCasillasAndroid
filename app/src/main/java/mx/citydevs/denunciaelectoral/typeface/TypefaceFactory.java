package mx.citydevs.denunciaelectoral.typeface;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by zace3d on 5/26/15.
 */
public class TypefaceFactory {
    public static final int Akkurat = 0;
    public static final int BrauerNeue_Regular = 1;
	
	private static String typefaceDirAkkurat = "fonts/Akkurat/";
    private static String typefaceDirBrauerNeue = "fonts/BrauerNeue/";
	
	public static Typeface createTypeface(Context context, int type) {
        Typeface typeface;

		if(type == Akkurat) {
			typeface = Typeface.createFromAsset(context.getAssets(), typefaceDirAkkurat + "Akkurat.ttf");
			return typeface;
		} else if(type == BrauerNeue_Regular) {
			typeface = Typeface.createFromAsset(context.getAssets(), typefaceDirBrauerNeue + "BrauerNeue-Regular.ttf");
			return typeface;
		} else {
            typeface = Typeface.createFromAsset(context.getAssets(), typefaceDirAkkurat + "BrauerNeue-Regular.ttf");
			return typeface;
		}
	}
}
