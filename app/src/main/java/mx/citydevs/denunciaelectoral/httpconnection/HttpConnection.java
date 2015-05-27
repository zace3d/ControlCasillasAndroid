package mx.citydevs.denunciaelectoral.httpconnection;

import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import mx.citydevs.denunciaelectoral.dialogues.Dialogues;

/**
 * Created by zace3d on 5/26/15.
 */
public class HttpConnection {
	public static final String TAG_CLASS = HttpConnection.class.getSimpleName();
	
	public static final String URL = "http://dc.netoxico.com/api/";

    public static final String CATEGORIES = "categories";
	public static final String TYPES = "types";
	public static final String COMPLAINTS = "complaints";

	public static String GET(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		String result = null;
		
		HttpResponse response;
		try {
			response = client.execute(request);
			
			Dialogues.Log(TAG_CLASS, "Http Post Response:" + response.toString(), Log.DEBUG);
			
			HttpEntity httpEntity = response.getEntity();

			result = EntityUtils.toString(httpEntity);
			
			Dialogues.Log(TAG_CLASS, result, Log.DEBUG);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public static String POST(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		
		String result = null;
		
		try {
			//httpPost.setEntity(createStringEntity(json));
		    //httpPost.setHeader("Accept", "application/json");
		    //httpPost.setHeader("Content-Type", "application/json");

			String authorizationString = getB64Auth("", "");
			httpPost.setHeader("Authorization", authorizationString);

			HttpResponse response = httpClient.execute(httpPost);
			Dialogues.Log(TAG_CLASS, "Http Post Response:" + response.toString(), Log.DEBUG);
			
			HttpEntity httpEntity = response.getEntity();
			
			result = EntityUtils.toString(httpEntity);
			
			Dialogues.Log(TAG_CLASS, result, Log.ERROR);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	private static String getB64Auth(String user, String pass) {
		String source = user + ":" + pass;
		return "Basic " + Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
	}

	private static HttpEntity createStringEntity(String json) {
		StringEntity se = null;
		try {
			se = new StringEntity(json, "UTF-8");
			se.setContentType("application/json; charset=UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG_CLASS, "Failed to create StringEntity", e);
		}
		return se;
	}
}
