package mx.citydevs.denunciaelectoral;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import mx.citydevs.denunciaelectoral.beans.CategoriesType;
import mx.citydevs.denunciaelectoral.beans.Complaint;
import mx.citydevs.denunciaelectoral.beans.ComplaintType;
import mx.citydevs.denunciaelectoral.dialogues.Dialogues;
import mx.citydevs.denunciaelectoral.httpconnection.HttpConnection;
import mx.citydevs.denunciaelectoral.location.LocationClientListener;
import mx.citydevs.denunciaelectoral.parser.GsonParser;
import mx.citydevs.denunciaelectoral.utils.LocationUtils;
import mx.citydevs.denunciaelectoral.utils.NetworkUtils;
import mx.citydevs.denunciaelectoral.utils.PreferencesUtils;
import mx.citydevs.denunciaelectoral.utils.TelephonyUtils;
import mx.citydevs.denunciaelectoral.views.CustomTextView;

/**
 * Created by zace3d on 5/31/15.
 */
public class ComplaintPreviewActivity extends ActionBarActivity implements View.OnClickListener {
    public static final String TAG_CLASS = ComplaintsListActivity.class.getSimpleName();

    public static final String COMPLAINT = "complaint";
    public static final String IMAGE = "image";
    public static final String DESCRIPTION = "description";

    private ComplaintType complaint;
    private String description;
    private Bitmap bitmap;
    private byte[] imageBytes;

