package mx.citydevs.denunciaelectoral.beans;

import android.content.Context;

import mx.citydevs.denunciaelectoral.R;

/**
 * Created by zace3d on 1/30/15.
 */
public enum CategoriesType {
    CIUDADANO,
    FUNCIONARIO,
    CANDIDATO;

    public static String getValue(CategoriesType type) {
        switch (type) {
            case CIUDADANO:
                return "ciudadano";
            case FUNCIONARIO:
                return "funcionario";
            case CANDIDATO:
                return "candidato";
            default:
                return "";
        }
    }

    public static int getIconCategoryValue(CategoriesType type) {
        switch (type) {
            case CIUDADANO:
                return R.drawable.ic_ciudadano;
            case FUNCIONARIO:
                return R.drawable.ic_funcionario;
            case CANDIDATO:
                return R.drawable.ic_candidato;
            default:
                return -1;
        }
    }

    public static String getTextCategoryValue(Context context, CategoriesType type) {
        switch (type) {
            case CIUDADANO:
                return context.getResources().getString(R.string.label_un_ciudadano);
            case FUNCIONARIO:
                return context.getResources().getString(R.string.label_un_funcionario);
            case CANDIDATO:
                return context.getResources().getString(R.string.label_un_candidato);
            default:
                return "";
        }
    }
}
