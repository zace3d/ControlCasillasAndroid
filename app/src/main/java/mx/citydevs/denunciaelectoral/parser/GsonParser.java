package mx.citydevs.denunciaelectoral.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import mx.citydevs.denunciaelectoral.beans.ComplaintType;

/**
 * Created by zace3d on 5/26/15.
 */
public class GsonParser {
	private static String TAG_CLASS = GsonParser.class.getName();

	public static ComplaintType getComplaintTypeFromJSON(String json) throws Exception {
		Gson gson = new Gson();
		return gson.fromJson(json, ComplaintType.class);
	}

    public static List<ComplaintType> getListComplaintsTypesFromJSON(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ComplaintType>>(){}.getType();
        return gson.fromJson(json, listType);
    }

	public static String createJsonFromObject(Object object) {
		Gson gson = new Gson();
		String json = gson.toJson(object);
		
		return json;
	}
	
	public static String createJsonFromObjectWithoutExposeAnnotations(Object object) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String json = gson.toJson(object);
		
		return json;
	}
}