    private LocationClientListener clientListener;
    private LatLng userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaintpreview);

        setSupportActionBar();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            complaint = (ComplaintType) bundle.getSerializable(COMPLAINT);
            description = bundle.getString(DESCRIPTION);

            imageBytes = bundle.getByteArray(IMAGE);
            if (imageBytes != null) {
                bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            }

            if (complaint != null && description != null && bitmap != null) {
                initLocation();

                initUI();
            }
        }
    }

    protected void initLocation() {
        String location = PreferencesUtils.getStringPreference(getApplication(), PreferencesUtils.LOCATION);
        if (location != null) {
            String[] arrayLocation = location.split(",");
            if (arrayLocation.length == 2) {
                userLocation = LocationUtils.getLatLngFromLocation(Double.parseDouble(arrayLocation[0]), Double.parseDouble(arrayLocation[1]));
                // Dialogues.Toast(getBaseContext(), "Latitude: " + userLocation.latitude + ", Longitude: " + userLocation.longitude, Toast.LENGTH_SHORT);
            }
        } else {
            if (LocationUtils.isGpsOrNetworkProviderEnabled(getBaseContext())) {
                initLocationClientListener();
            }
        }
    }

    protected void setSupportActionBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.actionbar);
        mToolbar.setTitle("");
        mToolbar.getBackground().setAlpha(255);
        CustomTextView actionbarTitle = (CustomTextView) mToolbar.findViewById(R.id.actionbar_title);
        actionbarTitle.setText(getString(R.string.complaint_preview_name));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void initUI() {
        CustomTextView tvCategory = (CustomTextView) findViewById(R.id.complaintpreview_tv_category);
        ImageView ivLogo = (ImageView) findViewById(R.id.complaintpreview_iv_logo);

        if (complaint.getCategory() != null) {
            if (complaint.getCategory().getId() == CategoriesType.CIUDADANO_ID) {
                tvCategory.setText(getString(R.string.label_un_ciudadano));
                ivLogo.setImageResource(R.drawable.ic_ciudadano);

            } else if (complaint.getCategory().getId() == CategoriesType.FUNCIONARIO_ID) {
                tvCategory.setText(getString(R.string.label_un_funcionario));
                ivLogo.setImageResource(R.drawable.ic_funcionario);

            } else if (complaint.getCategory().getId() == CategoriesType.CANDIDATO_ID) {
                tvCategory.setText(getString(R.string.label_un_candidato));
                ivLogo.setImageResource(R.drawable.ic_candidato);
            }
        }

        CustomTextView tvTitle = (CustomTextView) findViewById(R.id.complaintpreview_tv_title);
        tvTitle.setText(complaint.getName());

        CustomTextView tvDescription = (CustomTextView) findViewById(R.id.complaintpreview_tv_description);
        tvDescription.setText(description);

        ImageView ivPhoto = (ImageView) findViewById(R.id.complaintpreview_iv_photo);
        ivPhoto.setImageBitmap(bitmap);

        findViewById(R.id.complaintpreview_btn_send).setOnClickListener(this);
    }

    protected void initLocationClientListener() {
        clientListener = new LocationClientListener(this);
        clientListener.setOnLocationClientListener(new LocationClientListener.OnLocationClientListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLocation = LocationUtils.getLatLngFromLocation(location);

                //Dialogues.Toast(getBaseContext(), "Location.", Toast.LENGTH_SHORT);

                PreferencesUtils.putStringPreference(getApplication(),
                        PreferencesUtils.LOCATION, userLocation.latitude + "," + userLocation.longitude);

                clientListener.stopLocationUpdates();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complaintpreview_btn_send:
                if (userLocation != null) {
                    sendComplaint();
                } else {
                    if (LocationUtils.isGpsOrNetworkProviderEnabled(getBaseContext())) {
                        initLocationClientListener();
                    } else {
                        Dialogues.Toast(getBaseContext(), getString(R.string.no_gps_enabled), Toast.LENGTH_SHORT);
                    }
                }
                break;
            default:
                break;
        }
    }

    protected void sendComplaint() {
        if (NetworkUtils.isNetworkConnectionAvailable(getBaseContext())) {
            Complaint complaint = new Complaint();
            complaint.setName("Anónimo");
            complaint.setLastName("Anónimo");
            complaint.setComplaintType((int) this.complaint.getId());
            complaint.setContent(description);
            complaint.setLatitude(formatLocation(userLocation.latitude));
            complaint.setLongitude(formatLocation(userLocation.longitude));
            //complaint.setPhone("12345678");
            complaint.setImei(TelephonyUtils.getDeviceId(getBaseContext()));
            complaint.setIp(NetworkUtils.getIPAddress(true));
            /*StringBuilder sb = new StringBuilder();
            sb.append("data:image/jpg;base64,");
            sb.append(Base64.encodeToString(imageBytes, Base64.DEFAULT));
            String encodedImage = sb.toString();
            complaint.setPicture(encodedImage);*/
            complaint.setPicture(imageBytes);

            //String json = GsonParser.createJsonFromObject(complaint);
            //Dialogues.Log(TAG_CLASS, "JSON: " + json, Log.ERROR);

            doPostImage(HttpConnection.URL + HttpConnection.COMPLAINTS + "/", complaint);
            //PostComplaintAsyncTask task = new PostComplaintAsyncTask(complaint);
            //task.execute();
        } else {
            Dialogues.Toast(getBaseContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT);
        }
    }

    public String formatLocation(double value) {
        DecimalFormat myFormatter = new DecimalFormat("00.000000");
        return myFormatter.format(value);
    }

    private class PostComplaintAsyncTask extends AsyncTask<String, String, String> {
        private ProgressDialog dialog;

        private final Complaint complaint;

        public PostComplaintAsyncTask(Complaint complaint) {
            this.complaint = complaint;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ComplaintPreviewActivity.this);
            dialog.setMessage("Enviando denuncia...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return HttpConnection.POST(HttpConnection.URL + HttpConnection.COMPLAINTS + "/", complaint);
        }

        @Override
        protected void onPostExecute(String result) {
            Dialogues.Log(TAG_CLASS, "Result: " + result, Log.INFO);

            if (result != null) {
                try {
                    Dialogues.Toast(getBaseContext(), " Result: " + result, Toast.LENGTH_LONG);
                    Dialogues.Log(TAG_CLASS, "Result: " + result, Log.ERROR);

                    /*listComplaintsTypes = (ArrayList<ComplaintType>) GsonParser.getListComplaintsTypesFromJSON(result);

                    if (listComplaintsTypes != null && listComplaintsTypes.size() > 0) {
                        Dialogues.Toast(getBaseContext(), listComplaintsTypes.size() + " Result: " + result, Toast.LENGTH_LONG);
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
        }
    }

    private ProgressDialog dialog;
    private void doPostImage(String url, Complaint complaint) {
        dialog = new ProgressDialog(ComplaintPreviewActivity.this);
        dialog.setMessage("Enviando denuncia...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        //String fileName = "/mnt/sdcard/IMG_.jpg";
        //File myFile = new File(fileName);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 0);
        client.setBasicAuth(HttpConnection.username, HttpConnection.pwd);

        RequestParams params = new RequestParams();
        params.put("name", complaint.getName());
        params.put("last_name", complaint.getLastName());
        params.put("complaint_type", complaint.getComplaintType());
        params.put("latitude", complaint.getLatitude());
        params.put("longitude", complaint.getLongitude());
        params.put("ip", complaint.getIp());
        params.put("content", complaint.getContent());
        try {
            File tempFile = File.createTempFile("IMG_", ".jpg", null);
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(complaint.getPicture());

            params.put("picture", tempFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Dialogues.Log(TAG_CLASS, "=======Json================" + new String(responseBody), Log.ERROR);
                Dialogues.Toast(getBaseContext(), getString(R.string.success_response), Toast.LENGTH_LONG);

                dismissDialog();
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Dialogues.Log(TAG_CLASS, "=======Error================" + new String(responseBody), Log.ERROR);
                dismissDialog();
                Dialogues.Toast(getBaseContext(), getString(R.string.error_try_again_response), Toast.LENGTH_LONG);
            }
        });
    }

    protected void dismissDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }
}

